package org.sang.a8_1qqmusic.showMusic.presenter;

import android.os.Handler;

import org.sang.a8_1qqmusic.BasePresenter;
import org.sang.a8_1qqmusic.showMusic.model.IRightData;
import org.sang.a8_1qqmusic.showMusic.model.IRightDataImpl;
import org.sang.a8_1qqmusic.showMusic.model.OnDataLoadListener;
import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;
import org.sang.a8_1qqmusic.showMusic.view.fragment.IRightView;

import java.util.List;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class RightPresenter implements BasePresenter {

    private IRightView iRightView;
    private IRightData iRightData;
    private Handler mHandler = new Handler();

    public RightPresenter(IRightView iRightView) {
        this.iRightView = iRightView;
        iRightData = new IRightDataImpl();
    }

    public void start(int id)
    {
        iRightData.getPlayList(id, new OnDataLoadListener() {
            @Override
            public void onLoadSuccess(final List<MusicBean> list) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iRightView.initRv(list);
                    }
                });
            }

            @Override
            public void onLoadFailed(final String errorMsg) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iRightView.showErrorMsg(errorMsg);
                    }
                });
            }
        });
    }

    @Override
    public void start() {

    }
}
