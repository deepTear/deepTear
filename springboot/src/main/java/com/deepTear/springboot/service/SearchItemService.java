package com.deepTear.springboot.service;

import java.util.List;

import com.deepTear.springboot.entity.User;

public interface SearchItemService {

	public List<User> findByNameLike(String name);
}
