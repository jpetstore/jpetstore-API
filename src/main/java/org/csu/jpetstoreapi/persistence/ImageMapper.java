package org.csu.jpetstoreapi.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.jpetstoreapi.entity.ImageOfProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMapper extends BaseMapper<ImageOfProduct> {
}
