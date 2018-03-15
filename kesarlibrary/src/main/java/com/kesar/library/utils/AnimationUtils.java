package com.kesar.library.utils;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * 
 * ClassName: AnimationUtil 
 * @Description: 动画工具类
 * @author kesar
 * @date 2015-11-12
 */
public class AnimationUtils
{
	private static final int TRANSLATE_DURATION = 300;
	private static final int ALPHA_DURATION = 300;
	/**
	 * 
	 * @Description: 创建转入动画
	 * @param @return
	 * @return Animation
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	public static Animation createTranslationInAnimation()
	{
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				1, type, 0);
		an.setDuration(TRANSLATE_DURATION);
		return an;
	}

	/**
	 * 
	 * @Description: 创建淡入动画
	 * @param @return
	 * @return Animation
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	public static Animation createAlphaInAnimation()
	{
		AlphaAnimation an = new AlphaAnimation(0, 1);
		an.setDuration(ALPHA_DURATION);
		return an;
	}

	/**
	 * 
	 * @Description: 创建转出动画
	 * @param @return
	 * @return Animation
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	public static Animation createTranslationOutAnimation()
	{
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				0, type, 1);
		an.setDuration(TRANSLATE_DURATION);
		an.setFillAfter(true);
		return an;
	}

	/**
	 * 
	 * @Description: 创建淡出动画
	 * @param @return
	 * @return Animation
	 * @throws
	 * @author kesar
	 * @date 2015-11-11
	 */
	public static Animation createAlphaOutAnimation()
	{
		AlphaAnimation an = new AlphaAnimation(1, 0);
		an.setDuration(ALPHA_DURATION);
		an.setFillAfter(true);
		return an;
	}
}
