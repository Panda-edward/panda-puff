package demo;

import com.ed.demo.DemoApplication;
import com.ed.demo.OrderService;
import com.ed.demo.vo.AddOrderParam;
import com.ed.nuts.NutsHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author : Edward
 * @date : 2020/11/2 下午4:16
 */
@SpringBootTest(classes = DemoApplication.class)
@RunWith(SpringRunner.class)
public class OrderServiceTest {

    @Resource
    OrderService orderService;

    @Resource
    NutsHandler handler;


    @Test
    public void test() throws Exception{
        AddOrderParam param = new AddOrderParam().setOrderId("D283273").setPayTime(new Date());
        orderService.createOrder(param);

//        String s = "{\"bizNo\":\"123\",\"bizType\":\"sendMsg\",\"createTime\":1611127432657,\"nextRetryTime\":1611127432657,\"params\":\"{\\\"arg1\\\":{\\\"orderId\\\":\\\"D283273\\\",\\\"status\\\":1},\\\"arg0\\\":123}\",\"retryCount\":0,\"retryStatus\":0,\"updateTime\":1611127432657}\n";
//        handler.aSynHandle(JSONObject.parseObject(s, BizArgsLog.class));

        Thread.sleep(10000);
    }

}
