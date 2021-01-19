package com.ed.panda.business.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author : Edward
 * @date : 2020/11/2 下午4:12
 */
@Data
@Accessors(chain = true)
public class AddOrderParam {

    private String orderId;

    private Date payTime;
}
