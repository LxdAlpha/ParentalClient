package com.i61.parent.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.i61.parent.R;
import com.i61.parent.common.data.UserCenter.ClassmateMoney;

import java.util.ArrayList;

/**
 * Created by linxiaodong on 2018/4/3.
 */

public class ClassDrawCoinAdapter extends RecyclerView.Adapter<ClassDrawCoinAdapter.ViewHolder>{
    ArrayList<ClassmateMoney> classmateMoneyArrayList;

    public ClassDrawCoinAdapter(ArrayList<ClassmateMoney> classmateMoneyArrayList) {
        this.classmateMoneyArrayList = classmateMoneyArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int     viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classcoin_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(classmateMoneyArrayList.get(position).getUserName());
        holder.drawMoney.setText("×" + classmateMoneyArrayList.get(position).getDrawMoney());
    }

    @Override
    public int getItemCount() {
        return classmateMoneyArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView drawMoney;
        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.classmatemoney_username);
            drawMoney = itemView.findViewById(R.id.classmatemoney_drawMoney);
        }
    }
}
