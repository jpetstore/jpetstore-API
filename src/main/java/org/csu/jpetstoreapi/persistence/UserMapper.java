package org.csu.jpetstoreapi.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.jpetstoreapi.entity.User;
import org.springframework.stereotype.Repository;

@Repository("userMapper")
public interface UserMapper extends BaseMapper<User> {
}
