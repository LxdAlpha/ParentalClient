package com.i61.parent.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;
import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.util.ScreenUtil;
import com.i61.parent.util.UriUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Set;

public class CameraActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        AspectRatioFragment.Listener {



    private static final String TAG = "MainActivity";

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 4;

    private static final String FRAGMENT_DIALOG = "dialog";

    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    private static final int REQUEST_CODE_IMAGE_CROP = 3;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 5;

    private Calendar calendar;

    private Context context;

    private Activity activity;

    private File imageFile;


    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };

    private int mCurrentFlash;

    private CameraView mCameraView;

    private Handler mBackgroundHandler;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.take_picture:
                    if (mCameraView != null) {
                        mCameraView.takePicture();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera);
        mCameraView = (CameraView) findViewById(R.id.camera);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.take_picture);
        if (fab != null) {
            fab.setOnClickListener(mOnClickListener);
        }
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        */


        FloatingActionButton takeCancel = (FloatingActionButton) findViewById(R.id.take_cancel);
        FloatingActionButton takeAlbum = (FloatingActionButton) findViewById(R.id.take_album);
        takeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });

        //打开相册，使用startActivityForResult获取返回值，requestCode=12用于区分这个请求是从打开相册按钮发出的
        takeAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppApplication.getContext(), SelectImageActivity.class);
                activity.startActivityForResult(intent, 12);
            }
        });

        calendar = Calendar.getInstance();

        context = this;

        activity = this;


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraView.start();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ConfirmationDialogFragment
                    .newInstance(R.string.camera_permission_confirmation,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION,
                            R.string.camera_permission_not_granted)
                    .show(getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    //申请权限的回调，判断申请权限的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }
                // No need to start camera here; it is handled by onResume
                break;
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the task you need to do.
                    CropImage.activity(UriUtil.getImageContentUri(context, imageFile.getAbsoluteFile()))
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(activity);
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(this, "获取权限失败，不能对照片进行剪切", Toast.LENGTH_SHORT).show();
                    this.finishActivity();
                }
                break;
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aspect_ratio:
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (mCameraView != null
                        && fragmentManager.findFragmentByTag(FRAGMENT_DIALOG) == null) {
                    final Set<AspectRatio> ratios = mCameraView.getSupportedAspectRatios();
                    final AspectRatio currentRatio = mCameraView.getAspectRatio();
                    AspectRatioFragment.newInstance(ratios, currentRatio)
                            .show(fragmentManager, FRAGMENT_DIALOG);
                }
                return true;
            case R.id.switch_flash:
                if (mCameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;
            case R.id.switch_camera:
                if (mCameraView != null) {
                    int facing = mCameraView.getFacing();
                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
                            CameraView.FACING_BACK : CameraView.FACING_FRONT);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    public void onAspectRatioSelected(@NonNull AspectRatio ratio) {
        if (mCameraView != null) {
            Toast.makeText(this, ratio.toString(), Toast.LENGTH_SHORT).show();
            mCameraView.setAspectRatio(ratio);
        }
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(final CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
            Toast.makeText(cameraView.getContext(), R.string.picture_taken, Toast.LENGTH_SHORT)
                    .show();
            getBackgroundHandler().post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run() {
                    /*
                    File file = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/Camera",
                            calendar.get(Calendar.YEAR) + "_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.DATE) + "_" + calendar.get(Calendar.HOUR_OF_DAY) + "_" + calendar.get(Calendar.MINUTE) + "_" + calendar.get(Calendar.SECOND) + ".jpg");
                    */
                    String fileName = calendar.get(Calendar.YEAR) + "_" + (calendar.get(Calendar.MONTH)+1) + "_" + calendar.get(Calendar.DATE) + "_" + calendar.get(Calendar.HOUR_OF_DAY) + "_" + calendar.get(Calendar.MINUTE) + "_" + calendar.get(Calendar.SECOND) + ".jpg";
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
                    Log.d("lxd", "CameraActivity->onPictureTaken: " + file.getAbsolutePath());
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(file);
                        os.write(data);
                        os.close();
                    } catch (IOException e) {
                        Log.w(TAG, "Cannot write to " + file, e);
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }


                    /* //将图片保存到系统Images项目的pictures文件架下
                    MediaStore.Images.Media.insertImage(getContentResolver(), Bytes2Bimap(data), "title", "description");
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(Environment.DIRECTORY_PICTURES))));
                    */


                    imageFile = file;
                    // Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {    //运行时获取读写内存权限，如果没有相关权限，则申请，并在onRequestPermissionsResult函数中获取申请结果
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }else{                                                                                                                                      //如果之前已经申请获得过权限，则直接跳转到剪切图片
                        CropImage.activity(UriUtil.getImageContentUri(context, imageFile.getAbsoluteFile()))
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(activity);
                    }




                }
            });
        }

    };

    //响应下一个Activity返回的结果，剪切图片后获得图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                Log.d("lxd", resultUri.getPath());

                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View layout = layoutInflater.inflate(R.layout.dialog_nameimage, null);
                AlertDialog.Builder builder =new AlertDialog.Builder(context);
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
                            Log.w(TAG, "Cannot write to " + copyFile, e);
                        } finally {
                            if (os != null) {
                                try {
                                    os.close();
                                } catch (IOException e) {
                                    // Ignore
                                }
                            }
                        }

                        //拍照和剪切图片均完成后，新建intent将数据回送到WorkFragment中，回调WorkFragment中的onActivityResult函数
                        Intent intent = new Intent();
                        intent.putExtra("imageUri", String.valueOf(UriUtil.getImageContentUri(AppApplication.getContext(), copyFile)));
                        setResult(RESULT_OK, intent);
                        Log.d("lxd", "CameraActivity->onActivityResult 执行到指定位置");
                        finish();
                    }
                });



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }else if(requestCode == 12){ //此返回结果对应“打开相册”
            Uri imageUri = Uri.parse(data.getStringExtra("imageUri"));
            Intent intent = new Intent();
            intent.putExtra("imageUri", String.valueOf(imageUri));
            setResult(RESULT_OK, intent);
            Log.d("lxd", "CameraActivity->onActivityResult 执行到指定位置 " + imageUri);
            finish();
        }
    }

    public static class ConfirmationDialogFragment extends DialogFragment {

        private static final String ARG_MESSAGE = "message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";

        public static ConfirmationDialogFragment newInstance(@StringRes int message,
                                                             String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_MESSAGE, message);
            args.putStringArray(ARG_PERMISSIONS, permissions);
            args.putInt(ARG_REQUEST_CODE, requestCode);
            args.putInt(ARG_NOT_GRANTED_MESSAGE, notGrantedMessage);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Bundle args = getArguments();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(args.getInt(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] permissions = args.getStringArray(ARG_PERMISSIONS);
                                    if (permissions == null) {
                                        throw new IllegalArgumentException();
                                    }
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions, args.getInt(ARG_REQUEST_CODE));
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(),
                                            args.getInt(ARG_NOT_GRANTED_MESSAGE),
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create();
        }

    }

    private void finishActivity(){
        this.finish();
    }

    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
}
