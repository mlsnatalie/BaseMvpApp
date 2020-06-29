//package com.libs.core.business.sensor;
//
//import com.libs.core.common.manager.UserManager;
//import com.libs.core.common.utils.DateUtil;
//import com.libs.core.common.utils.LogUtils;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
///**
// * 神策业务统计类
// *
// * @author zhang.zheng
// * @version 2018-06-19
// */
//public class SensorsUtils {
//
//    /**
//     * APP启动
//     */
//    public static void appStart(boolean isFirstStart) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            // 是否首次启动
//            jsonObject.put("is_first_strart", isFirstStart);
//            // 渠道ID
//            jsonObject.put("channel", SensorsTracker.getInstance().getAppChannel());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track(SensorConst.M_APP_START, jsonObject);
//    }
//
//    /**
//     * 点击注册
//     */
//    public static void clickRegister() {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            // 注册渠道
//            jsonObject.put("register_channel", SensorsTracker.getInstance().getAppChannel());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("register_xigua", jsonObject);
//    }
//
//    /**
//     * 西瓜智选股账号登录
//     */
//    public static void trackLogin(String login_method) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            // 注册渠道
//            jsonObject.put("login_method", login_method);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("login_xigua_userid", jsonObject);
//    }
//
//    /**
//     * 客服聊天
//     */
//    public static void trackCustomerChat(String cs_name, String click_name) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            // 客服名称
//            jsonObject.put("cs_name", cs_name);
//            // 点击名称
//            jsonObject.put(SensorConst.APP_CLICK_NAME, click_name);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track(SensorConst.M_CUSTOMER_CHAT, jsonObject);
//    }
//
//    /**
//     * 忘记密码
//     */
//    public static void trackForgetPassWord(String code, String modify) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("is_code", code);
//            jsonObject.put("is_modify", modify);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("forget_spsset", jsonObject);
//    }
//
//    /**
//     * 修改头像
//     */
//    public static void trackModifyHead(String modify_method, String modify) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("modify_method", modify_method);
//            jsonObject.put("is_modify", modify);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("modify_headpic", jsonObject);
//    }
//
//    /**
//     * 修改昵称
//     */
//    public static void trackModifyNickName(String modify) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("is_modify", modify);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("modify_nick", jsonObject);
//    }
//
//    public static void trackSetupPassword(String modify) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("is_modify", modify);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("setup_pwd", jsonObject);
//    }
//
//    public static void trackWxLogin(String state) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("state", state);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("login_click_wx", jsonObject);
//    }
//
//    public static void trackMobileLogin() {
//        JSONObject jsonObject = new JSONObject();
//        SensorsTracker.getInstance().track("login_mobile", jsonObject);
//    }
//
//    public static void trackMobileAuthLogin(String phone, boolean is_ok) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("phone", phone);
//            jsonObject.put("is_ok", is_ok);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("mobile_auth", jsonObject);
//    }
//
//
//    public static void trackMobileBind(String phone, String modify) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("phone", phone);
//            jsonObject.put("is_modify", modify);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("bind_mobile", jsonObject);
//    }
//
//
//    /**
//     * 修改密码
//     */
//    public static void trackModifyPassword(String modify) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("is_modify", modify);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("modify_pwd", jsonObject);
//    }
//
//    public static void trackRegisterCode(boolean is_ok, boolean is_reg) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("is_ok", is_ok);
//            jsonObject.put("is_reg", is_reg);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("get_vercode", jsonObject);
//    }
//
//    public static void trackEnterCode(String phone) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("phone", phone);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("input_vercode", jsonObject);
//    }
//
//    public static void trackEnterPhone() {
//        JSONObject jsonObject = new JSONObject();
//        SensorsTracker.getInstance().track("input_mobile", jsonObject);
//    }
//
//    public static void trackNextStep(String phone) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("phone", phone);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("login_next_step", jsonObject);
//    }
//
//    public static void trackLoginPassword(String phone) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("phone", phone);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("login_click_pwd", jsonObject);
//    }
//
//    public static void trackLoginPasswordAgain(String phone) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("phone", phone);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("set_ps_again", jsonObject);
//    }
//
//    public static void trackRegisterOk(String phone, boolean ok) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("phone", phone);
//            jsonObject.put("is_ok", ok);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("register_ok_xigua", jsonObject);
//    }
//
//    public static void trackReplacePhone(String is_code, String is_modify) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("is_code", is_code);
//            jsonObject.put("is_modify", is_modify);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track("modify_mobile", jsonObject);
//    }
//
//    public static void playVideo(String modelName, String clickName, String videoId) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("ViewID", videoId);
//            jsonObject.put(SensorConst.APP_CLICK_NAME, clickName);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track(modelName, jsonObject);
//    }
//
//    public static void stockSearch(String modelName, String clickName) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put(SensorConst.APP_CLICK_NAME, clickName);
//            jsonObject.put("click_src", "研报");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track(modelName, jsonObject);
//    }
//
//    /**
//     * 公共方法1
//     */
//    public static void trackCommon(String modelName, String clickName) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("authority", UserManager.getInstance().getChaosIndex());
//            jsonObject.put(SensorConst.APP_CLICK_NAME, clickName);
//
//            SensorsTracker.getInstance().track(modelName, jsonObject);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 公共方法1
//     */
//    public static void trackKingStock(String modelName, String clickName, String ssdx_assisstant_plat, String ssdx_assisstant_name) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("authority", UserManager.getInstance().getChaosIndex());
//            jsonObject.put(SensorConst.APP_CLICK_NAME, clickName);
//            jsonObject.put("ssdx_assisstant_plat", ssdx_assisstant_plat);
//            jsonObject.put("ssdx_assisstant_name", ssdx_assisstant_name);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track(modelName, jsonObject);
//    }
//
//    public static void clickNotification(String modelName, String clickName, String channel) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("push_channel", channel);
//            jsonObject.put(SensorConst.APP_CLICK_NAME, clickName);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track(modelName, jsonObject);
//    }
//
//
//    public static void clickABTest(String modelName, String clickName, String abType) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put(SensorConst.APP_CLICK_NAME, clickName);
//            jsonObject.put("ab_version", abType);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        SensorsTracker.getInstance().track(modelName, jsonObject);
//    }
//
//
//    /**
//     * 通用map埋点方法(可变参数实现)
//     */
//    public static void tracks(String event, String... ags) {
//        try {
//            JSONObject json = new JSONObject();
//            json.put("create_time", DateUtil.getCurrentDate(DateUtil.dateFormatYMDHMS));// 时间
//            json.put("UserID", UserManager.getInstance().getUid());
//            json.put("nick_name", UserManager.getInstance().isLogin() ? UserManager.getInstance().getUserVo().getNickName() : "");
//            json.put("plat", "Android");
//            Map<String, String> map = new LinkedHashMap<>();
//            for (int i = 0; i < ags.length; i = i + 2) {
//                if (i > ags.length - 1) {
//                    break;
//                }
//                map.put(ags[i], ags[i + 1]);
//            }
//            for (String s : map.keySet()) {
//                json.put(s, map.get(s));
//            }
////            SensorsDataAPI.sharedInstance().track(event, json);
//            printEventAndJson(event, json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 打印方法
//     */
//    private static void printEventAndJson(String event, JSONObject json) {
//        LogUtils.d("SensorUtils", event + ">>>>>>" + json.toString());
//    }
//
//
//}
