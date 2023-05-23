package org.csu.jpetstoreapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.jpetstoreapi.VO.ReviewVO;
import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.Review;
import org.csu.jpetstoreapi.persistence.ReviewMapper;
import org.csu.jpetstoreapi.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public CommonResponse deleteReviewById(Integer id) {
        QueryWrapper<Review> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",id);

        List<Review> reviewList = reviewMapper.selectList(queryWrapper);
        if(reviewList.size()==0){
            return CommonResponse.createForError("无此评论");
        }
        reviewMapper.deleteById(id);
        return CommonResponse.createForSuccess();
    }

    @Override
    public CommonResponse<List<Review>> getReviewListByUsernameAndItemid(String username, String itemid) {
        QueryWrapper<Review> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.eq("itemid",itemid);

        List<Review> reviewList = reviewMapper.selectList(queryWrapper);
//        List<ReviewVO> reviewVOList = new ArrayList<>();
//
//        Iterator reviewListIterato=reviewList.iterator();
//        for(int i=0;reviewListIterato.hasNext()&&i<reviewList.size();i++) {
//            ReviewVO result = new ReviewVO();
//
//            String reviewtmp = reviewList.get(i).getReview();
//            Integer grade = reviewList.get(i).getGrade();
//            Integer top = reviewList.get(i).getTop();
//            String time = reviewList.get(i).getTime();
//
//            result.setReview(reviewtmp);
//            result.setTime(time);
//            result.setGrade(grade);
//            result.setTop(top);
//
//            reviewVOList.add(result);
//        }
        if(reviewList.size()==0){
            return CommonResponse.createForSuccessMessage("评论为空");
        }
        return CommonResponse.createForSuccess(reviewList);
    }

    @Override
    public CommonResponse<List<Review>> getReviewListById(Integer id) {
        QueryWrapper<Review> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",id);

        List<Review> reviewList = reviewMapper.selectList(queryWrapper);
        if(reviewList.size()==0){
            return CommonResponse.createForSuccessMessage("评论为空");
        }
        return CommonResponse.createForSuccess(reviewList);
    }

    @Override
    public CommonResponse<List<Review>> getReviewListByUsername(String username) {
        QueryWrapper<Review> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);

        List<Review> reviewList = reviewMapper.selectList(queryWrapper);
        if(reviewList.size()==0){
            return CommonResponse.createForSuccessMessage("评论为空");
        }
        return CommonResponse.createForSuccess(reviewList);
    }

    @Override
    public CommonResponse<List<Review>> getReviewListByItemId(String itemid) {
        QueryWrapper<Review> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("itemid",itemid);

        List<Review> reviewList = reviewMapper.selectList(queryWrapper);
        if(reviewList.size()==0){
            return CommonResponse.createForSuccessMessage("评论为空");
        }
        return CommonResponse.createForSuccess(reviewList);
    }

    @Override
    public CommonResponse<List<Review>> getAllReviewList() {
        return CommonResponse.createForSuccess(reviewMapper.selectList(null));
    }

    @Override
    public CommonResponse<Review> insertReview(Review review) {
        Integer idmax = reviewMapper.selectMaxId() + 1;
        review.setId(idmax);
        if(review==null){
            return CommonResponse.createForError("请输入正确的格式");
        }
        reviewMapper.insert(review);
        return CommonResponse.createForSuccess("加入评论成功",review);
    }

    @Override
    public CommonResponse setTop(Integer id) {
        //将一个评论在其所在商品下置顶，其余商品取消置顶
        Review result1 = reviewMapper.selectById(id);

        String itemid = result1.getItemid();
        Map<String, Object> map = new HashMap<>();
        map.put("itemid", itemid);
        map.put("top", 1);
        List<Review> result2 = reviewMapper.selectByMap(map);

        result1.setTop(1);
        reviewMapper.updateById(result1);
        if(!result2.isEmpty()){
            result2.get(0).setTop(0);
            reviewMapper.updateById(result2.get(0));
        }
        return CommonResponse.createForSuccess();
    }

    @Override
    public CommonResponse cancelTop(Integer id) {
        Review review = reviewMapper.selectById(id);
        review.setTop(0);
        reviewMapper.updateById(review);
        return CommonResponse.createForSuccess();
    }
}
