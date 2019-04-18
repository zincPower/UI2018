package com.zinc.code_x2_recycleview.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zinc.code_x2_recycleview.R;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/2/27
 * @description
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mInflater;
    private final List<RecyclerBean> mData;

    // 绑定监听器
    private BindListener mBindListener;

    public RecyclerAdapter(Context context, List<RecyclerBean> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void setBindListener(BindListener bindListener) {
        this.mBindListener = bindListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case RecyclerBean.TYPE_ONE:
                viewHolder = new TypeOneViewHolder(
                        mInflater.inflate(R.layout.item_type_one, parent, false));
                break;
            case RecyclerBean.TYPE_TWO:
                viewHolder = new TypeTwoViewHolder(
                        mInflater.inflate(R.layout.item_type_two, parent, false));
                break;
            case RecyclerBean.TYPE_THREE:
                viewHolder = new TypeThreeViewHolder(
                        mInflater.inflate(R.layout.item_type_three, parent, false));
                break;
            case RecyclerBean.TYPE_FOUR:
                viewHolder = new TypeFourViewHolder(
                        mInflater.inflate(R.layout.item_type_four, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = mData.get(position).getType();
        switch (type) {
            case RecyclerBean.TYPE_ONE:
                TypeOneViewHolder typeOneViewHolder = (TypeOneViewHolder) holder;
                typeOneViewHolder.textView.setText(mData.get(position).getData().toString());
                break;
            case RecyclerBean.TYPE_TWO:

                break;
            case RecyclerBean.TYPE_THREE:

                break;
            case RecyclerBean.TYPE_FOUR:
                TypeFourViewHolder typeFourViewHolder = (TypeFourViewHolder) holder;
                typeFourViewHolder.textView.setText(mData.get(position).getData().toString());
                break;
        }

        if (mBindListener != null) {
            String targetText = "Bind:『" + mData.get(position).getData().toString() + "(Pos:" + position + ")』\n";
            mBindListener.onBind(targetText);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    static class TypeOneViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        TypeOneViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_type_one);
        }

        @Override
        public String toString() {
            return textView.getText().toString();
        }
    }

    static class TypeTwoViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        TypeTwoViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_type_two);
        }

        @Override
        public String toString() {
            return textView.getText().toString();
        }
    }

    static class TypeThreeViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        TypeThreeViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_type_three);
        }

        @Override
        public String toString() {
            return textView.getText().toString();
        }
    }

    static class TypeFourViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        TypeFourViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_type_four);
        }

        @Override
        public String toString() {
            return textView.getText().toString();
        }
    }

    public interface BindListener {
        void onBind(String text);
    }

}
