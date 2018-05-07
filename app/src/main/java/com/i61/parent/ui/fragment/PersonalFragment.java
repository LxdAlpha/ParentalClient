package com.i61.parent.ui.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.LoginResponse.Account;
import com.i61.parent.common.data.UserCenter.ClassmateMoney;
import com.i61.parent.common.data.UserCenter.MoneyInfo;
import com.i61.parent.common.data.UserCenter.UserCenterValue;
import com.i61.parent.ui.ClassDrawCoinAdapter;
import com.i61.parent.ui.DialogChangeAccountAdapter;
import com.i61.parent.ui.SelectImageAdapter;
import com.i61.parent.ui.SettingActivity;
import com.i61.parent.ui.ShowMessageActivity;
import com.i61.parent.ui.TakePhotoPopWin;
import com.i61.parent.ui.inter.ResumeInterface;
import com.i61.parent.util.DialogUtil;
import com.i61.parent.util.ScreenUtil;
import com.i61.parent.util.UriUtil;
import com.i61.parent.utils.LogUtils;
import com.i61.parent.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;

import static android.app.Activity.RESULT_OK;
import static com.i61.parent.util.UriUtil.getImagePath;

/**
 * @author lixianhua
 * @Description: 个人中心页面
 * @time 2018/3/17  15:46
 */
public class PersonalFragment extends TitleBarFragment {

    private static final String TAG = "PersonalFragment";

    private View rootView = null;    //缓存fragment的view，避免每次都创建新的

    private static final String PERMISSION_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;  //写内存权限
    private static final int REQUEST_PERMISSION_CODE = 267;                                                //申请权限代号
    private File imageFile;          //拍照返回的图片文件
    private Uri mImageUriFromFile;  //系统相册文件中路径转uri
    private Uri mImageUri;           //系统相册中文件路径转uri
    private static final String FILE_PROVIDER_AUTHORITY = "com.i61.parent.fileprovider";  //provider的名字，注意需与Androidmanifest.xml中的配置的provider的名字相同
    private static final int TAKE_PHOTO = 189;    //标识照相intent
    private static final int CHOOSE_PHOTO = 385; //标识打开相册intent
    private Uri saveUpdateHeadUri;

