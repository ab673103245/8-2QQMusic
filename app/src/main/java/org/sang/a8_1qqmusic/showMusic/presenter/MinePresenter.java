package org.sang.a8_1qqmusic.showMusic.presenter;

import android.content.Context;
import android.os.Handler;

import org.sang.a8_1qqmusic.BasePresenter;
import org.sang.a8_1qqmusic.showMusic.model.IMineData;
import org.sang.a8_1qqmusic.showMusic.model.IMineDataImpl;
import org.sang.a8_1qqmusic.showMusic.model.OnDataLoadListener;
import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;
import org.sang.a8_1qqmusic.showMusic.view.fragment.IMineView;

import java.util.List;

/**
 * 该Presenter对应MineFragment
 */

public class MinePresenter implements BasePresenter {
    private IMineData iMineData;
    private IMineView iMineView;
    private Handler mHandler = new Handler();

    public MinePresenter(IMineView iMineView) {
        this.iMineView = iMineView;
        iMineData = new IMineDataImpl();
    }

    @Override
    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                iMineData.getPlayList(new OnDataLoadListener() {
                    @Override
                    public void onLoadSuccess(final List<MusicBean> list) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                iMineView.setPlayList(list);
                            }
                        });
                    }

                    @Override
                    public void onLoadFailed(String errorMsg) {

                    }
                });
            }
        }).start();
    }

    public void initLoginInfo(Context context) {
        iMineView.initLoginInfo(iMineData.loadUserInfo(context));
    }
}
