package com.libs.core.business.events;

public class QuoteEvent extends BaseEvent {

    public QuoteEvent(int eventId) {
        super(eventId);
    }


    public static final int EVENT_REFRESH_INDEX_CONFIG = 0x5001;
    public static final int EVENT_REFRESH_CHART_CONFIG = 0x5002;
}
