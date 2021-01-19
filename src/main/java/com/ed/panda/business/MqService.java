package com.ed.panda.business;

import com.ed.panda.business.vo.OrderStatusMsgVO;
import com.ed.panda.hunting.anno.BizSerialNo;
import com.ed.panda.hunting.anno.Prey;
import org.springframework.stereotype.Service;

/**
 * @author : Edward
 * @date : 2020/11/2 下午4:08
 */
@Service
public class MqService {

    @Prey
    public void sendMsg(@BizSerialNo Long bizRequestId, OrderStatusMsgVO msgVO) {
        System.out.println("发送消息:" + msgVO);
    }
}
