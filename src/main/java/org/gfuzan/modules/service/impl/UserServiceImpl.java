package org.gfuzan.modules.service.impl;

import java.util.List;

import org.gfuzan.datasources.DataSourceName;
import org.gfuzan.datasources.annotation.DataSource;
import org.gfuzan.modules.entity.User;
import org.gfuzan.modules.mapper.UserMapper;
import org.gfuzan.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames="UserServiceImpl")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper um;

	@DataSource(DataSourceName.FIRST)
	public List<User> getAllUser1() {
		return um.getAllUser();
	}

	@Override
	@DataSource(DataSourceName.SECOND)
	public List<User> getAllUser2() {
		return um.getAllUser();
	}
	
	@Override
	@Cacheable(cacheNames="getAllUser")
	public List<User> getAllUser() {
		System.err.println("getAllUser");
		return um.getAllUser();
	}

	@Override
	@CacheEvict(cacheNames="getAllUser")
	public int updateUser() {
		System.err.println("updateUser");
		return um.updateUser();
	}
}
