package com.example.tiktok.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.example.tiktok.BR;

import java.util.Objects;

/**
 * 视频实体类（继承BaseObservable支持DataBinding双向绑定）
 */
public class VideoInfo extends BaseObservable {
    // 视频ID
    private int id;
    // 视频标题
    private String title;
    // 视频封面（本地资源名）
    private String coverResName;
    // 视频地址（本地/网络URL）
    private String videoUrl;
    // 发布者昵称
    private String publisherName;
    // 发布者头像（本地资源名）
    private String publisherAvatarResName;
    // 点赞数
    private int likeCount;
    // 是否已点赞
    private boolean isLiked;
    // 评论数
    private int commentCount;

    // 无参构造（DataBinding需要）
    public VideoInfo() {}

    // 带参构造（Mock数据用）
    public VideoInfo(int id, String title, String coverResName, String videoUrl,
                     String publisherName, String publisherAvatarResName,
                     int likeCount, boolean isLiked, int commentCount) {
        this.id = id;
        this.title = title;
        this.coverResName = coverResName;
        this.videoUrl = videoUrl;
        this.publisherName = publisherName;
        this.publisherAvatarResName = publisherAvatarResName;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.commentCount = commentCount;
    }

    // ----------------- Getter/Setter（带@Bindable支持DataBinding刷新） -----------------
    @Bindable
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getCoverResName() {
        return coverResName;
    }
    public void setCoverResName(String coverResName) {
        this.coverResName = coverResName;
        notifyPropertyChanged(BR.coverResName);
    }

    @Bindable
    public String getVideoUrl() {
        return videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        notifyPropertyChanged(BR.videoUrl);
    }

    @Bindable
    public String getPublisherName() {
        return publisherName;
    }
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
        notifyPropertyChanged(BR.publisherName);
    }

    @Bindable
    public String getPublisherAvatarResName() {
        return publisherAvatarResName;
    }
    public void setPublisherAvatarResName(String publisherAvatarResName) {
        this.publisherAvatarResName = publisherAvatarResName;
        notifyPropertyChanged(BR.publisherAvatarResName);
    }

    @Bindable
    public int getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
        notifyPropertyChanged(BR.likeCount);
    }

    @Bindable
    public boolean isLiked() {
        return isLiked;
    }
    public void setLiked(boolean liked) {
        isLiked = liked;
        // 点赞状态变化时，同步更新点赞数
        if (liked) {
            setLikeCount(getLikeCount() + 1);
        } else {
            setLikeCount(getLikeCount() - 1);
        }
        notifyPropertyChanged(BR.liked);
    }

    @Bindable
    public int getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
        notifyPropertyChanged(BR.commentCount);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 地址相同直接返回true
        if (o == null || getClass() != o.getClass()) return false; // 类型不同返回false
        VideoInfo videoInfo = (VideoInfo) o;
        // 比较所有需要判断“内容是否相同”的字段
        return id == videoInfo.id &&
                likeCount == videoInfo.likeCount &&
                isLiked == videoInfo.isLiked &&
                commentCount == videoInfo.commentCount &&
                Objects.equals(title, videoInfo.title) &&
                Objects.equals(coverResName, videoInfo.coverResName) &&
                Objects.equals(videoUrl, videoInfo.videoUrl) &&
                Objects.equals(publisherName, videoInfo.publisherName) &&
                Objects.equals(publisherAvatarResName, videoInfo.publisherAvatarResName);
    }

    // 重写hashCode()：与equals()保持一致（规范要求）
    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverResName, videoUrl, publisherName, publisherAvatarResName, likeCount, isLiked, commentCount);
    }
}