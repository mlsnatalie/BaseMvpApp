package com.example.basemvpapp

import android.content.Context
import android.util.Log
import com.example.basemvpapp.api.NetApi
import com.google.gson.reflect.TypeToken
import com.libs.core.business.http.HttpURL
import com.libs.core.business.http.proxy.RetrofitProxy
import com.libs.core.common.base.BasePresenter
import com.libs.core.common.utils.GsonUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * @author: mlsnatalie
 * @date: 2020/6/29 3:39 PM
 * @UpdateDate: 2020/6/29 3:39 PM
 * email：mlsnatalie@163.com
 * description：
 */
class NetPresenter(context: Context?) : BasePresenter<INetMv.NetView>(context), INetMv.NetModel {

    override fun getWeather(APPID: String, zip: String) {
        RetrofitProxy<Any>().create(NetApi::class.java, HttpURL.getWhetherBaseURL())
            .getWeather(APPID, zip)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .`as`(mView!!.bindLifecycle())
            .subscribe(object : Observer<ResponseBody?> {
                override fun onSubscribe(d: Disposable) {

                }
                override fun onNext(responseBody: ResponseBody) {
                    val jsonObject = JSONObject(responseBody.string())
                    val code = jsonObject.opt("cod")
                    Log.e("aaaddd", jsonObject.toString())
                    if (code == "200") {
                        val result = jsonObject.toString()
                        val data: WhetherData = GsonUtils.fromJson(result, object : TypeToken<WhetherData>() {}.type)
                        mView.onNetActionResult(INetMv.ACTION_NET, data)
                    }

                }
                override fun onError(e: Throwable) {

                }
                override fun onComplete() {

                }
            })
    }
}