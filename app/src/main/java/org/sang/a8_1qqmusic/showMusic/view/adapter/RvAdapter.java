package org.sang.a8_1qqmusic.showMusic.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class RvAdapter<T> extends RecyclerView.Adapter {

    private List<T> list;
    private Context context;
    private LayoutInflater inflater;
    private int variableId;
    private int layoutResId;

    public RvAdapter(List<T> list, Context context, int variableId, int layoutResId) {
        this.list = list;
        this.context = context;
        this.variableId = variableId;
        this.layoutResId = layoutResId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(DataBindingUtil.inflate(inflater,layoutResId,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewDataBinding binding = ((MyViewHolder) holder).getBinding();

        binding.setVariable(variableId,list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;
        public MyViewHolder(ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());
            binding = dataBinding;

            dataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v  ) {
                    if(onItemClick != null)
                    {
                        onItemClick.itemClick(getLayoutPosition());
                    }
                }
            });

        }

        public ViewDataBinding getBinding()
        {
            return binding;
        }

    }

    public interface OnItemClick
    {
        void itemClick(int position);
    }

    private OnItemClick onItemClick;


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}
