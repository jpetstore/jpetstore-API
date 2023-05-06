package org.csu.jpetstoreapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.jpetstoreapi.VO.ItemVO;
import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.Category;
import org.csu.jpetstoreapi.entity.Item;
import org.csu.jpetstoreapi.entity.ItemInventory;
import org.csu.jpetstoreapi.entity.Product;
import org.csu.jpetstoreapi.persistence.CategoryMapper;
import org.csu.jpetstoreapi.persistence.ItemInventoryMapper;
import org.csu.jpetstoreapi.persistence.ItemMapper;
import org.csu.jpetstoreapi.persistence.ProductMapper;
import org.csu.jpetstoreapi.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//写下面value为了自动注解提高效率
@Service("catalogService")
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemInventoryMapper itemInventoryMapper;

    @Override
    public CommonResponse<List<Category>> getCategoryList() {
        List<Category> categoryList = categoryMapper.selectList(null);
        if(categoryList.isEmpty()){
            return CommonResponse.createForSuccessMessage("没有分类信息");
        }
        return CommonResponse.createForSuccess(categoryList);
    }

    @Override
    public CommonResponse<Category> getCategoryById(String categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if(category==null){
            return CommonResponse.createForSuccessMessage("没有该Id的category");
        }
        return CommonResponse.createForSuccess(category);
    }

    @Override
    public CommonResponse<List<Product>> getProductListByCategoryId(String categoryId) {
        QueryWrapper<Product> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("category",categoryId);

        List<Product> productList = productMapper.selectList(queryWrapper);
        if(productList.isEmpty()){
            return CommonResponse.createForSuccessMessage("该分类下没有Product子类");
        }
        return CommonResponse.createForSuccess(productList);
    }

    @Override
    public CommonResponse<Product> getProductById(String productId) {
        Product product = productMapper.selectById(productId);
        if(product==null){
            return CommonResponse.createForSuccessMessage("没有该Id的product");
        }
        return CommonResponse.createForSuccess(product);
    }

    @Override
    public CommonResponse<List<ItemVO>> getItemListByProductId(String productId) {
        QueryWrapper<Item> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("productid",productId);
        List<Item> itemList = itemMapper.selectList(queryWrapper);
        if(itemList.isEmpty()){
            return CommonResponse.createForSuccessMessage("该分类下没有Item子类");
        }
        Product product = productMapper.selectById(productId);
        List<ItemVO> itemVOList = new ArrayList<>();

        for(Item item:itemList){
            ItemInventory itemInventory = itemInventoryMapper.selectById(item.getItemId());
            ItemVO itemVO = itemToItemVO(item,product,itemInventory);
            itemVOList.add(itemVO);
        }

        return CommonResponse.createForSuccess(itemVOList);
    }

    @Override
    public CommonResponse<ItemVO> getItemById(String itemId) {
        Item item = itemMapper.selectById(itemId);
        if(item==null){
            return CommonResponse.createForSuccessMessage("没有该Id的item");
        }
        Product product = productMapper.selectById(item.getProductId());
        ItemInventory itemInventory = itemInventoryMapper.selectById(item.getItemId());
        ItemVO itemVO =itemToItemVO(item,product,itemInventory);
        return CommonResponse.createForSuccess(itemVO);
    }

    @Override
    public ItemVO getItem(String itemId) {
        Item item = itemMapper.selectById(itemId);
//        if(item==null){
//            return CommonResponse.createForSuccessMessage("没有该Id的item");
//        }
        Product product = productMapper.selectById(item.getProductId());
        ItemInventory itemInventory = itemInventoryMapper.selectById(item.getItemId());
        ItemVO itemVO =itemToItemVO(item,product,itemInventory);
        return  itemVO;
    }

    @Override
    public CommonResponse<List<Product>> searchProductList(String keyword){

        keyword = "%"+keyword.toLowerCase()+"%";
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",keyword);

        List<Product> productList = productMapper.selectList(queryWrapper);

        if(productList.isEmpty()){
            return CommonResponse.createForSuccessMessage("未搜寻到结果");
        }else {
            return CommonResponse.createForSuccess(productList);
        }

    }



    private ItemVO itemToItemVO(Item item,Product product,ItemInventory itemInventory){
        ItemVO itemVO = new ItemVO();
        itemVO.setItemId(item.getItemId());
        itemVO.setProductId(item.getProductId());
        itemVO.setListPrice(item.getListPrice());
        itemVO.setUnitCost(item.getUnitCost());
        itemVO.setSupplierId(item.getSupplierId());
        itemVO.setStatus(item.getStatus());
        itemVO.setAttribute1(item.getAttribute1());
        itemVO.setAttribute2(item.getAttribute2());
        itemVO.setAttribute3(item.getAttribute3());
        itemVO.setAttribute4(item.getAttribute4());
        itemVO.setAttribute5(item.getAttribute5());

        itemVO.setCategoryId(product.getCategoryId());
        itemVO.setProductName(product.getName());
        itemVO.setProductDescription(product.getDescription());
        itemVO.setQuantity(itemInventory.getQuantity());

        return itemVO;
    }
    @Override
    public boolean isItemInStock(String itemId){
        QueryWrapper<ItemInventory> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("itemId",itemId);
        ItemInventory itemInventory = itemInventoryMapper.selectOne(queryWrapper);
        return itemInventory.getQuantity()>0;
    }

}
