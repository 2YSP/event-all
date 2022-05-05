package cn.sp.listener;

import cn.sp.event.Event;

/**
 * @author 2YSP
 * @description: 事件监听器
 * @date 2022/4/10 14:45
 */
public interface EventListener<E extends Event> {
    /**
     * 触发事件
     * @param e
     */
    void onEvent(E e);

}
