package com.i61.parent.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import static android.provider.Contacts.SettingsColumns.KEY;

/**
 * 
 * ClassName: SharedPreferencesUtils 
 * @Description: 设置配置工具类
 * @author kesar
 * @date 2015-12-4
 */
public class SharedPreferencesUtils
{
	/** 设置的文件 */
	public final static String SETTING = "setting";

	/**
	 * 
	 * @Description: 写入值
	 * @param @param context
	 * @param @param key
	 * @param @param value   
	 * @return void  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static void putValue(Context context, String key, int value)
	{
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.putInt(key, value);
		sp.commit();
	}

	/**
	 * 
	 * @Description: 写入值
	 * @param @param context
	 * @param @param key
	 * @param @param value   
	 * @return void  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static void putValue(Context context, String key, boolean value)
	{
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.putBoolean(key, value);
		sp.commit();
	}

	/**
	 * 
	 * @Description: 写入值
	 * @param @param context
	 * @param @param key
	 * @param @param value   
	 * @return void  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static void putValue(Context context, String key, String value)
	{
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.putString(key, value);
		sp.commit();
	}
	
	/**
	 * 
	 * @Description: 删除key
	 * @param @param context
	 * @param @param key   
	 * @return void  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static void removeValue(Context context, String key)
	{
		Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
				.edit();
		sp.remove(key);
		sp.commit();
	}
	
	/**
	 * 
	 * @Description: 得到值
	 * @param @param context
	 * @param @param key
	 * @param @param defValue
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static int getValue(Context context, String key, int defValue)
	{
		SharedPreferences sp = context.getSharedPreferences(SETTING,
				Context.MODE_PRIVATE);
		int value = sp.getInt(key, defValue);
		return value;
	}

	/**
	 * 
	 * @Description: 得到值
	 * @param @param context
	 * @param @param key
	 * @param @param defValue
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static boolean getValue(Context context, String key, boolean defValue)
	{
		SharedPreferences sp = context.getSharedPreferences(SETTING,
				Context.MODE_PRIVATE);
		boolean value = sp.getBoolean(key, defValue);
		return value;
	}

	/**
	 * 
	 * @Description: 得到值
	 * @param @param context
	 * @param @param key
	 * @param @param defValue
	 * @param @return   
	 * @return String  
	 * @throws
	 * @author kesar
	 * @date 2015-12-4
	 */
	public static String getValue(Context context, String key, String defValue)
	{
		// TODO 这里会有个NullPointerException
		SharedPreferences sp = context.getSharedPreferences(SETTING,
				Context.MODE_PRIVATE);
		String value = sp.getString(key, defValue);
		return value;
	}


	/**
	 * desc:保存对象
	 * @param context 上下文
	 * @param key 键值对名称
	 * @param obj 要保存的对象，只能保存实现了serializable的对象
	 * modified:
	 */
	public static void saveObject(Context context,Object obj, String key){
		try {
			// 保存对象
			SharedPreferences.Editor sharedata = context.getSharedPreferences(SETTING, 0).edit();
			//先将序列化结果写到byte缓存中，其实就分配一个内存空间
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			ObjectOutputStream os=new ObjectOutputStream(bos);
			//将对象序列化写入byte缓存
			os.writeObject(obj);
			//将序列化的数据转为16进制保存
			String bytesToHexString = bytesToHexString(bos.toByteArray());
			//保存该16进制数组
			sharedata.putString(key, bytesToHexString);
			sharedata.commit();
		} catch (IOException e) {
			e.printStackTrace();
			LogUtils.e("", "保存obj失败");
		}
	}

	/**
	 * desc:将数组转为16进制
	 * @param bArray
	 * @return
	 * modified:
	 */
	public static String bytesToHexString(byte[] bArray) {
		if(bArray == null){
			return null;
		}
		if(bArray.length == 0){
			return "";
		}
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	/**
	 * desc:获取保存的Object对象
	 * @param context
	 * @param key 键值对名称
	 * @return
	 * modified:
	 */
	public static Object readObject(Context context, String key){
		try {
			SharedPreferences sharedata = context.getSharedPreferences(SETTING, 0);
			if (sharedata.contains(key)) {
				String string = sharedata.getString(key, "");
				if(TextUtils.isEmpty(string)){
					return null;
				}else{
					//将16进制的数据转为数组，准备反序列化
					byte[] stringToBytes = StringToBytes(string);
					ByteArrayInputStream bis=new ByteArrayInputStream(stringToBytes);
					ObjectInputStream is=new ObjectInputStream(bis);
					//返回反序列化得到的对象
					Object readObject = is.readObject();
					return readObject;
				}
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//所有异常返回null
		return null;

	}
	/**
	 * desc:将16进制的数据转为数组
	 * <p>创建人：聂旭阳 , 2014-5-25 上午11:08:33</p>
	 * @param data
	 * @return
	 * modified:
	 */
	public static byte[] StringToBytes(String data){
		String hexString=data.toUpperCase().trim();
		if (hexString.length()%2!=0) {
			return null;
		}
		byte[] retData=new byte[hexString.length()/2];
		for(int i=0;i<hexString.length();i++)
		{
			int int_ch;  // 两位16进制数转化后的10进制数
			char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
			int int_ch3;
			if(hex_char1 >= '0' && hex_char1 <='9')
				int_ch3 = (hex_char1-48)*16;   //// 0 的Ascll - 48
			else if(hex_char1 >= 'A' && hex_char1 <='F')
				int_ch3 = (hex_char1-55)*16; //// A 的Ascll - 65
			else
				return null;
			i++;
			char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
			int int_ch4;
			if(hex_char2 >= '0' && hex_char2 <='9')
				int_ch4 = (hex_char2-48); //// 0 的Ascll - 48
			else if(hex_char2 >= 'A' && hex_char2 <='F')
				int_ch4 = hex_char2-55; //// A 的Ascll - 65
			else
				return null;
			int_ch = int_ch3+int_ch4;
			retData[i/2]=(byte) int_ch;//将转化后的数放入Byte里
		}
		return retData;
	}
}
