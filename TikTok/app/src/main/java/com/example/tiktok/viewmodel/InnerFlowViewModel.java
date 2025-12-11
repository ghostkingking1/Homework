package com.example.tiktok.viewmodel;

import android.app.Application;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.tiktok.model.VideoInfo;
import com.example.tiktok.repository.VideoRepository;

/**
 * 视频内流ViewModel：处理视频播放页面的业务逻辑
 */
public class InnerFlowViewModel extends AndroidViewModel {
    // 当前视频LiveData
    public MutableLiveData<VideoInfo> currentVideoLiveData = new MutableLiveData<>();
    // 视频播放状态
    public MutableLiveData<Boolean> isPlaying = new MutableLiveData<>(false);
    // 数据仓库
    private final VideoRepository mRepository;

    public InnerFlowViewModel(@NonNull Application application) {
        super(application);
        mRepository = VideoRepository.getInstance(application);
    }

    public VideoInfo getCurrentVideo() {
        return currentVideoLiveData.getValue();
    }

    /**
     * 设置当前播放的视频
     */
    public void setCurrentVideo(VideoInfo video) {
        currentVideoLiveData.setValue(video);
        // 视频设置后默认播放
        startPlay();
    }

    /**
     * 开始播放视频
     */
    public void startPlay() {
        isPlaying.setValue(true);
    }

    /**
     * 暂停播放视频
     */
    public void pausePlay() {
        isPlaying.setValue(false);
    }

    /**
     * 切换播放/暂停状态
     */
    public void togglePlayPause() {
        isPlaying.setValue(!isPlaying.getValue());
    }

    /**
     * 切换点赞状态
     */
    public void toggleLike() {
        VideoInfo video = currentVideoLiveData.getValue();
        if (video != null) {
            video.setLiked(!video.isLiked());
            currentVideoLiveData.setValue(video); // 通知View层刷新
        }
    }

    /**
     * 获取视频Uri（供VideoView播放）
     */
    public Uri getVideoUri() {
        VideoInfo video = currentVideoLiveData.getValue();
        if (video != null) {
            return Uri.parse(video.getVideoUrl());
        }
        return null;
    }
}