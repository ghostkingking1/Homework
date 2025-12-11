//package com.example.tiktok.adapter;
//
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import androidx.annotation.NonNull;
//import androidx.databinding.DataBindingUtil;
//import androidx.recyclerview.widget.DiffUtil;
//import androidx.recyclerview.widget.ListAdapter;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.tiktok.R;
//import com.example.tiktok.databinding.ItemInnerFlowVideoBinding;
//import com.example.tiktok.model.VideoInfo;
//import com.example.tiktok.viewmodel.InnerFlowViewModel;
//
///**
// * 内流视频Adapter：适配竖屏滑动播放的短视频列表
// * 核心功能：绑定视频布局、关联ViewModel处理播放/点赞/评论逻辑
// */
//public class InnerFlowAdapter extends ListAdapter<VideoInfo, InnerFlowAdapter.ViewHolder> {
//    // 内流ViewModel（处理视频播放/业务逻辑）
//    private final InnerFlowViewModel mViewModel;
//
//    public InnerFlowAdapter(InnerFlowViewModel viewModel) {
//        super(new DiffUtil.ItemCallback<VideoInfo>() {
//            @Override
//            public boolean areItemsTheSame(@NonNull VideoInfo oldItem, @NonNull VideoInfo newItem) {
//                return oldItem.getId() == newItem.getId();
//            }
//
//            @Override
//            public boolean areContentsTheSame(@NonNull VideoInfo oldItem, @NonNull VideoInfo newItem) {
//                return oldItem.equals(newItem);
//            }
//        });
//        this.mViewModel = viewModel;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // 绑定内流视频Item布局
//        ItemInnerFlowVideoBinding binding = DataBindingUtil.inflate(
//                LayoutInflater.from(parent.getContext()),
//                R.layout.item_inner_flow_video,
//                parent,
//                false
//        );
//        // 关联ViewModel到布局
//        binding.setViewModel(mViewModel);
//        binding.setLifecycleOwner(() -> null); // 简化：复用Activity的LifecycleOwner
//        return new ViewHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        VideoInfo video = getItem(position);
//        // 设置当前视频到ViewModel
//        mViewModel.setCurrentVideo(video);
//        // 绑定视频数据到布局
//        holder.binding.setVideo(video);
//        holder.binding.executePendingBindings();
//
//        // 播放当前可见的视频（滑动到该Item时自动播放）
//        mViewModel.startPlay();
//
//        // 评论按钮点击跳转
//        holder.binding.ivComment.setOnClickListener(v -> {
//            mViewModel.jumpToCommentPanel(holder.itemView.getContext(), video.getId());
//        });
//    }
//
//    @Override
//    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
//        super.onViewDetachedFromWindow(holder);
//        // 视频滑出屏幕时暂停播放
//        mViewModel.pausePlay();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        private final ItemInnerFlowVideoBinding binding;
//
//        public ViewHolder(ItemInnerFlowVideoBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//    }
//}