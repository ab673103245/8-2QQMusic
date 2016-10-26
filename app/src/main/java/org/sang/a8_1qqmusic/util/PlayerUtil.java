package org.sang.a8_1qqmusic.util;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;

import org.sang.a8_1qqmusic.service.MusicService;
import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class PlayerUtil {
    public static MediaPlayer player = null;
    public final static int PLAY = 0;
    public final static int PAUSE = 1;
    public final static int STOP = 2;

    public final static int LOCALMUSICBEAN = 3;
    public final static int PLAYUTILMUSICBEAN = 4;
    //记录当前的播放状态
    public static int CURRENT_STATE = 2;


    public static MusicBean CurrentMusicBean; // 正在播放的Music对象，无论是网络歌曲还是本地歌曲还是退出应用前曾经播放过的歌曲，都在这个CurrentMusicBean这里记录着

    public static int CURRENTPOSITION = -1;
    public static List<MusicBean> CURRENT_PLAY_LIST;

    public final static String STOPSERVICE_ACTION = "stopservice_action";
    public final static String UPDATE_BOTTOM_MUSIC_MSG_ACTION = "update_bottom_music_msg_action";



    public static void play(Context context,String musicPath)
    {

        // 所有播放歌曲的入口都是startService(x,x,int type)，然后这个type如果是PLAY的话，那就是一定会进入到这里来
        // 那播放本地歌曲的来源都是这个musicPath，如果网络歌曲下载好的话，我也要在把网络歌曲的url改成本地路径啊，就可以直接播放啦！网络歌曲的id是唯一标识的，我可以用它来作为文件名，起到唯一区分的作用。
        if(player == null)
        {
            init(context);
        }
        // 这一行代码确保了能在ListView的item上点击多个歌曲时，能用自由的播放歌曲，而不会崩溃，
        // 因为这个MediaPlayer实例里面的setDataSource已经被赋值了，再赋一次就会报错，所以要先清空，再赋值。
        player.reset();




        try {

            // 一点击播放就肯定有这个CurrentMusicBean对象，没点击还是有这个对象，因为是从sp中加载的
            if( !SDCardUtil.isLocal(PlayerUtil.CurrentMusicBean.getSongid()+""))
            {
                // MusicPath:这里是歌曲的url，是网络播放的,如果SD卡中找不到，就从网络上播放
                player.setDataSource(musicPath);
            }else  // 如果歌曲已经被下载下来，就在本地播放，修改这个MediaPlayer的setDataSource()准备路径
            {
                player.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyMusic" + File.separator + CurrentMusicBean.getSongid() + ".mp3");
            }

            // 异步准备
            player.prepareAsync(); // 异步准备，准备好之后就会回调 player.setOnPreparedListener() 这个方法

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static void pause()
    {
        if(player != null && player.isPlaying())
        {
            player.pause();
            CURRENT_STATE  = PAUSE;
        }else if(player != null && !player.isPlaying())
        {
            player.start();
            CURRENT_STATE = PLAY;
        }
    }

    public static void stop()
    {
        if(player != null)
        {
            player.stop();
            player.release();
            player = null;
            CURRENT_STATE = STOP;
        }
    }

    private static void init(final Context context) {
        player = new MediaPlayer();

        // 异步准备好之后，才会回调这个方法
        // player.prepareAsync(); 这个是异步准备，以前我们用的 player.prepare() 是同步准备,所以不需要设置监听。
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 准备工作完成好后，就准备播放
                mp.start();
                CURRENT_STATE = PLAY;
                context.sendBroadcast(new Intent(PlayerUtil.UPDATE_BOTTOM_MUSIC_MSG_ACTION));

            }
        });
    }

    public static void startService(Context context, MusicBean musicBean, int type)
    {

        CurrentMusicBean = musicBean;

        Intent intent = new Intent(context, MusicService.class);

        intent.putExtra("type",type);
        intent.putExtra("musicPath",musicBean.getUrl());
        intent.putExtra("musicName",musicBean.getSongname());

        // 在这个PlayerUtil工具类中，启动Service
        // 在这里启动service
        context.startService(intent);

    }


}
