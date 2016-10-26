package org.sang.a8_1qqmusic.showMusic.model;

import android.content.Context;

import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;
import org.sang.a8_1qqmusic.showMusic.model.bean.UserBean;

import java.util.List;

/**
 * Created by 王松 on 2016/10/17.
 */

public interface IMineData {

    void getPlayList(OnDataLoadListener onDataLoadListener);

    List<MusicBean> getLocalMusic();

    // 需要context这个是因为需要从sp中加载数据
    UserBean loadUserInfo(Context context);
}

