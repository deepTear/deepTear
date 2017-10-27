package com.deepTear.springboot.base;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public class BaseDao<T,ID extends Serializable> implements CrudRepository,PagingAndSortingRepository{

	@Override
	public Iterable findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object save(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable save(Iterable entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findOne(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(Serializable id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable findAll(Iterable ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Serializable id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Object entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Iterable entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

}
