package org.sang.a8_1qqmusic.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class SDCardUtil {

    public static boolean isLocal(String songid) {
        // String MEDIA_MOUNTED = "mounted"
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MMyMusic" + File.separator + songid + ".mp3";

            File file = new File(filePath);

            if (file.exists()) {
                return true;
            }
        }

        return false;
    }


    public static boolean isLruExist(String songid) { // 我这个方法只是想验证歌曲文件是否存在，在别的地方下载，利用三级缓存机制。
        // 歌曲和歌词都可以用id唯一标识

        // String MEDIA_MOUNTED = "mounted"
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MMyMusic" + File.separator + songid;

            File file = new File(filePath);

            if (file.exists()) {
                return true;
            }
        }

        return false;
    }


}
