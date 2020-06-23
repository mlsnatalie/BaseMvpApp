package com.libs.core.business.events;

/**
 * 用户相关的事件
 */
public class MessageEvent extends BaseEvent {

    public MessageEvent(int eventId) {
        super(eventId);
    }

    //消息中心消息数量
    public static final int EVENT_MESSAGE_CENTER = 0x4001;

    public static final int EVENT_MASTER_TAB = 0x4002;

    public static final int EVENT_CHAT_MESSAGE = 5000;

}
