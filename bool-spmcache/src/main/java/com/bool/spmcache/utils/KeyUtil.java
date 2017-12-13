package com.bool.spmcache.utils;

/**
 * 缓存Key生成工具类，根据被拦截的方法类名全称、方法名称、参数生成一个相对唯一的缓存Key；
 * 建议开发者严格控制类名，方法名和参数不要重复，不同系统使用前缀来避免缓存错乱 缓存一旦错乱，那问题会比较严重，甚至比较难重现 ClassName:
 * KeyUtil <br/>
 * date: 2017年12月12日 下午4:32:49 <br/>
 *
 * @author Bool
 * @version
 */
public class KeyUtil {

	// 默认缓存前缀，可以在资源文件中修改
	public static String keyPrefix = "SPMC_";

	/**
	 * processCacheKey:(组合缓存Key，根据类名，方法名，参数等生成一个缓存key，最后MD5后返回). <br/>
	 *
	 * @author Bool
	 * @param className 类名
	 * @param method 方法名
	 * @param append 自定义后缀
	 * @param args 参数列表
	 * @return String 缓存key
	 */
	public static String processCacheKey(String className, String method, String append, Object[] args) {

		StringBuffer sbKey = new StringBuffer();
		sbKey.append(className);
		sbKey.append("-");
		sbKey.append(method);
		sbKey.append("-");
		sbKey.append(generateCacheKeyFromArgs(args));
		sbKey.append("-");
		sbKey.append(append);
		String key = sbKey.toString().replace(".", "_");

		return keyPrefix + "" + MD5Util.MD5(key);
	}

	/**
	 * 
	 * generateCacheKeyFromArgs:(根据一串参数值组合成一个相对不重复的字符串). <br/>
	 *
	 * @author Bool
	 * @param args
	 * @return 一个组合好的参数字符串，如：P1=100&P2=200&P3=GOOD
	 */
	public static String generateCacheKeyFromArgs(Object[] args) {

		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (Object obj : args) {
			if (obj != null) {
				sb.append("P").append(i).append("=").append(obj.toString()).append("&");
			}
			i++;
		}
		return sb.toString();
	}

}
