package org.sang.a8_1qqmusic.localMusic.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class ILocalDataImpl implements ILocalData {

    // 在这里使用ContentProvider拿到数据

    @Override
    public List<MusicBean> getLocalMusic(Context context) {

        ContentResolver contentResolver = context.getContentResolver();

        List<MusicBean> list = new ArrayList<>();

        ContentResolver resolver = contentResolver;
        // 音乐数据全部在mediaStore
        //data/data/应用包名/ ..(view)
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        while(cursor.moveToNext())
        {
            // DATA:是存储音频文件的路径的字符串
            //  public static final String DATA = "_data";
            // 最好能开个模拟器(获取root权限)，自己看看数据库里面的音频的数据库的字段。
            String musicPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

            //  public static final String DISPLAY_NAME = "_display_name";
            String musicName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));

            //public static final String ARTIST = "artist";
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

            //public static final String DURATION = "duration";
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

            MusicBean musicBean = new MusicBean();
            musicBean.setSongname(musicName);
            musicBean.setUrl(musicPath);
            musicBean.setSingername(artist);
            musicBean.setSeconds((int) (duration/1000));
            list.add(musicBean);


        }

        cursor.close();

        Log.d("google-my:", "getLocalMusic: list[][]:" + list);
        return list;
    }
}
