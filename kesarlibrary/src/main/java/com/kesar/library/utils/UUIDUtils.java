package com.kesar.library.utils;

import java.util.UUID;

/**
 * 
 * ClassName: UUIDUtils 
 * @Description: UUID字符串生成工具
 * @author kesar
 * @date 2015-12-2
 */
public class UUIDUtils
{
	public static String getUUID()
	{
		return UUID.randomUUID().toString().replace("-", "");
	}
}
