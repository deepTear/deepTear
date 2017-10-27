package com.deepTear.springboot.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepTear.springboot.entity.User;

public interface SearchItemDao extends JpaRepository<User,String> {

	public List<User> findByNameLike(String name);
}
