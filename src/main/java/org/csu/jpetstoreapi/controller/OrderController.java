package org.csu.jpetstoreapi.controller;

import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.User;
import org.csu.jpetstoreapi.service.CartService;
import org.csu.jpetstoreapi.service.OrderService;
import org.csu.jpetstoreapi.service.PayService;
import org.csu.jpetstoreapi.VO.CartItemVO;
import org.csu.jpetstoreapi.VO.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/order/")
public class OrderController {

    @Resource
    private PayService payService;//调用支付服务

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    //退款申请
    @RequestMapping("refundOrder")
    @ResponseBody
    public CommonResponse refundOrder(String orderid, String msg){
        orderService.addRefundOrder(orderid, msg);
        return CommonResponse.createForSuccess("退款申请已提交");
    }

    @GetMapping("getAllOrder")
    @ResponseBody
    public CommonResponse<List<OrderVO>>  getAllOrder(HttpSession session){
        User account = (User)session.getAttribute("loginUser");
        List<OrderVO> orderVOList = orderService.getAllOrderVO(account.getId());
        return CommonResponse.createForSuccess(orderVOList);
    }

    //根据购物车生成订单
    @GetMapping("getOrder")
    @ResponseBody
    public CommonResponse<OrderVO>  getOrder(HttpSession session){
        List<CartItemVO> cart = (List<CartItemVO>) session.getAttribute("cart");
        User account = (User)session.getAttribute("loginUser");

        OrderVO ordervo = new OrderVO();
        ordervo.initOrder(account,cart);
        session.setAttribute("order",ordervo);
        return CommonResponse.createForSuccess(ordervo);
    }

    @PostMapping("/confirmOrder")
    @ResponseBody
    public CommonResponse<OrderVO> confirmOrder(HttpServletRequest request, HttpSession session){
        int orderId = orderService.getNextOrderId();
        //PrintWriter out = response.getWriter();
        String shippingAddressRequired=request.getParameter("shippingAddressRequired");
        //没选修改地址
        if(shippingAddressRequired.equals("false")){
            String cardType = request.getParameter("cardType");
            String creditCard = request.getParameter("creditCard");
            String expiryDate = request.getParameter("expiryDate");
            OrderVO order = (OrderVO)session.getAttribute("order");
            order.setCardType(cardType);
            order.setCreditCard(creditCard);
            order.setExpiryDate(expiryDate);
            order.setOrderId(orderId);
            session.setAttribute("order",order);
            //out.print("Checked");
            return CommonResponse.createForSuccess(order);

        }else{
            String cardType = request.getParameter("cardType");
            String creditCard = request.getParameter("creditCard");
            String expiryDate = request.getParameter("expiryDate");
            User account = (User)session.getAttribute("loginUser");

            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String address1 = request.getParameter("address1");
            String address2 = request.getParameter("address2");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String zip = request.getParameter("zip");
            String country = request.getParameter("country");
            // 修改订单消息
            OrderVO order = (OrderVO)session.getAttribute("order");
            order.setOrderId(orderId);
            order.setCardType(cardType);
            order.setCreditCard(creditCard);
            order.setExpiryDate(expiryDate);

            order.setShipToFirstName(firstName);
            order.setShipToLastName(lastName);
            order.setBillToFirstName(account.getFirstname());
            order.setBillToLastName(account.getLastname());

            order.setShipAddress1(address1);
            order.setShipAddress2(address2);
            order.setBillAddress1(account.getAddr1());
            order.setBillAddress2(account.getAddr2());


            order.setShipCity(city);
            order.setBillCity(account.getCity());


            order.setShipState(state);
            order.setBillState(account.getState());


            order.setShipZip(zip);
            order.setBillZip(account.getZip());


            order.setShipCountry(country);
            order.setBillCountry(account.getCountry());


            session.setAttribute("order",order);
            //out.print("No Checked");
            return CommonResponse.createForSuccess(order);
        }
        //out.flush();
        //out.close();
    }

    @GetMapping("getConfirmOrder")
    @ResponseBody
    public CommonResponse<OrderVO>  getConfirmOrder(HttpSession session){
        OrderVO ordervo = (OrderVO)session.getAttribute("order");
        return CommonResponse.createForSuccess(ordervo);
    }


    @PostMapping("/finalOrder")
    @ResponseBody
    public String order(HttpSession session) throws AlipayApiException {
        OrderVO order = (OrderVO) session.getAttribute("order");

        cartService.updateCartToPay(session);

        orderService.InsertOrderVOToDB(order);

        return  payService.aliPay(new OrderVO()
                .setBody(order.getUserId())
                .setOut_trade_no(String.valueOf(order.getOrderId())+Math.random() * 100)
                .setTotal_amount(new StringBuffer().append(order.getTotalPrice()))
                .setSubject("MyPetStore OrderId: "+order.getOrderId()));
    }

}