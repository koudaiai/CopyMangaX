package com.crow.copymanga

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import coil.Coil
import coil.EventListener
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transition.CrossfadeTransition
import com.crow.base.app.BaseApp
import com.crow.base.tools.extensions.SpNameSpace
import com.crow.copymanga.model.di.factoryModule
import com.crow.copymanga.model.di.fragmentModule
import com.crow.copymanga.model.di.networkModule
import com.crow.copymanga.model.di.servicesModule
import com.crow.copymanga.model.di.singleModule
import com.crow.copymanga.model.di.viewModelModule
import com.crow.mangax.copymanga.entity.AppConfig
import com.crow.mangax.copymanga.entity.CatlogConfig
import com.crow.mangax.copymanga.entity.CatlogConfig.mDarkMode
import com.crow.mangax.copymanga.okhttp.AppProgressFactory
import com.crow.mangax.tools.language.ChineseConverter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named


/*************************
 * @Machine: RedmiBook Pro 15 Win11
 * @Path: app/src/main/java/com/crow/interview
 * @Time: 2022/12/29 21:44
 * @Author: CrowForKotlin
 * @Description: MyApplication
 * @formatter:on
 **************************/
class MainApplication : BaseApp(), ImageLoaderFactory {

    private var mConfigJob: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        val sp = CatlogConfig.getCatlogConfigSp()

        mDarkMode = sp.getBoolean(SpNameSpace.Key.ENABLE_DARK, false)

        mConfigJob = GlobalScope.launch(Dispatchers.IO) {
            CatlogConfig.initialization(sp)
            AppConfig.readAppConfig()
        }

        AppCompatDelegate.setDefaultNightMode(if(mDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)

        ChineseConverter.initialize(applicationContext)

        Coil.setImageLoader(this)

        startKoin {
            fragmentFactory()
            androidContext(this@MainApplication)
            modules(
                listOf(
                    singleModule,
                    networkModule,
                    servicesModule,
                    viewModelModule,
                    factoryModule,
                    fragmentModule
                )
            )
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .okHttpClient(get<OkHttpClient>(named("ProgressOkHttp")))
            .transitionFactory(CrossfadeTransition.Factory(200, true))
            .eventListener(object : EventListener {
                override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                    super.onSuccess(request, result)
                    AppProgressFactory.getProgressFactory(request.data.toString())?.apply {
                        removeProgressListener()
                        remove()
                    }
                }
            })
            .build()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        mConfigJob?.cancel()
        mConfigJob = null
        ChineseConverter.cancel()
    }
}