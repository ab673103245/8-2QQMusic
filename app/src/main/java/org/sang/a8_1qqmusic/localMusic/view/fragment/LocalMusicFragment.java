package org.sang.a8_1qqmusic.localMusic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.sang.a8_1qqmusic.BaseFragment;
import org.sang.a8_1qqmusic.R;
import org.sang.a8_1qqmusic.localMusic.presenter.LocalMusicPresenter;
import org.sang.a8_1qqmusic.localMusic.view.adapter.LocalMusicAdapter;
import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;
import org.sang.a8_1qqmusic.util.PlayerUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class LocalMusicFragment extends BaseFragment implements ILocalMusicView {

    private LocalMusicPresenter localMusicPresenter = new LocalMusicPresenter(this);

    private ListView lv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.localmusic_fg_layout, container, false);
        initView(view);

        localMusicPresenter.start(getActivity());

        return view;
    }

    private void initView(View view) {
        lv = ((ListView) view.findViewById(R.id.lv));
    }


    @Override
    public void initLv(final List<MusicBean> list) {

        // 在这里适配数据？ adapter是view部分的
        lv.setAdapter(new LocalMusicAdapter(list,getActivity()));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // startService
                PlayerUtil.startService(getActivity(),list.get(position),PlayerUtil.PLAY);
            }
        });
    }
}
