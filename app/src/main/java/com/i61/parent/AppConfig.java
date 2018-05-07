package com.i61.parent;

/**
 * @Author: lixianhua
 * @Description: 配置文件常量
 * @Date: Created in 2018/3/14 16:12
 */

public class AppConfig {
	public static final String saveFolder = "/HuaLaLa";
	public static final String httpCachePath = saveFolder + "/httpCache";    // 网络缓存路径
	public static final String imgCachePath = saveFolder + "/imageCache";    // 图片缓存路径
	public static final String audioPath = saveFolder + "/audio";            // 语音文件路径
	public static final int httpTimeOut = 30000;                            // 网络请求超时
}
