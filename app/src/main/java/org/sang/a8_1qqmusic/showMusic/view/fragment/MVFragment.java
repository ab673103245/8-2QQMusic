package org.sang.a8_1qqmusic.showMusic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.sang.a8_1qqmusic.BaseFragment;

/**
 * Created by 王松 on 2016/10/17.
 */

public class MVFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(getActivity());
        tv.setText("MV");
        return tv;
    }
}
