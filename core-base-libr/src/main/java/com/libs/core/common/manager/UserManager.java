package com.libs.core.common.manager;

import android.text.TextUtils;

import com.libs.core.business.consts.PreferConst;
import com.libs.core.business.http.vo.CustomUserVo;
import com.libs.core.business.http.vo.UserVo;
import com.libs.core.business.sensor.SensorsTracker;
import com.networkbench.agent.impl.NBSAppAgent;


/**
 * 用户信息管理
 *
 * @author zhang.zheng
 * @version 2018-06-20
 */
public class UserManager {

    // 用户信息
    private static final String KEY_USER_VO = "key_user_vo";
    // 用户登录类型
    public static final int WX_LOGIN = 1;  // 微信登录
    public static final int CODE_LOGIN = 2;// 验证码登录
    public static final int PWD_LOGIN = 3; // 密码登录
    public static final int AUTH_LOGIN = 4;// 一键登录

    // 权限指标状态
    private static final String CHAOS_INDEX = "-chaos";      //混沌权限
    private static final String DIAGNOSE_INDEX = "-diagnose";//诊股权限
    public static final String INDEX_STATUS_OPEN = "open";        //审核通过权限开启
    public static final String INDEX_STATUS_CLOSE = "close";      //审核拒绝权限关闭
    public static final String INDEX_STATUS_APPLYING = "applying";//权限申请中待审核

    // 最后一次登录的uid，用于极光解绑
    public static final String KEY_LAST_LOGIN_USERID = "lastLoginUserId";

    private UserVo mUserVo;
    private CustomUserVo mCustomUserVo;
    private static volatile UserManager mUserManager;

    private UserManager() {
    }

    public static UserManager getInstance() {
        if (mUserManager == null) {
            synchronized (UserManager.class) {
                if (mUserManager == null) {
                    mUserManager = new UserManager();
                }
            }
        }
        return mUserManager;
    }

    /**
     * 用户是否登录
     */
    public boolean isLogin() {
        return getUserVo() != null && !TextUtils.isEmpty(mUserVo.getToken());
    }

    public boolean isCustomerLogin() {
        return getCustomUserVo() != null && !TextUtils.isEmpty(getCustomUserVo().getUid());
    }

    /**
     * 是否要调用复活卡接口
     *
     * @return
     */
    public boolean isRequestReviveInterface() {
        return isLogin() ? mUserVo.isRequestReviveInterface() : false;
    }

    /**
     * 只用于直播答题
     *
     * @return
     */
   /* public String getUserChannelId() {
        return isLogin() ? "" : mUserVo.getChannelid();
    }*/

    /**
     * 获取用户信息
     */
    public UserVo getUserVo() {
        if (mUserVo == null) {
            mUserVo = (UserVo) PreferenceManager.getObject(KEY_USER_VO, UserVo.class);
        }
        return mUserVo;
    }

    //用于提现中判断
    public boolean isHasBindWx(){
        return (mUserVo != null && !TextUtils.isEmpty(mUserVo.getPbOpenId()) && !TextUtils.isEmpty(mUserVo.getWxNickName())) ? true : false;
    }

    public boolean isLoginHasBindWx() {
        return (mUserVo != null && mUserVo.isBindingWx()) ? true : false;
    }

    //是否有绑定微博
    public boolean isHasBindWeiBo(){
        //return (mUserVo != null && !TextUtils.isEmpty(mUserVo.getPbOpenId()) && !TextUtils.isEmpty(mUserVo.getWxNickName())) ? true : false;
        return  false;
    }
    /**
     * 保存用户信息
     */
    public void saveUserVo(UserVo userVo) {
        this.mUserVo = userVo;
        PreferenceManager.putObject(KEY_USER_VO, userVo);
        if (!TextUtils.isEmpty(userVo.getId())) {
            // 神策登录
            SensorsTracker.getInstance().setProperties(true);
            SensorsTracker.getInstance().login(UserManager.getInstance().getUid());
            NBSAppAgent.setUserIdentifier(userVo.getId());
            NBSAppAgent.setUserCrashMessage("uid", userVo.getId());
            PreferenceManager.putString(KEY_LAST_LOGIN_USERID, userVo.getId());
        }
    }

    /**
     * 清除用户信息
     */
    public void clearUserVo() {
        // 此处仅保留手机号
        UserVo userVo = new UserVo();
        userVo.setMobile(getUserMobile());
        PreferenceManager.putObject(KEY_USER_VO, userVo);
        this.mUserVo = userVo;
        this.mCustomUserVo = null;

        // 清空诊股指标权限
        PreferenceManager.putString(getUid() + DIAGNOSE_INDEX, "");

        SensorsTracker.getInstance().setProperties(false);
    }

    /**
     * 获取用户ID
     */
    public String getUid() {
        return isLogin() ? getUserVo().getId() : "";
    }

    /**
     * 获取token
     */
    public String getToken() {
        return getUserVo() != null ? (!TextUtils.isEmpty(getUserVo().getToken()) ? getUserVo().getToken() : "") : "";
    }

    public String getCustomerToken() {
        String token = getToken();
        if (TextUtils.isEmpty(token)) {
            if (mCustomUserVo != null /*&& mCustomUserVo.getIs_youke() == 1*/ && !TextUtils.isEmpty(mCustomUserVo.getUkey())) {
                token = mCustomUserVo.getUkey();
            }
        }

        return token;
    }

    /**
     * 获取历史储存的ukey 防止多次生成游客 id
     *
     * @return
     */
    public String getOldUkey() {
        String token = getToken();
        if (TextUtils.isEmpty(token)) {
            token = PreferenceManager.getString(PreferConst.KEY_IM_CUSTOMER, null);
        }
        return token;
    }

    /**
     * 获取用户手机号
     */
    public String getUserMobile() {
        String mobile = null;
        if (getUserVo() != null) {
            mobile = getUserVo().getMobile();
        }
        return mobile;
    }

    /**
     * 获取客服信息
     */
    public CustomUserVo getCustomUserVo() {
        return mCustomUserVo;
    }

    public void setCustomUserVo(CustomUserVo customUserVo) {
        this.mCustomUserVo = customUserVo;
        //存到本地
        if (customUserVo != null && !isLogin() && customUserVo.getIs_youke() == 1) { //游客状态
            PreferenceManager.putString(PreferConst.KEY_IM_CUSTOMER, customUserVo.getUkey());
        }
    }

    public CustomUserVo.ServiceBean getCustomService() {
        if (isCustomerLogin() && getCustomUserVo().getCustom_service() != null) {
            return getCustomUserVo().getCustom_service();
        }
        return null;
    }

    /**
     * 获取混沌指标权限
     */
    public boolean getChaosIndex() {
        return isLogin() && PreferenceManager.getBoolean(getUid() + CHAOS_INDEX, false);
    }

    public void setChaosIndex(boolean status) {
        PreferenceManager.putBoolean(UserManager.getInstance().getUid() + CHAOS_INDEX, status);
    }


    /**
     * 获取诊股权限指标
     */
    public String getDiagnoseIndex() {
        return isLogin() ? PreferenceManager.getString(getUid() + DIAGNOSE_INDEX, "") : "";
    }

    public void setDiagnoseIndex(String status) {
        PreferenceManager.putString(getUid() + DIAGNOSE_INDEX, status);
    }

}
