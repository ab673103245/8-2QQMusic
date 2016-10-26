package org.sang.a8_1qqmusic.showMusic.model;

import org.sang.a8_1qqmusic.util.JsonParse;
import org.sang.a8_1qqmusic.util.NetUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class IRightDataImpl implements IRightData {
    @Override
    public void getPlayList(int id, final OnDataLoadListener onDataLoadListener) {
        Request request = new Request.Builder().url(String.format(NetUtil.PLAYLISTURL,id)).build();
        NetUtil.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onDataLoadListener.onLoadFailed(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onDataLoadListener.onLoadSuccess(JsonParse.parseJson2List(response.body().string()));
                }
            }
        });
    }
}
