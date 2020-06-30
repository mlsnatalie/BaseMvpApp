package com.example.basemvpapp

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.multidex.MultiDex
import com.bun.miitmdid.core.JLibrary
import com.example.basemvpapp.helper.AppActivityTrack
import com.example.basemvpapp.helper.MiitHelper
import com.libs.core.business.AppConfig
import com.libs.core.business.http.HttpCommonParamsProxy
import com.libs.core.business.http.proxy.ApiRetrofit
import com.libs.core.common.ApplicationProxy
import com.libs.core.common.base.BaseApplication
import com.libs.core.common.utils.AppContext
import com.libs.core.common.utils.DeviceInfo
import com.libs.core.common.utils.LogUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import io.reactivex.plugins.RxJavaPlugins

/**
 * @author: mlsnatalie
 * @date: 2020/6/29 3:19 PM
 * @UpdateDate: 2020/6/29 3:19 PM
 * email：mlsnatalie@163.com
 * description：
 */
class Application : BaseApplication() {

    companion object {
        var instance: Application? = null
            private set
        var oaid: String? = null
            private set
        private var isSupportOaid = true
        private var errorCode = 0

        fun getErrorCode(): String {
            return errorCode.toString()
        }

        fun isSupportOaid(): Boolean {
            return isSupportOaid
        }

        fun setIsSupportOaid(isSupportOaid: Boolean) {
            Companion.isSupportOaid = isSupportOaid
        }

        fun setIsSupportOaid(isSupportOaid: Boolean, ErrorCode: Int) {
            Companion.isSupportOaid = isSupportOaid
            errorCode = ErrorCode
        }

        //static 代码段可以防止内存泄露
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(
                    R.color.color_edf0f7,
                    R.color.color_aaaaa7
                ) //全局设置主题颜色
                //layout.setDragRate(0.3f);
                ClassicsHeader(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }
    }

//    private val instance: Application? = null
//    private var oaid: String? = null
//    private var isSupportOaid = true
//    private var errorCode = 0
//
//    fun getOaid(): String? {
//        return oaid
//    }
//
//    fun getErrorCode(): String? {
//        return errorCode.toString()
//    }
//
//    fun isSupportOaid(): Boolean {
//        return isSupportOaid
//    }
//
//    public fun setIsSupportOaid(isSupportOaid: Boolean) {
//        this.isSupportOaid = isSupportOaid
//    }
//
//    public fun setIsSupportOaid(isSupportOaid: Boolean, ErrorCode: Int) {
//        this.isSupportOaid = isSupportOaid
//        this.errorCode = ErrorCode
//    }
//
//    fun getInstance(): Application? {
//        return instance
//    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        try {
            JLibrary.InitEntry(base)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun init() {
        //AppConfig.setDebug(false);

        AppConfig.setDebug(BuildConfig.DEBUG)
        //AppConfig.setDebug(false);
        ApplicationProxy.getInstance().application = this
        AppActivityTrack.getInstance().init(this)
        //获取OAID等设备标识符
        val miitHelper = MiitHelper(appIdsUpdater)
        miitHelper.getDeviceIds(applicationContext)
        //网络请求公参
        HttpCommonParamsProxy.getInstance()
            .setDebug(BuildConfig.DEBUG)
            .setAppVersion(BuildConfig.VERSION_NAME).appChannel =
            DeviceInfo.getAppStringMetaData(this, "UMENG_CHANNEL")
        LogUtils.setLEVEL(if (BuildConfig.DEBUG) Log.DEBUG else Log.ERROR)
        AppContext.getInstance().init(this)
        ApiRetrofit.init(
            BuildConfig.VERSION_NAME,
            DeviceInfo.getAppStringMetaData(this, "UMENG_CHANNEL")
        )

//        initNewlens()
//        initUMengShare()
//        initSensorStatistic()
//        initEMSDK()
//        initQuoteBridge()
//        initBaiJiaYun()
        //推送实例化
        //SelectPushManager.getInstance(this).initPush();
        //推送实例化
        //SelectPushManager.getInstance(this).initPush();
//        JPushHelper.getInstance().initJPush(this)
        //Rxjava全局异常抓取
        //Rxjava全局异常抓取
        RxJavaPlugins.setErrorHandler { throwable -> LogUtils.e("AppApplication", throwable?.message, throwable) }
    }

    private val appIdsUpdater: MiitHelper.AppIdsUpdater = object : MiitHelper.AppIdsUpdater {

        override fun OnIdsAvalid(ids: String) {
            oaid = ids
            //网络请求公参
            HttpCommonParamsProxy.getInstance().appOAId = oaid
        }
    }
}