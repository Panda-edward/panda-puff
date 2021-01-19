package com.ed.panda.business;

import com.ed.panda.business.vo.AddOrderParam;
import com.ed.panda.business.vo.OrderStatusMsgVO;
import com.ed.panda.hunting.anno.Hunter;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Edward
 * @date : 2020/11/2 下午4:35
 */
@Service
public class OrderService {

    @Resource
    MqService mqService;

    @Transactional(rollbackFor = Exception.class)
    @Hunter(bizType = "c_order")
    public void createOrder(AddOrderParam param) {
        System.out.println("创建订单:" + param.getOrderId());
        OrderStatusMsgVO msgVO = new OrderStatusMsgVO()
                .setOrderId(param.getOrderId()).setStatus(1);
        mqService.sendMsg(123L, msgVO);
    }

    public static void main(String[] args) throws Exception{
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(templateLoader);
        configuration.setDefaultEncoding("UTF-8");

        Map<String, Object> map = new HashMap<>();
        map.put("bizTradeNo", "d12345");
        Map<String, Object> channel = new HashMap<>();
        channel.put("channelId",101);
        map.put("channel",channel);
        String temp = "{\n" +
                "\t\"sourceTradeNo\": \"${bizTradeNo}\",\n" +
                "\t\"channelId\": ${channel.channelId},\n" +
                "}";
        StringWriter stringWriter = new StringWriter();
        Template template = new Template("templateName", temp, configuration);
        template.process(map, stringWriter);
        String ret = stringWriter.toString();
        System.out.println(ret);
    }
}
