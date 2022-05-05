package cn.sp.event;

/**
 * @author 2YSP
 * @description: 事件监听器
 * @date 2022/4/10 14:45
 */
public interface EventListener<E extends Event> {

    void onEvent(E e);

}
