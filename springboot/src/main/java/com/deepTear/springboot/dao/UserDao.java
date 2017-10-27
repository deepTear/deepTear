package com.deepTear.springboot.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepTear.springboot.entity.User;

public interface UserDao extends JpaRepository<User,String>{

	//public int addUser(User user);

	public User findById(String id);

	public List<User> findByTelephone(String telephone);
}
