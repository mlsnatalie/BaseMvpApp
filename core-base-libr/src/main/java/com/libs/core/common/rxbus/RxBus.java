package com.libs.core.common.rxbus;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * 支持背压处理的RxBus
 * 适合数据密集发送的场景
 */
public class RxBus {

    private static volatile RxBus rxbus;

    private final FlowableProcessor<Object> bus;


    private RxBus() {
        // toSerialized method made bus thread safe
        bus = PublishProcessor.create().toSerialized();
    }

    public static RxBus getInstance() {
        if (rxbus == null) {
            synchronized (RxBus.class) {
                if (rxbus == null) {
                    rxbus = new RxBus();
                }
            }
        }
        return rxbus;
    }

    /**
     * register rx event
     */
    public <T> Flowable<T> register(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    /**
     * post rx event
     */
    public void post(Object obj) {
        try {
            bus.onNext(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
