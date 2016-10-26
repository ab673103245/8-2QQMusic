package org.sang.a8_1qqmusic.localMusic.presenter;

import android.content.Context;
import android.util.Log;

import org.sang.a8_1qqmusic.BasePresenter;
import org.sang.a8_1qqmusic.localMusic.model.ILocalData;
import org.sang.a8_1qqmusic.localMusic.model.ILocalDataImpl;
import org.sang.a8_1qqmusic.localMusic.view.fragment.ILocalMusicView;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class LocalMusicPresenter implements BasePresenter{


    private ILocalData iLocalData; // 在 presenter这里得到 model通过接口传过来的数据

    private ILocalMusicView iLocalMusicView; // presenter是连接 model和view的，所以要传这两个变量过来，即使是fragment，也要传进来,这个fragment同时也是context

    // 只有一个参数需要传过来，那就是iLocalMusicView，数据源那一块是要自己new出来的
    public LocalMusicPresenter(ILocalMusicView iLocalMusicView) {
        this.iLocalMusicView = iLocalMusicView;
        iLocalData = new ILocalDataImpl();
    }

    public void start(Context context) // 这个context是给containProvider用的
    {
//        iLocalData.getLocalMusic(context); // 得到containProvider数据库里面的数据
        // 这个方法将view和model串起来
        // 用这个ListView来适配数据
        Log.d("google-my:", "start: " + iLocalData.getLocalMusic(context));
        iLocalMusicView.initLv(iLocalData.getLocalMusic(context));
    }

    @Override
    public void start() {

    }
}
