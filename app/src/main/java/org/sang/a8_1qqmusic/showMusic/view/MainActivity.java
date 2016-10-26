package org.sang.a8_1qqmusic.showMusic.view;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.lenve.customshapeimageview.CustomShapeImageView;
import org.sang.a8_1qqmusic.BaseActivity;
import org.sang.a8_1qqmusic.R;
import org.sang.a8_1qqmusic.playMusic.view.PlayerActivity;
import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;
import org.sang.a8_1qqmusic.showMusic.presenter.ShowPresenter;
import org.sang.a8_1qqmusic.showMusic.view.adapter.VpAdapter;
import org.sang.a8_1qqmusic.showMusic.view.fragment.FindFragment;
import org.sang.a8_1qqmusic.showMusic.view.fragment.MineFragment;
import org.sang.a8_1qqmusic.showMusic.view.fragment.MusicFragment;
import org.sang.a8_1qqmusic.showMusic.view.receiver.MusicReceiver;
import org.sang.a8_1qqmusic.util.MusicUtil;
import org.sang.a8_1qqmusic.util.PlayerUtil;

import java.util.ArrayList;
import java.util.List;

/*
shareSDK: 第三方的库，不需要下载其他应用的SDK就可以
进入

QQ开放者平台：注册称为开发者，下载SDK看官方文档的分
享代码，绑定应用，有些是个人无法申请，要企业才要申请
。
shareSDK: 第三方的库，不需要下载其他应用的SDK就可以
进入


QQ开放者平台：注册称为开发者，下载SDK看官方文档的分
享代码，绑定应用，有些是个人无法申请，要企业才要申请
。

wiki.mob.com

shareSDK:看mob官网上的文档。

 */

public class MainActivity extends BaseActivity implements IShowView {

    private CustomShapeImageView music_thumbnail;
    private TextView music_name;
    private ImageView pause_or_play;

    private ShowPresenter showPresenter = new ShowPresenter(this);
    private MusicReceiver musicReceiver;
    private MineFragment mineFragment;
    private ViewPager viewpager;
    private RelativeLayout toobar;



    //    private ShowPresenter showPresenter = new ShowPresenter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addAction(PlayerUtil.STOPSERVICE_ACTION);
        filter.addAction(PlayerUtil.UPDATE_BOTTOM_MUSIC_MSG_ACTION);
        musicReceiver = new MusicReceiver();
        registerReceiver(musicReceiver,filter);

        initView();
        showPresenter.loadData(PlayerUtil.LOCALMUSICBEAN);
    }

    private void initView() {

        toobar = (RelativeLayout) findViewById(R.id.toobar);


        viewpager = (ViewPager) findViewById(R.id.viewpager);



        // 这是ViewPager中的Fragment的List集合
        List<Fragment> list = new ArrayList<>();

        mineFragment = new MineFragment();
        list.add(mineFragment);
        list.add(new MusicFragment());
        list.add(new FindFragment());
        viewpager.setAdapter(new VpAdapter(getSupportFragmentManager(), list));

        music_thumbnail = (CustomShapeImageView) findViewById(R.id.music_thumbnail);

        music_name = (TextView) findViewById(R.id.music_name);

        pause_or_play = (ImageView) findViewById(R.id.pause_or_play);

    }


    // onResume() : 是从另一个Activity中返回，就会执行这个方法，而不会再执行onCreate()方法，
    // 所以在onCreate()方法中的更新UI的方法showPresenter.loadData(PlayerUtil.LOCALMUSICBEAN);需要在onResume中再执行多一次。
    // 不过这次更新的依据不是来源于sp，而是来源于静态变量
    @Override
    protected void onResume() {
        super.onResume();
        // 有三种方式解决另一个Activity销毁时，回到这个Activity更新数据的问题
        // 1.startActivtyForResult
        // 2.onResume() [onRestart() --> onStart() --> onResume() ]

//        updateMusic(PlayerUtil.CurrentMusicBean);

        showPresenter.loadData(PlayerUtil.PLAYUTILMUSICBEAN);

        // 从sp中读取键值对更新mineFragment的值
        mineFragment.updateUserInfo(this);

    }

    @Override
    public void updateMusic(MusicBean musicBean) {

//        music_thumbnail.setImageResource(R.drawable.a8);

        // 从网络上获取的MusicBean也是由这个static型的变量CurrentMusicBean记录的，这个变量是记录这个Player当前正在播放的歌曲或退出之后曾经播过的最近一首歌（sp中获取）
        PlayerUtil.CurrentMusicBean = musicBean;

        music_name.setText(musicBean.getSongname());

        Bitmap bitmap = MusicUtil.getThumbnail(musicBean.getUrl());

        if(bitmap != null)
        {
            music_thumbnail.setImageBitmap(bitmap);
        }else
        {
            music_thumbnail.setImageResource(R.drawable.default1);
        }

        updateBottomPauseBtn();

    }

    @Override
    public void updateBottomPauseBtn() {
        if(PlayerUtil.CURRENT_STATE == PlayerUtil.PLAY)
        {
            pause_or_play.setImageResource(R.drawable.search_stop_btn);
        }else
        {
            pause_or_play.setImageResource(R.drawable.ring_btnplay);
        }
    }

    public void pauseOrPlay(View view) { // 根据CURRENT_STATE决定怎么更新UI，而startService却不需要用到这些

        if(PlayerUtil.CURRENT_STATE == PlayerUtil.STOP)
        {
            pause_or_play.setImageResource(R.drawable.search_stop_btn);
            PlayerUtil.startService(this,PlayerUtil.CurrentMusicBean,PlayerUtil.PLAY);

        }else if(PlayerUtil.CURRENT_STATE == PlayerUtil.PAUSE)
        {
            pause_or_play.setImageResource(R.drawable.search_stop_btn);
            PlayerUtil.startService(this,PlayerUtil.CurrentMusicBean,PlayerUtil.PAUSE);
        }else
        {
            pause_or_play.setImageResource(R.drawable.ring_btnplay);
            PlayerUtil.startService(this,PlayerUtil.CurrentMusicBean,PlayerUtil.PAUSE);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(musicReceiver);
        showPresenter.saveData(PlayerUtil.CurrentMusicBean);
    }

    public void goPlayerActivity(View view) {
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
        // 应该在这里更新UI,传递过去
    }

    public void searchSong(View view) {

        // 点击之后马上进入搜索页面




//        Activity_STATE_Fragment = 1;
//
//        toobar.setVisibility(View.GONE);
//
//        viewpager.setVisibility(View.GONE);
//
//        ll.setVisibility(View.VISIBLE);
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        transaction.replace(R.id.ll,new SearchFragment()).commit();

        /*

//                getSupportFragmentManager().beginTransaction().replace(R.id.ll,fragment).commit();

                //add   //切换Fragment方式一：replace方法每次会将当前的Fragment销毁掉，然后创建新的Fragment实例添加进来
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.ll, fragment);
//                transaction.commit();
                //切换Fragment方式二：add、show、hide
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if(fragment.isAdded()) {
                    transaction.hide(currentFragment).show(fragment).commit(); // 记得commit啊，否则就不生效了啊！
                }else{
                    transaction.hide(currentFragment).add(R.id.ll,fragment).commit();
                }
                currentFragment = fragment;

            }
        });

        currentFragment = new ChatFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.ll, currentFragment).commit();
         */

    }


}
