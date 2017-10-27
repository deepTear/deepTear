package com.deepTear.springboot.service;

import java.util.List;

import com.deepTear.springboot.entity.User;

public interface UserService {

	public User findById(String id);

	public int addUser(User user);

	public List<User> findByUserAccount(String telephone);
}
