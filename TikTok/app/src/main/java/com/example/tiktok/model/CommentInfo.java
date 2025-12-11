package com.example.tiktok.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.example.tiktok.BR;

import java.util.Objects;

/**
 * 评论实体类
 */
public class CommentInfo extends BaseObservable {
    // 评论ID
    private int id;
    // 评论内容
    private String content;
    // 评论者昵称
    private String commenterName;
    // 评论者头像
    private String commenterAvatarResName;

    public CommentInfo() {}
    public CommentInfo(int id, String content, String commenterName, String commenterAvatarResName) {
        this.id = id;
        this.content = content;
        this.commenterName = commenterName;
        this.commenterAvatarResName = commenterAvatarResName;
    }

    @Bindable
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
        notifyPropertyChanged(BR.content);
    }

    @Bindable
    public String getCommenterName() {
        return commenterName;
    }
    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
        notifyPropertyChanged(BR.commenterName);
    }

    @Bindable
    public String getCommenterAvatarResName() {
        return commenterAvatarResName;
    }
    public void setCommenterAvatarResName(String commenterAvatarResName) {
        this.commenterAvatarResName = commenterAvatarResName;
        notifyPropertyChanged(BR.commenterAvatarResName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentInfo that = (CommentInfo) o;
        return Objects.equals(id, that.id)
                && Objects.equals(content, that.content) // 按需添加其他字段
                && Objects.equals(commenterName, that.commenterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, commenterName);
    }
}