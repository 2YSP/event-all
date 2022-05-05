package cn.sp.event;

import java.lang.annotation.*;

/**
 * @author 2YSP
 * @date 2022/4/16 17:35
 */
@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AsyncExecute {


}
