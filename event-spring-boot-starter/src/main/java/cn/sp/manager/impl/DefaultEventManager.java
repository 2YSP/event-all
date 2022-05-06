package cn.sp.manager.impl;

import cn.sp.annotation.AsyncExecute;
import cn.sp.config.EventPoolManager;
import cn.sp.domain.EventListenerRegistration;
import cn.sp.domain.constant.EventConstants;
import cn.sp.event.Event;
import cn.sp.listener.EventListener;
import cn.sp.manager.EventManager;
import com.google.common.collect.Lists;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 2YSP
 * @date 2022/4/16 16:12
 */
public class DefaultEventManager implements EventManager {
    /**
     * 事件map
     */
    private static Map<Class<? extends Event>, List<EventListener>> map = new HashMap<>(64);
    /**
     * 事件监听器map,key:事件类型
     */
    private static Map<Class<?>, List<EventListenerRegistration>> listenerMap = new HashMap<>(64);


    /**
     * 注册一个事件监听器
     *
     * @param clazz
     * @param eventListener
     * @param <E>
     */
    @Override
    public <E extends Event> void registerListener(Class<? extends Event> clazz, EventListener<E> eventListener) {
        List<EventListener> list = map.get(clazz);
        if (CollectionUtils.isEmpty(list)) {
            map.put(clazz, Lists.newArrayList(eventListener));
        } else {
            list.add(eventListener);
            map.put(clazz, list);
        }
    }

    /**
     * 移除一个事件监听器
     *
     * @param clazz
     * @param <E>
     */
    public <E extends Event> void removeListener(Class<E> clazz) {
        map.remove(clazz);
    }

    /**
     * 通知所有该事件的监听器
     *
     * @param <E>
     */
    @Override
    public <E extends Event> void notifyListener(E e) {
        List<EventListener> eventListeners = map.get(e.getClass());
        if (CollectionUtils.isEmpty(eventListeners)) {
            return;
        }
        eventListeners.forEach(eventListener -> {
            boolean async = false;
            try {
                Method method = eventListener.getClass().getDeclaredMethod(EventConstants.EVENT_METHOD_NAME, Event.class);
                AsyncExecute asyncExecute = AnnotationUtils.findAnnotation(method, AsyncExecute.class);
                async = asyncExecute != null;
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            }
            if (!async) {
                // 同步执行
                eventListener.onEvent(e);
            } else {
                // 异步执行
                EventPoolManager.INSTANCE.execute(() -> eventListener.onEvent(e));
            }
        });
    }


    @Override
    public void registerListener(Class<?> eventClazz, EventListenerRegistration listenerRegistration) {
        if (listenerMap.containsKey(eventClazz)) {
            List<EventListenerRegistration> configList = listenerMap.get(eventClazz);
            configList.add(listenerRegistration);
            listenerMap.put(eventClazz, configList);
        } else {
            listenerMap.put(eventClazz, Lists.newArrayList(listenerRegistration));
        }
    }

    @Override
    public void notifyListener(Object event) {
        Class<?> eventClass = event.getClass();
        List<EventListenerRegistration> eventListenerRegistrations = listenerMap.get(eventClass);
        eventListenerRegistrations.forEach(config -> {
            Class<?> clazz = config.getClazz();
            Object bean = config.getBean();
            Assert.notNull(bean, "the bean of event listener can not be null!");
            Method method = null;
            try {
                method = clazz.getMethod(config.getMethodName(), eventClass);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (config.getAsync()) {
                Method method2 = method;
                EventPoolManager.INSTANCE.execute(() -> invoke(method2, bean, event));
            } else {
                invoke(method, bean, event);
            }
        });
    }

    private void invoke(Method method, Object target, Object args) {
        try {
            method.invoke(target, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
