package cn.sp.config;

import cn.sp.listener.OnceApplicationContextEventListener;
import cn.sp.manager.EventManager;
import cn.sp.manager.impl.DefaultEventManager;
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
    public EventManager eventManager() {
        return new DefaultEventManager();
    }

    @Bean
    public EventPublisher eventPublisher(@Autowired EventManager eventManager) {
        return new DefaultEventPublisher(eventManager);
    }

    @Bean
    public OnceApplicationContextEventListener onceApplicationListener() {
        return new OnceApplicationContextEventListener();
    }
}
