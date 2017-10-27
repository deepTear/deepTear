package com.deepTear.springboot.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.JedisPool;

import com.deepTear.springboot.entity.User;
import com.deepTear.springboot.service.SearchItemService;

@Controller
public class SearchController {

	@Autowired
	private JedisPool jedisPool;

	@Autowired
	private SearchItemService searchItemService;

	@RequestMapping("/search")
    public String search() {
        return "search/search";
    }

	@RequestMapping("/search-item")
	@ResponseBody
    public void searchItem(HttpServletResponse response,String key) throws IOException {
		/*Jedis jedis = jedisPool.getResource();
		List<String> searchItem = jedis.keys(pattern)("search-item", 0, 6);*/

		List<User> list = searchItemService.findByNameLike(key);

		response.setCharacterEncoding("utf-8");
		response.setHeader("contentType", "application/json;charset=utf-8");
		if(list.size() > 0){
			response.getWriter().write(JSONArray.toJSONString(list));
		}
    }
}
