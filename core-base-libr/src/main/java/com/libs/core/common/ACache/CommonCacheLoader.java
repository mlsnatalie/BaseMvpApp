package com.libs.core.common.ACache;

import android.content.Context;
import android.text.TextUtils;

import androidx.collection.LruCache;

import com.libs.core.common.utils.LogUtils;
import com.libs.core.common.utils.MyUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zoubangyue on 2016/9/5.
 *
 * @link QuoteCacheLoader 和行情缓存的不同之处在于，此处使用/data/data/files/common目录，
 * 是长时间保存的数据文件，比如行情板块列表排序，行情指标等数据，应用没有提供清除这些的功能，
 * 因为这些数据长期有效，且不会过大。
 */
public class CommonCacheLoader {
    private String TAG = "CommonCacheLoader";

    /*内存缓存。Lru latest recently user 简单说就是喜新厌旧式缓存。*/
    private LruCache<String, Object> mMemoryCache;

    /*磁盘缓存管理器。ASimpleCache框架。*/
    private ACache mDiskCache;

    private Map<String, Integer> mExpireMap = new HashMap<>();

    /*单例缓存加载器*/
    private static CommonCacheLoader loader;

    public static CommonCacheLoader getInstance(Context context) {
        if (loader == null) {
            synchronized (CommonCacheLoader.class) {
                if (loader == null) {
                    loader = new CommonCacheLoader(context);
                }
            }
        }
        return loader;
    }

