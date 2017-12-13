package com.bool.spmcache.test.service.impl;

import org.springframework.stereotype.Service;

import com.bool.spmcache.annotation.SPMCache;
import com.bool.spmcache.test.action.TestAction;
import com.bool.spmcache.test.service.TestService;


/**
 * ����ҵ�񷽷�������ص�ҵ�񷽷�����@SPMCache����ʵ�ֻ��棻
 * ClassName: TestServiceImpl <br/>
 * date: 2017��12��12�� ����7:40:06 <br/>
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
		 * Ϊ����ʾ������һ����̬������ÿ�����󶼸ı������ֵ��
		 * ������Ϊ�����ϵ�����ص�ǰ�˵����ݲ���仯��
		 */
		return TestAction.cache;
	}
}
