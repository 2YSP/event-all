package cn.sp.manager;

import cn.sp.domain.EventListenerRegistration;
import cn.sp.event.Event;
import cn.sp.listener.EventListener;

/**
 * @author Ship
 * @version 1.0.0
 * @description:
 * @date 2022/05/05 15:26
 */
public interface EventListenerManager {


    /**
     * 注册一个事件监听器
     *
     * @param clazz         事件类型
     * @param eventListener
     * @param <E>
     */
    <E extends Event> void registerListener(Class<? extends Event> clazz, EventListener<E> eventListener);

    /**
     * 通知所有该事件的监听器
     *
     * @param e
     * @param <E>
     */
    <E extends Event> void notifyListener(E e);

    /**
     * 注册一个事件监听器
     *
     * @param eventClazz 事件类型
     * @param listenerRegistration
     */
    void registerListener(Class<?> eventClazz, EventListenerRegistration listenerRegistration);

    /**
     * 通知所有该事件的监听器
     *
     * @param event
     */
    void notifyListener(Object event);

}
