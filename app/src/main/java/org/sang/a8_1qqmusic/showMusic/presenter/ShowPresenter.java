package org.sang.a8_1qqmusic.showMusic.presenter;

import android.content.Context;

import org.sang.a8_1qqmusic.BasePresenter;
import org.sang.a8_1qqmusic.showMusic.model.IShowData;
import org.sang.a8_1qqmusic.showMusic.model.IShowDataImpl;
import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;
import org.sang.a8_1qqmusic.showMusic.view.IShowView;
import org.sang.a8_1qqmusic.util.PlayerUtil;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class ShowPresenter implements BasePresenter{

    private IShowView iShowView;
    private IShowData iShowData;

    public ShowPresenter(IShowView iShowView) {
        this.iShowView = iShowView;
        iShowData = new IShowDataImpl();
    }

    public void saveData(MusicBean musicBean)
    {
        iShowData.savaData(musicBean,(Context) iShowView);
    }

    public void loadData(int type){
        if (type == PlayerUtil.LOCALMUSICBEAN) {
            // 这是从sp中获取MusicBean
            iShowView.updateMusic(iShowData.loadData((Context) iShowView));
        }else if(type == PlayerUtil.PLAYUTILMUSICBEAN)
        {
            // 这是从PlayUtil中获取static型的MusicBean
            iShowView.updateMusic(PlayerUtil.CurrentMusicBean);
        }
    }

    @Override
    public void start() {

    }
}
