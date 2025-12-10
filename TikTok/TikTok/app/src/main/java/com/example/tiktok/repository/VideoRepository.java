package com.example.tiktok.repository;

import android.content.Context;
import com.example.tiktok.model.CommentInfo;
import com.example.tiktok.model.VideoInfo;
import com.example.tiktok.utils.ResourceUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 数据仓库：负责提供视频/评论数据（Mock模拟，实际项目可替换为网络请求）
 */
public class VideoRepository {
    // 单例模式（保证全局唯一）
    private static VideoRepository instance;
    private final Context mContext;
    private final Random mRandom = new Random();

    private VideoRepository(Context context) {
        this.mContext = context.getApplicationContext(); // 用Application Context避免内存泄漏
    }
    public static VideoRepository getInstance(Context context) {
        if (instance == null) {
            instance = new VideoRepository(context);
        }
        return instance;
    }

    /**
     * 获取双列外流的视频列表（Mock数据）
     */
    public List<VideoInfo> getOuterFlowVideoList() {
        List<VideoInfo> videoList = new ArrayList<>();
        // Mock 10条视频数据
        for (int i = 0; i < 10; i++) {
            videoList.add(new VideoInfo(
                    i,
                    "这是视频标题" + i,
                    "video_cover_" + (i % 5), // 假设本地有video_cover_0~4的资源
                    "android.resource://com.example.tiktok/raw/test_video", // 本地测试视频
                    "发布者" + i,
                    "avatar_" + (i % 3), // 假设本地有avatar_0~2的资源
                    mRandom.nextInt(10000), // 随机点赞数
                    mRandom.nextBoolean(), // 随机点赞状态
                    mRandom.nextInt(500) // 随机评论数
            ));
        }
        return videoList;
    }

    /**
     * 加载更多双列外流视频（Mock）
     */
    public List<VideoInfo> loadMoreOuterFlowVideos(int startIndex) {
        List<VideoInfo> moreVideos = new ArrayList<>();
        for (int i = startIndex; i < startIndex + 5; i++) {
            moreVideos.add(new VideoInfo(
                    i,
                    "加载更多视频标题" + i,
                    "video_cover_" + (i % 5),
                    "android.resource://com.example.tiktok/raw/test_video",
                    "发布者" + i,
                    "avatar_" + (i % 3),
                    mRandom.nextInt(10000),
                    mRandom.nextBoolean(),
                    mRandom.nextInt(500)
            ));
        }
        return moreVideos;
    }

    /**
     * 获取某视频的评论列表（Mock）
     */
    public List<CommentInfo> getCommentList(int videoId) {
        List<CommentInfo> commentList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            commentList.add(new CommentInfo(
                    videoId * 100 + i,
                    "这是评论内容" + i,
                    "评论者" + i,
                    "avatar_" + (i % 3)
            ));
        }
        return commentList;
    }

    /**
     * 发布新评论（Mock）
     */
    public CommentInfo publishComment(int videoId, String content) {
        return new CommentInfo(
                videoId * 100 + 100,
                content,
                "我自己",
                "avatar_0"
        );
    }
}