package cn.sp.test;

import cn.sp.event.EventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 2YSP
 * @date 2022/4/16 17:02
 */
@Service
public class OrderService {

    @Resource
    private EventPublisher<OrderCreateEvent> publisher;


    /**
     * 创建订单
     *
     * @param order
     */
    public void create(Order order) {
        // 发送订单创建事件
        order.setOrderNo("sssss");
//        publisher.publish(new OrderCreateEvent(this, order));
        publisher.publish(order);
    }
}
