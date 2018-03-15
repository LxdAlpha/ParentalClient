package com.i61.parent.ui;

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
import com.i61.parent.R;

import java.util.List;

/**
 * Created by linxiaodong on 2018/3/14.
 */

public class SelectAccountAdapter extends RecyclerView.Adapter<SelectAccountAdapter.ViewHolder>{

    private List<String> mNameList;

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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String name = mNameList.get(position);
        holder.name.setText(name);

        //Uri uri = Uri.parse("http://www.people.com.cn/mediafile/pic/20161022/76/4315084153778263996.jpg");
        //holder.image.setImageURI(uri);
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                .path(String.valueOf(R.drawable.head_portrait))
                .build();
        holder.image.setImageURI(uri);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("lxd", mNameList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNameList.size();
    }
}
