package org.sang.a8_1qqmusic.playMusic.view;

import java.util.List;

import qianfeng.lrulibs.bean.LrcBean;

/**
 * Created by Administrator on 2016/10/20 0020.
 */
public interface IPlayerView {
    void updatePlayerControl();

    void initLruView(List<LrcBean> list);

}
