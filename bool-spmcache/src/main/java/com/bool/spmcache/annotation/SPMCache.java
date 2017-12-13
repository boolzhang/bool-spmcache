package com.bool.spmcache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SPMCache {
	
	/**定义缓存时间,单位为：5分钟*/
	int exp() default 5*60;
	
	/**定义缓存KEY附加字符*/
	String keyAppend() default "";
}
