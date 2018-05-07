package com.i61.parent.ui;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.BaseBean;
import com.i61.parent.common.data.BaseBeanCallback;
import com.i61.parent.common.data.LoginResponse.Account;
import com.i61.parent.common.data.UserCenter.UserCenterValue;
import com.i61.parent.common.data.WorkCenter.WorkCenterValue;
import com.i61.parent.ui.inter.ResumeInterface;
import com.i61.parent.utils.LogUtils;
import com.i61.parent.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by linxiaodong on 2018/4/10.
 * 个人中心切换帐号按钮弹出窗口中用户列表中RecycleView的Adapter
 */


public class DialogChangeAccountAdapter extends RecyclerView.Adapter<DialogChangeAccountAdapter.ViewHolder>{
    private List<String> mNameList;       //存储用户名称的列表
    private List<String> mUrlList;        //存储用户头像url的列表
    ArrayList<Account> accountArrayList; //存储账户下所有用户对象的列表
    Dialog dialog;                         //存储窗口的对象
    ResumeInterface resumeInterface;      //重新绘制fragemnt的接口


    public DialogChangeAccountAdapter(Dialog dialog) {
        mNameList = new ArrayList<>();
        mUrlList = new ArrayList<>();
        accountArrayList = (ArrayList<Account>) SharedPreferencesUtils.readObject(AppApplication.getContext(), "accountList");
        for(Account account : accountArrayList){
            mNameList.add(account.getNickName());
            mUrlList.add(account.getHeadUrl());
        }
        this.dialog = dialog;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView image;
        TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.account_image);
            name = itemView.findViewById(R.id.account_name);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        DialogChangeAccountAdapter.ViewHolder holder = new DialogChangeAccountAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String name = mNameList.get(position);
        holder.image.setImageURI(mUrlList.get(position));
        holder.name.setText(mNameList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //选择一个孩子后，将这个孩子的信息放进SharePreference，记得在返回时将其删除
                SharedPreferencesUtils.saveObject(AppApplication.getContext(), accountArrayList.get(position), "activeAccount");
                getUserCenter();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNameList.size();
    }

    //获取UserCenter信息
    void getUserCenter(){
        OkHttpUtils.get().url(Urls.HOST + "uc/userCenter.json")
                .addParams("userId", ((Account)SharedPreferencesUtils.readObject(AppApplication.getContext(), "activeAccount")).getUserId()+"")
                .build()
                .execute(new BaseBeanCallback<BaseBean<UserCenterValue>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("onError:", e.getMessage());
                        dialog.dismiss();
                        Toast.makeText(AppApplication.getContext(), "切换帐号失败", Toast.LENGTH_SHORT).show(); //如果失败，提示信息
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        if(((BaseBean<UserCenterValue>)response).success == true && ((BaseBean<UserCenterValue>)response).getResultCode().success == true){
                            LogUtils.d("onResponse:", response.toString());
                            SharedPreferencesUtils.saveObject(AppApplication.getContext(), ((BaseBean<UserCenterValue>)response).value, "userCenter");
                            //注意必须先获取userCenter再获取workCenter信息
                            getWorkCenter();
                            dialog.dismiss(); //取消显示窗口
                            resumeInterface.resume(); //重新绘制Personfragment的画面
                        }
                    }
                });
    }

    //获取WorkCenter信息
    public void getWorkCenter() {
        OkHttpUtils.get().url(Urls.HOST + "wc/workCenter.json")
                .addParams("courseId", 0+"")
                .build()
                .execute(new BaseBeanCallback<BaseBean<WorkCenterValue>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("onError:", e.getMessage());
                        dialog.dismiss();
                        Toast.makeText(AppApplication.getContext(), "切换帐号失败", Toast.LENGTH_SHORT).show();  //获取失败，提示信息
                    }
                    @Override
                    public void onResponse(Object response, int id) {
                        if(((BaseBean<WorkCenterValue>)response).success == true && ((BaseBean<WorkCenterValue>)response).getResultCode().success == true){
                            LogUtils.d("onResponse:", response.toString());
                            SharedPreferencesUtils.saveObject(AppApplication.getContext(), ((BaseBean<WorkCenterValue>)response).value, "workCenter");
                        }
                    }
                });
    }


    public void setResumeInterface(ResumeInterface resumeInterface) {
        this.resumeInterface = resumeInterface;
    }

}
