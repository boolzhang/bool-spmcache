package com.bool.spmcache.provider.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bool.spmcache.provider.SPMCacheProvider;
import com.bool.spmcache.utils.KeyUtil;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

/**
 * 官方提供的客户端实现
 * ClassName: DangaProvider <br/>
 * date: 2017年12月12日 下午4:27:09 <br/>
 *
 * @author Bool
 * @version
 */
public class DangaProvider implements SPMCacheProvider{

	
	public static final Logger logger = LoggerFactory.getLogger(DangaProvider.class);
	
	//缓存客户端
	private MemCachedClient client;
	
	//缓存服务器参数
	private String servers;

	/**
	 * 构造方法，用于在初始化实例的时候，一并将缓存的配置初始化
	 */
	public DangaProvider(String servers , String keyPrefix) {
		super();
		
		this.servers = servers;
		
		//读取缓存key的前缀
		KeyUtil.keyPrefix = keyPrefix;
		
		if(this.client==null) {
			this.initFromConfig();
		}
	}

	/**
	 * initFromConfig:(从配置文件中初始化缓存配置). <br/>
	 * @author Bool
	 */
	private void initFromConfig(){
		
		
		logger.info("++++++ init memcache client...");
		try {
			
			if(servers!=null && !"".equals(servers)){
	

				//设置缓存服务器列表，当使用分布式缓存的时，可以指定多个缓存服务器
	            String [] serverArr = servers.split(",");

	            //创建Socked连接池实例
	            SockIOPool pool = SockIOPool.getInstance();
	            pool.setServers(serverArr);//设置连接池可用的cache服务器列表
	            pool.setFailover(true);//设置容错开关
	            pool.setInitConn(50);//设置开始时每个cache服务器的可用连接数
	            pool.setMinConn(5);//设置每个服务器最少可用连接数
	            pool.setMaxConn(1000);//设置每个服务器最大可用连接数
	            pool.setMaintSleep(30);//设置连接池维护线程的睡眠时间
	            pool.setNagle(false);//设置是否使用Nagle算法，因为我们的通讯数据量通常都比较大（相对TCP控制数据）而且要求响应及时， <br> 因此该值需要设置为false（默认是true） </br>
	            pool.setSocketTO(3000);//设置socket的读取等待超时值
	            pool.setAliveCheck(true);//设置连接心跳监测开关
	            pool.initialize();
	            this.client = new MemCachedClient();
			}
			
		} catch (Exception e) {
			logger.error("++++++++++ cache init fail , cache not working.");
		}
	}
	
	/**
	 * @see com.bool.spmcache.provider.SPMCacheProvider#put(java.lang.String, java.lang.Object, int)
	 */
	@Override
	public void put(String key, Object object, int exp) {
		try {
			//这个过期时间这里有些坑，如果是1分钟，那么则是60*1000*1
			long expTime = 60 * 1000 * exp;
			client.set(key, object , new Date(expTime));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see com.bool.spmcache.provider.SPMCacheProvider#get(java.lang.String)
	 */
	@Override
	public Object get(String key) {
		try{
			return client.get(key);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see com.bool.spmcache.provider.SPMCacheProvider#remove(java.lang.String)
	 */
	@Override
	public void remove(String key) {
		client.delete(key);
	}
	
	/**
	 * @see com.bool.spmcache.provider.SPMCacheProvider#containsKey(java.lang.String)
	 */
	@Override
	public boolean containsKey(String key){
		return (this.get(key)!=null);
	}


	/**
	 * @see com.bool.spmcache.provider.SPMCacheProvider#isReady()
	 */
	@Override
	public boolean isReady() {
		return client!=null;
	}
}
