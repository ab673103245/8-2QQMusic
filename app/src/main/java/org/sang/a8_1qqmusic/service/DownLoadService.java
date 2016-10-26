package org.sang.a8_1qqmusic.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.sang.a8_1qqmusic.R;
import org.sang.a8_1qqmusic.util.NetUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class DownLoadService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * Used to name the worker thread, important only for debugging.
     */

    private Handler mHandler = new Handler();
    private NotificationManager nfManager;
    private Notification.Builder builder;

    private RemoteViews remoteViews;

    // 使用IntentService一定要提供无参的构造方法
    public DownLoadService() {
        super("");
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        remoteViews = new RemoteViews(getPackageName(),R.layout.download_nf_layout);

        builder = new Notification.Builder(this);

        builder
//                .setContent(remoteViews)
//                .setSmallIcon(R.drawable.download_icon);
                .setSmallIcon(R.drawable.download_icon)
                .setContentTitle("正在下载...")
                .setContentInfo("已下载:");

        nfManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        nfManager.notify(2, builder.build());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // 在这里进行歌曲下载，以及RemoteViews的更新（这是能在子线程中直接更新的控件）
        // 老师推荐用Notification来做，当然你也可以使用自定义的RemoteViews，不过实际情况是有点卡啊

        final String songid = intent.getStringExtra("songid");
        Log.d("google-my:", "onHandleIntent: songid:" + songid);
        String downloadurl = intent.getStringExtra("downloadurl");
        Log.d("google-my:", "onHandleIntent: downloadurl" + downloadurl);

        final Request request = new Request.Builder().url(downloadurl).build();

        NetUtil.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DownLoadService.this, "亲爱的，这首歌是要收费的", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) // 这里有可能会失败啊
                {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { // 如果SD卡挂载了
                        File myMusic = new File(Environment.getExternalStorageDirectory(), "MMyMusic");//得到SD卡的外部存储目录
                        if (!myMusic.exists()) {
                            myMusic.mkdirs();
                        }
                        // 如果文件夹存在的话，那就下载啊！用fos
                        FileOutputStream fos = new FileOutputStream(new File(myMusic, songid + ".mp3"));

                        InputStream is = response.body().byteStream();

                        int currentLength = 0;
                        int totalLength = (int) response.body().contentLength();

                        int len = 0;
                        byte[] data = new byte[1024];
                        while ((len = is.read(data)) != -1) {
                            fos.write(data, 0, len);
                            fos.flush();
                            currentLength += len;

//                            remoteViews.setProgressBar(R.id.pb, totalLength, currentLength, false);
//                            remoteViews.setTextViewText(R.id.current_percent, (int) ((currentLength * 1.0f / totalLength) * 100) + "%");

                            builder.setProgress(totalLength, currentLength, false);
                            builder.setContentInfo("已下载:" + (int) ((currentLength * 1.0f / totalLength) * 100) + "%");
                            nfManager.notify(2, builder.build()); // 2这个id，是唯一标识 builder.build()这个对象的

                        }


                    }

                }
            }
        });


    }


}
