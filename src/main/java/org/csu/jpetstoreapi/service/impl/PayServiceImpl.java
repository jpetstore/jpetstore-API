package org.csu.jpetstoreapi.service.impl;

import com.alipay.api.AlipayApiException;
import org.csu.jpetstoreapi.VO.OrderVO;
import org.csu.jpetstoreapi.service.PayService;
import org.csu.jpetstoreapi.util.AlipayUtil;
import org.springframework.stereotype.Service;

/* 支付服务 */
@Service(value = "alipayOrderService")
public class PayServiceImpl implements PayService {
    @Override
    public String aliPay(OrderVO orderVo) throws AlipayApiException {
        return AlipayUtil.connect(orderVo);
    }
}