package com.deepTear.springboot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deepTear.springboot.dao.UserDao;
import com.deepTear.springboot.entity.User;
import com.deepTear.springboot.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;

	@Override
	public User findById(String id) {
		return userDao.findById(id);
	}

	@Override
	public int addUser(User user) {
		user = userDao.save(user);
		return 1;
	}

	@Override
	public List<User> findByUserAccount(String telephone) {
		return userDao.findByTelephone(telephone);
	}

}
