package com.example.tiktok.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiktok.R;
import com.example.tiktok.databinding.ItemOuterFlowVideoBinding;
import com.example.tiktok.model.VideoInfo;
import com.example.tiktok.viewmodel.OuterFlowViewModel;

/**
 * 双列外流Adapter：绑定双列Item布局
 */
public class OuterFlowVideoAdapter extends ListAdapter<VideoInfo, OuterFlowVideoAdapter.ViewHolder> {
    // ViewModel（供Item布局用）
    private final OuterFlowViewModel mViewModel;
    // Item点击回调
    private OnItemClickListener mOnItemClickListener;

    // DiffUtil：局部刷新（ListAdapter自带）
    public OuterFlowVideoAdapter(OuterFlowViewModel viewModel) {
        super(new DiffUtil.ItemCallback<VideoInfo>() {
            @Override
            public boolean areItemsTheSame(@NonNull VideoInfo oldItem, @NonNull VideoInfo newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull VideoInfo oldItem, @NonNull VideoInfo newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.mViewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // DataBinding加载Item布局
        ItemOuterFlowVideoBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_outer_flow_video,
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoInfo video = getItem(position);
        // 绑定数据到Item布局
        holder.binding.setVideo(video);
        holder.binding.setViewModel(mViewModel);
        // 设置Item点击事件
        holder.binding.setOnItemClick(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(video);
            }
        });
        // 立即执行绑定（避免延迟）
        holder.binding.executePendingBindings();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOuterFlowVideoBinding binding;

        public ViewHolder(ItemOuterFlowVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    // Item点击回调接口
    public interface OnItemClickListener {
        void onItemClick(VideoInfo video);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}