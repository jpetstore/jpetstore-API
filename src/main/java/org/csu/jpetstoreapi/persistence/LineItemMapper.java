package org.csu.jpetstoreapi.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.jpetstoreapi.entity.LineItem;
import org.springframework.stereotype.Repository;

@Repository
public interface LineItemMapper extends BaseMapper<LineItem> {
}
