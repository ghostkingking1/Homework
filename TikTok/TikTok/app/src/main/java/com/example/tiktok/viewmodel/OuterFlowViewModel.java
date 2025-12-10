package com.example.tiktok.viewmodel;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.tiktok.model.VideoInfo;
import com.example.tiktok.repository.VideoRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * 双列外流ViewModel：处理双列页面的业务逻辑
 * 修复点：
 * 1. 移除runOnUiThread（Thread类无此方法）
 * 2. 使用Handler(Looper.getMainLooper())切换主线程
 * 3. 补充异常处理，避免空指针
 */
public class OuterFlowViewModel extends AndroidViewModel {
    // 视频列表LiveData（通知View层更新UI）
    public MutableLiveData<List<VideoInfo>> videoListLiveData = new MutableLiveData<>();
    // 下拉刷新状态
    public MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>(false);
    // 上拉加载状态
    public MutableLiveData<Boolean> isLoadingMore = new MutableLiveData<>(false);
    // 数据仓库
    private final VideoRepository mRepository;
    // 当前视频列表（初始化为空列表，避免空指针）
    private List<VideoInfo> mVideoList = new ArrayList<>();
    // 主线程Handler：用于子线程切换到主线程更新LiveData
    private final Handler mainHandler;

    public OuterFlowViewModel(@NonNull Application application) {
        super(application);
        // 初始化主线程Handler（绑定主线程消息队列）
        mainHandler = new Handler(Looper.getMainLooper());
        // 初始化数据仓库
        mRepository = VideoRepository.getInstance(application);
        // 初始化加载视频列表
        loadOuterFlowVideos();
    }

    /**
     * 加载双列外流视频列表（核心方法）
     * 逻辑：子线程获取数据 → 主线程更新LiveData
     */
    public void loadOuterFlowVideos() {
        // 标记为刷新中
        isRefreshing.setValue(true);

        // 子线程执行耗时操作（模拟网络请求/数据加载）
        new Thread(() -> {
            try {
                // 从仓库获取Mock数据（实际项目中是网络请求）
                List<VideoInfo> newVideoList = mRepository.getOuterFlowVideoList();
                // 非空保护：避免返回null导致后续异常
                mVideoList = newVideoList != null ? newVideoList : new ArrayList<>();

                // 切换到主线程更新LiveData（LiveData必须在主线程setValue）
                mainHandler.post(() -> {
                    videoListLiveData.setValue(mVideoList); // 更新视频列表
                    isRefreshing.setValue(false); // 结束刷新状态
                });
            } catch (Exception e) {
                // 捕获异常，避免子线程崩溃
                e.printStackTrace();
                // 主线程更新状态：结束刷新，返回空列表
                mainHandler.post(() -> {
                    mVideoList = new ArrayList<>();
                    videoListLiveData.setValue(mVideoList);
                    isRefreshing.setValue(false);
                });
            }
        }).start();
    }

    /**
     * 下拉刷新：重新加载最新视频列表
     */
    public void refreshOuterFlowVideos() {
        isRefreshing.setValue(true);

        new Thread(() -> {
            try {
                List<VideoInfo> newVideoList = mRepository.getOuterFlowVideoList();
                mVideoList = newVideoList != null ? newVideoList : new ArrayList<>();

                mainHandler.post(() -> {
                    videoListLiveData.setValue(mVideoList);
                    isRefreshing.setValue(false);
                });
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> {
                    isRefreshing.setValue(false);
                });
            }
        }).start();
    }

    /**
     * 上拉加载更多：追加视频数据
     */
    public void loadMoreOuterFlowVideos() {
        // 防止重复加载
        if (isLoadingMore.getValue() != null && isLoadingMore.getValue()) {
            return;
        }
        isLoadingMore.setValue(true);

        new Thread(() -> {
            try {
                // 加载更多数据（从当前列表长度开始）
                List<VideoInfo> moreVideos = mRepository.loadMoreOuterFlowVideos(mVideoList.size());
                // 非空保护
                if (moreVideos != null && !moreVideos.isEmpty()) {
                    mVideoList.addAll(moreVideos);
                }

                mainHandler.post(() -> {
                    videoListLiveData.setValue(mVideoList); // 通知UI更新
                    isLoadingMore.setValue(false); // 结束加载状态
                });
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> {
                    isLoadingMore.setValue(false);
                });
            }
        }).start();
    }

    /**
     * 获取上下文（供ResourceUtils用）
     * @return Application级别的Context，避免内存泄漏
     */
    public Context getContext() {
        return getApplication();
    }

    /**
     * 页面销毁时释放资源（可选）
     */
    public void clear() {
        // 移除未执行的Handler任务，避免内存泄漏
        mainHandler.removeCallbacksAndMessages(null);
        // 清空视频列表
        mVideoList.clear();
        videoListLiveData.setValue(new ArrayList<>());
    }
}