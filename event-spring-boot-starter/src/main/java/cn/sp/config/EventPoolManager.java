package cn.sp.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Ship
 * @version 1.0.0
 * @description: 枚举单例模式
 * @date 2022/05/06 16:31
 */
public enum EventPoolManager {

    INSTANCE;

    /**
     * 事件执行线程池
     */
    private final static ExecutorService EVENT_POOL = new ThreadPoolExecutor(4,
            8, 30L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(512), new ThreadFactoryBuilder().setNameFormat("event-pool-%d").build());

    /**
     * 执行任务
     *
     * @param command
     */
    public void execute(Runnable command) {
        EVENT_POOL.execute(command);
    }
}
