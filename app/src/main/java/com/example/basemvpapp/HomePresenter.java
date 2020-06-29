package com.example.basemvpapp;

import android.content.Context;

import com.example.basemvpapp.api.NetApi;
import com.libs.core.business.http.HttpURL;
import com.libs.core.business.http.proxy.RetrofitProxy;
import com.libs.core.common.base.BasePresenter;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @author: mlsnatalie
 * @date: 2020/6/29 4:25 PM
 * @UpdateDate: 2020/6/29 4:25 PM
 * email：mlsnatalie@163.com
 * description：
 */
public class HomePresenter extends BasePresenter<INetMv.NetView> implements INetMv.NetModel {
    public HomePresenter(Context context) {
        super(context);
    }

    @Override
    public void getWeather(@NotNull String APPID, @NotNull String zip) {
        new RetrofitProxy<>().create(NetApi.class, HttpURL.getWhetherBaseURL())
                .getWeather(APPID, zip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(mView.bindLifecycle())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
