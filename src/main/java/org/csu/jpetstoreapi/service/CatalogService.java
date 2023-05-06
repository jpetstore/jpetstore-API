package org.csu.jpetstoreapi.service;

import org.csu.jpetstoreapi.VO.ItemVO;
import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.Category;
import org.csu.jpetstoreapi.entity.Product;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CatalogService {
    CommonResponse<List<Category>> getCategoryList();
    CommonResponse<Category> getCategoryById(@PathVariable("id")String categoryId);
    CommonResponse<List<Product>> getProductListByCategoryId(@PathVariable("id")String categoryId);
    CommonResponse<Product> getProductById(@PathVariable("id")String productId);
    CommonResponse<List<ItemVO>> getItemListByProductId(@PathVariable("id")String productId);
    CommonResponse<ItemVO> getItemById(@PathVariable("id")String itemId);
    CommonResponse<List<Product>> searchProductList(String keyword);

    boolean isItemInStock(String itemId);
    ItemVO getItem(String itemId);
}