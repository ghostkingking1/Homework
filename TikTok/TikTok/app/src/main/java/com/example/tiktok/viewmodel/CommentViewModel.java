package com.example.tiktok.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.tiktok.model.CommentInfo;
import com.example.tiktok.model.VideoInfo;
import com.example.tiktok.repository.VideoRepository;
import java.util.List;

public class CommentViewModel extends AndroidViewModel {
    public MutableLiveData<List<CommentInfo>> commentListLiveData = new MutableLiveData<>();
    public MutableLiveData<VideoInfo> currentVideoLiveData = new MutableLiveData<>();
    public MutableLiveData<String> newCommentContent = new MutableLiveData<>("");
    private final VideoRepository mRepository;
    private List<CommentInfo> mCommentList;
    // 新增：主线程Handler
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public CommentViewModel(@NonNull Application application) {
        super(application);
        mRepository = VideoRepository.getInstance(application);
    }

    public void setCurrentVideoAndLoadComments(VideoInfo video) {
        currentVideoLiveData.setValue(video);
        loadComments(video.getId());
    }

    private void loadComments(int videoId) {
        new Thread(() -> {
            mCommentList = mRepository.getCommentList(videoId);
            // 用Handler切换到主线程
            mainHandler.post(() -> {
                commentListLiveData.setValue(mCommentList);
            });
        }).start();
    }

    public void publishComment() {
        String content = newCommentContent.getValue();
        if (content == null || content.isEmpty()) {
            return;
        }
        VideoInfo video = currentVideoLiveData.getValue();
        if (video == null) {
            return;
        }
        new Thread(() -> {
            CommentInfo newComment = mRepository.publishComment(video.getId(), content);
            // 同样用Handler切换主线程
            mainHandler.post(() -> {
                mCommentList.add(0, newComment);
                commentListLiveData.setValue(mCommentList);
                video.setCommentCount(video.getCommentCount() + 1);
                currentVideoLiveData.setValue(video);
                newCommentContent.setValue("");
            });
        }).start();
    }
}