package org.csu.jpetstoreapi.controller;

import com.zhenzi.sms.ZhenziSmsClient;
import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.Category;
import org.csu.jpetstoreapi.entity.Item;
import org.csu.jpetstoreapi.entity.User;
import org.csu.jpetstoreapi.persistence.CartMapper;
import org.csu.jpetstoreapi.service.CartService;
import org.csu.jpetstoreapi.service.CatalogService;
import org.csu.jpetstoreapi.service.UserService;
import org.csu.jpetstoreapi.util.RandomNumberUtil;
import org.csu.jpetstoreapi.VO.CartItemVO;
import org.csu.jpetstoreapi.VO.ItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CatalogService catalogService;

    @GetMapping("getCart")
    @ResponseBody
    public CommonResponse<List<CartItemVO>> selectItemByUsername(HttpSession session){
        User user = (User) session.getAttribute("loginUser");//当前登录的用户
        System.out.println(user);
        String username;
        if(user != null)
            username = user.getId();
        else
            username = null;
        CommonResponse<List<CartItemVO>> listCartItemVOResponse = cartService.selectItemByUsername(username,session);
        session.setAttribute("cart",listCartItemVOResponse.getData());
//        System.out.println("........resp"+listCartItemVOResponse.getStatus());
        return listCartItemVOResponse;
    }

    @PostMapping("updateCartItem")
    @ResponseBody
    public void updateCartItem(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {

        User user = (User) session.getAttribute("loginUser");
        String username = user.getId();
        String itemId = request.getParameter("itemId");
        String quantityStr = request.getParameter("quantity");
        int quantity = 0;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if(quantityStr == "" || quantityStr.equals(null)){
            cartService.removeCartItemByUsernameAndItemId(username, itemId);
            out.write("{\"isRemoved\":\""+true+"{\"itemId\":\"" + itemId + "\"}");
        }
        else {
            quantity = Integer.parseInt(quantityStr);
            System.out.println(quantity);

            if(quantity == 0){
                cartService.removeCartItemByUsernameAndItemId(username, itemId);
                out.write("{\"isRemoved\":\"" + true + "\",\"itemId\":\"" + itemId + "\"}");
            }
            else {
                cartService.updateItemByItemIdAndQuantity(username, itemId, quantity);
                CartItemVO item = cartService.getCartItemByUsernameAndItemId(username, itemId);
                String html = "<fmt:formatNumber type='number' pattern='$#,##0.00'>$" + item.getTotalCost() + "</fmt:formatNumber>";
                out.write("{\"isRemoved\":\"" + false + "\",\"itemId\":\"" + itemId + "\",\"quantity\":\"" + quantity +
                        "\",\"totalcost\":\"" + item.getTotalCost() + "\",\"html\":\"" + html + "\"}");
            }
        }
        out.flush();
        out.close();

    }

    @GetMapping("removeItemFromCart")
    @ResponseBody
    public CommonResponse removeItemFromCart(String itemId,HttpSession session){

        User user = (User) session.getAttribute("loginUser");
        String username = user.getId();
        CartItemVO cartItemVO = cartService.getCartItemByUsernameAndItemId(username,itemId);
        if(cartItemVO != null){
            cartService.removeCartItemByUsernameAndItemId(username,itemId);
        }

        CommonResponse<List<CartItemVO> > cart = cartService.selectItemByUsername(username,session);

        return CommonResponse.createForSuccess();
    }

    @GetMapping("/addItemToCart")
    @ResponseBody
    public CommonResponse addItemToCart(String workingItemId, HttpSession session){
        User user = (User) session.getAttribute("loginUser");
        System.out.println(user);
        String username = user.getId();
        CartItemVO cartItemVO = cartService.getCartItemByUsernameAndItemId(username, workingItemId);

        if (cartItemVO != null) {
            if(!cartItemVO.isPay()) {
                cartService.incrementItemByUsernameAndItemId(username, workingItemId);
            }
            else {
                cartService.updateItemByItemIdAndPay(username, workingItemId, false);
                cartService.updateItemByItemIdAndQuantity(username, workingItemId, 1);
            }
        } else {
            boolean isInStock = catalogService.isItemInStock(workingItemId);
            ItemVO itemVO = catalogService.getItem(workingItemId);
            cartService.addItemByUsernameAndItemId(username, itemVO, isInStock);
        }

        CommonResponse<List<CartItemVO> > cart = cartService.selectItemByUsername(username,session);

        return CommonResponse.createForSuccess();
    }

}
