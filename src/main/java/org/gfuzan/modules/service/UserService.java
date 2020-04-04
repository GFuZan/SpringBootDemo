package org.gfuzan.modules.service;

import java.util.List;

import org.gfuzan.modules.entity.User;

public interface UserService {

    public List<User> getAllUser();
    
    public List<User> getAllUser1();
    
    public List<User> getAllUser2();
    
    public int updateUser();
    
    public int updateUserT();
    
    public int updateUserT1();
    
    public int updateUserT2();

	List<User> getAllUserAll();

    List<User> getAllUserPage(int pageNum);
    
    int testH2(String tableName, List<User> userList);
}
