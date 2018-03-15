/*
package com.i61.parent.ui;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.i61.parent.R;

import org.kymjs.kjframe.KJActivity;


*/
/**
 * 应用Activity基类
 *
 * @author kymjs (https://github.com/kymjs)
 * @since 2015-3
 *//*

public abstract class TitleBarActivity extends KJActivity
{
    public ImageView mImgBack;
    public TextView mTvTitle;
    public TextView mTvDoubleClickTip;
    public ImageView mImgMenu;
    public RelativeLayout mRlTitleBar;

    public TabHostFragment tabHostFragment = null;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        super.onCreate( savedInstanceState );
      */
/*  if (!"org.kymjs.blog".equals(getApplication().getPackageName())) {
            ViewInject.toast("非法启动");
            KJActivityStack.create().AppExit(aty);
        }*//*

    }

    public void initTabHostFragment( TabHostFragment tabHostFragment )
    {
        this.tabHostFragment = tabHostFragment;
    }

    public TabHostFragment getTabHostFragment()
    {
        return this.tabHostFragment;
    }

    @Override
    protected void onStart()
    {
        try
        {
            mRlTitleBar = (RelativeLayout) findViewById( R.id.titlebar );
            mImgBack = (ImageView) findViewById( R.id.titlebar_img_back );
            mTvTitle = (TextView) findViewById( R.id.titlebar_text_title );
            mTvDoubleClickTip = (TextView) findViewById( R.id.titlebar_text_exittip );
            mImgMenu = (ImageView) findViewById( R.id.titlebar_img_menu );
            mImgBack.setOnClickListener( this );
            mImgMenu.setOnClickListener( this );

        }
        catch ( NullPointerException e )
        {
            throw new NullPointerException(
                    "TitleBar Notfound from Activity layout" );
        }
        super.onStart();
    }

    @Override
    public void widgetClick( View v )
    {
        super.widgetClick( v );
        switch ( v.getId() )
        {
            case R.id.titlebar_img_back:
                onBackClick();
                break;
            case R.id.titlebar_img_menu:
                onMenuClick();
                break;
            default:
                break;
        }
    }

    protected void onBackClick()
    {
    }

    protected void onMenuClick()
    {
    }

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )
    {

        if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 )
        {

        }
        return super.onKeyDown( keyCode, event );
    }
};*/
