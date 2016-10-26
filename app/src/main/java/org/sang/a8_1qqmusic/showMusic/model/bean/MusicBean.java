package org.sang.a8_1qqmusic.showMusic.model.bean;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import org.sang.a8_1qqmusic.R;

/**
 * Created by 王松 on 2016/10/17.
 */

public class MusicBean {
    private String songname;
    private int seconds;
    private String albummid;
    private int songid;
    private int singerid;
    private String albumpic_big;
    private String albumpic_small;
    private String downUrl;
    private String url;
    private String singername;
    private int albumid;
    public static ImageLoader loader;

//    public void onItemClick(View view)
//    {
//        PlayerUtil.startService(view.getContext(),this,PlayerUtil.PLAY);
//    }

//    public void preMusic(View view) {
//        if (PlayerUtil.CURRENTPOSITION > 0) {
//            PlayerUtil.CURRENTPOSITION = PlayerUtil.CURRENTPOSITION - 1;
//            PlayerUtil.CurrentMusicBean = PlayerUtil.CURRENT_PLAY_LIST.get(PlayerUtil.CURRENTPOSITION);
//            PlayerUtil.startService(view.getContext(), PlayerUtil.CurrentMusicBean, PlayerUtil.PLAY);
//            updateMusicInfo();
//        }
//    }
//
//    private void updateMusicInfo() {
//        String songname = PlayerUtil.CurrentMusicBean.getSongname();
//        Log.d("google-my:", "updateMusicInfo: " + songname);
//        setSongname(songname);
//        String singername = PlayerUtil.CurrentMusicBean.getSingername();
//        Log.d("google-my:", "updateMusicInfo: " + singername);
//        setSingername(singername);
//        String albumpic_small = PlayerUtil.CurrentMusicBean.getAlbumpic_small();
//        Log.d("google-my:", "updateMusicInfo: " + albumpic_small);
//        setAlbumpic_small(albumpic_small);
//    }
//
//    public void nextMusic(View view) {
//        if (PlayerUtil.CURRENTPOSITION < PlayerUtil.CURRENT_PLAY_LIST.size() - 1) {
//            PlayerUtil.CURRENTPOSITION = PlayerUtil.CURRENTPOSITION + 1;
//            PlayerUtil.CurrentMusicBean = PlayerUtil.CURRENT_PLAY_LIST.get(PlayerUtil.CURRENTPOSITION);
//            PlayerUtil.startService(view.getContext(), PlayerUtil.CurrentMusicBean, PlayerUtil.PLAY);
//            updateMusicInfo();
//        }
//    }


    @BindingAdapter("bind:albumpic_small")
    public static void getNetImage(ImageView iv, String url) {

        if (url == null || "".equals(url) || !url.startsWith("http")) {
            return;
        }

//        Picasso.with(iv.getContext()).load(url).resize(iv.getWidth(),iv.getHeight()).centerCrop().into(iv);
        // 换用Vollery加载图片
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv, R.drawable.default1, R.drawable.default1);
        loader.get(url, listener);

    }

    public MusicBean() {
    }

    public MusicBean(String songname, int seconds, String albummid, int songid, int singerid, String albumpic_big, String albumpic_small, String downUrl, String url, String singername, int albumid) {
        this.songname = songname;
        this.seconds = seconds;
        this.albummid = albummid;
        this.songid = songid;
        this.singerid = singerid;
        this.albumpic_big = albumpic_big;
        this.albumpic_small = albumpic_small;
        this.downUrl = downUrl;
        this.url = url;
        this.singername = singername;
        this.albumid = albumid;
    }

    //class MusicBean extends BaseObservable 这个是注解的重点啊！
//    @Bindable
    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
        //notifyPropertyChanged(BR.songname);
        // 每当get方法上的@Bindable上的值改变时，就会用这个方法去更新xml中的属性，先get改变，再调用set去改变xml文件中的属性。
//        notifyPropertyChanged(BR.songname);
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getAlbummid() {
        return albummid;
    }

    public void setAlbummid(String albummid) {
        this.albummid = albummid;
    }

    public int getSongid() {
        return songid;
    }

    public void setSongid(int songid) {
        this.songid = songid;
    }

    public int getSingerid() {
        return singerid;
    }

    public void setSingerid(int singerid) {
        this.singerid = singerid;
    }

    public String getAlbumpic_big() {
        return albumpic_big;
    }

    public void setAlbumpic_big(String albumpic_big) {
        this.albumpic_big = albumpic_big;
    }

    //    @Bindable
    public String getAlbumpic_small() {
        return albumpic_small;
    }

    public void setAlbumpic_small(String albumpic_small) {
        this.albumpic_small = albumpic_small;
//        notifyPropertyChanged(BR.albumpic_small);
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //    @Bindable
    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
//        notifyPropertyChanged(BR.singername);
    }

    public int getAlbumid() {
        return albumid;
    }

    public void setAlbumid(int albumid) {
        this.albumid = albumid;
    }
}
