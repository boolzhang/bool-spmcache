package com.bool.spmcache.test.service.impl;

import org.springframework.stereotype.Service;

import com.bool.spmcache.annotation.SPMCache;
import com.bool.spmcache.test.action.TestAction;
import com.bool.spmcache.test.service.TestService;


/**
 * 测试业务方法，在相关的业务方法增加@SPMCache即可实现缓存；
 * ClassName: TestServiceImpl <br/>
 * date: 2017年12月12日 下午7:40:06 <br/>
 *
 * @author Bool
 * @version
 */
@Service
public class TestServiceImpl implements TestService{
	
	@SPMCache(exp=1)
	@Override
	public String findSometing() {
		/*
		 * 为了演示，用了一个静态变量，每次请求都改变了这个值，
		 * 但是因为缓存关系，返回到前端的数据不会变化；
		 */
		return TestAction.cache;
	}
}
