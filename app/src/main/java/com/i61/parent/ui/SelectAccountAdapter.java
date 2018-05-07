package com.i61.parent.ui;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.i61.parent.AppApplication;
import com.i61.parent.R;

import com.i61.parent.common.data.LoginResponse.Account;
import com.i61.parent.common.data.LoginResponse.LoginResponse;
import com.i61.parent.presenter.SelectAccountPresenter;
import com.i61.parent.util.DialogUtil;
import com.i61.parent.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linxiaodong on 2018/3/14.
 */

public class SelectAccountAdapter extends RecyclerView.Adapter<SelectAccountAdapter.ViewHolder>{

    private List<String> mNameList;
    private List<String> mUrlList;
    ArrayList<Account> accountArrayList;
    private SelectAccountPresenter presenter;

    static class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView image;
        TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.account_image);
            name = itemView.findViewById(R.id.account_name);
        }
    }

    public SelectAccountAdapter(List<String> mNameList) {
        this.mNameList = mNameList;

    }

    public SelectAccountAdapter() {
        mNameList = new ArrayList<>();
        mUrlList = new ArrayList<>();
        /*
        String accountList = SharedPreferencesUtils.getValue(AppApplication.getContext(), "accountList", "");
        LoginResponse loginResponse = new Gson().fromJson(accountList, LoginResponse.class);
        for(Account account : loginResponse.getValue().getAccountList()){
            mNameList.add(account.getNickName());
            mUrlList.add(account.getHeadUrl());
        }
         */
        accountArrayList = (ArrayList<Account>)SharedPreferencesUtils.readObject(AppApplication.getContext(), "accountList");

        for(Account account : accountArrayList){
            mNameList.add(account.getNickName());
            mUrlList.add(account.getHeadUrl());
        }
    }

    public SelectAccountAdapter(SelectAccountPresenter presenter) {
        mNameList = new ArrayList<>();
        mUrlList = new ArrayList<>();
        /*
        String accountList = SharedPreferencesUtils.getValue(AppApplication.getContext(), "accountList", "");
        LoginResponse loginResponse = new Gson().fromJson(accountList, LoginResponse.class);
        for(Account account : loginResponse.getValue().getAccountList()){
            mNameList.add(account.getNickName());
            mUrlList.add(account.getHeadUrl());
        }
         */
        this.presenter = presenter;
        accountArrayList = (ArrayList<Account>)SharedPreferencesUtils.readObject(AppApplication.getContext(), "accountList");
        for(Account account : accountArrayList){
            mNameList.add(account.getNickName());
            mUrlList.add(account.getHeadUrl());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String name = mNameList.get(position);
        //holder.name.setText(name);

        //Uri uri = Uri.parse("http://www.people.com.cn/mediafile/pic/20161022/76/4315084153778263996.jpg");
        //holder.image.setImageURI(uri);
        /*
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                .path(String.valueOf(R.drawable.head_portrait))
                .build();
        holder.image.setImageURI(uri);
        */
        holder.image.setImageURI(mUrlList.get(position));
        holder.name.setText(mNameList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //选择一个孩子后，将这个孩子的信息放进SharePreference，记得在返回时将其删除
                SharedPreferencesUtils.saveObject(AppApplication.getContext(), accountArrayList.get(position), "activeAccount");

                /*
                Intent intent = new Intent(AppApplication.getContext(), MainActivity.class);
                AppApplication.getContext().startActivity(intent);
                */
                Log.d("lxd", "SelectAccountAdapter中存入的activeAccount：" + ((Account)SharedPreferencesUtils.readObject(AppApplication.getContext(), "activeAccount")).getNickName());
                presenter.getRelatedInfo();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNameList.size();
    }


}
