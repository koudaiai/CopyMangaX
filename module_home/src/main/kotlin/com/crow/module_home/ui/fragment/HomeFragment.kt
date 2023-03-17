package com.crow.module_home.ui.fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.view.doOnLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.crow.base.extensions.animateFadeIn
import com.crow.base.extensions.dp2px
import com.crow.base.extensions.showSnackBar
import com.crow.base.fragment.BaseMviFragment
import com.crow.base.viewmodel.ViewState
import com.crow.base.viewmodel.doOnError
import com.crow.base.viewmodel.doOnLoading
import com.crow.base.viewmodel.doOnResult
import com.crow.module_home.databinding.HomeFragmentBinding
import com.crow.module_home.model.ComicType
import com.crow.module_home.model.intent.HomeIntent
import com.crow.module_home.model.resp.homepage.results.Results
import com.crow.module_home.ui.adapter.HomeBannerAdapter
import com.crow.module_home.ui.adapter.HomeBookAdapter1
import com.crow.module_home.ui.viewmodel.HomeViewModel
import com.google.android.material.button.MaterialButton
import com.to.aboomy.pager2banner.IndicatorView
import com.to.aboomy.pager2banner.ScaleInTransformer
import org.koin.androidx.viewmodel.ext.android.viewModel


/*************************
 * @Machine: RedmiBook Pro 15 Win11
 * @Path: module_home/src/main/kotlin/com/crow/module_home/view
 * @Time: 2023/3/6 0:14
 * @Author: CrowForKotlin
 * @Description: HomeBodyFragment
 * @formatter:on
 **************************/
class HomeFragment constructor() : BaseMviFragment<HomeFragmentBinding>() {

    constructor(clickListener: TapComicListener) : this() { mTapComicParentListener = clickListener }

    interface TapComicListener { fun onTap(type: ComicType, pathword: String) }

    private var mRefreshButton : MaterialButton? = null
    private var mTapComicChildListener = object : TapComicListener { override fun onTap(type: ComicType, pathword: String) { mTapComicParentListener?.onTap(type, pathword) } }
    private var mTapComicParentListener: TapComicListener? = null
    private var mRefreshListener: OnRefreshListener? = null
    private val mHomeVM by viewModel<HomeViewModel>()

    private val mHomeBannerAdapter: HomeBannerAdapter by lazy { HomeBannerAdapter(mutableListOf(), mTapComicChildListener) }
    private val mHomeTypeAdapter: HomeBookAdapter1<Any> by lazy { HomeBookAdapter1(mContext,null, mTapComicChildListener) }

    override fun getViewBinding(inflater: LayoutInflater) = HomeFragmentBinding.inflate(inflater)

    override fun onPause() {
        super.onPause()
        mRefreshListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mRefreshButton = null
    }

    override fun initObserver() {
        mHomeVM.onOutput { intent ->
            when (intent) {
                // （获取主页）（根据 刷新事件 来决定是否启用加载动画） 正常加载数据、反馈View
                is HomeIntent.GetHomePage -> {
                    intent.mViewState
                        .doOnLoading { if(mRefreshListener == null) showLoadingAnim() }
                        .doOnResult { if(mRefreshListener != null) doOnLoadHomePage(intent.homePageData!!.mResults) else dismissLoadingAnim { doOnLoadHomePage(intent.homePageData!!.mResults) } }
                        .doOnError { code, msg ->
                            if (code == ViewState.Error.UNKNOW_HOST) mBinding.root.showSnackBar(msg ?: "")
                            if (mRefreshListener != null) { mRefreshListener?.onRefresh() }
                            dismissLoadingAnim { mBinding.homeLinearLayout.animateFadeIn(300L) }
                        }
                }

                // （刷新获取）不启用 加载动画 正常加载数据 反馈View
                is HomeIntent.GetRecPageByRefresh -> {
                    intent.mViewState
                        .doOnError { _, _ -> mRefreshButton?.isEnabled = true }
                        .doOnResult {
                            mHomeTypeAdapter.setRecData(intent.recPageData!!.mResults.mResult)
                            mHomeTypeAdapter.notifyItemChanged(0)
                            mRefreshButton?.isEnabled = true
                        }
                }
            }
        }
    }

    // 获取主页数据
    override fun initData() { mHomeVM.input(HomeIntent.GetHomePage()) }

    override fun initView() {

        // 设置 Banner 的高度 （1.875 屏幕宽高指定倍数）、（添加页面效果、指示器、指示器需要设置BottomMargin不然会卡在Banner边缘（产生重叠））
        mBinding.homeBanner.doOnLayout { it.layoutParams.height = (it.width / 1.875 + 0.5).toInt() }
        mBinding.homeBanner.addPageTransformer(ScaleInTransformer())
            .setPageMargin(mContext.dp2px(20), mContext.dp2px(10))
            .setIndicator(
                IndicatorView(mContext)
                    .setIndicatorColor(Color.DKGRAY)
                    .setIndicatorSelectorColor(Color.WHITE)
                    .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_BEZIER)
                    .also { it.doOnLayout { view -> (view.layoutParams as RelativeLayout.LayoutParams).bottomMargin = mContext.resources.getDimensionPixelSize(com.crow.base.R.dimen.base_dp20) } })
            .adapter = mHomeBannerAdapter
        mBinding.homeRv.adapter = mHomeTypeAdapter
    }

    override fun initListener() {
        mHomeTypeAdapter.addRecRefreshListener {
            mRefreshButton?.isEnabled = false
            mHomeVM.input(HomeIntent.GetRecPageByRefresh())
        }
    }

    private fun doOnLoadHomePage(results: Results) {
        mHomeBannerAdapter.bannerList.clear()
        mHomeBannerAdapter.bannerList.addAll(results.mBanners.filter { banner -> banner.mType <= 2 })
        mHomeTypeAdapter.setData(
            arrayListOf (
                results.mRecComicsResult,
                results.mHotComics,
                results.mNewComics,
                results.mFinishComicDatas,
                results.mTopics,
                results.mRankDayComics
            )
        )

        mHomeBannerAdapter.notifyItemRangeChanged(0, mHomeBannerAdapter.bannerList.size)
        mHomeTypeAdapter.notifyItemRangeChanged(0, mHomeTypeAdapter.itemCount)

        // 刷新事件 为空则 执行淡入动画（代表第一次加载进入布局）
        if (mRefreshListener == null) mBinding.homeRv.animateFadeIn(300L)
    }

    fun doOnRefresh(refreshListener: OnRefreshListener) {
        mRefreshListener = refreshListener
        mHomeVM.input(HomeIntent.GetHomePage())
    }
}