package org.sang.a8_1qqmusic.localMusic.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.sang.a8_1qqmusic.R;
import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class LocalMusicAdapter extends BaseAdapter {

    private List<MusicBean> list;
    private Context context;
    private LayoutInflater inflater;

    public LocalMusicAdapter(List<MusicBean> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.localmusic_lv_item,parent,false);
            holder = new ViewHolder();
            holder.music_name = (TextView) convertView.findViewById(R.id.music_name);
            holder.artist = (TextView) convertView.findViewById(R.id.artist);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        MusicBean musicBean = list.get(position);
        holder.music_name.setText(musicBean.getSongname());


        holder.artist.setText(musicBean.getSingername());

        return convertView;
    }

    class ViewHolder
    {
        TextView music_name,artist;
    }
}
