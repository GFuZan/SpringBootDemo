package org.gfuzan.modules.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.gfuzan.modules.entity.User;

@Mapper
public interface UserMapper {
    public List<User> getAllUser();

	public int updateUser();
}
