package cn.sp.publisher.impl;

import cn.sp.event.Event;
import cn.sp.manager.EventManager;
import cn.sp.publisher.EventPublisher;

/**
 * @author 2YSP
 * @date 2022/4/16 16:07
 */
public class DefaultEventPublisher implements EventPublisher {

    private EventManager eventManager;

    public DefaultEventPublisher(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * 发布事件
     *
     * @param event
     * @param <E>
     */
    @Override
    public <E extends Event> void publish(E event) {
        eventManager.notifyListener(event);
    }

    @Override
    public void publish(Object event) {
        eventManager.notifyListener(event);
    }
}
