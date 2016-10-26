package org.sang.a8_1qqmusic.playMusic.presenter;

import android.os.Handler;

import org.sang.a8_1qqmusic.BasePresenter;
import org.sang.a8_1qqmusic.playMusic.model.IPlayData;
import org.sang.a8_1qqmusic.playMusic.model.IPlayDataImpl;
import org.sang.a8_1qqmusic.playMusic.model.OnLruLoadListener;
import org.sang.a8_1qqmusic.playMusic.view.IPlayerView;

import java.util.List;

import qianfeng.lrulibs.bean.LrcBean;

/**
 * Created by Administrator on 2016/10/20 0020.
 */
public class PlayerPresenter implements BasePresenter {

    private IPlayerView iPlayerView;
    private IPlayData iPlayData;

    private Handler mHandler = new Handler();

    public PlayerPresenter(IPlayerView iPlayerView) {
        this.iPlayerView = iPlayerView;
        iPlayData = new IPlayDataImpl();
    }

    @Override
    public void start() {
        iPlayerView.updatePlayerControl();
    }

    public void loadLrc(final int id)
    {
        iPlayData.loadLru(id, new OnLruLoadListener() {
            @Override
            public void onLoadSuccess(final List<LrcBean> list) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iPlayerView.initLruView(list);
                        // 你只能在这里存储这个List啊！
                    }
                });
            }

            @Override
            public void onLoadFailed(String msg) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }
        });
    }


}
