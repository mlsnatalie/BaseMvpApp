package com.libs.core.business.events;

public class MusicEvent extends BaseEvent {

    public static final int MUSIC_STATUS_CHANGE = 0x6000;

    public static final int BUFFERING = 0x6001;
    public static final int PLAYING = 0x6002;
    public static final int PAUSE = 0x6003;

    public MusicEvent(int eventId) {
        super(eventId);
    }
}