    //private ResumeInterface resumeInterface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		/*申请读取存储的权限*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getContext(), PERMISSION_WRITE_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{PERMISSION_WRITE_STORAGE}, REQUEST_PERMISSION_CODE);
            }
        }

    }

    @Override
    protected View inflaterView(LayoutInflater inflater, final ViewGroup container, Bundle bundle) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_personal, null);
        } else {
            //缓存的rootView需要判断是否已经在一个ViewGroup中， 如果在，就先移除自己，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }


        /*
        UserCenterValue userCenterValue = (UserCenterValue) SharedPreferencesUtils.readObject(AppApplication.getContext(), "userCenter");
        Account account = (Account) SharedPreferencesUtils.readObject(AppApplication.getContext(), "activeAccount");
        //Log.d("lxd", account.toString());
        String headUrl = account.getHeadUrl();
        Uri headUri = Uri.parse(headUrl);
        ((com.facebook.drawee.view.SimpleDraweeView) rootView.findViewById(R.id.head_image)).setImageURI(headUri);
        String nickName = account.getNickName();
        ((TextView) rootView.findViewById(R.id.personcenter_nickname)).setText(nickName);
        int learnedCourseNumber = userCenterValue.getUserProductInfo().getLearnedCourseNumber();
        int leftCourseCount = userCenterValue.getUserProductInfo().getLeftCourseCount();
        String text = "正在上：第" + learnedCourseNumber + "节 还剩课时：" + leftCourseCount + "节";
        ((TextView) rootView.findViewById(R.id.personcenter_remind)).setText(text);
        String bursaryText = "奖学金：" + userCenterValue.getMoneyInfo().getBursary();
        ((TextView) rootView.findViewById(R.id.personcenter_bursary)).setText(bursaryText);
        String drawMoneyText = "画币：" + userCenterValue.getMoneyInfo().getDrawMoneyTotal();
        ((TextView) rootView.findViewById(R.id.personcenter_drawMoney)).setText(drawMoneyText);
        ArrayList<ClassmateMoney> classmateMoneyArrayList = (ArrayList<ClassmateMoney>) userCenterValue.getClassmatesMoneyInfo();
        ClassmateMoney myMoney = new ClassmateMoney();
        myMoney.setUserName(userCenterValue.getMoneyInfo().getUserName());
        myMoney.setDrawMoney(userCenterValue.getMoneyInfo().getDrawMoney());
        classmateMoneyArrayList.add(0, myMoney);

        rootView.findViewById(R.id.personcenter_getcoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppApplication.getContext(), ShowMessageActivity.class);
                intent.putExtra("kind", 1);
                AppApplication.getContext().startActivity(intent);
            }
        });

        rootView.findViewById(R.id.personcenter_getbursary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppApplication.getContext(), ShowMessageActivity.class);
                intent.putExtra("kind", 2);
                AppApplication.getContext().startActivity(intent);
            }
        });

        int column = (int) Math.floor(ScreenUtil.getScreenParameterDp()[0] / 85); //根据屏幕宽度确定每一行显示的画币情况个数
        RecyclerView classCoinRecycleview = rootView.findViewById(R.id.classCoin_recycleview);
        GridLayoutManager layoutManager = new GridLayoutManager(AppApplication.getContext(), column);
        classCoinRecycleview.setLayoutManager(layoutManager);
        ClassDrawCoinAdapter adapter = new ClassDrawCoinAdapter(classmateMoneyArrayList);
        classCoinRecycleview.setAdapter(adapter);


        rootView.findViewById(R.id.head_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopFormBottom();
            }
        });


        rootView.findViewById(R.id.frag_personal_changeaccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogUtil.CommonDialog(getActivity(), R.style.PopupDialog, "您确定要退出该帐号吗？", new DialogUtil.CommonDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            //Toast.makeText(this,"点击确定", Toast.LENGTH_SHORT).show();
                            OkHttpUtils
                                    .get()
                                    .url(Urls.HOST + "uc/logout.json")
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            //Log.d("lxd", "退出帐号出错");
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            //Log.d("lxd", response);
                                            //退出登录时删除相关数据并将标识登录与否的isLogin置为false
                                            String[] deleteSharePreferences = {"activeAccount", "accountList", "workCenter", "userCenter"};
                                            for (String text : deleteSharePreferences) {
                                                SharedPreferencesUtils.removeValue(AppApplication.getContext(), text);
                                                //Log.d("lxd", "remove: " + text);
                                            }
                                            SharedPreferencesUtils.putValue(AppApplication.getContext(), "isLogin", "false");
                                        }
                                    });

                            dialog.dismiss();
                        }
                    }
                }).setTitle("提示").show();
            }
        });

        //设置按钮的响应函数
        rootView.findViewById(R.id.person_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppApplication.getContext(), SettingActivity.class);
                intent.putExtra("kind", 1);
                AppApplication.getContext().startActivity(intent);
            }
        });

        //切换帐号按钮的响应函数
        rootView.findViewById(R.id.frag_personal_changeaccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出选择帐号的窗口
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View layout = layoutInflater.inflate(R.layout.dialog_changeaccount, null);
                RecyclerView recyclerView = layout.findViewById(R.id.dialog_changeaccount_recycler);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);

                AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
                builder.setView(layout);
                builder.setCancelable(true);
                final AlertDialog dialog = builder.create();
                dialog.show();

                DialogChangeAccountAdapter adapter = new DialogChangeAccountAdapter(dialog);
                //adapter.setResumeInterface(resumeInterface);
                adapter.setResumeInterface(new ResumeInterface() {
                    @Override
                    public void resume() {
                        onResume();
                    }
                });
                recyclerView.setAdapter(adapter);

                android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
                //p.height = ScreenUtil.getScreenParameterPx()[1] * 5 / 7;
                p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                p.width = ScreenUtil.getScreenParameterPx()[0] * 5 / 6;
                dialog.getWindow().setAttributes(p);
                layout.findViewById(R.id.dialog_needsubmit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });*/

        print();

        return rootView;
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
    }

    @Override
    protected void setActionBarRes(TitleBarFragment.ActionBarRes actionBarRes) {
        super.setActionBarRes(actionBarRes);
        actionBarRes.titleBarUnVisible = true;
    }

    public void showPopFormBottom() {
        TakePhotoPopWin takePhotoPopWin = new TakePhotoPopWin(AppApplication.getContext(), onClickListener);
        //showAtLocation(View parent, int gravity, int x, int y)
        takePhotoPopWin.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_take_photo:   //点击“拍照”按钮
                    takePhoto();
                    break;
                case R.id.btn_pick_photo:   //点击“从相册选择按钮”
                    openAlbum();
                    break;
                case R.id.btn_cancel:
                    break;
            }
        }
    };

    //使用相机拍照
    private void takePhoto(){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //打开相机的Intent
        imageFile = createImageFile();                                          //新建存放相片的文件
        mImageUriFromFile = Uri.fromFile(imageFile);
        if (imageFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {             //7.0以上系统需通过FileProvider将File转化为Uri
                mImageUri = FileProvider.getUriForFile(getActivity(), FILE_PROVIDER_AUTHORITY, imageFile);
            } else {
                mImageUri = Uri.fromFile(imageFile);
            }
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将用于输出的文件Uri传递给相机
            startActivityForResult(takePhotoIntent, TAKE_PHOTO);//打开相机
        }
    }

    //从相册中选择图片
    private  void openAlbum(){
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CHOOSE_PHOTO);//打开相册
    }

    //在系统相册文件夹中新建文件存放照片
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }


    //申请权限的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {  //未能获取权限
            Toast.makeText(getActivity(), "您拒绝允许赋予内存操作权限，应用无法正常运行", Toast.LENGTH_SHORT).show();
        }
    }

    //处理相机或者相册返回来的数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(mImageUri)); //如果拍照成功，将Uri用BitmapFactory的decodeStream方法转为Bitmap
                        galleryAddPic(mImageUriFromFile);  //将拍的照片添加到相册
                        saveUpdateHeadUri = mImageUri;
                        uploadImage(imageFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(data == null){ //如果没有选取照片则退出
                    return;
                }
                if(resultCode  == RESULT_OK){
                    saveUpdateHeadUri = data.getData();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    //将拍的照片添加到相册
    private void galleryAddPic(Uri uri){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    //安卓4.4以下版本对返回的图片uri的处理
    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();

        String imagePath = getImagePath(uri, null);
        uploadImage(new File(imagePath));
    }

    //安卓4.4以上的版本对返回的图片uri进行处理
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this.getActivity(), uri)){
            //如果是document类型的uri，则提供document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的uri，则进行普通处理
            imagePath = getImagePath(uri, null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的uri，则直接获取路径
            imagePath = uri.getPath();
        }
        uploadImage(new File(imagePath));
    }

    //上传新的头像到后台
    private void uploadImage(File file){
        OkHttpUtils.post().url(Urls.HOST + "/uc/updateUserHead.json")
                .addFile("file", file.getName(), file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("onError", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.d("onResponse", response);
                        ((com.facebook.drawee.view.SimpleDraweeView) rootView.findViewById(R.id.head_image)).setImageURI(saveUpdateHeadUri);
                    }
                });
    }


    //如果重新显示，则需重新绘制昵称等控件的内容
    @Override
    public void onResume() {
        super.onResume();
        //重新绘制昵称
        print();
    }



    //绘制界面的函数，用于初始化和点击切换帐号后的重绘
    private void print(){
        UserCenterValue userCenterValue = (UserCenterValue) SharedPreferencesUtils.readObject(AppApplication.getContext(), "userCenter");
        Account account = (Account) SharedPreferencesUtils.readObject(AppApplication.getContext(), "activeAccount");
        //Log.d("lxd", account.toString());
        String headUrl = account.getHeadUrl();
        Uri headUri = Uri.parse(headUrl);
        ((com.facebook.drawee.view.SimpleDraweeView) rootView.findViewById(R.id.head_image)).setImageURI(headUri);
        String nickName = account.getNickName();
        ((TextView) rootView.findViewById(R.id.personcenter_nickname)).setText(nickName);
        int learnedCourseNumber = userCenterValue.getUserProductInfo().getLearnedCourseNumber();
        int leftCourseCount = userCenterValue.getUserProductInfo().getLeftCourseCount();
        String text = "正在上：第" + learnedCourseNumber + "节 还剩课时：" + leftCourseCount + "节";
        ((TextView) rootView.findViewById(R.id.personcenter_remind)).setText(text);
        String bursaryText = "奖学金：" + userCenterValue.getMoneyInfo().getBursary();
        ((TextView) rootView.findViewById(R.id.personcenter_bursary)).setText(bursaryText);
        String drawMoneyText = "画币：" + userCenterValue.getMoneyInfo().getDrawMoneyTotal();
        ((TextView) rootView.findViewById(R.id.personcenter_drawMoney)).setText(drawMoneyText);
        ArrayList<ClassmateMoney> classmateMoneyArrayList = (ArrayList<ClassmateMoney>) userCenterValue.getClassmatesMoneyInfo();
        ClassmateMoney myMoney = new ClassmateMoney();
        myMoney.setUserName(userCenterValue.getMoneyInfo().getUserName());
        myMoney.setDrawMoney(userCenterValue.getMoneyInfo().getDrawMoney());
        classmateMoneyArrayList.add(0, myMoney);

        rootView.findViewById(R.id.personcenter_getcoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppApplication.getContext(), ShowMessageActivity.class);
                intent.putExtra("kind", 1);
                AppApplication.getContext().startActivity(intent);
            }
        });

        rootView.findViewById(R.id.personcenter_getbursary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppApplication.getContext(), ShowMessageActivity.class);
                intent.putExtra("kind", 2);
                AppApplication.getContext().startActivity(intent);
            }
        });

        int column = (int) Math.floor(ScreenUtil.getScreenParameterDp()[0] / 85); //根据屏幕宽度确定每一行显示的画币情况个数
        RecyclerView classCoinRecycleview = rootView.findViewById(R.id.classCoin_recycleview);
        GridLayoutManager layoutManager = new GridLayoutManager(AppApplication.getContext(), column);
        classCoinRecycleview.setLayoutManager(layoutManager);
        ClassDrawCoinAdapter adapter = new ClassDrawCoinAdapter(classmateMoneyArrayList);
        classCoinRecycleview.setAdapter(adapter);


        rootView.findViewById(R.id.head_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopFormBottom();
            }
        });


        /*
        rootView.findViewById(R.id.frag_personal_changeaccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogUtil.CommonDialog(getActivity(), R.style.PopupDialog, "您确定要退出该帐号吗？", new DialogUtil.CommonDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            //Toast.makeText(this,"点击确定", Toast.LENGTH_SHORT).show();
                            OkHttpUtils
                                    .get()
                                    .url(Urls.HOST + "uc/logout.json")
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            //Log.d("lxd", "退出帐号出错");
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            //Log.d("lxd", response);
                                            //退出登录时删除相关数据并将标识登录与否的isLogin置为false
                                            String[] deleteSharePreferences = {"activeAccount", "accountList", "workCenter", "userCenter"};
                                            for (String text : deleteSharePreferences) {
                                                SharedPreferencesUtils.removeValue(AppApplication.getContext(), text);
                                                //Log.d("lxd", "remove: " + text);
                                            }
                                            SharedPreferencesUtils.putValue(AppApplication.getContext(), "isLogin", "false");
                                        }
                                    });

                            dialog.dismiss();
                        }
                    }
                }).setTitle("提示").show();
            }
        });
        */

        //设置按钮的响应函数
        rootView.findViewById(R.id.person_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppApplication.getContext(), SettingActivity.class);
                intent.putExtra("kind", 1);
                AppApplication.getContext().startActivity(intent);
            }
        });

        //切换帐号按钮的响应函数
        rootView.findViewById(R.id.frag_personal_changeaccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出选择帐号的窗口
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View layout = layoutInflater.inflate(R.layout.dialog_changeaccount, null);
                RecyclerView recyclerView = layout.findViewById(R.id.dialog_changeaccount_recycler);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);

                AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
                builder.setView(layout);
                builder.setCancelable(true);
                final AlertDialog dialog = builder.create();
                dialog.show();

                DialogChangeAccountAdapter adapter = new DialogChangeAccountAdapter(dialog);
                //adapter.setResumeInterface(resumeInterface);
                adapter.setResumeInterface(new ResumeInterface() {
                    @Override
                    public void resume() {
                        onResume();
                    }
                });
                recyclerView.setAdapter(adapter);

                android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
                //p.height = ScreenUtil.getScreenParameterPx()[1] * 5 / 7;
                p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                p.width = ScreenUtil.getScreenParameterPx()[0] * 5 / 6;
                dialog.getWindow().setAttributes(p);
                layout.findViewById(R.id.dialog_needsubmit_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
