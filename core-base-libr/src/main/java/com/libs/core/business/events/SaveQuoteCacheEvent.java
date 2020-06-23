package com.libs.core.business.events;

/**
 * 保存行情数据缓存事件。
 * <p/>
 * Created by zoubangyue on 2016/6/27.
 */
public class SaveQuoteCacheEvent {
    private String key;

    public SaveQuoteCacheEvent(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SaveQuoteCacheEvent) {
            if (key == ((SaveQuoteCacheEvent) o).getKey() || (key != null && key.equals(((SaveQuoteCacheEvent) o).getKey())))
                return true;
        }
        return super.equals(o);
    }
}
