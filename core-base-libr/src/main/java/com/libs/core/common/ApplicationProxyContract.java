package com.libs.core.common;

import android.app.Application;

/**
 * @author: amos
 * @date: 2020/1/17 11:12
 * @description:
 */
public interface ApplicationProxyContract {
    ApplicationProxy setApplication(Application application);
    Application getApplication();

}
