package org.sang.a8_1qqmusic.playMusic.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.sang.a8_1qqmusic.R;
import org.sang.a8_1qqmusic.playMusic.presenter.PlayerPresenter;
import org.sang.a8_1qqmusic.service.DownLoadService;
import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;
import org.sang.a8_1qqmusic.util.PlayerUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import qianfeng.lrulibs.LruView;
import qianfeng.lrulibs.bean.LrcBean;

public class PlayerActivity extends AppCompatActivity implements IPlayerView {

    private PlayerPresenter playerPresenter = new PlayerPresenter(this);

    private ImageView playOrPause;
    private TextView currentTimeTv;
    private TextView totalTimeTv;
    private SeekBar seekBar;
    private LruView lruView;


    private TextView toolbarMusicName;
    private TextView bottomMusicName;
    private TextView singerName;

    private SimpleDateFormat dataFormat = new SimpleDateFormat("mm:ss");


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 在这里干什么？更新啊
            // 要把这个保护起来，因为已经播放了，我才能确保PlayerUtil.player静态的对象不为空，这样才可以用到里面的getCurrentPosition()方法
            if (PlayerUtil.CURRENT_STATE == PlayerUtil.PLAY) // 记住这里，只有状态为PlAY，PlayerUtil.player才不为空，才可以调用里面的方法
            {
                currentTimeTv.setText(dataFormat.format(new Date(PlayerUtil.player.getCurrentPosition())));
                seekBar.setProgress(PlayerUtil.player.getCurrentPosition());
                // 更新完以上两个UI之后，才开始发送延迟消息
                sendEmptyMessageDelayed(0, 200);
            }

        }
    };
    private int seekBarMediaState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding =  DataBindingUtil.
        setContentView(R.layout.activity_player);

        // xml中文件获取数据源，就是从这个PlayerUtil.CurrentMusicBean中获取的
