package org.sang.a8_1qqmusic.showMusic.view.fragment;

import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;
import org.sang.a8_1qqmusic.showMusic.model.bean.UserBean;

import java.util.List;

/**
 * Created by 王松 on 2016/10/17.
 */

public interface IMineView {

    void setPlayList(List<MusicBean> list);

    void initLoginInfo(UserBean bean);

}
