package com.example.basemvpapp

import android.content.Context
import com.example.basemvpapp.INetMv.NetModel
import com.example.basemvpapp.INetMv.NetView
import com.example.basemvpapp.api.NetApi
import com.libs.core.business.http.HttpURL
import com.libs.core.business.http.proxy.RetrofitProxy
import com.libs.core.common.base.BasePresenter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

/**
 * @author: mlsnatalie
 * @date: 2020/6/29 4:25 PM
 * @UpdateDate: 2020/6/29 4:25 PM
 * email：mlsnatalie@163.com
 * description：
 */
class HomePresenter1(context: Context?) :
    BasePresenter<NetView?>(context), NetModel {
    override fun getWeather(APPID: String, zip: String) {
        RetrofitProxy<Any>().create(NetApi::class.java, HttpURL.getWhetherBaseURL())
            .getWeather(APPID, zip)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .`as`(mView!!.bindLifecycle())
            .subscribe(object : Observer<ResponseBody?> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(responseBody: ResponseBody) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }
}