//        binding.setMusic(PlayerUtil.CurrentMusicBean);

        initView();

        playerPresenter.start();


    }

    private void initView() {
        LinearLayout left_menu = (LinearLayout) findViewById(R.id.left_menu);

        left_menu.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels;

        // 重新测量LinearLayout的布局大小，这个和在ListView中是一致的。
        // 但是ListView虽然是容器但是不具备容器类的getChildAt等方法，要通过它的适配器的方法获取子控件的数量等信息。
        left_menu.requestLayout();

        LinearLayout right_menu = (LinearLayout) findViewById(R.id.right_menu);

        right_menu.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels;

        // 重新测量LinearLayout的布局大小，这个和在ListView中是一致的。
        // 但是ListView虽然是容器但是不具备容器类的getChildAt等方法，要通过它的适配器的方法获取子控件的数量等信息。
        right_menu.requestLayout();

        playOrPause = ((ImageView) findViewById(R.id.play_or_pause));
        currentTimeTv = ((TextView) findViewById(R.id.current_time));
        totalTimeTv = (TextView) findViewById(R.id.total_time);
        seekBar = ((SeekBar) findViewById(R.id.seek_bar));


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // 这里可以用一个变量来记录MediaPlayer的最原始的状态，之后在拖动结束时，设置回它最原本的状态

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // progress:是用户拖动改变了的seekBar的值
                    PlayerUtil.player.seekTo(progress); // 这里可能要加非null要验证
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 这里可以用一个变量来记录下拖动之前的MediaPlayer的播放状态
//                seekBarMediaState = PlayerUtil.CURRENT_STATE; 不能直接这样处理！因为mHandler里面是需要if来验证的！if(CURRENT_STATE==PLAY)是true才可以用mHandler来更新UI控件
                PlayerUtil.player.pause();
                PlayerUtil.CURRENT_STATE = PlayerUtil.PAUSE;
                // 开始拖动的时候,更新UI
                playerPresenter.start(); // start(): 这个方法的唯一作用就是更新UI
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PlayerUtil.pause();
                playerPresenter.start();// 更新UI
                // 这里就设置回MediaPlayer最开始记录的那个状态
//                PlayerUtil.CURRENT_STATE = seekBarMediaState; // 还原状态
            }
        });

        lruView = ((LruView) findViewById(R.id.lruView));

        toolbarMusicName = ((TextView) findViewById(R.id.music_name));
        bottomMusicName = ((TextView) findViewById(R.id.music_name2));
        singerName = ((TextView) findViewById(R.id.singerName));

    }

    @Override
    public void updatePlayerControl() {
        if (PlayerUtil.CURRENT_STATE == PlayerUtil.PLAY) {
            playOrPause.setImageResource(R.drawable.search_stop_btn);
        } else {
            playOrPause.setImageResource(R.drawable.ring_btnplay);
        }
        MusicBean musicBean = PlayerUtil.CurrentMusicBean;
        toolbarMusicName.setText(musicBean.getSongname());
        bottomMusicName.setText(musicBean.getSongname());
        singerName.setText(musicBean.getSingername());
        // 在更新UI这里用id请求http，得到歌词！多么美妙
        playerPresenter.loadLrc(PlayerUtil.CurrentMusicBean.getSongid()); // 这个songid是歌曲和歌词本地存储的唯一标记

        mHandler.sendEmptyMessage(0);

        // PlayerUtil.CurrentMusicBean ： 第一次用户没点击List中的item时，就可以从本地直接加载的！！！通过sp的读取加载的方法！
        // 就是用户没有点击歌曲直接进入第二个Activity时，怎么处理Activity的UI更新问题
        totalTimeTv.setText(dataFormat.format(new Date(PlayerUtil.CurrentMusicBean.getSeconds() * 1000)));
        seekBar.setMax(PlayerUtil.CurrentMusicBean.getSeconds() * 1000);


    }

    @Override
    public void initLruView(List<LrcBean> list) {
        // 为了封装成一个更好的工具类，MediaPlayer和list都是从外面传进来的
        lruView.setList(list);
        lruView.setMediaPlayer(PlayerUtil.player);
        // 调用这个方法重绘，因为xml文件刚加载时，list是null的，然后就return掉了onDraw方法，就不能执行延迟100毫秒重绘的
        // 所以需要在这里数据下载完成之后，调用刷新视图，重绘一次的方法
        lruView.init();
    }


    public void playOrPause(View view) {
        if (PlayerUtil.CURRENT_STATE == PlayerUtil.STOP) {
            playOrPause.setImageResource(R.drawable.search_stop_btn);
            PlayerUtil.startService(this, PlayerUtil.CurrentMusicBean, PlayerUtil.PLAY);
        } else if (PlayerUtil.CURRENT_STATE == PlayerUtil.PAUSE) {
            playOrPause.setImageResource(R.drawable.search_stop_btn);
            PlayerUtil.startService(this, PlayerUtil.CurrentMusicBean, PlayerUtil.PAUSE);
        } else {
            playOrPause.setImageResource(R.drawable.ring_btnplay);
            PlayerUtil.startService(this, PlayerUtil.CurrentMusicBean, PlayerUtil.PAUSE);
        }
    }

    public void preMusic(View view) {
        if (PlayerUtil.CURRENTPOSITION > 0) {
            PlayerUtil.CURRENTPOSITION = PlayerUtil.CURRENTPOSITION - 1;
            PlayerUtil.CurrentMusicBean = PlayerUtil.CURRENT_PLAY_LIST.get(PlayerUtil.CURRENTPOSITION);
            PlayerUtil.startService(view.getContext(), PlayerUtil.CurrentMusicBean, PlayerUtil.PLAY);
            playerPresenter.start(); // start():    是用来更新UI的
        }
    }


    public void nextMusic(View view) {
        if (PlayerUtil.CURRENTPOSITION < PlayerUtil.CURRENT_PLAY_LIST.size() - 1) {
            PlayerUtil.CURRENTPOSITION = PlayerUtil.CURRENTPOSITION + 1;
            PlayerUtil.CurrentMusicBean = PlayerUtil.CURRENT_PLAY_LIST.get(PlayerUtil.CURRENTPOSITION);
            PlayerUtil.startService(view.getContext(), PlayerUtil.CurrentMusicBean, PlayerUtil.PLAY);
            playerPresenter.start();

        }
    }


    public void downloadMusic(View view) {  // 在这里启动服务下载歌曲

        Intent intent = new Intent(this, DownLoadService.class);
        // 你能点到这个download按钮，证明你都是点击过播放？
        // 而且还考虑过你第一次进来的时候，PlayerUtil的MediaPlayer还没有准备好，这时候，只能加载从sp中读取进来的currentMusicBean
        // 这个CurrentMusicBean是记录各种来源的MusicBean，1.本地播放的 2.网络播放的 3.曾经播放的然后退出应用后的最近一首歌
        intent.putExtra("songid", PlayerUtil.CurrentMusicBean.getSongid() + "");
        intent.putExtra("downloadurl", PlayerUtil.CurrentMusicBean.getDownUrl());

        startService(intent);
    }

    public void share(View view) {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(PlayerUtil.CurrentMusicBean.getSongname());
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(PlayerUtil.CurrentMusicBean.getUrl());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(PlayerUtil.CurrentMusicBean.getSongname());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(PlayerUtil.CurrentMusicBean.getAlbumpic_small());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(PlayerUtil.CurrentMusicBean.getUrl());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(PlayerUtil.CurrentMusicBean.getSongname());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(PlayerUtil.CurrentMusicBean.getSongname());
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(PlayerUtil.CurrentMusicBean.getUrl());

// 启动分享GUI
        oks.show(this);

    }
}
