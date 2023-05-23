package org.csu.jpetstoreapi.controller;

import org.csu.jpetstoreapi.VO.ReviewVO;
import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.Review;
import org.csu.jpetstoreapi.entity.User;
import org.csu.jpetstoreapi.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/review/")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @DeleteMapping("/deletebyId")
    @ResponseBody
    public CommonResponse deleteReviewById(@RequestParam("id") Integer id){
        return reviewService.deleteReviewById(id);
    }

    @GetMapping("/byId")
    @ResponseBody
    public CommonResponse<List<Review>> getReviewListById(@RequestParam("id") Integer id){
        return reviewService.getReviewListById(id);
    }

    @GetMapping("/byItemid")
    @ResponseBody
    public CommonResponse<List<Review>> getReviewListByItemId(@RequestParam("itemid") String itemid){
        return reviewService.getReviewListByItemId(itemid);
    }

    @GetMapping("/byUsername")
    @ResponseBody
    public CommonResponse<List<Review>> getReviewListByUsername(@RequestParam("username") String username){
        return reviewService.getReviewListByUsername(username);
    }

    @GetMapping("/byUsernameAndItemid")
    @ResponseBody
    public CommonResponse<List<Review>> getReviewListByUsernameAndItemid
            (@RequestParam("username") String username,@RequestParam("itemid") String itemid){
        return reviewService.getReviewListByUsernameAndItemid(username,itemid);
    }

    @GetMapping("/all")
    @ResponseBody
    public CommonResponse<List<Review>> getAllReview(){
        return reviewService.getAllReviewList();
    }

    @PostMapping("/settop")
    @ResponseBody
    public CommonResponse setTop(@RequestParam("id") Integer id){
        return reviewService.setTop(id);
    }

    @PostMapping("/canceltop")
    @ResponseBody
    public CommonResponse cancelTop(@RequestParam("id") Integer id){
        return reviewService.cancelTop(id);
    }


    @PostMapping("/insert")
    @ResponseBody
    public CommonResponse<Review> insertReview(HttpServletRequest request){
        Review review = new Review();

        String username = request.getParameter("username");
        String itemId = request.getParameter("itemid");
        String review1 = request.getParameter("review");
        String grade1 = request.getParameter("grade");
        Integer grade = Integer.parseInt(grade1);

//        String id1 = request.getParameter("id");
//        Integer id = Integer.parseInt(id1);
//        String top1 = request.getParameter("top");
//        Integer top = Integer.parseInt(top1);
        //默认top为0
        Integer top = 0;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String time = formatter.format(date).toString();

        review.setReview(review1);
        review.setGrade(grade);
        review.setTime(time);
        review.setTop(top);
        review.setUsername(username);
        review.setItemid(itemId);
        return reviewService.insertReview(review);
    }

}
