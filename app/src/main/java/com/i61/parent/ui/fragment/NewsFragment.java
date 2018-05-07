package com.i61.parent.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.MsgCentreInfo;
import com.i61.parent.ui.MessageActivity;
import com.i61.parent.ui.inter.ChangeFragmentInterface;
import com.i61.parent.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * @author lixianhua
 * @Description: 消息页面
 * @time 2018/3/17  15:47
 */
public class NewsFragment extends TitleBarFragment {

	private static final String TAG = "NewsFragment";

	private View rootView = null; //缓存fragment的view，避免每次都创建新的

	ArrayList<ImageView> imageViewsList = null; //各个信息种类的提示红点



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageViewsList = new ArrayList<>();
	}

	@Override
	protected View inflaterView(final LayoutInflater inflater, ViewGroup container, Bundle bundle) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.frag_news, null);
		} else {
			//缓存的rootView需要判断是否已经在一个ViewGroup中， 如果在，就先移除自己，要不然会发生这个rootview已经有parent的错误。
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		rootView.findViewById(R.id.frag_news_inform).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AppApplication.getContext(), MessageActivity.class);
				intent.putExtra("kind", 1);
				//AppApplication.getContext().startActivity(intent);
				getActivity().startActivityForResult(intent, 1);
			}
		});
		rootView.findViewById(R.id.frag_news_coin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AppApplication.getContext(), MessageActivity.class);
				intent.putExtra("kind", 2);
				//AppApplication.getContext().startActivity(intent);
				getActivity().startActivityForResult(intent, 2);
			}
		});
		rootView.findViewById(R.id.frag_news_schoolarship).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AppApplication.getContext(), MessageActivity.class);
				intent.putExtra("kind", 3);
				//AppApplication.getContext().startActivity(intent);
				getActivity().startActivityForResult(intent, 3);
			}
		});
		rootView.findViewById(R.id.frag_news_work).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AppApplication.getContext(), MessageActivity.class);
				intent.putExtra("kind", 4);
				//AppApplication.getContext().startActivity(intent);
				getActivity().startActivityForResult(intent, 4);
			}
		});
		rootView.findViewById(R.id.frag_news_review).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AppApplication.getContext(), MessageActivity.class);
				intent.putExtra("kind", 5);
				//AppApplication.getContext().startActivity(intent);
				getActivity().startActivityForResult(intent, 5);
			}
		});

		imageViewsList.add(0, (ImageView) rootView.findViewById(R.id.frag_news_remind1));
		imageViewsList.add(1, (ImageView) rootView.findViewById(R.id.frag_news_remind2));
		imageViewsList.add(2, (ImageView) rootView.findViewById(R.id.frag_news_remind3));
		imageViewsList.add(3, (ImageView) rootView.findViewById(R.id.frag_news_remind4));
		imageViewsList.add(4, (ImageView) rootView.findViewById(R.id.frag_news_remind5));
		for(int i = 0; i < 5; i++){
			imageViewsList.get(i).setVisibility(View.GONE);
		}

		//获取各种消息的数据，判断是否有新的消息，如果有则显示红点以示提醒
		OkHttpUtils.get().url(Urls.HOST + "msg/msgCentreIndex.json").build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						LogUtils.e("onError", e.getMessage());
					}

					@Override
					public void onResponse(String response, int id) {
						LogUtils.d("onResponse", response);
						JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
						JsonElement jsonElement = jsonObject.get("value");
						ArrayList<MsgCentreInfo> list = new Gson().fromJson(jsonElement, new TypeToken<List<MsgCentreInfo>>(){}.getType());
						for(int i = 0; i < list.size(); i++){
							if(list.get(i).getMsgTypeCount() > 0){
								imageViewsList.get(i).setVisibility(View.VISIBLE);
							}
						}
					}
				});


		return rootView;
	}

	@Override
	protected void setActionBarRes(TitleBarFragment.ActionBarRes actionBarRes) {
		super.setActionBarRes(actionBarRes);
		actionBarRes.titleBarUnVisible = false;
		actionBarRes.title = "消息";
	}

	@Override
	public void onResume() {
		super.onResume();
		for(int i = 0; i < 5; i++){
			imageViewsList.get(i).setVisibility(View.GONE);
		}
		//获取各种消息的数据，判断是否有新的消息或有信息未查看，如果有则显示红点以示提醒
		OkHttpUtils.get().url(Urls.HOST + "msg/msgCentreIndex.json").build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						LogUtils.e("onError", e.getMessage());
					}

					@Override
					public void onResponse(String response, int id) {
						LogUtils.d("onResponse", response);
						JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
						JsonElement jsonElement = jsonObject.get("value");
						ArrayList<MsgCentreInfo> list = new Gson().fromJson(jsonElement, new TypeToken<List<MsgCentreInfo>>(){}.getType());
						for(int i = 0; i < list.size(); i++){
							if(list.get(i).getMsgTypeCount() > 0){
								imageViewsList.get(i).setVisibility(View.VISIBLE);
							}
						}
					}
				});
	}
}
