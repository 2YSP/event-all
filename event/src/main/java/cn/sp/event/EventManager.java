package cn.sp.event;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 2YSP
 * @date 2022/4/16 16:12
 */
@Component
public class EventManager implements ApplicationContextAware {
    /**
     * 事件map
     */
    private static Map<Class<? extends Event>, List<EventListener>> map = new HashMap<>(64);
    /**
     * 事件监听器map,key:事件类型
     */
    private static Map<Class<?>, List<EventListenerConfig>> listenerMap = new HashMap<>(64);

    private static ApplicationContext applicationContext;

    private static final String EVENT_METHOD = "onEvent";

    /**
     * 事件执行线程池
     */
    private static ExecutorService eventPool = new ThreadPoolExecutor(4,
            8, 30L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(512), new ThreadFactoryBuilder().setNameFormat("event-pool-%d").build());

    /**
     * 初始化事件缓存map
     */
    @PostConstruct
    private void initEventMap() {
        Map<String, EventListener> beanMap = applicationContext.getBeansOfType(EventListener.class);
        if (beanMap == null) {
            return;
        }
        beanMap.forEach((key, value) -> {
            // 反射获取onEvent方法的参数类型
            Method[] methods = ReflectionUtils.getDeclaredMethods(value.getClass());
            for (Method method : methods) {
                if (method.getName().equals(EVENT_METHOD)) {
                    Parameter parameter = method.getParameters()[0];
                    // 参数必须为Event的子类
                    if (parameter.getType().getName().equals(Event.class.getName())) {
                        continue;
                    }
                    registerListener((Class<? extends Event>) parameter.getType(), value);
                }
            }
        });
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(MyEventListener.class);
        if (map == null) {
            return;
        }
        map.forEach((key, value) -> {
            // 获取所有method
            Class<?> listenerClazz = value.getClass();
            Method[] methods = ReflectionUtils.getDeclaredMethods(listenerClazz);
            for (Method method : methods) {
                MyEventListener myEventListener = AnnotationUtils.findAnnotation(method, MyEventListener.class);
                if (myEventListener == null) {
                    continue;
                }
                Parameter parameter = method.getParameters()[0];
                Class<?> eventClazz = parameter.getType();
                EventListenerConfig config = new EventListenerConfig(listenerClazz, method.getName(), myEventListener.async());
                if (listenerMap.containsKey(eventClazz)) {
                    List<EventListenerConfig> configList = listenerMap.get(eventClazz);
                    configList.add(config);
                    listenerMap.put(eventClazz, configList);
                } else {
                    listenerMap.put(eventClazz, Lists.newArrayList(config));
                }
            }
        });
    }


    /**
     * 注册一个事件监听器
     *
     * @param clazz
     * @param eventListener
     * @param <E>
     */
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
    public <E extends Event> void notifyListener(E e) {
        List<EventListener> eventListeners = map.get(e.getClass());
        if (CollectionUtils.isEmpty(eventListeners)) {
            return;
        }
        eventListeners.forEach(eventListener -> {
            boolean async = false;
            try {
                Method method = eventListener.getClass().getDeclaredMethod(EVENT_METHOD, Event.class);
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
                eventPool.execute(() -> eventListener.onEvent(e));
            }
        });
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        EventManager.applicationContext = applicationContext;
    }


    public void notifyListener(Object event) {
        Class<?> eventClass = event.getClass();
        List<EventListenerConfig> eventListenerConfigs = listenerMap.get(eventClass);
        eventListenerConfigs.forEach(config -> {
            Object bean = applicationContext.getBean(config.getClazz());
            Method method = null;
            try {
                method = bean.getClass().getMethod(config.getMethodName(), eventClass);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (config.getAsync()) {
                Method method2 = method;
                eventPool.execute(() -> {
                    invoke(method2, bean, event);
                });
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
