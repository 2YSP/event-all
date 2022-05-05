package cn.sp.event;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 2YSP
 * @date 2022/4/16 16:07
 */
@Component
public class EventPublisher<E extends Event> {

    @Resource
    private EventManager eventManager;

    /**
     * 发布事件
     * @param event
     * @param <E>
     */
    public <E extends Event> void publish(E event) {
        eventManager.notifyListener(event);
    }


    public void publish(Object event){
        eventManager.notifyListener(event);
    }
}
