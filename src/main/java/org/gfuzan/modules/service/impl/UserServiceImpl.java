package org.gfuzan.modules.service.impl;

import java.time.LocalTime;
import java.util.List;

import org.gfuzan.common.datasources.DataSourceName;
import org.gfuzan.common.datasources.annotation.DataSource;
import org.gfuzan.modules.entity.User;
import org.gfuzan.modules.mapper.UserMapper;
import org.gfuzan.modules.service.UserService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper um;

	@Override
	@DataSource(DataSourceName.FIRST)
	public List<User> getAllUserAll() {
		
		((UserService)AopContext.currentProxy()).getAllUser1();
		((UserService)AopContext.currentProxy()).getAllUser2();
		((UserService)AopContext.currentProxy()).getAllUser2();
		((UserService)AopContext.currentProxy()).getAllUser1();
		
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
	
	/**
	 * 40秒执行一次
	 */
	@Scheduled(initialDelay=0,fixedRate=40000)
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
