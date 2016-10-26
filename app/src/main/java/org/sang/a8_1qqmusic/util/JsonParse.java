package org.sang.a8_1qqmusic.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;

import java.util.ArrayList;
import java.util.List;

import qianfeng.lrulibs.bean.LrcBean;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class JsonParse {

    public static List<MusicBean> parseJson2List(String json) {
        List<MusicBean> list = new ArrayList<>();
        try {
            JSONArray songList = new JSONObject(json).getJSONObject("showapi_res_body").getJSONObject("pagebean").getJSONArray("songlist");
            for (int i = 0; i < songList.length(); i++) {
                JSONObject data = songList.getJSONObject(i);
                String songname = data.getString("songname");
                int seconds = data.getInt("seconds");
                String albummid = data.getString("albummid");
                String albumpic_big = data.getString("albumpic_big");
                String albumpic_small = data.getString("albumpic_small");
                String downUrl = data.getString("downUrl");
                String url = data.getString("url");
                String singername = data.getString("singername");
                int songid = data.getInt("songid");
                int singerid = data.getInt("singerid");
                int albumid = data.getInt("albumid");
                list.add(new MusicBean(songname, seconds, albummid, songid, singerid, albumpic_big, albumpic_small, downUrl, url, singername, albumid));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<LrcBean> parseLru2List(String json) {
        List<LrcBean> list = new ArrayList<>();

        try {
            String lruText = new JSONObject(json).getJSONObject("showapi_res_body").getString("lyric")
                    // 其实处理来自HTML上的字符串的转义问题方法很简单，把所有转义字符全部用replaceAll来解决就可以，转义成java的ASCII码格式
                    // 你可以字符串打印一下，看出来的转义字符是哪些，然后根据网上百度的转义字符表，把他们都打印出来
                    .replaceAll("&#58;", ":")
                    .replaceAll("&#10;", "\n")
                    .replaceAll("&#46;", ".")
                    .replaceAll("&#32;", " ")
                    .replaceAll("&#45;", "-")
                    .replaceAll("&#39;","'")
                    .replaceAll("&#13;", "\r");

            Log.d("google-my:", "parseLru2List: " + lruText);

            String[] split = lruText.split("\n");

            for (int i = 0; i < split.length; i++) {
                if (split[i].contains(".")) {
                    String min = split[i].substring(split[i].indexOf("[") + 1, split[i].indexOf("[") + 3);
                    String seconds = split[i].substring(split[i].indexOf(":") + 1, split[i].indexOf(":") + 3);
                    String mills = split[i].substring(split[i].indexOf(".") + 1, split[i].indexOf(".") + 3);

                    String text = split[i].substring(split[i].indexOf("]") + 1);

                    if(text == null || "".equals(text))
                    {
                        text = "music";
                    }

                    long startTime = Long.valueOf(min) * 60 * 1000 + Long.valueOf(seconds) * 1000 + Long.valueOf(mills) * 10;

                    LrcBean lruBean = new LrcBean();
                    lruBean.setStart(startTime);
                    lruBean.setText(text);

                    list.add(lruBean);
                    if (list.size() > 1) {
                        list.get(list.size() - 2).setEnd(startTime);
                    }
                    if (i == split.length - 1)
                    {
                        list.get(list.size()-1).setEnd(startTime + 100000);
                    }
                }


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }
}
