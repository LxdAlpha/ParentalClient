package com.i61.parent.ui.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.BaseBean;
import com.i61.parent.common.data.BaseBeanCallback;
import com.i61.parent.common.data.UserCenter.Course;
import com.i61.parent.common.data.UserCenter.UserCenterValue;
import com.i61.parent.common.data.WorkCenter.CourseInfo;
import com.i61.parent.common.data.WorkCenter.WorkCenterValue;
import com.i61.parent.common.data.WorkCenter.WorkImageInfo;
import com.i61.parent.presenter.OnChangeSubmitWork;
import com.i61.parent.ui.CameraActivity;
import com.i61.parent.ui.NeedSubmitWorkAdapter;


import com.i61.parent.util.DialogUtil;
import com.i61.parent.util.PxUtil;
import com.i61.parent.util.ScreenUtil;
import com.i61.parent.util.UriUtil;
import com.i61.parent.utils.LogUtils;
import com.i61.parent.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;


import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

/**
 * @author lixianhua
 * @Description: 作业页面
 * @time 2018/3/17  15:46
 */
public class WorkFragment extends TitleBarFragment implements OnChangeSubmitWork{

	private static final String TAG = "WorkFragment";

	private View rootView = null; //缓存fragment的view，避免每次都创建新的

	private Context context;

	private View view;

	private Dialog dialog;

	private Fragment fragment;

	private int courseId; //存储课程id，用于上传作业

	private String title;  //存储课程名称，用于上传作业

	MotionEvent baseMotionEvent;

	final Uri[] uris = new Uri[2];

