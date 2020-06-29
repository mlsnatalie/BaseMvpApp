package com.example.basemvpapp

import android.os.Bundle
import com.libs.core.common.base.BaseRxActivity
import kotlinx.android.synthetic.main.activity_net.*

/**
 * @author: mlsnatalie
 * @date: 2020/6/29 3:37 PM
 * @UpdateDate: 2020/6/29 3:37 PM
 * email：mlsnatalie@163.com
 * description：
 */
class NetActivity : BaseRxActivity<NetPresenter>(), INetMv.NetView {

    override fun initLayout(): Int {
        return R.layout.activity_net
    }

    override fun initPresenter() {
        mPresenter = NetPresenter(this)
    }

    override fun initViewData(bundle: Bundle?) {
        mPresenter.getWeather("15646a06818f61f7b8d7823ca833e1ce", "94042")
    }

    override fun onNetActionResult(action: Int, vararg objects: Any?) {

        when(action) {
            INetMv.ACTION_NET -> {
                if (objects.isNotEmpty()) {
                    val text = objects[0] as WhetherData
                    tv_text.text = text.cod
                }
            }

        }
    }
}