package org.sang.a8_1qqmusic.showMusic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

import org.sang.a8_1qqmusic.App;
import org.sang.a8_1qqmusic.BR;
import org.sang.a8_1qqmusic.BaseFragment;
import org.sang.a8_1qqmusic.R;
import org.sang.a8_1qqmusic.showMusic.model.bean.MusicBean;
import org.sang.a8_1qqmusic.showMusic.presenter.RightPresenter;
import org.sang.a8_1qqmusic.showMusic.view.adapter.RvAdapter;
import org.sang.a8_1qqmusic.util.BitmapCache;
import org.sang.a8_1qqmusic.util.PlayerUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class RightFragment extends BaseFragment implements IRightView{

    private RecyclerView recyclerView;

    private RightPresenter rightPresenter = new RightPresenter(this);



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.right_fg_layout, container, false);
        initView(view);
        rightPresenter.start(3);
        return view;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }


    @Override
    public void initRv(final List<MusicBean> list) { // 初始化RecycleView

        MusicBean.loader = new ImageLoader(((App) getActivity().getApplication()).getRequestQueue(), new BitmapCache(getActivity()));

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        RvAdapter<MusicBean> adapter = new RvAdapter<MusicBean>(list,getActivity(), BR.music,R.layout.right_fg_rv_item);

        adapter.setOnItemClick(new RvAdapter.OnItemClick() {
            @Override
            public void itemClick(int position) {
                Toast.makeText(getActivity(), position+"", Toast.LENGTH_SHORT).show();
                PlayerUtil.startService(getActivity(),list.get(position),PlayerUtil.PLAY);
                PlayerUtil.CURRENT_PLAY_LIST = list;
                PlayerUtil.CURRENTPOSITION = position;
            }
        });

        recyclerView.setAdapter(adapter);

    }

    @Override
    public void showErrorMsg(String errorMsg) {

    }

    // 这里有问题，网络列表没有更新MainActivity的UI

    public void updateRv(int id) {
        rightPresenter.start(id);
    }
}
