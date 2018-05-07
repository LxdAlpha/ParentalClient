package com.i61.parent.ui;

import android.content.ContentResolver;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.common.FileItem;
import com.i61.parent.util.ScreenUtil;
import com.i61.parent.util.UriUtil;
import com.jaeger.library.StatusBarUtil;
import com.theartofdev.edmodo.cropper.CropImage;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.i61.parent.AppApplication.getContext;

public class SelectImageActivity extends TitleBarActivity implements BottomNavigationBar.OnTabSelectedListener {

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();

        ArrayList<FileItem> dataList = (ArrayList<FileItem>) getPhotos();
        RecyclerView recyclerView = findViewById(R.id.selectImageRecycle);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        SelectImageAdapter adapter = new SelectImageAdapter(dataList);
        adapter.setParentActivity(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_select_image);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.bar_icon_pressed_color), 0);
    }



    //获取所有图片，包括系统相机拍摄的图片和使用CameraActivity拍摄的图片
    private List<FileItem> getPhotos() {
        ArrayList<FileItem> mMedias = new ArrayList<FileItem>();
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContext().getContentResolver();
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.SIZE, MediaStore.Images.ImageColumns.DATE_ADDED
        };
        Cursor cursor = contentResolver.query(imageUri, projection, null, null,
                MediaStore.Images.Media.DATE_ADDED + " desc");

        if (cursor == null) {
            return null;
        }
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                String fileName =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                String date = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));

                FileItem item = new FileItem(path, fileName, size, date);
                item.setType(FileItem.Type.Image);
                mMedias.add(item);
            }
        }
        cursor.close();
        return mMedias;
    }


    @Override
    public void onTabSelected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mTvTitle.setText("选择图片");
        mImgMenu.setVisibility(View.GONE);
    }


    @Override
    protected void onBackClick() {
        super.onBackClick();
        this.finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                Log.d("lxd", resultUri.getPath());

                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View layout = layoutInflater.inflate(R.layout.dialog_nameimage, null);
                AlertDialog.Builder builder =new AlertDialog.Builder(this);
                builder.setView(layout);
                builder.setCancelable(false);
                final AlertDialog dialog = builder.create();
                dialog.show();
                android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
                //p.height = ScreenUtil.getScreenParameterPx()[1] * 5 / 7;
                p.width = ScreenUtil.getScreenParameterPx()[0] * 5 / 6;
                dialog.getWindow().setAttributes(p);
                layout.findViewById(R.id.dialog_nameimage_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                ((ImageView)layout.findViewById(R.id.dialog_nameimage_image)).setMaxHeight(p.width*2/3);
                ((ImageView)layout.findViewById(R.id.dialog_nameimage_image)).setImageURI(resultUri);

                final EditText editText = ((EditText)layout.findViewById(R.id.dialog_nameimage_edittext));
                layout.findViewById(R.id.dialog_nameimage_confrim).setOnClickListener(new View.OnClickListener() { //点击确认按钮
                    @Override
                    public void onClick(View view) {

                        String nameImageResult = editText.getText().toString().trim();
                        Log.d("lxd", "用户输入的名称：" + nameImageResult);
                        if(nameImageResult.equals("") || nameImageResult == null){
                            nameImageResult = calendar.get(Calendar.YEAR) + "_" + (calendar.get(Calendar.MONTH)+1) + "_" + calendar.get(Calendar.DATE) + "_" + calendar.get(Calendar.HOUR_OF_DAY) + "_" + calendar.get(Calendar.MINUTE) + "_" + calendar.get(Calendar.SECOND);
                        }
                        nameImageResult = nameImageResult + ".jpg";
                        final String fileName = nameImageResult;



                        File sourceFile = null;
                        try {
                            sourceFile = new File(new URI(resultUri.toString()));                                     //根据返回的uri从存放剪切后图片的位置读取图片
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream out = null;                                                             //复制剪切后的图片到应用的文件夹下
                        try {
                            FileInputStream in = new FileInputStream(sourceFile);
                            out = new ByteArrayOutputStream();
                            byte[] b = new byte[1024];
                            int i = 0;
                            while ((i = in.read(b)) != -1) {

                                out.write(b, 0, b.length);
                            }
                            out.close();
                            in.close();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        byte[] data = out.toByteArray();


                        File copyFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);      //在应用存放图片的文件夹下新建File接受复制过来的图片
                        OutputStream os = null;
                        try {
                            os = new FileOutputStream(copyFile);
                            os.write(data);
                            os.close();
                        } catch (IOException e) {
                            Log.w("SelectImageActivity", "Cannot write to " + copyFile, e);
                        } finally {
                            if (os != null) {
                                try {
                                    os.close();
                                } catch (IOException e) {
                                    // Ignore
                                }
                            }
                        }


                        //选择图片和剪切图片均完成后，新建intent将数据回送到WorkFragment中，回调WorkFragment中的onActivityResult函数
                        Intent intent = new Intent();
                        intent.putExtra("imageUri", String.valueOf(UriUtil.getImageContentUri(AppApplication.getContext(), copyFile)));
                        setResult(RESULT_OK, intent);
                        Log.d("lxd", "SelectImageActivity->onActivityResult 执行到指定位置");
                        finish();
                    }
                });
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
