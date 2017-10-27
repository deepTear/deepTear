package com.deepTear.springboot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deepTear.springboot.dao.SearchItemDao;
import com.deepTear.springboot.entity.User;
import com.deepTear.springboot.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SearchItemDao searchItemDao;

	@Override
	public List<User> findByNameLike(String name) {
		return searchItemDao.findByNameLike("%" + name);
	}
}
