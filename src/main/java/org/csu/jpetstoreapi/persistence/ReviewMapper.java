package org.csu.jpetstoreapi.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.csu.jpetstoreapi.entity.Product;
import org.csu.jpetstoreapi.entity.Review;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewMapper extends BaseMapper<Review> {
    @Select("SELECT MAX(id) FROM review")
    Integer selectMaxId();
}