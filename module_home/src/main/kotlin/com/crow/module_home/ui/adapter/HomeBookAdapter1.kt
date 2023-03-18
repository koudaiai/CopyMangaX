@file:Suppress("UNCHECKED_CAST")

package com.crow.module_home.ui.adapter

import android.content.Context
import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crow.base.R.*
import com.crow.base.app.appContext
import com.crow.base.extensions.clickGap
import com.crow.module_home.R
import com.crow.module_home.databinding.HomeRvBookParentBinding
import com.crow.module_home.databinding.HomeRvBookParentBinding.inflate
import com.crow.module_home.model.ComicType
import com.crow.module_home.model.resp.homepage.*
import com.crow.module_home.model.resp.homepage.results.RecComicsResult
import com.crow.module_home.ui.fragment.HomeFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/*************************
 * @Machine: RedmiBook Pro 15 Win11
 * @Path: module_home/src/main/kotlin/com/crow/module_home/ui/adapter
 * @Time: 2023/3/11 2:17
 * @Author: CrowForKotlin
 * @Description: HomeBookAdapter
 * @formatter:on
 **************************/
class HomeBookAdapter1<T>(
    private val mContext: Context,
    private var mData: ArrayList<T>? = null,
    private val mTapComicListener: HomeFragment.TapComicListener
) : RecyclerView.Adapter<HomeBookAdapter1<T>.ViewHolder>() {

    inner class ViewHolder(val rvBinding: HomeRvBookParentBinding) : RecyclerView.ViewHolder(rvBinding.root) { var mPathWord: String = "" }

    fun interface RecRefreshListener { fun onRefresh(button: MaterialButton) }

    companion object {
        private const val UPDATE_3 = 3
        private const val UPDATE_4 = 4
        private const val UPDATE_6 = 6
        private const val UPDATE_12 = 12
    }

    // 漫画卡片高度
    private val mChildCardHeight: Int = run {
        val width = appContext.resources.displayMetrics.widthPixels
        val height = appContext.resources.displayMetrics.heightPixels
        (width.toFloat() / (3 - width.toFloat() / height.toFloat())).toInt()
    }


    private lateinit var mRecRefreshButton: MaterialButton

    private var mRecRefreshListener: RecRefreshListener? = null
    private val mHomeRecAdapter: HomeBookAdapter2<ComicDatas<RecComicsResult>> by lazy { HomeBookAdapter2(null, ComicType.Rec, mTapComicListener) }
    private val mHomeHotAdapter: HomeBookAdapter2<List<HotComic>> by lazy { HomeBookAdapter2(null, ComicType.Hot, mTapComicListener) }
    private val mHomeNewAdapter: HomeBookAdapter2<List<NewComic>> by lazy { HomeBookAdapter2(null, ComicType.New, mTapComicListener) }
    private val mHomeCommitAdapter: HomeBookAdapter2<FinishComicDatas> by lazy { HomeBookAdapter2(null, ComicType.Commit, mTapComicListener) }
    private val mHomeTopicAapter: HomeBookAdapter2<ComicDatas<Topices>> by lazy { HomeBookAdapter2(null, ComicType.Topic, mTapComicListener) }
    private val mHomeRankAapter: HomeBookAdapter2<ComicDatas<RankComics>> by lazy { HomeBookAdapter2(null, ComicType.Rank, mTapComicListener) }

    override fun getItemCount(): Int = if (mData == null) 0 else mData!!.size

    private var isEnd = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(inflate(from(parent.context), parent, false))

    override fun onBindViewHolder(vh: ViewHolder, pos: Int) {
        when (pos) {
            0 -> {
                mHomeRecAdapter.setData((mData!![pos] as ComicDatas<RecComicsResult>), UPDATE_3)
                vh.rvBinding.initHomeItem(R.drawable.home_ic_recommed_24dp, R.string.home_recommend_comic, mHomeRecAdapter).also {
                    mRecRefreshButton = initRefreshButton(it.homeItemBt.id)
                    it.homeItemConstraint.addView(mRecRefreshButton)
                }
            }
            1 -> {
                mHomeHotAdapter.setData((mData!![pos] as List<HotComic>), UPDATE_12)
                vh.rvBinding.initHomeItem(R.drawable.home_ic_hot_24dp, R.string.home_hot_comic, mHomeHotAdapter)
            }
            2 -> {
                mHomeNewAdapter.setData((mData!![pos] as List<NewComic>), UPDATE_12)
                vh.rvBinding.initHomeItem(R.drawable.home_ic_new_24dp, R.string.home_new_comic, mHomeNewAdapter)
            }
            3 -> {
                mHomeCommitAdapter.setData((mData!![pos] as FinishComicDatas), UPDATE_6)
                vh.rvBinding.initHomeItem(R.drawable.home_ic_commit_24dp, R.string.home_commit_comic, mHomeCommitAdapter)
            }
            4 -> {
                mHomeTopicAapter.setData((mData!![pos] as ComicDatas<Topices>), UPDATE_4)
                vh.rvBinding.initHomeItem(R.drawable.home_ic_topic_24dp, R.string.home_topic_comic, mHomeTopicAapter)
                vh.rvBinding.homeItemBookRv.layoutManager = GridLayoutManager(mContext, 2)

            }
            5 -> {
                mHomeRankAapter.setData((mData!![pos] as ComicDatas<RankComics>), UPDATE_3)
                vh.rvBinding.initHomeItem(R.drawable.home_ic_rank_24dp, R.string.home_rank_comic, mHomeRankAapter)
            }
            else -> { }
        }
    }
    private fun <T> HomeRvBookParentBinding.initHomeItem(@DrawableRes iconRes: Int, @StringRes iconText: Int, adapter: HomeBookAdapter2<T>): HomeRvBookParentBinding {
        homeItemBt.setIconResource(iconRes)
        homeItemBt.text = appContext.getString(iconText)
        homeItemBookRv.adapter = adapter
        return this
    }

    // 初始化刷新控件
    private fun initRefreshButton(@IdRes recItemBtId: Int): MaterialButton {
        val constraintParams = ConstraintLayout.LayoutParams(0, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        constraintParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        constraintParams.topToTop = recItemBtId
        constraintParams.bottomToBottom = recItemBtId
        constraintParams.setMargins(appContext.resources.getDimensionPixelSize(dimen.base_dp5))
        return MaterialButton(mContext, null, com.google.android.material.R.attr.materialIconButtonStyle).apply {
            layoutParams = constraintParams
            icon = ContextCompat.getDrawable(appContext, R.drawable.home_ic_refresh_24dp)
            iconSize = appContext.resources.getDimensionPixelSize(dimen.base_dp24)
            iconTint = null
            iconPadding = appContext.resources.getDimensionPixelSize(dimen.base_dp6)
            text = appContext.getString(R.string.home_refresh)
            clickGap(500) { _, _ -> mRecRefreshListener?.onRefresh(this) }
        }
    }

    // 添加推荐页刷新事件
    fun addRecRefreshListener(recRefreshListener: HomeBookAdapter1.RecRefreshListener) { mRecRefreshListener = recRefreshListener }

    // 对外暴露设置数据
    fun setData(value: ArrayList<T>) { mData = value }

    // 对外暴露设置推荐数据
    fun notifyRec(lifecycleOwner: LifecycleOwner, value: ComicDatas<RecComicsResult>) {
        lifecycleOwner.lifecycleScope.launch {
            mHomeRecAdapter.setData(value)
            mHomeRecAdapter.notifyItemChanged(0)
            delay(10L)
            mHomeRecAdapter.notifyItemChanged(1)
            delay(10L)
            mHomeRecAdapter.notifyItemChanged(2)
        }
    }
}