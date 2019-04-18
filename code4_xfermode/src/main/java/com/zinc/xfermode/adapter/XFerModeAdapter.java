package com.zinc.xfermode.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zinc.xfermode.R;
import com.zinc.xfermode.bean.XFerModeBean;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/29
 * @description
 */
public class XFerModeAdapter extends RecyclerView.Adapter<XFerModeAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final List<XFerModeBean> mData;

    private XFerModeListener mListener;

    public XFerModeAdapter(Context context, List<XFerModeBean> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void setListener(XFerModeListener listener) {
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_xfermode, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        XFerModeBean item = mData.get(position);
        holder.checkbox.setChecked(item.isSelect());
        holder.tvName.setText(item.getName());
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickItem(pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llItem;
        private CheckBox checkbox;
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            llItem = itemView.findViewById(R.id.ll_item);
            checkbox = itemView.findViewById(R.id.checkbox);
            tvName = itemView.findViewById(R.id.tv_name);
        }

    }

    public interface XFerModeListener {
        void onClickItem(int position);
    }

}
