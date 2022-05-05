package cn.sp.event.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Ship
 * @version 1.0.0
 * @description:
 * @date 2022/04/24 11:42
 */
@Component
public class Test implements CommandLineRunner {

    @Resource
    private OrderService orderService;

    @Override
    public void run(String... args) throws Exception {
        orderService.create(new Order());
    }
}
