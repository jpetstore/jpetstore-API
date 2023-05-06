package org.csu.jpetstoreapi.service;

import com.alipay.api.AlipayApiException;
import org.csu.jpetstoreapi.VO.OrderVO;

public interface PayService {
    /*支付宝*/
    String aliPay(OrderVO orderVo) throws AlipayApiException;
}
