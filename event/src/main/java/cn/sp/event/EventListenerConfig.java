package cn.sp.event;

/**
 * @author Ship
 * @version 1.0.0
 * @description:
 * @date 2022/04/24 13:59
 */
public class EventListenerConfig {

    /**
     * 类
     */
    private Class<?> clazz;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 是否异步执行
     */
    private Boolean async;


    public EventListenerConfig() {
    }

    public EventListenerConfig(Class<?> clazz, String methodName, Boolean async) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.async = async;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Boolean getAsync() {
        return async;
    }

    public void setAsync(Boolean async) {
        this.async = async;
    }
}
