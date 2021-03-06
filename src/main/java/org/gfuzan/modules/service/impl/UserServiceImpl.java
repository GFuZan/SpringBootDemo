package org.gfuzan.modules.service.impl;

import java.time.LocalTime;
import java.util.List;

import com.github.pagehelper.PageHelper;

import org.gfuzan.common.config.datasources.DataSourceName;
import org.gfuzan.common.config.datasources.annotation.DataSource;
import org.gfuzan.modules.convert.UserConvertMapper;
import org.gfuzan.modules.dto.UserVo;
import org.gfuzan.modules.mapper.UserMapper;
import org.gfuzan.modules.service.UserService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper um;

	@Autowired
	private UserConvertMapper ucm;

	@Autowired
	private DataSourceTransactionManager transactionManager;

	@Autowired
	private TransactionDefinition transactionDefinition;

	@Override
	@DataSource(DataSourceName.FIRST)
	public List<UserVo> getAllUserAll() {
		
		((UserService)AopContext.currentProxy()).getAllUser1();
		((UserService)AopContext.currentProxy()).getAllUser2();
		((UserService)AopContext.currentProxy()).getAllUser2();
		((UserService)AopContext.currentProxy()).getAllUser1();
		
		return ucm.toDto(um.getAllUser());
	}

	/**
	 * 测试数据源1
	 */
	@Override
	@DataSource(DataSourceName.FIRST)
	public List<UserVo> getAllUser1() {
		return ucm.toDto(um.getAllUser());
	}

	/**
	 * 测试数据源2
	 */
	@Override
	@DataSource(DataSourceName.SECOND)
	public List<UserVo> getAllUser2() {
		return ucm.toDto(um.getAllUser());
	}
	
	/**
	 * 测试缓存
	 */
	@Override
	@Cacheable(cacheNames="AllUser")
	public List<UserVo> getAllUser() {
		System.err.println("getAllUser");
		return ucm.toDto(um.getAllUser());
	}

	/**
	 * 测试清理缓存
	 */
	@Override
	@CacheEvict(cacheNames="AllUser")
	public int updateUser() {
		System.err.println("updateUser");
		return um.updateUser();
	}
	
	

	/**
	 * 测试事务
	 */
	@Transactional(rollbackFor=Exception.class)
	public int updateUserT() {
		um.updateUser();
		throw new NullPointerException();
	}
	
	/**
	 * 测试事务与数据源注解
	 */
	@Override
	@DataSource(DataSourceName.FIRST)
	@Transactional(rollbackFor=Exception.class)
	public int updateUserT1() {
		um.updateUser();
		throw new NullPointerException();
	}
	
	/**
	 * 测试事务与数据源注解2
	 */
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
	
	/**
	 * 测试分页
	 */
	@Override
	@DataSource(DataSourceName.SECOND)
	public List<UserVo> getAllUserPage(int pageNum) {
		PageHelper.startPage(pageNum, 1);
		List<UserVo> allUser = ucm.toDto(um.getAllUser());
		return allUser;
	}

	@Override
	@DataSource(DataSourceName.H2)
	@Transactional(rollbackFor = Exception.class)
	public int testH2(String tableName, List<UserVo> userList) {
		um.createUserTable(tableName);
		um.insertUser(tableName, ucm.toEntity(userList));
		int age = um.sumAge(tableName);
		um.dropUserTable(tableName);
		return age;
	}

	@Override
	@DataSource(DataSourceName.H2)
	public int testManualTransaction(String tableName){
		int res = -1;
		TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
		try {
			um.dropUserTable(tableName);
			res = um.createUserTable(tableName);
			transactionManager.commit(transaction);
		} catch (Exception e) {
			transactionManager.rollback(transaction);
			throw e;
		}
		return res;
	}
}
