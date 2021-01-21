package com.ed.demo;

import com.ed.demo.vo.OrderStatusMsgVO;
import com.ed.nuts.RetryStrategy;
import com.ed.nuts.anno.Nuts;
import com.ed.nuts.anno.NutsBizNo;
import com.ed.nuts.anno.NutsRetry;
import org.springframework.stereotype.Service;

/**
 * @author : Edward
 * @date : 2020/11/2 下午4:08
 */
@Service
public class MqService {

    @Nuts(name = "sendMsg", nutsRetry = @NutsRetry(interval = 30, strategy = RetryStrategy.FIXED))
    public void sendMsg(@NutsBizNo Long bizRequestId, OrderStatusMsgVO msgVO) {
        System.out.println("发送消息:" + msgVO);
    }
}
