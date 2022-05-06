package cn.sp.config;

import cn.sp.listener.OnceApplicationContextEventListener;
import cn.sp.manager.EventListenerManager;
import cn.sp.manager.impl.DefaultEventListenerManager;
import cn.sp.publisher.EventPublisher;
import cn.sp.publisher.impl.DefaultEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ship
 * @version 1.0.0
 * @description:
 * @date 2022/05/05 15:09
 */
@Configuration
public class EventStarterAutoConfigure {

    @Bean
    public EventListenerManager eventListenerManager() {
        return new DefaultEventListenerManager();
    }

    @Bean
    public EventPublisher eventPublisher(@Autowired EventListenerManager eventListenerManager) {
        return new DefaultEventPublisher(eventListenerManager);
    }

    @Bean
    public OnceApplicationContextEventListener onceApplicationListener() {
        return new OnceApplicationContextEventListener();
    }
}
