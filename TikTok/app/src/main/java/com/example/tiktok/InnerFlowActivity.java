package com.example.tiktok;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiktok.adapter.InnerFlowVideoAdapter;
import com.example.tiktok.databinding.ActivityInnerFlowBinding;
import com.example.tiktok.model.VideoInfo;
import com.example.tiktok.viewmodel.InnerFlowViewModel;

public class InnerFlowActivity extends AppCompatActivity {
    private ActivityInnerFlowBinding mBinding;
    private InnerFlowViewModel mViewModel;
    private InnerFlowVideoAdapter mAdapter;
    private int mStartPosition; // 初始播放位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_inner_flow);
        mViewModel = new ViewModelProvider(this).get(InnerFlowViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);

        // 获取初始视频ID，计算起始位置
        int videoId = getIntent().getIntExtra("video_id", 0);
        mStartPosition = findPositionByVideoId(videoId);

        // 初始化RecyclerView
        initRecyclerView();

        // 监听滑动，切换视频播放状态
        mBinding.rvInnerFlow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 获取当前可见的Item位置
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    mViewModel.switchToPosition(firstVisiblePosition);
                }
            }
        });

        // 监听评论点击
        mViewModel.openCommentCallback = video -> {
            Intent intent = new Intent(this, CommentPanelActivity.class);
            intent.putExtra("video_id", video.getId());
            startActivity(intent);
        };
    }

    private void initRecyclerView() {
        mAdapter = new InnerFlowVideoAdapter(mViewModel);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL); // 垂直滑动
        mBinding.rvInnerFlow.setLayoutManager(layoutManager);
        mBinding.rvInnerFlow.setAdapter(mAdapter);

        // 观察视频列表变化
        mViewModel.videoListLiveData.observe(this, videoList -> {
            mAdapter.submitList(videoList);
            // 滚动到初始位置
            mBinding.rvInnerFlow.scrollToPosition(mStartPosition);
        });
    }

    // 根据视频ID查找位置
    private int findPositionByVideoId(int videoId) {
        List<VideoInfo> videoList = mViewModel.videoListLiveData.getValue();
        if (videoList == null) return 0;
        for (int i = 0; i < videoList.size(); i++) {
            if (videoList.get(i).getId() == videoId) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.isPlaying.setValue(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.isPlaying.setValue(true);
    }
}