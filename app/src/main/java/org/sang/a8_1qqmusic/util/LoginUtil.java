package org.sang.a8_1qqmusic.util;

import android.content.Context;
import android.content.SharedPreferences;

import cn.sharesdk.framework.PlatformDb;

/**
 * Created by Administrator on 2016/10/24 0024.
 */
public class LoginUtil {
    public static void saveData(PlatformDb db,Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(MusicUtil.USERINFO_SP,Context.MODE_PRIVATE);

        // db:是平台授权的时候用的
        if(db!=null)
        {
            String userGender = db.getUserGender();
            String userIcon = db.getUserIcon();
            String userId = db.getUserId();
            String userName = db.getUserName();
            sp.edit().putString("username",userName).putString("userface",userIcon).commit();
        }else
        {
            // 如果平台授权被清理了
            sp.edit().putString("username","").putString("userface","").commit();
        }

    }
}