    private CommonCacheLoader(final Context context) {
        //获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
//        int maxMemory = (int) Runtime.getRuntime().maxMemory();
//        int mCacheSize = maxMemory / 8;
        //给LruCache分配1/8 1M
        mMemoryCache = new LruCache<String, Object>(1 * 1024 * 1024) {

            @Override
            protected int sizeOf(String key, Object value) {
                // 因为无法计算java对象的大小，所以设置400kb
                //kline 300根大概60k
                //min/premin 最大大概180k
                if (key.contains("min"))
                    return 1024 * 200;
                else if (key.contains("kline"))
                    return 1024 * 60;
                return 1024 * 50;
            }

            /**
             * 记录被移除时的回调。
             * 1.空间不够需要移除
             * 2.新元素替换老元素
             * 注意：新增一个新的不会走这个回调
             * @param evicted
             * @param key
             * @param oldValue
             * @param newValue
             */
            @Override
            protected void entryRemoved(boolean evicted, String key, Object oldValue, Object newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                try {
                    if (evicted) {
                        // 释放空间时，持久化到磁盘
                        Integer expire = mExpireMap.remove(key);

                        if (expire != null && expire.intValue() > 0) {
                            putToDeviceCache(key, oldValue, expire);
                            LogUtils.d(TAG, "lru release space, persistenting[" + key + "][" + expire + "] to disk.");
                        } else {
                            putToDeviceCache(key, oldValue);
                            LogUtils.d(TAG, "lru release space, persistenting[" + key + "][-1] to disk.");
                        }
                    } else {
                        // 添加实体，添加后直接持久化
                        if (newValue != null) {
                            Integer expire = mExpireMap.remove(key);
                            LogUtils.d(TAG, "lru lazy putting[" + key + "]expire[" + expire + "] entry.");
                            //persistent 加入缓存后进行延迟持久化
                            if (expire > 0)
                                putToDeviceCache(key, newValue, expire);
                            else
                                putToDeviceCache(key, newValue);
                        } else {
                            //移除实体
                            mExpireMap.remove(key);
                            try {
                                mDiskCache.remove(key);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            LogUtils.d(TAG, "lru removing[" + key + "] entry.");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        try {
            mDiskCache = ACache.get(MyUtils.getInstance().getFile(context, "commoncache"));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                mDiskCache = ACache.get(context, "commoncache");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 添加数据到内存缓存。
     * 替换老数据。
     * <p/>
     * 当首次放入内存时，不会调用entryRemoved回调，所以此处需要直接持久化
     *
     * @param key
     * @param data
     */
    private void putToMemoryCache(String key, Object data) {
        if (!TextUtils.isEmpty(key) && data != null) {
            Object previous = mMemoryCache.put(key, data);
            LogUtils.d(TAG, "lru putting to memcache[" + key + "] entry.");
            if (previous == null) {
                Integer expire = mExpireMap.remove(key);
                if (expire == null) expire = -1;
                putToDeviceCache(key, data, expire);
            }
        }
    }

    /**
     * 添加数据到内存缓存。
     * 替换老数据。
     *
     * @param key
     * @param data
     * @param withoutToDisk 只放入缓存，不入磁盘
     */
    private void putToMemoryCache(String key, Object data, boolean withoutToDisk) {
        if (!TextUtils.isEmpty(key) && data != null) {
            mMemoryCache.put(key, data);
            LogUtils.d(TAG, "lru putting to memcache[" + key + "] entry.");
        }
    }

    /**
     * 添加数据到磁盘缓存。
     * 替换老数据。
     *
     * @param key
     * @param data
     * @return 是否保存成功
     */
    private boolean putToDeviceCache(String key, Object data) {
        return putToDeviceCache(key, data, -1);
    }

    /**
     * 添加数据到磁盘缓存。
     * 替换老数据。
     *
     * @param key
     * @param data
     * @return 是否保存成功
     */
    private boolean putToDeviceCache(String key, Object data, int expire) {
        if (!TextUtils.isEmpty(key) && data instanceof Serializable) {
            try {
                LogUtils.d(TAG, "lru putting to disk[" + key + "] entry.");
                mDiskCache.put(key, (Serializable) data, expire);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 添加数据到缓存。
     * 替换老数据。
     *
     * @param key
     * @param data
     */
    public void put(String key, Object data) {
        mExpireMap.put(key, -1);
        putToMemoryCache(key, data);
    }

    /**
     * 添加数据到缓存。
     * 替换老数据。
     *
     * @param key
     * @param data
     */
    public void put(String key, Object data, int expire) {
        mExpireMap.put(key, expire);
        putToMemoryCache(key, data);
    }

    /**
     * 立马存入内存和磁盘
     *
     * @param key
     * @param obj
     * @param putToDisk
     */
    public void put(String key, Object obj, boolean putToDisk) {
        mExpireMap.put(key, -1);
        putToMemoryCache(key, obj, putToDisk);
        if (putToDisk) {
            putToDeviceCache(key, obj);
        }
    }

    /**
     * 移除内存数据，同时也会持久化到磁盘。
     *
     * @param key
     */
    public void remove(String key) {
        if (!TextUtils.isEmpty(key)) {
            mMemoryCache.remove(key);
        }
    }

//    /**
//     * 从内存和磁盘删除
//     *
//     * @param key
//     * @param immediately
//     */
//    public void remove(String key, boolean immediately) {
//        try {
//            remove(key);
//            mDiskCache.remove(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 从内存缓存中获取数据。
     *
     * @param key
     * @return
     */
    private Object getDataFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 从磁盘缓存中获取数据。
     *
     * @param key
     * @return
     */
    private Object getDataFromDeviceCache(String key) {
        Object data = mDiskCache.getAsObject(key);
        //将磁盘获取到的数据，放入内存
        putToMemoryCache(key, data, true);
        return data;
    }

    /**
     * 同步加载对象缓存。
     *
     * @param key
     * @return
     */
    public Object loadData(final String key) {
        return loadData(key, (onDataLoaderListener) null);
    }

    /**
     * 同步加载对象缓存。
     *
     * @param key
     * @return
     */
    public Object loadData(final String key, Class<?> clazz) {
        Object object = loadData(key, (onDataLoaderListener) null);
        if (object != null && object.getClass() == clazz) {
            return object;
        }
        return null;
    }

    /**
     * 获取缓存，若加回调则内存没获取到就开始通过异步进行加载。
     *
     * @param key
     * @param listener
     * @return
     */
    public Object loadData(final String key, final onDataLoaderListener listener) {
        Object data = showCacheData(key);

        if (data != null) {
            return data;
        }

//        if (listener != null) {
//            final Handler handler = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    listener.onDataLoader(msg.obj, key);
//                }
//            };
//
//            getThreadPool().execute(new Runnable() {
//                @Override
//                public void run() {
//                    // 从磁盘加载
//                    Object data = null;
//                    try {
//                        data = getDataFromDeviceCache(key);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    //通知回调处理
//                    Message msg = handler.obtainMessage();
//                    msg.obj = data;
//                    handler.sendMessage(msg);
//
//                    // 加入内存缓存
//                    putToMemoryCache(key, data);
//                }
//            });
//        }

        return null;
    }

    /**
     * 获取数据, 内存中没有就去手机或者sd卡中获取，这一步在getView中会调用，比较关键的一步
     *
     * @param key
     * @return
     */
    private Object showCacheData(String key) {
        Object data = getDataFromMemCache(key);
        if (data != null) {
            // 从内存获取到数据
            LogUtils.d(TAG, "geted from memcache[" + key + "]");
            return data;
        } else {
            //从磁盘获取缓存数据
            try {
                data = getDataFromDeviceCache(key);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (data != null)
                LogUtils.d(TAG, "geted from diskcache[" + key + "]");
            else
                LogUtils.d(TAG, "no data from memcache and diskcache[" + key + "]");

            return data;
        }
    }

    /**
     * 异步加载缓存数据。
     */
    public interface onDataLoaderListener {
        void onDataLoader(Object data, String key);
    }
}
