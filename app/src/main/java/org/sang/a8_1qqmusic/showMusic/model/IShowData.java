package org.sang.a8_1qqmusic.showMusic.model;

import android.content.Context;

import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public interface IShowData {
    void savaData(MusicBean musicBean,Context context);
    MusicBean loadData(Context context);
}
