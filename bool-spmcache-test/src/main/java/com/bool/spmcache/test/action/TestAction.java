package com.bool.spmcache.test.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bool.spmcache.test.service.TestService;

@Controller
public class TestAction {

	@Autowired
	private TestService testService;
	
	public static String cache = "";

	@RequestMapping("/spmcache/data")
	public @ResponseBody String test(String data) {
		
		//将传过来的值放在一个静态变量里面，用于测试
		cache = data;
		
		//获取返回数据
		return testService.findSometing();
	}
}
