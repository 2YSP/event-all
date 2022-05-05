package cn.sp.event.test;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 2YSP
 * @date 2022/4/16 17:10
 */
@RequestMapping("/order")
@RestController
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("")
    public void create(@RequestBody Order order) {
        orderService.create(order);
    }
}
