package com.zinc.animation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zinc.animation.R;
import com.zinc.animation.bean.TimeInterpolatorBean;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/24
 * @description
 */
public class TimeInterpolatorAdapter extends RecyclerView.Adapter<TimeInterpolatorAdapter.ViewHolder> {

    private final List<TimeInterpolatorBean> mDataList;
    private LayoutInflater mInflater;
    private ClickListener mListener;

    public TimeInterpolatorAdapter(Context context, List<TimeInterpolatorBean> list) {
        mDataList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_time_interpolator, parent, false));
    }

    public void setListener(ClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int curPosition = position;
        TimeInterpolatorBean timeInterpolatorBean = mDataList.get(curPosition);

        holder.checkbox.setChecked(timeInterpolatorBean.isSelect());
        holder.name.setText(timeInterpolatorBean.getName());

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onTimeInterpolatorClick(curPosition);
                }
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkbox;
        private TextView name;
        private LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox);
            name = itemView.findViewById(R.id.name);
            llItem = itemView.findViewById(R.id.ll_item);
        }
    }

    public interface ClickListener {
        void onTimeInterpolatorClick(int position);
    }

}
