package com.libs.core.business.events;

public class ShareEvent extends BaseEvent {

    public ShareEvent(int eventId) {
        super(eventId);
    }


    /**
     * 分享
     */
    public static final int EVENT_SAVE_SHARE_IMG = 0x1001;
    public static final int EVENT_SHARE_POSTER = 0x1002;
    public static final int EVENT_SHARE_POSTER_SENT = 0x1003;
    public static final int EVENT_SHARE_POSTER_CANCLE = 0x1004;
    public static final int EVENT_SHARE_POSTER_SAVE = 0x1005;
    public static final int EVENT_SHARE_COPY_LINK = 0x1006;    // 复制链接
    public static final int EVENT_SHARE_MINI_PROGRAM = 0x1007;    // 分享微信小程序
}
