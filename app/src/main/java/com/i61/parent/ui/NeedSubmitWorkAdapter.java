package com.i61.parent.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.i61.parent.AppApplication;
import com.i61.parent.R;
import com.i61.parent.common.Urls;
import com.i61.parent.common.data.BaseBean;
import com.i61.parent.common.data.BaseBeanCallback;
import com.i61.parent.common.data.UserCenter.Course;
import com.i61.parent.common.data.UserCenter.UserCenterValue;
import com.i61.parent.common.data.WorkCenter.CourseInfo;
import com.i61.parent.common.data.WorkCenter.WorkCenterValue;
import com.i61.parent.presenter.OnChangeSubmitWork;
import com.i61.parent.utils.LogUtils;
import com.i61.parent.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import okhttp3.Call;


/**
 * Created by linxiaodong on 2018/3/27.
 */


//补交作业窗口（弹出）的补交作业列表的RecycleView的适配器
public class NeedSubmitWorkAdapter extends RecyclerView.Adapter<NeedSubmitWorkAdapter.ViewHolder> {

    private List<String> dataList;//需要显示的作业列表内容
    private OnChangeSubmitWork onChangeSubmitWork;

    public NeedSubmitWorkAdapter(List<String> dataList) {
        this.dataList = dataList;
    }

    public NeedSubmitWorkAdapter(List<String> dataList, OnChangeSubmitWork onChangeSubmitWork) {
        this.dataList = dataList;
        this.onChangeSubmitWork = onChangeSubmitWork;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_needsubmititem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //由于使用了fragment，所以是使用LayoutInflater.inflate获取弹出窗口的布局进而获取到RecycleView，所以在布局文件中定义的height：wrap_content没有生效，通过下面两行代码重新规定每个item的高度
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        String text = dataList.get(position);
        holder.needSubmitTitle.setText(text);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = dataList.get(position);
                int index = Integer.parseInt(text.substring(text.indexOf("第") + 1, text.indexOf("节：")));
                List<Course> courseList = ((UserCenterValue) SharedPreferencesUtils.readObject(AppApplication.getContext(), "userCenter")).getUserCourseInfos();
                for (Course course : courseList) {
                    if (course.getIndex() == index) {
                        Log.d("lxd", "点击了需提交作业列表中的一项 " + index + " " + course.getCourseId());
                        onChangeSubmitWork.changeSubmitWork(course.getCourseId()); //type为1时代表点击一项未提交作业项目的情况，需访问网络确定用户是否提交过作业
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView needSubmitTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            needSubmitTitle = itemView.findViewById(R.id.need_submit_title);
        }

        public TextView getNeedSubmitTitle() {
            return needSubmitTitle;
        }
    }
}
