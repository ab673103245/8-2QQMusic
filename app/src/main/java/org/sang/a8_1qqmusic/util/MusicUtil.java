package org.sang.a8_1qqmusic.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

/**
 * Created by 王松 on 2016/10/17.
 */

public class MusicUtil {

    public static final String MUSICBEAN = "musicbean";
    public static final String USERINFO_SP= "userinfo_sp";
        /**
         * 获取一个mp3文件的专辑图片
         * @param filePath
         * @return
         */
    public static Bitmap getThumbnail(String filePath) {

        if(filePath==null || "".equals(filePath) || filePath.startsWith("http"))
        {
            return null;
        }
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = null;
        try {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(filePath);
            byte[] bytes = retriever.getEmbeddedPicture();
            if (bytes != null && bytes.length > 0) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
//            bitmap = BitmapFactory.decodeFile(String.valueOf(retriever.getEmbeddedPicture()));

        } finally {
            if (retriever != null) {
                retriever.release();
            }
        }
        return bitmap;
    }
}
