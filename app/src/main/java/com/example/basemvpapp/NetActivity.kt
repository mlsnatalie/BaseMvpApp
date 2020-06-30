package com.example.basemvpapp

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.basemvpapp.component.CommonTopBarContract
import com.libs.core.common.base.BaseRxActivity
import com.libs.core.common.helper.StatusBarCompatHelper
import com.libs.core.common.utils.StatusBarUtils
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
//        StatusBarUtils.setLightStatusBar(this)
        StatusBarCompatHelper.setStatusBarStyle(
            this,
            false,
            ContextCompat.getColor(this, R.color.red_pay),
            true
        )
        initView()
        mPresenter.getWeather("15646a06818f61f7b8d7823ca833e1ce", "94042")
    }

    private fun initView() {
        top_bar.setTitleWord("网络请求")
        top_bar.setRightWord("测试")
        top_bar.setStatusViewColor(ContextCompat.getColor(this, R.color.red_pay))
//        top_bar.setEnableStatusView(false)
        top_bar.setCallBack(object : CommonTopBarContract.OnCallBack() {
            override fun onBackClick() {
                super.onBackClick()
                finish()
            }
        })
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