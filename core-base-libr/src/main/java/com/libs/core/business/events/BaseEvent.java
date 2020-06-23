package com.libs.core.business.events;


import android.content.Intent;

/**
 * 通用简单事件基类
 *
 * @author zhang.zheng
 * @version 2018-06-15
 */
public abstract class BaseEvent extends Intent {

    private static final String EVENT_ID = "event_id";

    public BaseEvent(int eventId) {
        putExtra(EVENT_ID, eventId);
    }

    public int getEventId() {
        return getIntExtra(EVENT_ID, 0);
    }


}
