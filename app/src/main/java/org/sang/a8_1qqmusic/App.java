package org.sang.a8_1qqmusic;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class    App extends Application {
    private RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        // 使用Volley必须要在App中先得到一个 requestQueue实例
        requestQueue = Volley.newRequestQueue(this);

        // shareSDK的初始化
        ShareSDK.initSDK(this);
    }

    public RequestQueue getRequestQueue()
    {
        return requestQueue;
    }
}
