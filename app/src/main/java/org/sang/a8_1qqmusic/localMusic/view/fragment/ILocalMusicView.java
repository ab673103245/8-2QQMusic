package org.sang.a8_1qqmusic.localMusic.view.fragment;

import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public interface ILocalMusicView {

    // 这个接口是用来干嘛的？是用来初始化lv的数据的!
    void initLv(List<MusicBean> list);

}
