package cn.sp.publisher.impl;

import cn.sp.event.Event;
import cn.sp.manager.EventListenerManager;
import cn.sp.publisher.EventPublisher;

/**
 * @author 2YSP
 * @date 2022/4/16 16:07
 */
public class DefaultEventPublisher implements EventPublisher {

    private EventListenerManager eventListenerManager;

    public DefaultEventPublisher(EventListenerManager eventListenerManager) {
        this.eventListenerManager = eventListenerManager;
    }

    /**
     * 发布事件
     *
     * @param event
     * @param <E>
     */
    @Override
    public <E extends Event> void publish(E event) {
        eventListenerManager.notifyListener(event);
    }

    @Override
    public void publish(Object event) {
        eventListenerManager.notifyListener(event);
    }
}
