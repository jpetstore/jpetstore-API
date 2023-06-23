package org.csu.jpetstoreapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.jpetstoreapi.VO.LineItemVO;
import org.csu.jpetstoreapi.VO.OrderVO;
import org.csu.jpetstoreapi.entity.*;
import org.csu.jpetstoreapi.persistence.*;
import org.csu.jpetstoreapi.service.CartService;
import org.csu.jpetstoreapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartService cartService;

    @Autowired
    private RefundOrdersMapper refundOrdersMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SequenceMapper sequenceMapper;

    @Autowired
    private LineItemMapper lineItemMapper;

    //退款申请
    public void addRefundOrder(String orderId, String msg){
        //P完成付款 Q待处理 Y已退款 S拒绝退款
        RefundOrders refundOrder = new RefundOrders();
        refundOrder.setOrderid(orderId);
        Order order = orderMapper.selectById(orderId);
        OrderStatus orderStatus =orderStatusMapper.selectById(order.getOrderId());
        System.out.println(orderStatus);
        orderStatus.setStatus("Q");
        orderStatusMapper.updateById(orderStatus);
        refundOrder.setRefund_amount(order.getTotalPrice());
        refundOrder.setRefund_reason(msg);
        refundOrder.set_processed(false);
        refundOrder.set_refused(false);
        refundOrder.setRefuse_reason(null);
        refundOrdersMapper.insert(refundOrder);
    }

    //获取对应用户的所有订单
    public List<OrderVO> getAllOrderVO(String userId){
        System.out.println(userId);
        QueryWrapper<Order> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("userid", userId);
        List<Order> orderList = orderMapper.selectList(queryWrapper);
        List<OrderVO> orderVOList = new ArrayList<>();
        ListIterator<Order> orderListIterator = orderList.listIterator();
        while(orderListIterator.hasNext()){
            Order order = orderListIterator.next();
            System.out.println(order);
            OrderStatus orderStatus =orderStatusMapper.selectById(order.getOrderId());
            System.out.println(orderStatus);
            OrderVO orderVO = orderToOrderVO(order, orderStatus);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    public OrderVO getOrderVO(String orderId){

        OrderStatus orderStatus =orderStatusMapper.selectById(orderId);
        Order order = orderMapper.selectById(orderId);
        OrderVO orderVO = orderToOrderVO(order,orderStatus);
        return orderVO;
    }

    private OrderVO orderToOrderVO(Order order, OrderStatus orderStatus){

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(order.getOrderId());
        orderVO.setUserId(order.getUserid());
        orderVO.setOrderDate(order.getOrderDate());
        orderVO.setShipAddress1(order.getShipAddress1());
        orderVO.setShipAddress2(order.getShipAddress2());
        orderVO.setShipCity(order.getShipCity());
        orderVO.setShipState(order.getShipState());
        orderVO.setShipZip(order.getShipZip());
        orderVO.setShipCountry(order.getShipCountry());
        orderVO.setBillAddress1(order.getBillAddress1());
        orderVO.setBillAddress2(order.getBillAddress2());
        orderVO.setBillCity(order.getBillCity());
        orderVO.setBillState(order.getBillState());
        orderVO.setBillZip(order.getBillZip());
        orderVO.setBillCountry(order.getBillCountry());
        orderVO.setCourier(order.getCourier());
        orderVO.setTotalPrice(order.getTotalPrice());
        orderVO.setBillToFirstName(order.getBillToFirstName());
        orderVO.setBillToLastName(order.getBillToLastName());
        orderVO.setShipToFirstName(order.getShipToFirstName());
        orderVO.setShipToLastName(order.getShipToLastName());
        orderVO.setCreditCard(order.getCreditCard());
        orderVO.setExpiryDate(order.getExpiryDate());
        orderVO.setCardType(order.getCardType());
        orderVO.setLocale(order.getLocale());
        orderVO.setIsExpr(order.getIsExpr());

        orderVO.setStatus(orderStatus.getStatus());
        return orderVO;
    }

    public int getNextOrderId(){
        Sequence ordernum = sequenceMapper.selectById("ordernum");
        return ordernum.getNextId()+1;
    }

    public void updateNextOrderId(){
        Sequence ordernum = sequenceMapper.selectById("ordernum");
        ordernum.setNextId(ordernum.getNextId()+1);
        sequenceMapper.updateById(ordernum);
    }

    public void InsertOrderVOToDB(OrderVO orderVO){

        //更新sequence表
        updateNextOrderId();

        //插入order表
        Order order = orderVOToOrder(orderVO);
        orderMapper.insert(order);
        //插入orderStatus表
        OrderStatus orderStatus = orderVOToOrderStatus(orderVO);
        orderStatusMapper.insert(orderStatus);

        //插入orderStatus表
        LineItem lineItem = new LineItem();
        lineItem.setOrderId(orderVO.getOrderId());
        List<LineItemVO> lineItems = orderVO.getLineItems();
        Iterator<LineItemVO> lineItemsIterator = lineItems.iterator();
        int num = 1;
        while(lineItemsIterator.hasNext()){
            LineItemVO lineItemVO = lineItemsIterator.next();
            lineItem.setLineNumber(num);
            lineItem.setItemId(lineItemVO.getItemId());
            lineItem.setQuantity(lineItemVO.getQuantity());
            lineItem.setUnitPrice(lineItemVO.getUnitPrice());
            lineItemMapper.insert(lineItem);
            num += 1;
        }

    }

    private Order orderVOToOrder(OrderVO orderVO){
        Order order = new Order();
        order.setOrderId(orderVO.getOrderId());
        order.setUserid(orderVO.getUserId());
        order.setOrderDate(orderVO.getOrderDate());
        order.setShipAddress1(orderVO.getShipAddress1());
        order.setShipAddress2(orderVO.getShipAddress2());
        order.setShipToFirstName(orderVO.getShipToFirstName());
        order.setShipToLastName(orderVO.getShipToLastName());
        order.setShipCity(orderVO.getShipCity());
        order.setShipState(orderVO.getShipState());
        order.setShipZip(orderVO.getShipZip());
        order.setShipCountry(orderVO.getShipCountry());
        order.setBillAddress1(orderVO.getBillAddress1());
        order.setBillAddress2(orderVO.getBillAddress2());
        order.setBillCity(orderVO.getBillCity());
        order.setBillState(orderVO.getBillState());
        order.setBillZip(orderVO.getBillZip());
        order.setBillCountry(orderVO.getBillCountry());
        order.setBillState(orderVO.getBillState());
        order.setCourier(orderVO.getCourier());
        order.setTotalPrice(orderVO.getTotalPrice());
        order.setBillToFirstName(orderVO.getBillToFirstName());
        order.setBillToLastName(orderVO.getBillToLastName());
        order.setCreditCard(orderVO.getCreditCard());
        order.setExpiryDate(orderVO.getExpiryDate());
        order.setCardType(orderVO.getCardType());
        order.setLocale(orderVO.getLocale());
        order.setIsExpr(orderVO.getIsExpr());
        return order;
    }

    private OrderStatus orderVOToOrderStatus(OrderVO orderVO){
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderVO.getOrderId());
        orderStatus.setLinenum(orderVO.getOrderId());
        orderStatus.setTimestamp(orderVO.getOrderDate());
        orderStatus.setStatus("P");
        return orderStatus;
    }


}
