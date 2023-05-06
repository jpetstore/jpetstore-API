package org.csu.jpetstoreapi.service;

import org.csu.jpetstoreapi.VO.CartItemVO;
import org.csu.jpetstoreapi.VO.ItemVO;
import org.csu.jpetstoreapi.common.CommonResponse;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    void addItemByUsernameAndItemId(String username, ItemVO item, boolean isInStock);

    void incrementItemByUsernameAndItemId(String username,String itemId);

    CartItemVO getCartItemByUsernameAndItemId(String username, String itemId);

    void removeCartItemByUsernameAndItemId(String username, String itemId);

    void updateItemByItemIdAndQuantity(String username, String itemId, int quantity);

    CommonResponse<List<CartItemVO>> selectItemByUsername(String username, HttpSession session );

    void updateItemByItemIdAndPay(String username, String itemId, boolean pay);

    void updateCartToPay(HttpSession session);

    void addItem(String username, BigDecimal listPrice, String itemId);
}