	@Override
	protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.frag_work, null);
		} else {
			//缓存的rootView需要判断是否已经在一个ViewGroup中， 如果在，就先移除自己，要不然会发生这个rootview已经有parent的错误。
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}




		return rootView;
	}

	@Override
	protected void setActionBarRes(TitleBarFragment.ActionBarRes actionBarRes) {
		super.setActionBarRes(actionBarRes);
		actionBarRes.titleBarUnVisible = false;
		actionBarRes.title = "作业";
		actionBarRes.menuText = "补交作业";

	}

	@Override
	public void onTvMenuClick() {
		/*
		super.onTvMenuClick();
		LogUtils.e(TAG, "onTvMenuClick");
		ViewInject.toast("onTvMenuClick");
		*/


		/*
		List<String> dataList = new ArrayList<>();
		dataList.add("新建专题");
		dataList.add("已有专题");
		dataList.add("系统专题");
		dataList.add("地域");
		*/

		//每次用户点击补交作业按钮，意味着之前拍摄或者剪切的照片都无效了，所以需要将存储的uri删除
		uris[0] = null;
		uris[1] = null;

		List<CourseInfo> needSubmitWorkList = ((WorkCenterValue)SharedPreferencesUtils.readObject(AppApplication.getContext(), "workCenter")).getNeedSubmitWorkCourseList();
		List<Course> courseList = ((UserCenterValue)SharedPreferencesUtils.readObject(AppApplication.getContext(), "userCenter")).getUserCourseInfos();
		List<String> dataList = new ArrayList<>();
		for(CourseInfo courseInfo : needSubmitWorkList){
			for(Course course : courseList){
				if(course.getCourseId() == courseInfo.getCourseId()){
					String text = "第" + course.getIndex() + "节：" + courseInfo.getChallengeTitle();
					dataList.add(text);
					this.courseId = course.getCourseId();        //存储课程id，用于上传作业
					this.title = courseInfo.getChallengeTitle(); //存储课程名称，用于上传作业
				}
			}
		}

		//指定显示需提交的作业的RecycleView的布局和适配器，并构造窗口并弹出
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View layout = layoutInflater.inflate(R.layout.dialog_needsubmitwork, null);
		RecyclerView recyclerView = layout.findViewById(R.id.needSubmitWorkList);
		LinearLayoutManager layoutManager = new LinearLayoutManager(context);
		recyclerView.setLayoutManager(layoutManager);
		NeedSubmitWorkAdapter adapter = new NeedSubmitWorkAdapter(dataList, this);
		recyclerView.setAdapter(adapter);
		AlertDialog.Builder builder =new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setCancelable(true);
		final AlertDialog dialog = builder.create();
		dialog.show();
		android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
		p.height = ScreenUtil.getScreenParameterPx()[1] * 5 / 7;
		p.width = ScreenUtil.getScreenParameterPx()[0] * 5 / 6;
		dialog.getWindow().setAttributes(p);
		layout.findViewById(R.id.dialog_needsubmit_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		this.dialog = dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.submit_homework, container, false);

		context = getActivity();

		fragment = this;

		view.findViewById(R.id.submit_homework_button).setVisibility(View.GONE);

		TextView submitTitle = view.findViewById(R.id.submit_title);
		submitTitle.setWidth((int) PxUtil.dpToPx(AppApplication.getContext(), ScreenUtil.getScreenParameterDp()[0]/3*2));
		TextView submitWorkContent = view.findViewById(R.id.submit_work_content);

		CourseInfo courseInfo = ((WorkCenterValue) SharedPreferencesUtils.readObject(AppApplication.getContext(), "workCenter")).getCourseInfo();
		List<Course> userCourseInfos = ((UserCenterValue)SharedPreferencesUtils.readObject(AppApplication.getContext(), "userCenter")).getUserCourseInfos();
		int chapter = 0;
		for(Course course : userCourseInfos){
			if(course.getCourseId() == courseInfo.getCourseId()){
				chapter = course.getIndex();
			}
		}
		submitTitle.setText("第" + chapter + "课：" + courseInfo.getChallengeTitle());
		submitWorkContent.setText(courseInfo.getChallengeMessage());

       view.findViewById(R.id.image_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppApplication.getContext(), CameraActivity.class);
                //intent.putExtra("type", 2); //向CameraActivity传递拍摄图片类别，type为1时代表拍摄课堂作业照片
                //context.startActivity(intent);
				fragment.startActivityForResult(intent, 10); //能接收返回数据的intent启动方式，10用于标识此次调用相机由课堂作业发起
            }
        });

		view.findViewById(R.id.image_right).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AppApplication.getContext(), CameraActivity.class);
				//intent.putExtra("type", 3); //向CameraActivity传递拍摄图片类别，type为2时代表拍摄亲子作业照片
				//context.startActivity(intent);
				fragment.startActivityForResult(intent, 11); //能接收返回数据的intent启动方式，11用于标识此次调用相机由亲子作业发起
			}
		});

		if(courseInfo.getWorkType() == 1){   // WorkType==1：只有课堂作业没有亲子作业，则课堂作业居中显示，亲子作业不显示。
			view.findViewById(R.id.txtHide).setVisibility(View.GONE);
			view.findViewById(R.id.image_right).setVisibility(View.GONE);
			RelativeLayout.LayoutParams relLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			relLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			view.findViewById(R.id.image_left).setLayoutParams(relLayoutParams);
		}


		return view;
	}


	@Override
	public void changeSubmitWork(int courseId) {
		Log.d("lxd", "WorkFragment->changeSubmitWork");
		this.courseId = courseId;
		resume(courseId);
	}


	public void resume(final int courseId) {
		super.onResume();

		//调用resume的情况只存在于在“需提交作业列表”中选择项目，这种情况下不需显示提交作业按钮
		view.findViewById(R.id.submit_homework_button).setVisibility(View.GONE);


			OkHttpUtils.get().url(Urls.HOST + "wc/workCenter.json")
					.addParams("courseId", courseId+"")
					.build()
					.execute(new BaseBeanCallback<BaseBean<WorkCenterValue>>() {
						@Override
						public void onError(Call call, Exception e, int id) {
							LogUtils.e("onError:", e.getMessage());
						}

						@Override
						public void onResponse(Object response, int id) {
							if(((BaseBean<WorkCenterValue>)response).success == true && ((BaseBean<WorkCenterValue>)response).getResultCode().success == true){
								Log.d("lxd", "WorkFragment->OnResume->onResponse");
								LogUtils.d("onResponse:", response.toString());
								CourseInfo courseInfo = ((BaseBean<WorkCenterValue>)response).getValue().getCourseInfo();
								TextView submitTitle = view.findViewById(R.id.submit_title);
								submitTitle.setWidth((int) PxUtil.dpToPx(AppApplication.getContext(), ScreenUtil.getScreenParameterDp()[0]/3*2));
								TextView submitWorkContent = view.findViewById(R.id.submit_work_content);
								List<Course> userCourseInfos = ((UserCenterValue)SharedPreferencesUtils.readObject(AppApplication.getContext(), "userCenter")).getUserCourseInfos();
								int chapter = 0;
								for(Course course : userCourseInfos){
									if(course.getCourseId() == courseInfo.getCourseId()){
										chapter = course.getIndex();
									}
								}
								submitTitle.setText("第" + chapter + "课：" + courseInfo.getChallengeTitle());
								submitWorkContent.setText(courseInfo.getChallengeMessage());


							/*
							Button studentHomework = view.findViewById(R.id.btn_studenthomework);
							studentHomework.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									Intent intent = new Intent(AppApplication.getContext(), CameraActivity.class);
									AppApplication.getContext().startActivity(intent);
								}
							});
							*/





								//如果已经提交过作业，则显示缩略图
								List<WorkImageInfo> workImageInfoList = ((BaseBean<WorkCenterValue>)response).getValue().getWorkImageInfoList();
								WorkImageInfo studentImage = null; //学生提交的作业的缩略图
								WorkImageInfo parentImage = null;  //家长提交的作业的缩略图


								if(courseInfo.getWorkType() == 3){         // WorkType==3：课堂作业、亲子作业，两个作业并排显示。
									view.findViewById(R.id.txtHide).setVisibility(View.VISIBLE);
									view.findViewById(R.id.image_right).setVisibility(View.VISIBLE);
									RelativeLayout.LayoutParams relLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
									relLayoutParams.addRule(RelativeLayout.LEFT_OF, R.id.txtHide);
									view.findViewById(R.id.image_left).setLayoutParams(relLayoutParams);
								}else if(courseInfo.getWorkType() == 1){  // WorkType==1：只有课堂作业没有亲子作业，则课堂作业居中显示，亲子作业不显示。
									view.findViewById(R.id.txtHide).setVisibility(View.GONE);
									view.findViewById(R.id.image_right).setVisibility(View.GONE);
									RelativeLayout.LayoutParams relLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
									relLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
									view.findViewById(R.id.image_left).setLayoutParams(relLayoutParams);
								}


								if(workImageInfoList != null){     //返回的数据中包含WorkImageInfoList，代表用户提交过作业
									//显示“已提交”按钮，背景为灰色
									((Button)view.findViewById(R.id.submit_homework_button)).setText("已提交");
									((Button)view.findViewById(R.id.submit_homework_button)).setBackgroundColor(Color.parseColor("#bbbbbb"));
									((Button)view.findViewById(R.id.submit_homework_button)).setVisibility(View.VISIBLE);
									view.findViewById(R.id.image_left).setClickable(false);
									view.findViewById(R.id.image_left).setLongClickable(false);
									view.findViewById(R.id.image_right).setClickable(false);
									view.findViewById(R.id.image_right).setLongClickable(false);

									for(WorkImageInfo imageInfo : workImageInfoList){  //type=1：课堂作业已提交作业图片  type=2：亲子作业已提交作业图片
										if(imageInfo.getType() == 1){
											studentImage = imageInfo;
										}else if(imageInfo.getType() == 2){
											parentImage = imageInfo;
										}
									}
									if(studentImage != null){      //如果课堂作业已提交作业图片非空
										Log.d("lxd", "studentImage: " + studentImage.getSmallUrl());
										OkHttpUtils.get().url(studentImage.getSmallUrl())  //访问网络获取图片并设置显示
												.build()
												.execute(new BitmapCallback() {
													@Override
													public void onError(Call call, Exception e, int id) {

													}

													@Override
													public void onResponse(Bitmap response, int id) {
														((ImageView)view.findViewById(R.id.image_button_left)).setScaleType(ImageView.ScaleType.CENTER_CROP);
														((ImageView)view.findViewById(R.id.image_button_left)).setImageBitmap(response);
														view.findViewById(R.id.image_button_text_left).setVisibility(View.GONE);
													}
												});
										view.findViewById(R.id.image_left).setClickable(false);
										view.findViewById(R.id.image_left).setLongClickable(false);
									}else{
										((ImageView)view.findViewById(R.id.image_button_left)).setScaleType(ImageView.ScaleType.FIT_CENTER);
										((ImageView)view.findViewById(R.id.image_button_left)).setImageResource(R.drawable.work_addimage_child);
										view.findViewById(R.id.image_left).setClickable(true);
										view.findViewById(R.id.image_button_text_left).setVisibility(View.VISIBLE);
									}

									if(parentImage != null){		//如果亲子作业已提交作业图片非空
										Log.d("lxd", "parentImage: " + parentImage.getSmallUrl());
										OkHttpUtils.get().url(parentImage.getSmallUrl())	//访问网络获取图片并设置显示
												.build()
												.execute(new BitmapCallback() {
													@Override
													public void onError(Call call, Exception e, int id) {

													}

													@Override
													public void onResponse(Bitmap response, int id) {
														((ImageView)view.findViewById(R.id.image_button_right)).setScaleType(ImageView.ScaleType.CENTER_CROP);
														((ImageView)view.findViewById(R.id.image_button_right)).setImageBitmap(response);
														view.findViewById(R.id.image_button_text_right).setVisibility(View.GONE);
													}
												});

										view.findViewById(R.id.image_right).setClickable(false);
										view.findViewById(R.id.image_right).setLongClickable(false);
									}else {
										((ImageView)view.findViewById(R.id.image_button_right)).setScaleType(ImageView.ScaleType.FIT_CENTER);
										((ImageView)view.findViewById(R.id.image_button_right)).setImageResource(R.drawable.work_addimage_child);
										view.findViewById(R.id.image_right).setClickable(true);
										view.findViewById(R.id.image_button_text_right).setVisibility(View.VISIBLE);
									}
								}else if(workImageInfoList == null){//返回的数据中没有WorkImageInfoList，（之前可能处于显示图片的状态）取消在亲子作业和课堂作业部分显示图片
									((ImageView)view.findViewById(R.id.image_button_left)).setScaleType(ImageView.ScaleType.FIT_CENTER);
									((ImageView)view.findViewById(R.id.image_button_left)).setImageResource(R.drawable.work_addimage_child);
									view.findViewById(R.id.image_left).setClickable(true);
									view.findViewById(R.id.image_button_text_left).setVisibility(View.VISIBLE);
									((ImageView)view.findViewById(R.id.image_button_right)).setScaleType(ImageView.ScaleType.FIT_CENTER);
									((ImageView)view.findViewById(R.id.image_button_right)).setImageResource(R.drawable.work_addimage_child);
									view.findViewById(R.id.image_right).setClickable(true);
									view.findViewById(R.id.image_button_text_right).setVisibility(View.VISIBLE);
								}



								dialog.dismiss(); //取消显示“需要提交作业”列表
							}
						}
					});






    }

    //响应CametaActivity关闭后返回的信息，根据相机调用者信息更新对应部分内容
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);


		//todo 有bug，两张照片同时提交只能成功提交最后一个，第一个被覆盖


		switch (requestCode){
			case 10:
				if(resultCode == RESULT_OK){
					Log.d("lxd", "课堂作业部分调用相机和剪切功能");
					Uri imageUri = Uri.parse(data.getStringExtra("imageUri"));
					uris[0] = imageUri;
					((ImageView)view.findViewById(R.id.image_button_left)).setScaleType(ImageView.ScaleType.CENTER_CROP);
					((ImageView)view.findViewById(R.id.image_button_left)).setImageURI(imageUri);
					view.findViewById(R.id.image_left).setClickable(false);
					view.findViewById(R.id.image_button_text_left).setVisibility(View.GONE);
					view.findViewById(R.id.submit_homework_button).setVisibility(View.VISIBLE);

					final File uploadFile = new File(UriUtil.getRealFilePath(AppApplication.getContext(), imageUri));
					Log.d("lxd", "imageUri: " + imageUri);
					Log.d("lxd", "imagePath: " + UriUtil.getRealFilePath(AppApplication.getContext(), imageUri));
					Log.d("lxd", "imageName: " + uploadFile.getName());

					((Button)view.findViewById(R.id.submit_homework_button)).setText("提交作业");
					((Button)view.findViewById(R.id.submit_homework_button)).setBackgroundResource(R.drawable.red_button);
					//作业类型type(1.课堂作业 2.亲子作业)
					//uploadHomework(1, uploadFile);


					//单击提交作业按钮，上传作业，上传期间显示圆形进度条，上传结束后进度条不显示
					/*
					view.findViewById(R.id.submit_homework_button).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {

							//在上传文件期间显示进度条
							LayoutInflater layoutInflater = LayoutInflater.from(context);
							View layout = layoutInflater.inflate(R.layout.dialog_uploadprogress, null);
							AlertDialog.Builder builder =new AlertDialog.Builder(context);
							builder.setView(layout);
							builder.setCancelable(true);
							final AlertDialog dialog = builder.create();
							dialog.show();
							android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
							p.width = ScreenUtil.getScreenParameterPx()[0] * 2 / 6;
							dialog.getWindow().setAttributes(p);

							uploadHomework(1, uploadFile, dialog);

						}
					});*/


					/*
					//长按显示“重选”、“取消”效果
					view.setOnLongClickListener(new View.OnLongClickListener() {
						@Override
						public boolean onLongClick(View view) {
							LayoutInflater layoutInflater = LayoutInflater.from(context);
							View layout = layoutInflater.inflate(R.layout.dialog_reselection, null);
							AlertDialog.Builder builder =new AlertDialog.Builder(context);
							builder.setView(layout);
							builder.setCancelable(true);
							final AlertDialog dialog = builder.create();
							dialog.getWindow().setGravity(Gravity.LEFT | Gravity.TOP);
							dialog.show();
							android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
							p.height = 250;     //窗口高度
							p.width = 200;      //窗口宽度
							p.dimAmount =0f;   //窗口背景不变灰

							p.x= (int) baseMotionEvent.getRawX();             //位置x轴
							p.y= (int) baseMotionEvent.getRawY();             //位置y轴

							dialog.getWindow().setAttributes(p);
							return true;
						}
					});
					*/


					view.findViewById(R.id.image_left).setOnTouchListener(new View.OnTouchListener() {
						@Override
						public boolean onTouch(View view, MotionEvent motionEvent) {
							baseMotionEvent = motionEvent;
							return false;
						}
					});



				}
				break;
			case 11:
				if(resultCode == RESULT_OK){
					Log.d("lxd", "亲子作业部分调用相机和剪切功能 " + data.getStringExtra("imageUri"));
					Uri imageUri = Uri.parse(data.getStringExtra("imageUri"));
					uris[1] = imageUri;
					((ImageView)view.findViewById(R.id.image_button_right)).setScaleType(ImageView.ScaleType.CENTER_CROP);
					((ImageView)view.findViewById(R.id.image_button_right)).setImageURI(imageUri);
					view.findViewById(R.id.image_right).setClickable(false);
					view.findViewById(R.id.image_button_text_right).setVisibility(View.GONE);
					view.findViewById(R.id.submit_homework_button).setVisibility(View.VISIBLE);

					final File uploadFile = new File(UriUtil.getRealFilePath(AppApplication.getContext(), imageUri));
					Log.d("lxd", "imageUri: " + imageUri);
					Log.d("lxd", "imagePath: " + UriUtil.getRealFilePath(AppApplication.getContext(), imageUri));
					Log.d("lxd", "imageName: " + uploadFile.getName());

					//单击提交作业按钮，上传作业，上传期间显示圆形进度条，上传结束后进度条不显示
					/*
					view.findViewById(R.id.submit_homework_button).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {

							//在上传文件期间显示进度条
							LayoutInflater layoutInflater = LayoutInflater.from(context);
							View layout = layoutInflater.inflate(R.layout.dialog_uploadprogress, null);
							AlertDialog.Builder builder =new AlertDialog.Builder(context);
							builder.setView(layout);
							builder.setCancelable(true);
							final AlertDialog dialog = builder.create();
							dialog.show();
							android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
							p.width = ScreenUtil.getScreenParameterPx()[0] * 2 / 6;
							dialog.getWindow().setAttributes(p);

							//作业类型type(1.课堂作业 2.亲子作业)
							uploadHomework(2, uploadFile, dialog);

						}
					});
					*/

				}
				break;
		}
		view.findViewById(R.id.submit_homework_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				//在上传文件期间显示进度条
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				View layout = layoutInflater.inflate(R.layout.dialog_uploadprogress, null);
				AlertDialog.Builder builder =new AlertDialog.Builder(context);
				builder.setView(layout);
				builder.setCancelable(true);
				final AlertDialog dialog = builder.create();
				dialog.show();
				android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
				p.width = ScreenUtil.getScreenParameterPx()[0] * 2 / 6;
				dialog.getWindow().setAttributes(p);

				//作业类型type(1.课堂作业 2.亲子作业)
				if(uris[0] != null){
					File uploadFile = new File(UriUtil.getRealFilePath(AppApplication.getContext(), uris[0]));
					uploadHomework(1, uploadFile, dialog);
				}
				if(uris[1] != null){
					File uploadFile = new File(UriUtil.getRealFilePath(AppApplication.getContext(), uris[1]));
					uploadHomework(2, uploadFile, dialog);
				}

			}
		});
	}

	//提交作业
	private void uploadHomework(int type, File file, final AlertDialog dialog){
		OkHttpUtils.post().url(Urls.HOST + "wc/uploadHomeWork.json")
				.addParams("courseId", this.courseId + "")
				.addParams("title", this.title)
				.addParams("type", type+"")
				.addFile("file", file.getName(), file)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						LogUtils.e("onError", e.getMessage());
						Log.d("lxd", "上传失败");
					}

					@Override
					public void onResponse(String response, int id) {
						LogUtils.d("onResponse", response);
						Log.d("lxd", response);
						dialog.dismiss();//上传完成后取消显示圆形进度条
						//将提交作业按钮背景颜色改为灰色，文字改成“已提交”
						((Button)view.findViewById(R.id.submit_homework_button)).setText("已提交");
						((Button)view.findViewById(R.id.submit_homework_button)).setBackgroundColor(Color.parseColor("#bbbbbb"));
						view.findViewById(R.id.image_left).setClickable(false);
						view.findViewById(R.id.image_left).setLongClickable(false);
						view.findViewById(R.id.image_right).setClickable(false);
						view.findViewById(R.id.image_right).setLongClickable(false);
					}
				});
	}
}
