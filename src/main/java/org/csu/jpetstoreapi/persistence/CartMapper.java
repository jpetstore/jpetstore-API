package org.csu.jpetstoreapi.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.jpetstoreapi.entity.Cart;
import org.springframework.stereotype.Repository;

@Repository
public interface CartMapper extends BaseMapper<Cart> {
}
