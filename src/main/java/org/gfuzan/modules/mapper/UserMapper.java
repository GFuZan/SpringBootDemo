package org.gfuzan.modules.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.gfuzan.modules.entity.User;

@Mapper
public interface UserMapper {
    public List<User> getAllUser();

    public int updateUser();

    public int createUserTable(@Param("tableName") String tableName);

    public int insertUser(@Param("tableName") String tableName, @Param("userList") List<User> userList);

    public int sumAge(@Param("tableName") String tableName);

    public int dropUserTable(@Param("tableName") String tableName);
}
