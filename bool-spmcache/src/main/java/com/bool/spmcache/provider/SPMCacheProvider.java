package com.bool.spmcache.provider;

/**
 * 缓存提供者接口方法，如果需要实现自己的缓存提供实例，只需要实现此方法即可；
 * ClassName: SPMCacheProvider <br/>
 * date: 2017年12月12日 下午4:14:13 <br/>
 *
 * @author Bool
 * @version 1.0
 */
public interface SPMCacheProvider {
	
	/**
	 * isReady:(缓存初始化状态). <br/>
	 * @author Bool
	 * @return true为已经初始化，false为未正确初始化
	 */
	public boolean isReady();

	/**
	 * put:(将数据放入缓存中). 
	 * @author Bool
	 * @param key 缓存键
	 * @param object 缓存值
	 * @param exp 缓存生命周期，分钟
	 */
	public void put(String key, Object object, int exp);


	/**
	 * get:(从缓存中读取数据).
	 * @author Bool
	 * @param key 缓存键
	 * @return Object 可反序列化的对象
	 */
	public Object get(String key);

	
	/** 
	 * remove:(根据缓存键移除缓存). <br/>
	 * @author Bool
	 * @param key 缓存键
	 */
	public void remove(String key);
	
	/**
	 * containsKey:(判断缓存Key是否存在，如果存在才进一步获取数据). <br/>
	 * @author Bool
	 * @param key
	 * @return 是否存在
	 */
	public boolean containsKey(String key);
	
}
