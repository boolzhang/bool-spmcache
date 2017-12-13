# bool-spmcache

一个基于Memcached和Spring的简单封装框架，使用自定义注解+Spring AOP实现了Memcached缓存功能；
提供了Java官方的缓存客户端和sypmemcached客户端作为实现，可以普遍用于自建Memcached或阿里云、
腾讯云等缓存服务，阿里云建议使用sypmemcached实现，腾讯云建议使用官方的实现，至于自建Memcached
看个人喜好把；

1、优点：适用于Spring框架搭建的Web项目中，能够快速集成；
2、缺点：具备一定的入侵性质，需要自己在业务方法中增加@SPMCache来实现缓存功能；


使用方法：
1、将jar包加入项目中；
```java
将target文件夹中的bool-spmcache.jar拷贝到项目中；
```

2、定义SpringXML文件，用于拦截自定义注解；

```xml
<!-- 缓存提供服务，spymemcache缓存客户端，用户可以将此框架换成其它的-->
<bean id="spyProvider" class="com.bool.spmcache.provider.impl.SpyProvider">
	<!-- 多个服务器用逗号隔开,如：127.0.0.1:11211,192.168.1.188:11211 -->
	<constructor-arg name="servers" value="127.0.0.1:11211" />
	<constructor-arg name="keyPrefix" value="SPMC_" />
</bean>

<bean id="spMCacheAspect" class="com.bool.spmcache.aspect.SPMCacheAspect">
	 <property name="cahceProvider" ref="spyProvider"></property>
</bean>
	
<!-- AOP配置，用于拦截带了@SPMCache注解的方法，执行缓存环绕事物 -->
<aop:config>
      <aop:aspect ref="spMCacheAspect">
        <aop:pointcut id="cachePointcut" expression="@annotation(com.bool.spmcache.annotation.SPMCache)"/>
        <aop:around method="checkCache" pointcut-ref="cachePointcut" />
      </aop:aspect>
</aop:config>
```

3、增加引用
```xml
<!-- 公共的引用 -->
<dependency>
	<groupId>org.aspectj</groupId>
	<artifactId>aspectjweaver</artifactId>
	<version>1.8.7</version>
</dependency>

<dependency>
	<groupId>org.slf4j</groupId>
	<artifactId>slf4j-api</artifactId>
	<version>1.6.1</version>
</dependency>

<dependency>
	<groupId>org.slf4j</groupId>
	<artifactId>slf4j-log4j12</artifactId>
	<version>1.6.1</version>
</dependency>


<!-- 官方缓存客户端，引用的东西比较多 -->
<dependency>
	<groupId>com.danga</groupId>
	<artifactId>java-memcached</artifactId>
	<version>2.6.6</version>
</dependency>

<dependency>
	<groupId>org.slf4j</groupId>
	<artifactId>slf4j-simple</artifactId>
	<version>1.6.1</version>
	<scope>test</scope>
</dependency>

<dependency>
	<groupId>commons-pool</groupId>
	<artifactId>commons-pool</artifactId>
	<version>1.5.6</version>
</dependency>

<!-- SPY客户端，引用的东西少一些 -->
<dependency>
	<groupId>net.spy</groupId>
	<artifactId>spymemcached</artifactId>
	<version>2.12.1</version>
</dependency>
```

4、业务代码修改；exp单位为分钟，可根据业务自行决定缓存时间；
```java
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
```

