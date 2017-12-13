package com.bool.spmcache.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bool.spmcache.annotation.SPMCache;
import com.bool.spmcache.provider.SPMCacheProvider;
import com.bool.spmcache.utils.KeyUtil;

/**
 * 缓存拦截方法，用于AOP拦截
 * ClassName: SPMCacheAspect <br/>
 * date: 2017年12月12日 下午4:29:00 <br/>
 *
 * @author Bool
 * @version
 */
public class SPMCacheAspect {

	public static final Logger logger = LoggerFactory.getLogger(SPMCacheAspect.class);

	//缓存提供者，必须在初始化时通过xml注入
	private SPMCacheProvider cahceProvider;
	
	public SPMCacheAspect() {
		super();
	}

	public SPMCacheProvider getCahceProvider() {
		return cahceProvider;
	}

	public void setCahceProvider(SPMCacheProvider cahceProvider) {
		this.cahceProvider = cahceProvider;
	}

	/**
	 * checkCache:(缓存检验，这里使用环绕事物执行此方法对缓存进行检测，是否需要查询新数据或使用缓存). <br/>
	 * @author Bool
	 * @param point
	 * @return
	 */
	public Object checkCache(ProceedingJoinPoint point) {
		
		// 调用方法获取注解参数
		SPMCache SPMCache = getAnnotationParamter(point);
		// 调用方法产生缓存KEY并MD5
		String cacheKey = generateKey(point);
		Object object = null;

		// 缓存服务器不存在直接返回结果
		if (cahceProvider == null || !cahceProvider.isReady()) {
			try {
				logger.error("++++++++++ spmcache server not ready yet,continue...");
				object = (Object) point.proceed();
				return object;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		if (cahceProvider.containsKey(cacheKey)) {
			object = cahceProvider.get(cacheKey);
			logger.info("++++++++++ get data from spmcache, key:" + cacheKey);
		} else {
			try {
				object = (Object) point.proceed();
				if (object != null) {
					cahceProvider.put(cacheKey, object, SPMCache.exp());
					logger.info("++++++++++ query new data from database , put in meSPMCache with key:" + cacheKey + ",Time exp:"+ SPMCache.exp() + " min");

				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return object;
	}


	/**
	 * 获取注解实例
	 * @param point
	 * @return
	 */
	private SPMCache getAnnotationParamter(ProceedingJoinPoint point) {

		SPMCache SPMCache = null;
		// 获取方法名称
		String method = point.getSignature().getName();
		// 获取当前类名
		Class<?> cls = point.getTarget().getClass();
		// 获取class中的方法
		Method[] methods = cls.getMethods();
		for (Method m : methods) {
			// 获取执行方法前的注解信息
			if (m.getName().equals(method)) {
				SPMCache = m.getAnnotation(SPMCache.class);
			}
		}
		return SPMCache;
	}

	/**
	 * 产生缓存的Key
	 * @param point
	 * @return
	 */
	private String generateKey(ProceedingJoinPoint point) {
		/** 获取类的全称 */
		String className = point.getSignature().getDeclaringTypeName();
		/** 获取方法名称 */
		String method = point.getSignature().getName();
		SPMCache SPMCache = getAnnotationParamter(point);
		String cacheKey = KeyUtil.processCacheKey(className, method, SPMCache.keyAppend(), point.getArgs());
		return cacheKey;
	}
}
