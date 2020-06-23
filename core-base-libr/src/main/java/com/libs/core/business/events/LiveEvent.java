package com.libs.core.business.events;

public class LiveEvent extends BaseEvent {
    //直播聊天事件

    public LiveEvent(int eventId) {
        super(eventId);
    }

    public static final String CHAT_DATA = "chat_data";
    public static final String CHAT_PEOPLE = "chat_peopel";
    public static final String CHAT_MESSAGE = "chat_message";

//    public static final String EVENT_WD_LIVE_START = "wd_live_start";
//    public static final String EVENT_WD_LIVE_END = "wd_live_end";
//    public static final String EVENT_WD_START = "wd_start";
//    public static final String EVENT_WD_QS_END = "wd_qs_end";
//    public static final String EVENT_WD_END = "wd_end";
//    public static final String EVENT_WD_QUESTION = "wd_question";
//    public static final String EVENT_WD_ANSWERS = "wd_answers";
//    public static final String EVENT_COURSE_LIVE_START = "course_live_start";
//    public static final String EVENT_COURSE_LIVE_CLOSE = "course_live_close";
//    public static final String EVENT_COURSE_LIVE_PAUSE = "course_live_pause";
//    public static final String EVENT_COURSE_NOTICE = "course_notice";
//    public static final String EVENT_COURSE_ACTIVITY = "course_activity";

    public static final int LIVE_CHAT_DATA = 0x3001;          //聊天消息
    public static final int LIVE_CHAT_PEOPLE = 0x3002;          //聊天人数
    public static final int LIVE_CHAT_MESSAGE = 0x3003;          //聊天事件
    public static final int LIVE_CHAT_LIKE = 0x3004;          //点赞事件
    public static final int EVENT_SWITCH_LIVE = 0x3005;          //推送切换至直播
    public static final int EVENT_SWITCH_LIVEHOME = 0x3006;          //推送切换至页面
    public static final int EVENT_WEB_TO_LIVE = 0x3007;          //给H5发送广播用于请求视频相关信息

    public static final int LIVE_WD_LIVE_START = 0x3008;          //直播答题开始信号
    public static final int LIVE_WD_LIVE_END = 0x3009;          //直播答题结束信号
    public static final int LIVE_WD_START = 0x3010;          //推送答题开始信号
    public static final int LIVE_WD_QS_END = 0x300A;          //所有答题结束信号
    public static final int LIVE_WD_END = 0x300B;          //推送获奖榜单
    public static final int LIVE_WD_QUESTION = 0x300C;          //推送题目
    public static final int LIVE_WD_ANSWERS = 0x300D;          //推送答题情况
    public static final int LIVE_COURSE_LIVE_START = 0x300E; //开始直播
    public static final int LIVE_COURSE_LIVE_CLOSE = 0x300F; //关闭直播
    public static final int LIVE_COURSE_LIVE_PAUSE = 0x3010; //暂停直播
    public static final int LIVE_COURSE_NOTICE = 0x3011; //房间公告广播
    public static final int LIVE_COURSE_ACTIVITY = 0x3012; //房间活动广播


}
