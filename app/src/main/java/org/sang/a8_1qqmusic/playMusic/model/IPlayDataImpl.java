package org.sang.a8_1qqmusic.playMusic.model;

import android.os.Environment;
import android.util.Log;

import org.sang.a8_1qqmusic.util.JsonParse;
import org.sang.a8_1qqmusic.util.NetUtil;
import org.sang.a8_1qqmusic.util.SDCardUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import qianfeng.lrulibs.bean.LrcBean;

/**
 * Created by Administrator on 2016/10/20 0020.
 */
public class IPlayDataImpl implements IPlayData {
    @Override
    public void loadLru(final int id, final OnLruLoadListener onLruLoadListener) {
        // 把歌词JSOn全部拿下来，保存在一个以歌曲名命名的文件夹里。
        // 先看Sd卡有没有歌词文件，如果没有再进行网络数据请求。还是有成功找到和没有找到，如果遍历本地SD卡失败的话，马上请求网络数据。

        Log.d("google-my:", "loadLru: lruload:有进来吗？");
        if (!SDCardUtil.isLruExist(id + "")) { // 如果歌词不存在SDCard中，就发起网络请求

            Log.d("google-my:", "loadLru: 什么情况");
            LruNetworkLoading(id, onLruLoadListener);

        } else  // 否则，本地加载
        {
//            加载路径，读取到一个字符串，再调用接口的方法onSuccessful,返回一个list集合
//             读取一个字符串到内存中
//             在内存进行解析返回list集合
//            ByteArrayInputStream bis = new ByteArrayInputStream();
            Log.d("google-my:", "loadLru: 这又是什么情况");
            BufferedReader br = null;
            try {
                // linux下如果文本文件的后缀名你不知道要什么，那就直接不写吧，然后用BufferedReader来读取，一样可以将二进制字符转化为文本字符。
                br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(Environment.getExternalStorageDirectory().
                        getAbsolutePath() + File.separator + "MMyMusic"+ File.separator + id ))));
                int len2 = 0;
                byte[] b = new byte[1024];
                String str = null;
                StringBuffer buffer = new StringBuffer();

                while((str = br.readLine())!=null)
                {
                    buffer.append(str);
                }

                Log.d("google-my:", "onResponse: buffer::" + buffer.toString());
                List<LrcBean> list = JsonParse.parseLru2List(buffer.toString());
                onLruLoadListener.onLoadSuccess(list);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    private void LruNetworkLoading(final int id, final OnLruLoadListener onLruLoadListener) {
        Request request = new Request.Builder().url(String.format(NetUtil.LRUDOWNLOADURL, id)).build();
        NetUtil.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLruLoadListener.onLoadFailed(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 在这里先往本地缓存一份歌词文件，歌词文件可以放在外部存储SDCard上


                    Log.d("google-my:", "onResponse: OKHTTP onSuccessful??成功了进入了onSuccessful方法了" + id);
                    if (!SDCardUtil.isLruExist(id + "")) {
                        FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().
                          getAbsolutePath() + File.separator + "MMyMusic" + File.separator + id ));

                        InputStream is = response.body().byteStream();
                        int len = 0;
                        byte[] data = new byte[1024];
                        while ((len = is.read(data)) != -1) {
                            fos.write(data, 0, len);
                            fos.flush();
                        }
                        fos.close();

                        Log.d("google-my:", "onResponse: 会执行完成吗？ 会有文件夹吗？");

                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(Environment.getExternalStorageDirectory().
                                getAbsolutePath() + File.separator + "MMyMusic" + File.separator + id ))));

                        int len2 = 0;
                        byte[] b = new byte[1024];
                        String str = null;
                        StringBuffer buffer = new StringBuffer();

                        while((str = br.readLine())!=null)
                        {
                            buffer.append(str);
                        }

                        Log.d("google-my:", "onResponse: buffer::" + buffer.toString());
                        List<LrcBean> list = JsonParse.parseLru2List(buffer.toString());
                        onLruLoadListener.onLoadSuccess(list);

                    }

                    Log.d("google-my:", "onResponse: 为什么这个onResponse没有打印");

                    // 已经拿了okhttp里面的东西了？

//                  这是没有做歌词缓存时的源码，只有五行
//                    List<LrcBean> list = JsonParse.parseLru2List(response.body().string());
//                    for (LrcBean lruBean : list) {
//                        Log.d("google-my:", "onResponse: " + lruBean);
//                    }
//                    onLruLoadListener.onLoadSuccess(list);
                }
            }
        });
    }
}
