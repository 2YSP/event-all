package cn.sp.test;

import cn.sp.event.MyEventListener;

/**
 * @author Ship
 * @version 1.0.0
 * @description:
 * @date 2022/04/24 11:20
 */
@MyEventListener
public class OrderCreateEventListener3 {

    @MyEventListener(async = true)
    public void onListen(Order order){
        System.out.println(Thread.currentThread().getName() + "--监听订单创建事件3。。。。。。。。。");
    }
}
