package org.sang.a8_1qqmusic.showMusic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sang.a8_1qqmusic.BaseFragment;
import org.sang.a8_1qqmusic.R;

/**
 * Created by 王松 on 2016/10/17.
 */

public class RankFragment extends BaseFragment {

    // 只有静态创建的Fragment会这样！ 就是静态的Fragment销毁了，但是里面的View还没被销毁
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (view == null) {
            view = inflater.inflate(R.layout.rank_fg_layout, container, false);
        }

        FragmentManager manager = getChildFragmentManager();

        // 在这个父Fragment中，把两个静态创建的子Fragment建立起关联！！！！

        LeftFragment left_fg = (LeftFragment) manager.findFragmentById(R.id.left_fg);
        RightFragment right_fg = (RightFragment) manager.findFragmentById(R.id.right_fg);

        left_fg.setRightFragment(right_fg);

        return view;
    }
}
