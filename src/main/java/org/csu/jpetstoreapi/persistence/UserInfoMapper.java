package org.csu.jpetstoreapi.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.jpetstoreapi.entity.UserInfo;
import org.springframework.stereotype.Repository;

@Repository("userInfoMapper")
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
