package cn.sp.event;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author Ship
 * @version 1.0.0
 * @description: 事件监听标记注解
 * @date 2022/04/24 11:17
 */
@Component
@Documented
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyEventListener {

    /**
     * 是否异步执行，默认否
     *
     * @return
     */
    boolean async() default false;

}
