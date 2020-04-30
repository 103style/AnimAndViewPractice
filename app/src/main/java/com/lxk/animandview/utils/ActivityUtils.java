package com.lxk.animandview.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.ViewGroup;

/**
 * @author https://github.com/103style
 * @date 2020/4/30 15:54
 */
public class ActivityUtils {


    /**
     * 根据View的Context来获取对应的Activity
     *
     * @return 该View所在的Activity
     */
    public static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        throw new RuntimeException("Activity not found!");
    }

    /**
     * 获取当前activity的根视图
     */
    public static ViewGroup getRootView(Context context) {
        return (ViewGroup) ActivityUtils.getActivity(context).getWindow().getDecorView();
    }
}
