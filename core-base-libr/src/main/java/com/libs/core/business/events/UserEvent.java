package com.libs.core.business.events;

/**
 * 用户相关的事件
 */
public class UserEvent extends BaseEvent {

    public UserEvent(int eventId) {
        super(eventId);
    }

    public static final int EVENT_USER_LOGIN = 0x1001;          //登录成功
    public static final int EVENT_USER_LOGOUT = 0x1002;         //退出登录
    public static final int EVENT_USER_INFO_UPDATE = 0x1003;    //用户信息更新
    public static final int EVENT_USER_MODIFY_NICKNAME = 0x1004;//修改昵称
    public static final int EVENT_USER_MODIFY_MOBILE = 0x1005;  //修改手机
    public static final int EVENT_BIND_MOBILE_OK = 0x1006;      //绑定手机
    public static final int EVENT_USER_CLEAR_CACHE = 0x1007;    //清除缓存
    public static final int EVENT_AUTO_LOGIN = 0x1008;          //注册后自动登录
    public static final int EVENT_NEW_MESSAGE = 0x1009;         //客服新消息
    public static final int EVENT_SWITCH_QUOTE = 0x1010;        //切换到行情
    public static final int EVENT_SWITCH_DIAGNOSE = 0x1011;     //切换到诊股
    public static final int EVENT_SWITCH_LIVE = 0x1012;       //切换到财圈
    public static final int EVENT_AUTH_INPUT_LOGIN = 0x1013;    //一键登录
    public static final int EVENT_AUTH_MOBILE_LOGIN = 0x1014;   //一键登录
    public static final int EVENT_SWITCH_STOCK_VIDEO = 0x1015;   //在线人数
    public static final int EVENT_STOCK_DETAIL = 0x1016;   //股票详情
    public static final int EVENT_UPDATE_TAB = 0x1017;   //在线人数
    public static final int EVENT_REFRESH_LIVE = 0x1018;   //在线人数
    public static final int EVENT_REFRESH_MASTER_TAB = 0x1019;   //切换大咖电台tab
    public static final int EVENT_CHANGE_TO_HOME_FRAGMENT = 0x1020;          //切换到首页
    public static final int EVENT_CHANGE_TO_MASTER = 0x1021;          //切换到大咖
    public static final int EVENT_CHANGE_TO_DIAGNOSE = 0x1022;          //切换到选股
    public static final int EVENT_CHANGE_TO_QUOTE = 0x1023;          //切换到行情


    public static final String MOBILE = "mobile";
    public static final String PASSWORD = "password";

    public static final String MARKET_CODE = "market_code";


}
