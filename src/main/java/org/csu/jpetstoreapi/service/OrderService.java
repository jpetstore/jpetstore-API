package org.csu.jpetstoreapi.service;

import org.csu.jpetstoreapi.VO.OrderVO;

import java.util.List;

public interface OrderService {
    public List<OrderVO> getAllOrderVO(String userId);
    OrderVO getOrderVO(String orderId);
    int getNextOrderId();
    void updateNextOrderId();
    void InsertOrderVOToDB(OrderVO orderVO);
}
