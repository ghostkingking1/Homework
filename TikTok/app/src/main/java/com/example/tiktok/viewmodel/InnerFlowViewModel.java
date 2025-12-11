package com.example.tiktok.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.tiktok.model.VideoInfo;
import com.example.tiktok.repository.VideoRepository;
import java.util.List;

public class InnerFlowViewModel extends AndroidViewModel {
    public MutableLiveData<List<VideoInfo>> videoListLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPlaying = new MutableLiveData<>(false);
    public MutableLiveData<Integer> currentPosition = new MutableLiveData<>(0); // 当前播放位置
    private final VideoRepository mRepository;
    private VideoView mCurrentVideoView; // 当前正在播放的VideoView

    public InnerFlowViewModel(@NonNull Application application) {
        super(application);
        mRepository = VideoRepository.getInstance(application);
        // 加载所有视频列表（实际项目可分页加载）
        videoListLiveData.setValue(mRepository.getOuterFlowVideoList());
    }

    /**
     * 初始化指定位置的视频
     */
    public void initVideoAtPosition(VideoView videoView, int position, VideoInfo video) {
        videoView.setVideoURI(Uri.parse(video.getVideoUrl()));
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true); // 循环播放
            // 如果是当前位置，自动播放
            if (position == currentPosition.getValue()) {
                mCurrentVideoView = videoView;
                videoView.start();
                isPlaying.setValue(true);
            }
        });
    }

    /**
     * 切换到指定位置的视频
     */
    public void switchToPosition(int position) {
        // 暂停上一个视频
        if (mCurrentVideoView != null) {
            mCurrentVideoView.pause();
        }
        currentPosition.setValue(position);
        isPlaying.setValue(true);
    }

    /**
     * 切换点赞状态
     */
    public void toggleLike(VideoInfo video) {
        video.setLiked(!video.isLiked());
        // 通知列表刷新
        videoListLiveData.setValue(videoListLiveData.getValue());
    }

    /**
     * 打开评论页
     */
    public void openComment(VideoInfo video) {
        // 触发Activity跳转（通过接口回调实现，见下一步）
    }

    // 播放/暂停控制
    public void togglePlayPause() {
        if (mCurrentVideoView == null) return;
        if (isPlaying.getValue()) {
            mCurrentVideoView.pause();
        } else {
            mCurrentVideoView.start();
        }
        isPlaying.setValue(!isPlaying.getValue());
    }
}