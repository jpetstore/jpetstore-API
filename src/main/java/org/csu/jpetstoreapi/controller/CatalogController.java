package org.csu.jpetstoreapi.controller;


import org.csu.jpetstoreapi.VO.ItemVO;
import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.Category;
import org.csu.jpetstoreapi.entity.Item;
import org.csu.jpetstoreapi.entity.Product;
import org.csu.jpetstoreapi.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/catalog/")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping("categories")
    @ResponseBody
    public CommonResponse<List<Category>> getCategoryList(){
        return catalogService.getCategoryList();
    }

    @GetMapping("categories/{id}")
    @ResponseBody
    public CommonResponse<Category> getCategoryById(@PathVariable("id")String categoryId){
        return catalogService.getCategoryById(categoryId);
    }

    @GetMapping("categories/{id}/products")
    @ResponseBody
    public CommonResponse<List<Product>> getProductListByCategoryId(@PathVariable("id")String categoryId){
        return catalogService.getProductListByCategoryId(categoryId);
    }

    @GetMapping("product/{id}")
    @ResponseBody
    public CommonResponse<Product> getProductById(@PathVariable("id")String productId){
        return catalogService.getProductById(productId);
    }

    @GetMapping("product/{id}/items")
    @ResponseBody
    public CommonResponse<List<ItemVO>> getItemListByProductId(@PathVariable("id")String productId){
        return catalogService.getItemListByProductId(productId);
    }

    @GetMapping("items/{id}")
    @ResponseBody
    public CommonResponse<ItemVO> getItemById(@PathVariable("id")String itemId){
        return catalogService.getItemById(itemId);
    }

    @PostMapping("search")
    @ResponseBody
    public CommonResponse<List<Product>> searchProduct(@RequestParam("keyword") String keyword){
        System.out.println(keyword);
        return catalogService.searchProductList(keyword);
    }

    @GetMapping("searchThis")
    public void searchAutoComplete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String keyword;
        keyword=request.getParameter("keyword");
        Product product=new Product();
        Item item=new Item();
        List<Product> productList=new ArrayList<Product>();

        CommonResponse<List<Product>> res=catalogService.searchProductList(keyword);

        if(res.getMsg()!="未搜寻到结果"){
            productList = res.getData();
            StringBuffer sb = new StringBuffer("[");
            for(int i=0;i<productList.size();i++){
                if(i== productList.size()-1) {
                    sb.append("\"" + productList.get(i).getName() + "\"]");
                }else{
                    sb.append("\"" + productList.get(i).getName() + "\",");
                }
            }
            response.getWriter().write(sb.toString());
        }

    }

    @RequestMapping(value = "/showImage")
    public  void showImage(HttpServletResponse response, HttpServletRequest request) throws IOException, SQLException {
        String productId = request.getParameter("productId");
        byte[] bb=catalogService.getimage(productId);
        //从数据库读取出二进制数据……
        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = response.getOutputStream();
        sos.write(bb, 0, bb.length);
        sos.close();
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/gif/png");
    }
}
