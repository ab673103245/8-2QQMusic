package org.sang.a8_1qqmusic.playMusic.model;

import java.util.List;

import qianfeng.lrulibs.bean.LrcBean;

/**
 * Created by Administrator on 2016/10/20 0020.
 */
public interface OnLruLoadListener {
    void onLoadSuccess(List<LrcBean> list);
    void onLoadFailed(String msg);
}
