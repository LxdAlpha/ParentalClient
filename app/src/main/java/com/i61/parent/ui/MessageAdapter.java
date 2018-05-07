package com.i61.parent.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.common.data.MsgInfoV2;
import com.i61.parent.ui.fragment.MessageFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by linxiaodong on 2018/4/13.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private static final int TYPE_ITEM =0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //底部FootView

    private ArrayList<MsgInfoV2> list;
    private int kind; //消息种类
    MessageFragment messageFragment;



    public MessageAdapter(ArrayList<MsgInfoV2> list, int kind, MessageFragment messageFragment) {
        this.list = list;
        this.kind = kind;
        this.messageFragment = messageFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //判断类型创建不同的View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.content.setText(list.get(position).getContent()); //设置内容

        SimpleDateFormat sf = new SimpleDateFormat("MM月dd日 HH:mm");  //设置时间
        Date date = new Date(list.get(position).getPushTime());
        String time = sf.format(date);
        holder.time.setText(time);

        //根据消息类型更改消息item左上角图标
        Bitmap bitmap = null;
        switch(kind){
            case 1:
                //根据用户是否阅读过信息改变图标颜色，若有则为彩色，若无则为灰色
                bitmap = getIcon(R.drawable.news_icon_inform, position);
                if(bitmap != null){
                    holder.icon.setImageBitmap(bitmap);
                }else{
                    holder.icon.setImageResource(R.drawable.news_icon_inform);
                }
                //holder.icon.setImageResource(R.drawable.news_icon_inform);
                break;
            case 2:
                bitmap = getIcon(R.drawable.news_icon_coin, position);
                if(bitmap != null){
                    holder.icon.setImageBitmap(bitmap);
                }else{
                    holder.icon.setImageResource(R.drawable.news_icon_coin);
                }
                //holder.icon.setImageResource(R.drawable.news_icon_coin);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        messageFragment.getActivity().setResult(Activity.RESULT_OK); //设置取消活动后的Intent返回值，实现点击消息项跳转到主页不同界面
                        messageFragment.getActivity().finish();

                    }
                });
                break;
            case 3:
                bitmap = getIcon(R.drawable.news_icon_scholarship, position);
                if(bitmap != null){
                    holder.icon.setImageBitmap(bitmap);
                }else{
                    holder.icon.setImageResource(R.drawable.news_icon_scholarship);
                }
                //holder.icon.setImageResource(R.drawable.news_icon_scholarship);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        messageFragment.getActivity().setResult(Activity.RESULT_OK);
                        messageFragment.getActivity().finish();
                    }
                });
                break;
            case 4:
                bitmap = getIcon(R.drawable.news_icon_work, position);
                if(bitmap != null){
                    holder.icon.setImageBitmap(bitmap);
                }else{
                    holder.icon.setImageResource(R.drawable.news_icon_work);
                }
                //holder.icon.setImageResource(R.drawable.news_icon_work);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        messageFragment.getActivity().setResult(Activity.RESULT_OK);
                        messageFragment.getActivity().finish();
                    }
                });
                break;
            case 5:
                bitmap = getIcon(R.drawable.news_icon_review, position);
                if(bitmap != null){
                    holder.icon.setImageBitmap(bitmap);
                }else{
                    holder.icon.setImageResource(R.drawable.news_icon_review);
                }
                //holder.icon.setImageResource(R.drawable.news_icon_review);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        messageFragment.getActivity().setResult(Activity.RESULT_OK);
                        messageFragment.getActivity().finish();
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        //return list.size();
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView time;
        TextView content;
        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.message_item_icon);
            time = itemView.findViewById(R.id.message_item_time);
            content = itemView.findViewById(R.id.message_item_content);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    //新建调整图片为灰色的矩阵ColorMatrix
    public float[] reset() {
        final float[] a = new float[20];
        Arrays.fill(a, 0);
        a[0] = 0.33F;  a[1] = 0.59F;  a[2] = 0.11F;  a[3] = 0;  a[4] = 0;
        a[5] = 0.33F;  a[6] = 0.59F;  a[7] = 0.11F;  a[8] = 0;  a[9] = 0;
        a[10] = 0.33F; a[11] = 0.59F; a[12] = 0.11F; a[13] = 0; a[14] = 0;
        a[15] = 0;     a[16] = 0;     a[17] = 0;     a[18] = 1; a[19] = 0;
        return a;
    }

    //新建灰色图标，当信息状态为已读时显示灰色图标，当信息状态为未读时显示彩色图标
    private Bitmap setImageMatrix(Bitmap bitmap){
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(reset());
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap,0,0,paint);
        return bmp;
    }

    //根据用户是否阅读过信息改变图标颜色
    private Bitmap getIcon(int id, int position){
        if(list.get(position).getReadState() == 1){//已阅读过
            return setImageMatrix(BitmapFactory.decodeResource(AppApplication.getContext().getResources(),id));
        }else{ //未阅读过
            return null;
        }
    }

    public void setList(ArrayList<MsgInfoV2> list) {
        this.list.clear();
        for(MsgInfoV2 msgInfoV2 : list){
            this.list.add(msgInfoV2);
        }
    }

    public void appendData(ArrayList<MsgInfoV2> list){
        for(MsgInfoV2 msgInfoV2 : list){
            this.list.add(msgInfoV2);
        }
    }

}
