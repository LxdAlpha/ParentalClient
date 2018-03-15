package com.kesar.library.utils;

import java.util.Date;

/**
 * 
 * ClassName: TimeUtils
 * 
 * @Description: 时间工具类
 * @author kesar
 * @date 2015-12-13
 */
public class TimeUtils
{
	/**
	 * 
	 * @Description: 日期转中文显示
	 * @param @param time
	 * @param @return
	 * @return String
	 * @throws
	 * @author kesar
	 * @date 2015-12-13
	 */
	public static String getRencentTime(Date time)
	{
		Date now = new Date(); // 当前时间

		if (now.getYear() > time.getYear())
		{
			return now.getYear() - time.getYear() + "年前";
		}
		else if (now.getYear() == time.getYear())
		{
			if (now.getMonth() > time.getMonth())
			{
				return now.getMonth() - time.getMonth() + "月前";
			}
			else if (now.getMonth() == time.getMonth())
			{
				if (now.getDate() > time.getDate())
				{
					return now.getDate() - time.getDate() + "天前";
				}
				else if (now.getDate() == time.getDate())
				{
					if (now.getHours() > time.getHours())
					{
						return now.getHours() - time.getHours() + "小时前";
					}
					else if (now.getHours() == time.getHours())
					{
						if (now.getMinutes() > time.getMinutes())
						{
							return now.getMinutes() - time.getMinutes() + "分钟前";
						}
						else if (now.getMinutes() == time.getMinutes())
						{
							return "1分钟前";
						}
						else
						{
							return DateUtils.DateToString(time,
									DateUtils.DateStyle.HH_MM);
						}
					}
					else
					{
						return DateUtils.DateToString(time,
								DateUtils.DateStyle.HH_MM);
					}
				}
				else
				{
					return DateUtils.DateToString(time,
							DateUtils.DateStyle.MM_DD);
				}
			}
			else
			{
				return DateUtils.DateToString(time, DateUtils.DateStyle.MM_DD);
			}
		}
		else
		{
			return DateUtils.DateToString(time, DateUtils.DateStyle.YYYY_MM_DD);
		}
	}

	/**
	 * 
	 * @Description: 日期转中文显示
	 * @param @param date
	 * @param @return
	 * @return String
	 * @throws
	 * @author kesar
	 * @date 2015-12-13
	 */
	public static String getRencentTime(String date)
	{
		return getRencentTime(DateUtils.StringToDate(date));
	}

	/**
	 * 
	 * @Description: 获取显示的时间
	 * @param @param date
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author kesar
	 * @date 2015-12-13
	 */
	public static String getShowTime(String date)
	{
		return getShowTime(DateUtils.StringToDate(date));
	}

	/**
	 * 
	 * @Description: 获取显示的时间
	 * @param @param time
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author kesar
	 * @date 2015-12-13
	 */
	public static String getShowTime(Date time)
	{
		Date now = new Date(); // 当前时间

		if (now.getYear() == time.getYear()
				&& now.getMonth() == time.getMonth())
		{
			if (now.getDate() == time.getDate())
			{
				return DateUtils.DateToString(time, DateUtils.DateStyle.HH_MM);
			}
			else if (now.getDate() - time.getDate() < 8)
			{
				if (now.getDate() - time.getDate() == 1)
				{
					return "昨天";
				}
				return DateUtils.getWeek(time).getChineseName();
			}
		}
		return DateUtils.DateToString(time, DateUtils.DateStyle.YYYY_MM_DD);
	}
}
