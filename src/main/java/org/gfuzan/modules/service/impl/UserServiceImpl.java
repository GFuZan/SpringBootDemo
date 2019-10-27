package org.gfuzan.modules.service.impl;

import java.time.LocalTime;
import java.util.List;

import org.gfuzan.datasources.DataSourceName;
import org.gfuzan.datasources.annotation.DataSource;
import org.gfuzan.modules.entity.User;
import org.gfuzan.modules.mapper.UserMapper;
import org.gfuzan.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper um;

	@Override
	@DataSource(DataSourceName.FIRST)
	public List<User> getAllUserAll() {
		
		getAllUser1();
		getAllUser2();
		getAllUser2();
		getAllUser1();
		
		return um.getAllUser();
	}

	@Override
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
	@Cacheable(cacheNames="AllUser")
	public List<User> getAllUser() {
		System.err.println("getAllUser");
		return um.getAllUser();
	}

	@Override
	@CacheEvict(cacheNames="AllUser")
	public int updateUser() {
		System.err.println("updateUser");
		return um.updateUser();
	}
	
	

	@Transactional(rollbackFor=Exception.class)
	public int updateUserT() {
		um.updateUser();
		throw new NullPointerException();
	}
	
	@Override
	@DataSource(DataSourceName.FIRST)
	@Transactional(rollbackFor=Exception.class)
	public int updateUserT1() {
		um.updateUser();
		throw new NullPointerException();
	}
	
	@Override
	@DataSource(DataSourceName.SECOND)
	@Transactional(rollbackFor=Exception.class)
	public int updateUserT2() {
		um.updateUser();
		throw new NullPointerException();
	}
	
	@Scheduled(initialDelay=0,fixedRate=4000)
	public void scheduledTest() {
		System.out.println("执行定时任务, 执行时间:"+ LocalTime.now());
	}
	
	@Override
	@DataSource(DataSourceName.SECOND)
	public List<User> getAllUserPage(int pageNum) {
		PageHelper.startPage(pageNum, 1);
		List<User> allUser = um.getAllUser();
		return allUser;
	}
}
