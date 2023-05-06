package org.csu.jpetstoreapi.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.jpetstoreapi.entity.ItemInventory;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemInventoryMapper extends BaseMapper<ItemInventory> {
}

