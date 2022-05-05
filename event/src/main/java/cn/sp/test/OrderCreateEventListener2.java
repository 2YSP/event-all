package cn.sp.test;

import cn.sp.event.AsyncExecute;
import cn.sp.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author 2YSP
 * @date 2022/4/16 17:07
 */
@Component
public class OrderCreateEventListener2 implements EventListener<OrderCreateEvent> {

    @AsyncExecute
    @Override
    public void onEvent(OrderCreateEvent orderCreateEvent) {
        System.out.println(Thread.currentThread().getName() + "--监听订单创建事件2。。。。。。。。。");
        Order order = orderCreateEvent.getOrder();
        System.out.println(order.getOrderNo());
    }
}
