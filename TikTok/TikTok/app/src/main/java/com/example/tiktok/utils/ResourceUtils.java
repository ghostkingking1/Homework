package com.example.tiktok.utils;

import android.content.Context;
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
    public static int getDrawableResId(Context context, String resName) {
        if (resName == null || resName.isEmpty()) {
            return 0;
        }
        // 查缓存
        if (DRAWABLE_CACHE.containsKey(resName)) {
            return DRAWABLE_CACHE.get(resName);
        }
        // 获取资源ID
        int resId = context.getResources().getIdentifier(
                resName,
                "drawable",
                context.getPackageName()
        );
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