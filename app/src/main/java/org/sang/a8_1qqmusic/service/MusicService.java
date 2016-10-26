package org.sang.a8_1qqmusic.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import org.sang.a8_1qqmusic.R;
import org.sang.a8_1qqmusic.util.PlayerUtil;

import java.io.File;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class MusicService extends Service {


    private static final int STOPSERVICE = 3;
    private RemoteViews remoteViews;
    private Notification.Builder builder;
    private NotificationManager notificationManager;


    // 启动式service
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {  // onCreate方法和onStart方法，真特么的不一样！！！绘制视图的所有方法都是在onCreate()中完成，只要涉及到绘制视图，那就要在onCreate()中完成。
        super.onCreate();     // onStart()方法中就不能再执行绘制视图的操作了！不然就会绘制不出来咯
        builder = new Notification.Builder(this);

        remoteViews = new RemoteViews(getPackageName(), R.layout.nf_layout);

        Intent intent2 = new Intent(this,MusicService.class);
        intent2.putExtra("type",STOPSERVICE);

        PendingIntent pendingIntent = PendingIntent.getService(this,0,intent2,PendingIntent.FLAG_ONE_SHOT);
        remoteViews.setOnClickPendingIntent(R.id.nf_close_btn,pendingIntent);

        builder.setContent(remoteViews).setSmallIcon(R.drawable.default1);

        startForeground(1, builder.build());

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int type = intent.getIntExtra("type", PlayerUtil.STOP);
        //intent:是从别的界面跳转进来，而传进的参数
        String musicName = intent.getStringExtra("musicName");

        remoteViews.setTextViewText(R.id.music_name,musicName);

        // 在这里缓存网络图片到通知栏上

        String albumpic_small = PlayerUtil.CurrentMusicBean.getAlbumpic_small();
        if(albumpic_small != null && !"".equals(albumpic_small))
        {
            // 进行缓存，首先截取文件名, 因为这个文件名在用vollery下载的时候就已经进行过截取了！已经缓存了，现在是从缓存的位置把图片读出来(Bitmap)
            String filename = albumpic_small.substring(albumpic_small.lastIndexOf("/") + 1);
            Bitmap bitmap = BitmapFactory.decodeFile(new File(this.getExternalCacheDir(), filename).getAbsolutePath());
            if(bitmap != null)
            {
                remoteViews.setImageViewBitmap(R.id.music_thumbnail,bitmap);
            }
        }


        // 通知管理器，不用new那么多个Notification对象，用一个不断地用nitifiy更新就可以
        notificationManager.notify(1,builder.build());

        switch (type)
        {
            case PlayerUtil.PLAY:
                PlayerUtil.play(this,intent.getStringExtra("musicPath"));
                break;

            case PlayerUtil.PAUSE:
                PlayerUtil.pause();// 会执行工具类PlayerUtil里面的pause()方法，里面有判断播放和暂停的if语句
                break;

            case PlayerUtil.STOP:
                PlayerUtil.stop();
                break;
            case STOPSERVICE:
                stopSelf(); // 通过stopSelf调用本onDestroy()方法
                sendBroadcast(new Intent(PlayerUtil.STOPSERVICE_ACTION));
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PlayerUtil.stop();
    }

}
