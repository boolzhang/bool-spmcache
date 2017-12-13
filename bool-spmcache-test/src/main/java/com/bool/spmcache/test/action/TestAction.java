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
		
		//����������ֵ����һ����̬�������棬���ڲ���
		cache = data;
		
		//��ȡ��������
		return testService.findSometing();
	}
}
