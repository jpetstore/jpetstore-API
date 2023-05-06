package org.csu.jpetstoreapi.service;

import org.csu.jpetstoreapi.VO.OrderVO;

public interface OrderService {
    OrderVO getOrderVO(String orderId);
    int getNextOrderId();
    void updateNextOrderId();
    void InsertOrderVOToDB(OrderVO orderVO);
}
