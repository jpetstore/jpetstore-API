package org.csu.jpetstoreapi.service;

import org.csu.jpetstoreapi.VO.ReviewVO;
import org.csu.jpetstoreapi.common.CommonResponse;
import org.csu.jpetstoreapi.entity.Product;
import org.csu.jpetstoreapi.entity.Review;
import org.csu.jpetstoreapi.entity.User;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ReviewService {

    CommonResponse deleteReviewById(Integer id);
    CommonResponse<List<Review>> getReviewListByUsernameAndItemid(String username, String itemid);

    CommonResponse<List<Review>> getReviewListById(Integer id);
    CommonResponse<List<Review>> getReviewListByUsername(String username);
    CommonResponse<List<Review>> getReviewListByItemId(String itemid);
    CommonResponse<List<Review>> getAllReviewList();
    CommonResponse<Review> insertReview(Review review);

    CommonResponse setTop(Integer id);
    CommonResponse cancelTop(Integer id);

}
