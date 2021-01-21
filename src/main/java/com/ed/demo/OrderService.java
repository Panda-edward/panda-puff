package com.ed.demo;

import com.ed.demo.vo.AddOrderParam;
import com.ed.demo.vo.OrderStatusMsgVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author : Edward
 * @date : 2020/11/2 下午4:35
 */
@Service
public class OrderService {

    @Resource
    MqService mqService;

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(AddOrderParam param) {
        System.out.println("创建订单:" + param.getOrderId());
        OrderStatusMsgVO msgVO = new OrderStatusMsgVO()
                .setOrderId(param.getOrderId()).setStatus(1);
        mqService.sendMsg(123L, msgVO);
    }
}
