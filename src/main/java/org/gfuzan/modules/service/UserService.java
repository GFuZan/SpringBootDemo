package org.gfuzan.modules.service;

import java.util.List;

import org.gfuzan.modules.dto.UserVo;

public interface UserService {

    public List<UserVo> getAllUser();
    
    public List<UserVo> getAllUser1();
    
    public List<UserVo> getAllUser2();
    
    public int updateUser();
    
    public int updateUserT();
    
    public int updateUserT1();
    
    public int updateUserT2();

	List<UserVo> getAllUserAll();

    List<UserVo> getAllUserPage(int pageNum);
    
    int testH2(String tableName, List<UserVo> userList);

    int testManualTransaction(String tableName);
}
