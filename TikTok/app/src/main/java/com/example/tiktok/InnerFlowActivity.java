package com.example.tiktok;

import android.content.Intent;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.example.tiktok.databinding.ActivityInnerFlowBinding;
import com.example.tiktok.model.VideoInfo;
import com.example.tiktok.repository.VideoRepository;
import com.example.tiktok.viewmodel.InnerFlowViewModel;

/**
 * 视频内流Activity：播放视频
 */
public class InnerFlowActivity extends AppCompatActivity {
    private ActivityInnerFlowBinding mBinding;
    private InnerFlowViewModel mViewModel;
    private VideoInfo mCurrentVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_inner_flow);
        mViewModel = new ViewModelProvider(this).get(InnerFlowViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        // 获取传递的视频ID
        int videoId = getIntent().getIntExtra("video_id", 0);
        // 从仓库获取对应视频
        mCurrentVideo = VideoRepository.getInstance(this)
                .getOuterFlowVideoList()
                .stream()
                .filter(video -> video.getId() == videoId)
                .findFirst()
                .orElse(null);

        // 设置当前视频到ViewModel
        if (mCurrentVideo != null) {
            mViewModel.setCurrentVideo(mCurrentVideo);
            // 初始化VideoView
            initVideoView(mBinding.videoView);
        }

        // 观察播放状态，控制VideoView
        mViewModel.isPlaying.observe(this, isPlaying -> {
            if (isPlaying) {
                mBinding.videoView.start();
            } else {
                mBinding.videoView.pause();
            }
        });

        // 视频点击事件 - 切换播放/暂停
        mBinding.videoView.setOnClickListener(v -> mViewModel.togglePlayPause());

        // 评论按钮点击事件
        mBinding.ivComment.setOnClickListener(v -> {
            if (mCurrentVideo != null) {
                mViewModel.jumpToCommentPanel(InnerFlowActivity.this, mCurrentVideo.getId());
            }
        });

        // 点赞按钮点击事件（假设布局中有此按钮）
        mBinding.ivLike.setOnClickListener(v -> mViewModel.toggleLike());
    }

    /**
     * 初始化VideoView
     */
    private void initVideoView(VideoView videoView) {
        // 设置视频Uri
        videoView.setVideoURI(mViewModel.getVideoUri());
        // 设置媒体控制器（可选）
        videoView.setMediaController(new MediaController(this));
        // 准备完成后根据ViewModel状态决定是否播放
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true); // 设置循环播放
            if (mViewModel.isPlaying.getValue()) {
                videoView.start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 暂停播放
        mViewModel.pausePlay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 恢复播放
        mViewModel.startPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放资源
        mBinding.videoView.stopPlayback();
    }
}