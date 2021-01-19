package com.ed.panda.business.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : Edward
 * @date : 2020/11/3 下午4:27
 */
@Data
@Accessors(chain = true)
public class OrderStatusMsgVO {

    private String orderId;

    private Integer status;
}
