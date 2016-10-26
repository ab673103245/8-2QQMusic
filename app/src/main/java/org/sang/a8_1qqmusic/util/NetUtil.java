package org.sang.a8_1qqmusic.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class NetUtil {
    public static String PLAYLISTURL = "http://route.showapi.com/213-4?showapi_appid=25798&showapi_sign=6d9b08da66aa47adb911f1065b7baa4e&topid=%d&";
    public static String LRUDOWNLOADURL = "http://route.showapi.com/213-2?showapi_appid=25798&showapi_sign=6d9b08da66aa47adb911f1065b7baa4e&musicid=%d&";

    public static String QUERYBYSONGNAME = "http://route.showapi.com/213-1?showapi_appid=25798&showapi_sign=6d9b08da66aa47adb911f1065b7baa4e&keyword=%s&page=%d&";



    private static  OkHttpClient  okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.SECONDS)
            .build();

    public static OkHttpClient getClient() {
        return okHttpClient;
    }



}
