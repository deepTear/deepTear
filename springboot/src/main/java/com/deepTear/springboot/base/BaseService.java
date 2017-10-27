package com.deepTear.springboot.base;

import java.io.Serializable;

import org.springframework.data.domain.Page;

public interface BaseService<T,ID extends Serializable> {

	public Page<T> findPageList();

}
