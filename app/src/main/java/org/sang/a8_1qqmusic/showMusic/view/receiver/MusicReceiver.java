package org.sang.a8_1qqmusic.showMusic.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.sang.a8_1qqmusic.showMusic.view.IShowView;
import org.sang.a8_1qqmusic.util.PlayerUtil;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class MusicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {   // intent的getAction就是广播的频道啊！
        if(intent.getAction().equals(PlayerUtil.STOPSERVICE_ACTION))
        {
            ((IShowView) context).updateBottomPauseBtn();
        }else if(intent.getAction().equals(PlayerUtil.UPDATE_BOTTOM_MUSIC_MSG_ACTION))
        {
            ((IShowView) context).updateMusic(PlayerUtil.CurrentMusicBean);
        }

    }
}
