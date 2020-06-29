package com.example.basemvpapp

import com.libs.core.common.base.BaseView

/**
 * @author: mlsnatalie
 * @date: 2020/6/29 3:41 PM
 * @UpdateDate: 2020/6/29 3:41 PM
 * email：mlsnatalie@163.com
 * description：
 */
interface INetMv {

    companion object {
        var ACTION_NET = 1
    }

    interface NetModel {
        fun getWeather(APPID: String, zip: String)
    }

    interface NetView : BaseView {
        fun onNetActionResult(action: Int, vararg objects: Any?)
    }

}