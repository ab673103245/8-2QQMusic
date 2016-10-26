package org.sang.a8_1qqmusic.showMusic.model;

import android.content.Context;
import android.content.SharedPreferences;

import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;
import org.sang.a8_1qqmusic.util.MusicUtil;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class IShowDataImpl implements IShowData {


    @Override
    public void savaData(MusicBean musicBean, Context context) {
        SharedPreferences sp = context.getSharedPreferences(MusicUtil.MUSICBEAN, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("musicName",musicBean.getSongname());
        edit.putString("musicPath",musicBean.getUrl());
        // 一开始进入第一个Activity，然后立即点击第二个Activity时，出现的异常崩了的问题，是因为没有从SDCard中获取到以前的歌曲的信息，
        // 必须要手动点击网络歌单或者本地歌单，第二页的歌单才可以显示,因为出现这个问题，所以我们要把点击过的item记录到sp中，直接从本地上读取信息更新UI
        edit.putInt("Seconds",musicBean.getSeconds());
        edit.putInt("id",musicBean.getSongid());
        edit.commit();
    }

    @Override
    public MusicBean loadData(Context context) {

        // 从sp中加载读取一个MusicBean对象
        MusicBean musicBean = new MusicBean();

        SharedPreferences sp = context.getSharedPreferences(MusicUtil.MUSICBEAN, Context.MODE_PRIVATE);

        musicBean.setSongname(sp.getString("musicName", ""));
        musicBean.setUrl(sp.getString("musicPath",""));
        musicBean.setSongid(sp.getInt("id",0));
        musicBean.setSeconds(sp.getInt("Seconds",0));
        return musicBean;
    }

}
