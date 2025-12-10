package com.example.tiktok.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiktok.R;
import com.example.tiktok.databinding.ItemCommentBinding;
import com.example.tiktok.model.CommentInfo;

/**
 * 评论列表Adapter
 */
public class CommentAdapter extends ListAdapter<CommentInfo, CommentAdapter.ViewHolder> {

    public CommentAdapter() {
        super(new DiffUtil.ItemCallback<CommentInfo>() {
            @Override
            public boolean areItemsTheSame(@NonNull CommentInfo oldItem, @NonNull CommentInfo newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull CommentInfo oldItem, @NonNull CommentInfo newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommentBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_comment,
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentInfo comment = getItem(position);
        holder.binding.setComment(comment);
        holder.binding.executePendingBindings();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCommentBinding binding;

        public ViewHolder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}