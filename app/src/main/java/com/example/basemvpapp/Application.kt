package com.example.basemvpapp

import androidx.multidex.MultiDex
import com.libs.core.common.base.BaseApplication

/**
 * @author: mlsnatalie
 * @date: 2020/6/29 3:19 PM
 * @UpdateDate: 2020/6/29 3:19 PM
 * email：mlsnatalie@163.com
 * description：
 */
class Application : BaseApplication() {
    override fun init() {
        MultiDex.install(this)
    }
}