package com.kesar.library.utils;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.kesar.library.KConfig;

/**
 * 
 * ClassName: AddImageUtils
 * 
 * @Description: 获取图片工具类(加权限：WRITE_EXTERNAL_STORAGE)
 * @author kesar
 * @date 2015-11-12
 */
public class AddImageUtils
{
	public static final int REQUEST_CODE_TACKPHONE = 0x144;// 裁剪照片
	public static final int REQUEST_CODE_ALBUM = 0x245;// 从相册获取

	private static File photoFile; // 照片的文件

	/**
	 * 
	 * @Description: 从相册中获取图片
	 * @param @param aty
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static void fromAlbum(Activity aty)
	{
		Intent intent;
		if (Build.VERSION.SDK_INT < 19)
		{
			intent = new Intent(Intent.ACTION_GET_CONTENT);
		}
		else
		{
			intent = new Intent(Intent.ACTION_PICK);
		}
		intent.setType("image/*");
		aty.startActivityForResult(intent, REQUEST_CODE_ALBUM);
	}

	/**
	 * 
	 * @Description: 自行拍照
	 * @param @param aty
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static void takePhoto(Activity aty)
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		photoFile=creatImagePath(KConfig.IMAGE_DIR);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile)); // 有这一句intenet就会为空
		aty.startActivityForResult(intent, REQUEST_CODE_TACKPHONE);
	}
	
	/**
	 * 
	 * @Description: 自行拍照
	 * @param @param aty
	 * @param @param imgCachePath 图片保存路径
	 * @return void  
	 * @throws
	 * @author kesar
	 * @date 2015-11-13
	 */
	public static void takePhoto(Activity aty,String imgCachePath)
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		photoFile=creatImagePath(imgCachePath);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile)); // 有这一句intenet就会为空
		aty.startActivityForResult(intent, REQUEST_CODE_TACKPHONE);
	}

	/**
	 * 
	 * @Description: 创建保存相册的路径
	 * @param @return
	 * @return File
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	private static File creatImagePath(String imgCachePath)
	{
		if (CommonUtils.isExitsSdcard())
		{
			String saveDir = CommonUtils.getSdcardPath()+imgCachePath;
			File dir = new File(saveDir);
			if (!dir.exists())
			{
				dir.mkdirs();
			}
			File file = new File(saveDir, UUIDUtils.getUUID()+".png");
			file.delete();
			if (!file.exists())
			{
				try
				{
					file.createNewFile();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			return file;
		}
		return null;
	}
	
	/**
	 * 
	 * @Description: 获取照片的图片文件
	 * @param @return   
	 * @return File  
	 * @throws
	 * @author kesar
	 * @date 2015-11-13
	 */
	public static File getPhotoFile()
	{
		return photoFile;
	}
}