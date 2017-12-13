package com.bool.spmcache.provider.impl;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bool.spmcache.provider.SPMCacheProvider;
import com.bool.spmcache.utils.KeyUtil;

import net.spy.memcached.MemcachedClient;

/**
 * 基于spymemcached的缓存框架实现的缓存服务提供类；
 * ClassName: SpyProvider <br/>
 * date: 2017年12月12日 下午4:05:35 <br/>
 *
 * @author Bool
 * @version
 */
public class SpyProvider implements SPMCacheProvider{

	public static final Logger logger = LoggerFactory.getLogger(SpyProvider.class);
	
	//spymemcached缓存客户端
	private MemcachedClient client;
	
	//缓存服务器参数
	private String servers;
	
	/**
	 * 构造方法，用于构造和初始化缓存配置
	 */
	public SpyProvider(String servers , String keyPrefix) {
		super();
		
		this.servers = servers;
		
		//读取缓存key的前缀
		KeyUtil.keyPrefix = keyPrefix;
		
		if(this.client==null) {
			this.initFromConfig();
		}
	}
	
	/**
	 * initFromConfig:初始化缓存，使用运行环境中的memcache.properties资源环境对缓存进行初始化
	 * @author Bool
	 */
	private void initFromConfig(){
		
		logger.info("++++++ init sypmemcache client...");
		
		try {
		
			if(servers!=null && !"".equals(servers)){
				
				
				
				System.out.println("++++++++++++服务器参数："+servers);
				
				//获取服务器列表
				String [] serverArr = servers.split(",");
				
				if(serverArr!=null && serverArr.length>0) {
					
					ArrayList<InetSocketAddress> addrs = new ArrayList<InetSocketAddress>();
					
					for(String server : serverArr) {
						String [] one = server.split(":");
						if(one!=null && one.length!=2) {
							continue;
						}
						addrs.add(new InetSocketAddress(one[0], Integer.parseInt(one[1])));
					}
					
					InetSocketAddress [] inetAddrs = addrs.toArray(new InetSocketAddress[addrs.size()]);
					this.client = new MemcachedClient(inetAddrs);
				}
			}
			
		} catch (ConnectException e){
			e.printStackTrace();
			logger.error("++++++++++ Memcached服务器连接失败，请检查配置！");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("++++++++++ 读取Memcached配置文件失败，请检查：memcache.properties文件是否存在！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("++++++++++ Memcached配置不正确，将不能正常工作！");
		}
	}
	
	@Override
	public boolean isReady() {
		return client!=null;
	}
	
	
	
	
	/**
	 * @see com.bool.spmcache.provider.SPMCacheProvider#put(java.lang.String, java.lang.Object, int)
	 */
	@Override
	public void put(String key, Object object, int exp) {
		client.set(key, exp * 60, object);
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

	
	public static void main(String[] args) {
		
	}

}
