package com.example.tiktok;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.example.tiktok.R;
import com.example.tiktok.databinding.ActivityCommentPanelBinding;
import com.example.tiktok.model.VideoInfo;
import com.example.tiktok.repository.VideoRepository;
import com.example.tiktok.viewmodel.CommentViewModel;

/**
 * 评论面板Activity：展示视频评论列表，支持发布新评论
 */
public class CommentPanelActivity extends AppCompatActivity {
    // DataBinding对象
    private ActivityCommentPanelBinding mBinding;
    // 评论ViewModel
    private CommentViewModel mViewModel;
    // 当前视频信息
    private VideoInfo mCurrentVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1. 初始化DataBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_comment_panel);
        mBinding.setLifecycleOwner(this);

        // 2. 获取传递的视频ID
        int videoId = getIntent().getIntExtra("video_id", 0);
        // 3. 从仓库获取对应的视频信息
        mCurrentVideo = VideoRepository.getInstance(this)
                .getOuterFlowVideoList()
                .stream()
                .filter(video -> video.getId() == videoId)
                .findFirst()
                .orElse(null);

        // 4. 初始化ViewModel
        mViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        mBinding.setViewModel(mViewModel);
        // 5. 设置当前视频并加载评论
        if (mCurrentVideo != null) {
            mViewModel.setCurrentVideoAndLoadComments(mCurrentVideo);
        }

        // 6. 发布评论按钮点击事件
        mBinding.btnPublishComment.setOnClickListener(v -> {
            mViewModel.publishComment();
        });
    }
}