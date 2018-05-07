package com.i61.parent.ui;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.common.FileItem;
import com.i61.parent.util.ScreenUtil;
import com.i61.parent.util.UriUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.List;

/**
 * Created by linxiaodong on 2018/3/30.
 */

public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.ViewHolder>{
    //private List<Uri> dataList;

    private List<FileItem> testDataList;

    private Activity parentActivity;

    public SelectImageAdapter(List<FileItem> dataList) {
        this.testDataList = dataList;
    }

    public void setParentActivity(Activity parentActivity) {
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_image_item, parent, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.getLayoutParams());
        params.height = ScreenUtil.getScreenParameterPx()[0] / 3;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(params);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String filePath = testDataList.get(position).getFilePath();
        Uri uri = UriUtil.getImageContentUri(AppApplication.getContext(), new File(filePath));
        holder.imageView.setImageURI(uri);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity(UriUtil.getImageContentUri(AppApplication.getContext(), new File(testDataList.get(position).getFilePath())))
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(parentActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return testDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.select_image);
        }
    }
}
