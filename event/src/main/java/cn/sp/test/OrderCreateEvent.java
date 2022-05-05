package cn.sp.test;

import cn.sp.event.Event;

/**
 * @author 2YSP
 * @date 2022/4/16 17:03
 */
public class OrderCreateEvent extends Event {

    private Order order;

    public OrderCreateEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
