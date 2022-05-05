package cn.sp.publisher;

import cn.sp.event.Event;

/**
 * @author Ship
 * @version 1.0.0
 * @description: 事件发布器
 * @date 2022/05/05 15:23
 */
public interface EventPublisher {

    /**
     * 发布继承Event类的事件
     * @param event
     * @param <E>
     */
    <E extends Event> void publish(E event);

    /**
     * 发布任意事件
     * @param event
     */
    void publish(Object event);
}
