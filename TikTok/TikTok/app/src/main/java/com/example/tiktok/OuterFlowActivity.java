package com.example.tiktok;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.tiktok.R;
import com.example.tiktok.adapter.OuterFlowVideoAdapter;
import com.example.tiktok.databinding.ActivityOuterFlowBinding;
import com.example.tiktok.model.VideoInfo;
import com.example.tiktok.viewmodel.OuterFlowViewModel;

/**
 * 双列外流Activity：展示双列视频列表（核心页面）
 * 功能：
 * 1. 双列RecyclerView展示视频列表
 * 2. 下拉刷新视频数据
 * 3. 上拉加载更多视频
 * 4. Item点击跳转到视频内流页面
 * 5. 底部Tab/顶部Tab布局（模拟）
 */
public class OuterFlowActivity extends AppCompatActivity {
    // DataBinding对象（绑定activity_outer_flow.xml布局）
    private ActivityOuterFlowBinding mBinding;
    // 双列外流ViewModel（处理业务逻辑）
    private OuterFlowViewModel mViewModel;
    // 双列视频列表Adapter
    private OuterFlowVideoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ==================== 1. 初始化DataBinding ====================
        // 绑定布局，替代setContentView
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_outer_flow);
        // 允许DataBinding感知Activity生命周期（关键：LiveData自动更新UI）
        mBinding.setLifecycleOwner(this);

        // ==================== 2. 初始化ViewModel ====================
        // 通过ViewModelProvider创建ViewModel（避免重复创建）
        mViewModel = new ViewModelProvider(this).get(OuterFlowViewModel.class);
        // 将ViewModel绑定到布局（供XML中直接使用）
        mBinding.setViewModel(mViewModel);

        // ==================== 3. 初始化RecyclerView ====================
        // 创建Adapter，传入ViewModel（供Item布局使用）
        mAdapter = new OuterFlowVideoAdapter(mViewModel);
        // 设置RecyclerView布局管理器：2列网格布局
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mBinding.rvOuterFlow.setLayoutManager(gridLayoutManager);
        // 设置Adapter
        mBinding.rvOuterFlow.setAdapter(mAdapter);

        // ==================== 4. 观察ViewModel数据变化 ====================
        // 观察视频列表变化：数据更新时自动刷新Adapter
        mViewModel.videoListLiveData.observe(this, videoList -> {
            // 提交新列表到Adapter（ListAdapter自带DiffUtil局部刷新）
            mAdapter.submitList(videoList);
        });

        // 观察刷新状态：同步SwipeRefreshLayout的刷新动画
        mViewModel.isRefreshing.observe(this, isRefreshing -> {
            mBinding.swipeRefresh.setRefreshing(isRefreshing);
        });

        // ==================== 5. 下拉刷新监听 ====================
        // 修复点：规范OnRefreshListener写法（Lambda表达式）
        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 触发ViewModel的刷新方法
                mViewModel.refreshOuterFlowVideos();
            }
        });

        // ==================== 6. 上拉加载更多监听 ====================
        // 通过RecyclerView滚动监听实现上拉加载
        mBinding.rvOuterFlow.addOnScrollListener(new androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(androidx.recyclerview.widget.RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 仅处理向上滚动（dy>0）
                if (dy <= 0) {
                    return;
                }
                // 获取最后一个可见的Item位置
                int lastVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                // 获取列表总数量
                int totalItemCount = gridLayoutManager.getItemCount();
                // 滑到最后一个Item，且不在加载中 → 触发加载更多
                if (lastVisibleItemPosition == totalItemCount - 1
                        && !mViewModel.isLoadingMore.getValue()) {
                    mViewModel.loadMoreOuterFlowVideos();
                }
            }
        });

        // ==================== 7. Item点击事件 ====================
        // 设置视频Item点击监听，跳转到视频内流页面
        mAdapter.setOnItemClickListener(video -> {
            // 构建跳转Intent，传递视频ID
            Intent intent = new Intent(OuterFlowActivity.this, InnerFlowActivity.class);
            intent.putExtra("video_id", video.getId());
            startActivity(intent);
        });
    }

    // ==================== 生命周期管理 ====================
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放ViewModel资源（避免内存泄漏）
        mViewModel.clear();
        // 解除DataBinding绑定（可选，DataBinding会自动处理，但显式解除更安全）
        if (mBinding != null) {
            mBinding.unbind();
        }
    }
}