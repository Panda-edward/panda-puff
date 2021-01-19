package hunting;

import com.ed.panda.PuffApplication;
import com.ed.panda.business.MqService;
import com.ed.panda.business.OrderService;
import com.ed.panda.business.vo.AddOrderParam;
import com.ed.panda.business.vo.OrderStatusMsgVO;
import com.ed.panda.hunting.context.HuntingContext;
import com.ed.panda.hunting.context.PreyInfo;
import com.ed.panda.hunting.invoker.PreyInvoker;
import com.ed.panda.hunting.repository.ILocalMsgLogRepository;
import com.ed.panda.hunting.repository.LocalMsgLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author : Edward
 * @date : 2020/11/2 下午4:16
 */
@SpringBootTest(classes = PuffApplication.class)
@RunWith(SpringRunner.class)
public class HuntingTest {

    @Resource
    OrderService orderService;



    @Test
    public void test() {
        AddOrderParam param = new AddOrderParam().setOrderId("D283273").setPayTime(new Date());
        orderService.createOrder(param);
    }


    @Resource
    PreyInvoker preyInvoker;

    @Resource
    ILocalMsgLogRepository repository;

    @Test
    public void test2() throws Exception{
        PreyInfo preyInfo = new PreyInfo().setBizIdx(0).setTargetClass(MqService.class).setMethod(MqService.class.getMethod("sendMsg", Long.class,OrderStatusMsgVO.class));
        HuntingContext.setPreyInfo("c_order",preyInfo);

        List<LocalMsgLog> localMsgLogs = repository.queryRetryLogs(10);
        preyInvoker.invoke(localMsgLogs.get(0));
    }


}
