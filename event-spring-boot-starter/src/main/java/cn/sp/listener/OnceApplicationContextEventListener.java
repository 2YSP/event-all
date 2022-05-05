package cn.sp.listener;

import cn.sp.annotation.MyEventListener;
import cn.sp.domain.EventListenerRegistration;
import cn.sp.domain.constant.EventConstants;
import cn.sp.event.Event;
import cn.sp.manager.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import static org.springframework.util.ObjectUtils.nullSafeEquals;

/**
 * @author Ship
 * @version 1.0.0
 * @description:
 * @date 2022/05/05 15:42
 */
public class OnceApplicationContextEventListener implements ApplicationListener, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static EventManager eventManager;

    private static Logger logger = LoggerFactory.getLogger(OnceApplicationContextEventListener.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        OnceApplicationContextEventListener.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (isOriginalEventSource(event) && event instanceof ApplicationContextEvent) {
            onApplicationContextEvent((ApplicationContextEvent) event);
        }
    }

    private void onApplicationContextEvent(ApplicationContextEvent event) {
        OnceApplicationContextEventListener.eventManager = applicationContext.getBean(EventManager.class);
        logger.info("start to init event spring boot starter config...");
        initConfig();
        logger.info("init event spring boot starter config end.");
    }

    private boolean isOriginalEventSource(ApplicationEvent event) {
        boolean originalEventSource = nullSafeEquals(getApplicationContext(), event.getSource());
        if (!originalEventSource) {
            if (logger.isDebugEnabled()) {
                logger.debug("The source of event[" + event.getSource() + "] is not original!");
            }
        }
        return originalEventSource;
    }

    public ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new NullPointerException("applicationContext must be not null, it has to invoke " +
                    "setApplicationContext(ApplicationContext) method first if "
                    + ClassUtils.getShortName(getClass()) + " instance is not a Spring Bean");
        }
        return applicationContext;
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        // 根据接口注册监听器
        registerListenerByInterface();
        // 根据注解注册监听器
        registerListenerByAnnotation();
    }

    private void registerListenerByInterface() {
        Map<String, EventListener> beanMap = applicationContext.getBeansOfType(EventListener.class);
        if (beanMap == null) {
            return;
        }
        beanMap.forEach((key, value) -> {
            // 反射获取onEvent方法的参数类型
            Method[] methods = value.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(EventConstants.EVENT_METHOD_NAME)) {
                    Parameter parameter = method.getParameters()[0];
                    // 参数必须为Event的子类
                    if (parameter.getType().getName().equals(Event.class.getName())) {
                        continue;
                    }
                    eventManager.registerListener((Class<? extends Event>) parameter.getType(), value);
                }
            }
        });
    }

    private void registerListenerByAnnotation() {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(MyEventListener.class);
        if (map == null) {
            return;
        }
        map.forEach((key, value) -> {
            // 获取所有method
            Class<?> listenerClazz = value.getClass();
            Method[] methods = listenerClazz.getDeclaredMethods();
            for (Method method : methods) {
                MyEventListener myEventListener = AnnotationUtils.findAnnotation(method, MyEventListener.class);
                if (myEventListener == null) {
                    continue;
                }
                Parameter parameter = method.getParameters()[0];
                Class<?> eventClazz = parameter.getType();
                EventListenerRegistration registration = new EventListenerRegistration(listenerClazz, method.getName(), myEventListener.async());
                eventManager.registerListener(eventClazz, registration);
            }
        });
    }
}
