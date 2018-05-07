package com.kesar.library.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 
 * ClassName: ImageUtils
 * 
 * @Description: 图片工具类
 * @author kesar
 * @date 2015-11-12
 */
public class ImageUtils
{
	public final static int IMAGE_MAX_WIDTH=4096; // imageview能显示的bitmap最大width
	public final static int IMAGE_MAX_HEIGHT=4096; // imageview能显示的bitmap最大height
	/**
	 * 
	 * @Description: 保存原来的图片
	 * @param @param bitmap 图片的二进制数据
	 * @param @param spath 保存图片路径
	 * @param @throws Exception
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static void saveOrginalImage(Bitmap bitmap, String spath)
			throws Exception
	{
		saveImage(bitmap, new File(spath), 100);
	}

	/**
	 * 
	 * @Description: 保存原来的图片
	 * @param @param bitmap 图片的二进制数据
	 * @param @param dirPath 文件夹路径
	 * @param @param name 文件名
	 * @param @throws Exception
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static void saveOrginalImage(Bitmap bitmap, String dirPath,
			String name) throws Exception
	{
		saveImage(bitmap, new File(dirPath, name), 100);
	}

	/**
	 * 
	 * @Description: 保存原来的图片
	 * @param @param bitmap 图片的二进制数据
	 * @param @param file 保存图片file
	 * @param @throws Exception
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static void saveOrginalImage(Bitmap bitmap, File file)
			throws Exception
	{
		saveImage(bitmap, file, 100);
	}

	/**
	 * 
	 * @Description: 保存压缩后的图片
	 * @param @param bitmap 图片的二进制数据
	 * @param @param spath 保存图片路径
	 * @param @throws Exception
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static void saveThumbnailImage(Bitmap bitmap, String spath)
			throws Exception
	{
		saveImage(bitmap, new File(spath), 60);
	}

	/**
	 * 
	 * @Description: 保存压缩后的图片
	 * @param @param bitmap 图片的二进制数据
	 * @param @param dirPath 保存图片文件夹路径
	 * @param @param name 图片名
	 * @param @throws Exception
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static void saveThumbnailImage(Bitmap bitmap, String dirPath,
			String name) throws Exception
	{
		saveImage(bitmap, new File(dirPath, name), 60);
	}

	/**
	 * 
	 * @Description: 保存压缩后的图片
	 * @param @param bitmap 图片的二进制数据
	 * @param @param file 保存图片file
	 * @param @throws Exception
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static void saveThumbnailImage(Bitmap bitmap, File file)
			throws Exception
	{
		saveImage(bitmap, file, 60);
	}

	/**
	 * 
	 * @Description: 保存某质量的图片
	 * @param @param bitmap 图片的二进制数据
	 * @param @param file 保存图片file
	 * @param @param quality 图片质量0-100
	 * @param @throws Exception
	 * @return void
	 * @throws
	 * @author kesar
	 * @date 2015-11-12
	 */
	public static void saveImage(Bitmap bitmap, File file, int quality)
			throws Exception
	{
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(file, false));
		bitmap.compress(Bitmap.CompressFormat.PNG, quality, bos);
		bos.flush();
		bos.close();
	}

	/**
	 * 
	 * @Description: 加载本地图片（不建议这样使用）
	 * @param @param path 本地路径文件
	 * @param @return
	 * @return Bitmap
	 * @throws
	 * @author kesar
	 * @date 2015-11-13
	 */
	public static Bitmap getLocalBitmap(String path) throws Exception
	{
		return getLocalBitmap(new File(path));
	}

	/**
	 * 
	 * @Description: 加载本地图片（不建议这样使用）
	 * @param @param file 本地路径文件
	 * @param @return
	 * @param @throws Exception
	 * @return Bitmap
	 * @throws
	 * @author kesar
	 * @date 2015-11-13
	 */
	public static Bitmap getLocalBitmap(File file) throws Exception
	{ 
		Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
		// 防止出现Bitmap too large to be uploaded into a texture(4096*4096)
		if(isBitmapTooLarge(bitmap))
		{
			bitmap.recycle();
			bitmap=null;
			return getLocalBitmap(file, 2);
		}
		return bitmap;
	}

	/**
	 * 
	 * @Description: 加载本地图片
	 * @param @param path 本地路径文件
	 * @param @param scalSize SDK建议2，数值越大，失真越大
	 * @param @return
	 * @return Bitmap
	 * @throws
	 * @author kesar
	 * @date 2015-11-13
	 */
	public static Bitmap getLocalBitmap(String path, int scalSize)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = scalSize;
		return BitmapFactory.decodeFile(path, options);
	}

	/**
	 * 
	 * @Description: 加载本地图片
	 * @param @param file 本地路径文件
	 * @param @param scalSize SDK建议2，数值越大，失真越大
	 * @param @return
	 * @param @throws Exception
	 * @return Bitmap
	 * @throws
	 * @author kesar
	 * @date 2015-11-13
	 */
	public static Bitmap getLocalBitmap(File file, int scalSize)
			throws Exception
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = scalSize;
		return BitmapFactory.decodeStream(new FileInputStream(file), null,
				options);
	}

	/**
	 * 
	 * @Description: 判断bitmap是否过大
	 * @param @param bitmap
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author kesar
	 * @date 2015-12-1
	 */
	public static boolean isBitmapTooLarge(Bitmap bitmap)
	{
		return bitmap.getWidth() > IMAGE_MAX_WIDTH || bitmap.getHeight() > IMAGE_MAX_HEIGHT;
	}
}
