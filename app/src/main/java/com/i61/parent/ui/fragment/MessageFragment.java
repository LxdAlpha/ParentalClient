package com.i61.parent.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.i61.parent.R;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.MsgInfoV2;
import com.i61.parent.ui.MessageAdapter;
import com.i61.parent.utils.LogUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by linxiaodong on 2018/4/12.
 */

public class MessageFragment extends TitleBarFragment{
    private static final String TAG = "ReviewFragment";
    private int kind;   //标识信息类型，1：通知 2：画币 3：奖学金 4：作业 5：点评
    RecyclerView recyclerView = null;
    ArrayList<MsgInfoV2> list = null; //根据消息类型获取信息放入
    MessageFragment messageFragment = null;
    MessageAdapter messageAdapter;

    int count;


    public MessageFragment() {

    }

    private View rootView = null; //缓存fragment的view，避免每次都创建新的


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        kind = intent.getIntExtra("kind", -1);
        messageFragment = this;

    }

    @Override
    protected View inflaterView(LayoutInflater inflater, final ViewGroup container, Bundle bundle) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_message, null);
        } else {
            //缓存的rootView需要判断是否已经在一个ViewGroup中， 如果在，就先移除自己，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }




        //设置recuclerView的相关事项
        recyclerView = rootView.findViewById(R.id.frag_message_recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        final LinearLayoutManager l = layoutManager;
        recyclerView.setLayoutManager(layoutManager);

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION, 25);//top间距
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION,25);//底部间距
        recyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));


        //设置下拉上拉刷新
        final RefreshLayout refreshLayout = (RefreshLayout)rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                refreshMsg(10, kind, 0, refreshLayout);
                Log.d("lxd", "count=" + count);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                loadMoreMsg(10, kind, count, refreshlayout);
                //refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });


        return rootView;
    }

    @Override
    protected void setActionBarRes(TitleBarFragment.ActionBarRes actionBarRes) {
        super.setActionBarRes(actionBarRes);
        actionBarRes.titleBarUnVisible = false;



        //todo 测试数据
        /*
        ArrayList<String> list = new ArrayList<>();
        list.add("习近平新时代中国特色社会主义思想");
        list.add("习近平新时代中国特色社会主义思想");
        list.add("习近平新时代中国特色社会主义思想");
        list.add("习近平新时代中国特色社会主义思想");
        list.add("习近平新时代中国特色社会主义思想");
        list.add("习近平新时代中国特色社会主义思想");
        list.add("习近平新时代中国特色社会主义思想");
        */




        MessageAdapter adapter = null;
        //通过kind的值分别设置fragment标题栏标题
        if(kind != -1){
            switch (kind){
                case 1:
                    actionBarRes.title = "通知";
                    /*
                    adapter = new MessageAdapter(list, 1, this);
                    recyclerView.setAdapter(adapter);
                    */
                    try {
                        getMsgByType(10, 1, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    actionBarRes.title = "画币消息";
                    /*
                    adapter = new MessageAdapter(list, 2, this);
                    recyclerView.setAdapter(adapter);
                    */
                    try {
                        getMsgByType(10, 2, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    actionBarRes.title = "奖学金消息";
                    /*
                    adapter = new MessageAdapter(list, 3, this);
                    recyclerView.setAdapter(adapter);
                    */
                    try {
                        getMsgByType(10, 3, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    actionBarRes.title = "作业消息";
                    /*
                    adapter = new MessageAdapter(list, 4, this);
                    recyclerView.setAdapter(adapter);
                    */
                    try {
                        getMsgByType(10, 4, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    actionBarRes.title = "点评消息";
                    /*
                    adapter = new MessageAdapter(list, 5, this);
                    recyclerView.setAdapter(adapter);
                    */
                    try {
                        getMsgByType(10, 5, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        actionBarRes.backImageId = R.drawable.titlebar_back; //设置返回按钮图片资源id
        actionBarRes.backText = "返回";                        //设置返回按钮右侧的文字


    }




    class RecyclerViewSpacesItemDecoration extends RecyclerView.ItemDecoration {
        private HashMap<String, Integer> mSpaceValueMap;
        public static final String TOP_DECORATION = "top_decoration";
        public static final String BOTTOM_DECORATION = "bottom_decoration";

        public RecyclerViewSpacesItemDecoration(HashMap<String, Integer> mSpaceValueMap){
            this.mSpaceValueMap = mSpaceValueMap;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (mSpaceValueMap.get(TOP_DECORATION) != null)
                outRect.top = mSpaceValueMap.get(TOP_DECORATION);
            if (mSpaceValueMap.get(BOTTOM_DECORATION) != null)
                outRect.bottom = mSpaceValueMap.get(BOTTOM_DECORATION);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    //根据消息类型获取消息列表
    private ArrayList<MsgInfoV2> getMsgByType(int limit, final int msgType, int offset) throws IOException {
        OkHttpUtils.get().url(Urls.HOST + "msg/getMsg.json")
                .addParams("limit", limit+"")
                .addParams("msgType", msgType+"")
                .addParams("offset", offset+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("onError:", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                        JsonElement jsonElement = jsonObject.get("value");
                        ArrayList<MsgInfoV2> msgInfoV2List = new Gson().fromJson(jsonElement, new TypeToken<List<MsgInfoV2>>(){}.getType());
                        MessageAdapter adapter = new MessageAdapter(msgInfoV2List, msgType, messageFragment);
                        recyclerView.setAdapter(adapter);
                        messageAdapter = adapter;

                        count = msgInfoV2List.size();
                    }
                });
        return null;
    }

    //下拉更新数据
    private void refreshMsg(int limit, final int msgType, int offset, final RefreshLayout refreshLayout){
        OkHttpUtils.get().url(Urls.HOST + "msg/getMsg.json")
                .addParams("limit", limit+"")
                .addParams("msgType", msgType+"")
                .addParams("offset", offset+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("onError:", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                        JsonElement jsonElement = jsonObject.get("value");
                        ArrayList<MsgInfoV2> msgInfoV2List = new Gson().fromJson(jsonElement, new TypeToken<List<MsgInfoV2>>(){}.getType());
                        messageAdapter.setList(msgInfoV2List);
                        messageAdapter.notifyDataSetChanged();
                        count = msgInfoV2List.size();
                        refreshLayout.finishRefresh();
                }
                });
    }


    //上拉加载更多数据
    private void loadMoreMsg(int limit, final int msgType, int offset, final RefreshLayout refreshLayout){
        OkHttpUtils.get().url(Urls.HOST + "msg/getMsg.json")
                .addParams("limit", limit+"")
                .addParams("msgType", msgType+"")
                .addParams("offset", offset+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("onError:", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                        JsonElement jsonElement = jsonObject.get("value");
                        ArrayList<MsgInfoV2> msgInfoV2List = new Gson().fromJson(jsonElement, new TypeToken<List<MsgInfoV2>>(){}.getType());
                        messageAdapter.appendData(msgInfoV2List);
                        messageAdapter.notifyDataSetChanged();
                        count = count + msgInfoV2List.size();
                        refreshLayout.finishLoadMore();
                    }
                });
    }



}
