package com.example.tiktok.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.example.tiktok.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 资源工具类：文件名转资源ID（带缓存）
 */
public class ResourceUtils {
    private static final Map<String, Integer> DRAWABLE_CACHE = new HashMap<>();

    /**
     * 获取Drawable资源ID
     */
    // 补充缓存的定义（如果还没定义）
    @BindingAdapter(value = {"resName", "context"}, requireAll = true)
    public static void loadImage(ImageView imageView, String resName, Context context) {
        // 1. 通过资源名获取资源ID
        int resId = ResourceUtils.getDrawableResId(context, resName);

        // 2. 将资源ID转换为Drawable并设置到ImageView
        if (resId != 0) {
            imageView.setImageResource(resId);
        }
    }
    public static int getDrawableResId(Context context, String resName) {
        // 日志1：打印传入的参数
        Log.d("ResourceUtil", "resName: " + resName + ", packageName: " + context.getPackageName());

        if (resName == null || resName.isEmpty()) {
            Log.d("ResourceUtil", "resName is null or empty, return 0");
            return 0;
        }
        // 查缓存
        if (DRAWABLE_CACHE.containsKey(resName)) {
            int cacheResId = DRAWABLE_CACHE.get(resName);
            Log.d("ResourceUtil", "get from cache, resName: " + resName + ", resId: " + cacheResId);
            return cacheResId;
        }
        // 获取资源ID
        int resId = context.getResources().getIdentifier(
                resName,
                "drawable",
                context.getPackageName()
        );
        // 日志2：打印获取的资源ID
        Log.d("ResourceUtil", "get from resources, resName: " + resName + ", resId: " + resId);

        // 缓存结果
        DRAWABLE_CACHE.put(resName, resId);
        return resId;
    }

    /**
     * 清空缓存
     */
    public static void clearCache() {
        DRAWABLE_CACHE.clear();
    }
}