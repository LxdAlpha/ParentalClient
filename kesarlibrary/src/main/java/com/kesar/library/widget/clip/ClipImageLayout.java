package com.kesar.library.widget.clip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import com.kesar.library.utils.DensityUtils;

public class ClipImageLayout extends RelativeLayout
{
    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;

    private int mHorizontalPadding = 60;

    public ClipImageLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mZoomImageView = new ClipZoomImageView(context);
        mClipImageView = new ClipImageBorderView(context);

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);

        // 计算padding的px(dip转px)
        mHorizontalPadding = DensityUtils.dip2px(getContext(), mHorizontalPadding);

        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
    }

    /**
     *
     * @Description: 对外公布设置边距的方法,单位为dp
     * @param @param mHorizontalPadding
     * @return void
     * @throws
     * @author kesar
     * @date 2015-12-1
     */
    public void setHorizontalPadding(int mHorizontalPadding)
    {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    /**
     *
     * @Description: 裁切圆形图片
     * @param @return
     * @return Bitmap
     * @throws
     * @author kesar
     * @date 2015-12-1
     */
    public Bitmap clipCircle()
    {
        return mZoomImageView.clipCircle();
    }

    /**
     *
     * @Description: 裁切方形图片
     * @param @return
     * @return Bitmap
     * @throws
     * @author kesar
     * @date 2015-12-2
     */
    public Bitmap clipRect()
    {
        return mZoomImageView.cliipRect();
    }

    /**
     *
     * @Description: 显示图片
     * @param @param bitmap
     * @return void
     * @throws
     * @author kesar
     * @date 2015-12-1
     */
    public void setBitmap(Bitmap bitmap)
    {
        mZoomImageView.setImageBitmap(bitmap);
    }

    /**
     *
     * ClassName: ClipImageBorderView
     * @Description: 圆形切图的框框
     * @author kesar
     * @date 2015-12-1
     */
    static class ClipImageBorderView extends View
    {
        /** 水平方向与View的边距 */
        private int mHorizontalPadding;

        /** 边框的宽度 单位dp */
        private int mBorderWidth = 2;

        private Paint mPaint;

        public ClipImageBorderView(Context context)
        {
            super(context);
            mBorderWidth = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
                            .getDisplayMetrics());
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            // 绘制边框
            mPaint.setColor(Color.parseColor("#FFFFFF"));
            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setStyle(Style.STROKE);
            // 方形边框
            // canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()-
            // mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);
            // 圆形边框
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2
                    - mHorizontalPadding, mPaint);
        }

        public void setHorizontalPadding(int mHorizontalPadding)
        {
            this.mHorizontalPadding = mHorizontalPadding;
        }
    }
}