package com.example.tiktok.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.tiktok.CommentPanelActivity;
import com.example.tiktok.model.VideoInfo;
import com.example.tiktok.repository.VideoRepository;
import java.util.List;

/**
 * 视频内流ViewModel：处理视频播放页面的业务逻辑
 */
public class InnerFlowViewModel extends AndroidViewModel {
    // 视频列表LiveData
    public MutableLiveData<List<VideoInfo>> videoListLiveData = new MutableLiveData<>();
    // 当前视频LiveData
    public MutableLiveData<VideoInfo> currentVideoLiveData = new MutableLiveData<>();
    // 视频播放状态
    public MutableLiveData<Boolean> isPlaying = new MutableLiveData<>(false);
    // 当前播放位置
    public MutableLiveData<Integer> currentPosition = new MutableLiveData<>(0);
    // 数据仓库
    private final VideoRepository mRepository;

    public InnerFlowViewModel(@NonNull Application application) {
        super(application);
        mRepository = VideoRepository.getInstance(application);
        // 加载所有视频列表（实际项目可分页加载）
        videoListLiveData.setValue(mRepository.getOuterFlowVideoList());
    }

    /**
     * 获取当前视频
     */
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
     * 切换到指定位置的视频
     */
    public void switchToPosition(int position) {
        currentPosition.setValue(position);
        // 更新当前视频信息
        List<VideoInfo> videoList = videoListLiveData.getValue();
        if (videoList != null && position >= 0 && position < videoList.size()) {
            currentVideoLiveData.setValue(videoList.get(position));
            startPlay(); // 切换视频后自动播放
        }
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
        Boolean currentState = isPlaying.getValue();
        isPlaying.setValue(currentState != null && !currentState);
    }

    /**
     * 切换点赞状态
     */
    public void toggleLike() {
        VideoInfo video = currentVideoLiveData.getValue();
        if (video != null) {
            video.setLiked(!video.isLiked());
            // 更新数据并通知UI刷新
            currentVideoLiveData.setValue(video);
            videoListLiveData.setValue(videoListLiveData.getValue());
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

    /**
     * 跳转到评论面板
     * @param context 上下文
     * @param videoId 视频ID
     */
    public void jumpToCommentPanel(Context context, int videoId) {
        // 跳转前暂停视频播放
        pausePlay();
        // 启动评论面板Activity
        Intent intent = new Intent(context, CommentPanelActivity.class);
        intent.putExtra("video_id", videoId);
        context.startActivity(intent);
    }

    // 新增：处理评论按钮点击的openComment方法
    public void openComment(VideoInfo video) {
        if (video != null) {
            // 调用已有的jumpToCommentPanel方法（需传入Context，这里用Application的上下文）
            jumpToCommentPanel(getApplication(), video.getId());
        }
    }
}