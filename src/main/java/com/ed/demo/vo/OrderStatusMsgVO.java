package com.ed.demo.vo;

import com.ed.nuts.NutsVO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : Edward
 * @date : 2020/11/3 下午4:27
 */
@Data
@Accessors(chain = true)
public class OrderStatusMsgVO implements NutsVO {

    private String orderId;

    private Integer status;

    /**
     * 获取业务主键
     *
     * @return
     */
    @Override
    public String getBizNo() {
        return orderId + "_" + status;
    }
}
