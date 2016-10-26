package org.sang.a8_1qqmusic.showMusic.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.sang.a8_1qqmusic.BaseFragment;
import org.sang.a8_1qqmusic.LoginActivity;
import org.sang.a8_1qqmusic.R;
import org.sang.a8_1qqmusic.localMusic.view.LocalMusicActivity;
import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;
import org.sang.a8_1qqmusic.showMusic.model.bean.UserBean;
import org.sang.a8_1qqmusic.showMusic.presenter.MinePresenter;
import org.sang.a8_1qqmusic.showMusic.view.IShowView;
import org.sang.a8_1qqmusic.showMusic.view.adapter.MineFgLvAdapter;
import org.sang.a8_1qqmusic.showMusic.view.view.MyListView;
import org.sang.a8_1qqmusic.util.LoginUtil;
import org.sang.a8_1qqmusic.util.PlayerUtil;

import java.util.List;

import cn.sharesdk.tencent.qq.QQ;

/**
 * Created by 王松 on 2016/10/17.
 */

// 这是首页的Fragment

public class MineFragment extends BaseFragment implements IMineView {
    private MinePresenter minePresenter = new MinePresenter(this);
    private MyListView lv;

    private Handler mHandler = new Handler();

    private IShowView iShowView;
    private TextView playListSizeTv;
    private LinearLayout ll_localMusic;

    private UserBean userBean;
    private TextView usernameTv;
    private ImageView userfaceIv;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof IShowView )
        {
            this.iShowView = (IShowView) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fg_layout, container, false);
        initView(view);
        minePresenter.start();
        return view;
    }

    private void initView(View view) {

        lv = ((MyListView) view.findViewById(R.id.play_list_lv));
        usernameTv = ((TextView) view.findViewById(R.id.username));
        userfaceIv = ((ImageView) view.findViewById(R.id.userface));

        RelativeLayout loginLayout = (RelativeLayout) view.findViewById(R.id.loginLayout);

        playListSizeTv = ((TextView) view.findViewById(R.id.play_list_size_tv));
        ll_localMusic = ((LinearLayout) view.findViewById(R.id.ll_localMusic));



        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 肯定是在这里跳转Activity，并且从sp中读取数据进行对照，判断是否要更新UI
                if("请登录".equals(usernameTv.getText().toString()))
                {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else
                {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setMessage("退出登录")
                            .setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 无论何时，你new这个QQ，它都是单例的
                                    QQ qq = new QQ(getActivity());
                                    // 清除单例对象QQ里面的授权数据，即登录数据(即登录令牌token)
                                    qq.removeAccount();
                                    // 如果saveData的第一个参数db传的是null的话，sp中edit()方法写入的值就是""，就是重新更新sp里面的数据为空字符串
                                    LoginUtil.saveData(null,getActivity());

                                    // 然后就利用sp里面的数据更新UI，而不是通过userBean，哈哈，因为是有两个更新UI的重载方法，要注意看这个minePresener哈
                                   // 在数据传递的时候initLoginInfo中就记录好this。userBean
                                    minePresenter.initLoginInfo(getActivity());

                                    // 这是从成员变量this。userBean，这是此Fragment记录的
                                    updateUserInfo(MineFragment.this.userBean);



                                }
                            })
                            .setNegativeButton("取消",null)
                            .create()
                            ;

                    dialog.show();

                }
            }
        });

        ll_localMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LocalMusicActivity.class));
            }
        });

    }


    @Override
    public void setPlayList(final List<MusicBean> list) {
        playListSizeTv.setText(list.size()+"");
        lv.setAdapter(new MineFgLvAdapter(list, getActivity()));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                PlayerUtil.startService(getActivity(),list.get(position),PlayerUtil.PLAY); // 注意这是异步准备，会有延迟
                PlayerUtil.CURRENT_PLAY_LIST = list;
                PlayerUtil.CURRENTPOSITION = position;

                // 而这行代码是 立即执行的,立即更新UI
                // 所以第一次点击了ListView，没有更新UI，第二次点了才会更新UI，就是这样异步准备，延迟的问题
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        if(iShowView!=null)
//                        iShowView.updateMusic(list.get(position));
//                    }
//                },500); // 延迟执行。

            }
        });
    }

    @Override
    public void initLoginInfo(UserBean bean) {
        // 在sp存储好键值对的时候，就用一个成员变量来记录下this.userBean的值，此时Fragment上的控件尚未初始化好
        // 当Fragment上的控件初始化好的时候，就用已经记录好sp的这个成员变量userBean来更新UI，传进去的是一个userBean对象
        this.userBean = bean;
        if(usernameTv != null)
        {
            updateUserInfo(bean);
        }
    }

    // 这个是Fragment声明周期的onResume，在initLoginInfo()(onCreate())的时候，就把这个成员变量userBean的值给记录好，然后在onResume里调用
    @Override
    public void onResume() {
        super.onResume();
        updateUserInfo(this.userBean);
    }

    public void updateUserInfo(UserBean bean) {
        // 注意这个传进来的参数UserBean bean 是全局的bean噢！
        String username = bean.getUsername();
        if (username != null && !"".equals(username)) {
            usernameTv.setText(username);
            Picasso.with(getActivity()).load(bean.getUserface()).into(userfaceIv);
        } else {
            usernameTv.setText("请登录");
            userfaceIv.setImageResource(R.drawable.default1);
        }
    }

    public void updateUserInfo(Context context) {
        minePresenter.initLoginInfo(context);
    }


}
