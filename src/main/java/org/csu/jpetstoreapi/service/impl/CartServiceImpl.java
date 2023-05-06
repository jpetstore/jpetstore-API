package org.csu.jpetstoreapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.csu.jpetstoreapi.VO.CartItemVO;
import org.csu.jpetstoreapi.VO.ItemVO;
import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.Cart;
import org.csu.jpetstoreapi.entity.User;
import org.csu.jpetstoreapi.persistence.CartMapper;
import org.csu.jpetstoreapi.service.CartService;
import org.csu.jpetstoreapi.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service("cartService")
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CatalogService catalogService;



    //重构
    //通过用户名获得没有支付的购物车项目
    @Override
    public CommonResponse<List<CartItemVO>> selectItemByUsername(String username, HttpSession session) {
        if(username==null){
            return CommonResponse.createForNeedLogin("请先登录后再查看购物车");
        }

        List<CartItemVO> cartItemList=new ArrayList<>();

        QueryWrapper<Cart> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        List<Cart> cartList = cartMapper.selectList(queryWrapper);
        System.out.println(cartList);


        Iterator cartListIterato=cartList.iterator();
        for(int i=0;cartListIterato.hasNext()&&i<cartList.size();i++) {
            CartItemVO result = new CartItemVO();
            String usernameTemp =cartList.get(i).getUsername();
            String itemId = cartList.get(i).getItemId();
            boolean isInStock = cartList.get(i).isInstock();
            int quantity = cartList.get(i).getQuantity();
            BigDecimal totalCost = cartList.get(i).getTotalCost();
            boolean pay = cartList.get(i).getPay();
            if(!pay) {
                result.setItemVO(catalogService.getItem(itemId));
                result.setUsername(usernameTemp);
                result.setInstock(isInStock);
                result.setQuantity(quantity);
                result.setTotalCost(totalCost);
                cartItemList.add(result);
            }
        }
        if(cartItemList.size()==0){
            return CommonResponse.createForSuccessMessage("购物车为空");
        }
        return CommonResponse.createForSuccess(cartItemList);
    }

    public void updateCartToPay(HttpSession session){
        User user = (User) session.getAttribute("loginUser");
        QueryWrapper<Cart> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",user.getId());
        List<Cart> cartList = cartMapper.selectList(queryWrapper);
        Iterator<Cart> cartIterator = cartList.iterator();
        while(cartIterator.hasNext()){
            Cart cart = cartIterator.next();
            cart.setPay(true);
            cartMapper.updateById(cart);
        }
    }


    //未重构部分
    @Override
    public void addItemByUsernameAndItemId(String username, ItemVO itemVO, boolean isInStock) {

        CartItemVO cartItemVO = new CartItemVO();
        cartItemVO.setUsername(username);
        cartItemVO.setItemVO(itemVO);
        cartItemVO.setQuantity(0);
        cartItemVO.setInstock(isInStock);
        cartItemVO.incrementQuantity();

        Cart cart = new Cart();
        cart.setUsername(username);
        cart.setItemId(itemVO.getItemId());
        cart.setInstock(cartItemVO.isInstock());
        cart.setQuantity(cartItemVO.getQuantity());
        cart.setTotalCost(cartItemVO.getTotalCost());

        cartMapper.insert(cart);

    }

    @Override
    public void incrementItemByUsernameAndItemId(String username, String itemId) {
        CartItemVO cartItemVO = getCartItemByUsernameAndItemId(username,itemId);
        cartItemVO.setItemVO(catalogService.getItem(itemId));
        cartItemVO.incrementQuantity();
        updateItemByItemIdAndQuantity(username,itemId,cartItemVO.getQuantity());
    }

    @Override
    public CartItemVO getCartItemByUsernameAndItemId(String username, String itemId) {

        CartItemVO cartItemVO = new CartItemVO();
        QueryWrapper<Cart> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("itemId",itemId);

        Cart cart = cartMapper.selectOne(queryWrapper);

        if(cart==null)return null;

        cartItemVO.setItemVO(catalogService.getItem(itemId));
        cartItemVO.setUsername(username);
        cartItemVO.setPay(cart.getPay());
        cartItemVO.setInstock(cart.isInstock());
        cartItemVO.setQuantity(cart.getQuantity());
        cartItemVO.setTotalCost(cart.getTotalCost());

        return cartItemVO;

    }

    @Override
    public void removeCartItemByUsernameAndItemId(String username, String itemId) {

        QueryWrapper<Cart> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("itemId",itemId);

        cartMapper.delete(queryWrapper);

    }

    @Override
    public void updateItemByItemIdAndQuantity(String username, String itemId, int quantity) {

        QueryWrapper<Cart> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("itemId",itemId);

        Cart cart = cartMapper.selectOne(queryWrapper);
        cart.setQuantity(quantity);

        CartItemVO cartItemVO = new CartItemVO();
        cartItemVO.setItemVO(catalogService.getItem(itemId));
        cartItemVO.updateQuantity(quantity);


        cart.setTotalCost(cartItemVO.getTotalCost());
//        System.out.println(".............total"+cartItemVO.getTotal());

        UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username",username);
        updateWrapper.eq("itemId",itemId);

        cartMapper.update(cart,updateWrapper);

    }

    @Override
    public void updateItemByItemIdAndPay(String username, String itemId, boolean pay) {

        QueryWrapper<Cart> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("itemId",itemId);

        Cart cart = cartMapper.selectOne(queryWrapper);
        cart.setPay(pay);

        UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username",username);
        updateWrapper.eq("itemId",itemId);

        cartMapper.update(cart,updateWrapper);

    }


    public void addItem(String username, BigDecimal listPrice, String itemId) {
        Cart cart = new Cart();
        cart.setUsername(username);
        cart.setItemId(itemId);
        cart.setInstock(true);
        cart.setQuantity(1);
        cart.setTotalCost(listPrice);
        cart.setPay(false);
        cartMapper.insert(cart);
    }
}
