package org.sang.a8_1qqmusic.localMusic.model;

import android.content.Context;

import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public interface ILocalData {
    List<MusicBean> getLocalMusic(Context context);
}
