package org.sang.a8_1qqmusic.showMusic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.sang.a8_1qqmusic.BaseFragment;
import org.sang.a8_1qqmusic.R;
import org.sang.a8_1qqmusic.showMusic.model.bean.Category;
import org.sang.a8_1qqmusic.showMusic.presenter.LeftPresenter;

import java.util.List;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class LeftFragment extends BaseFragment implements ILeftView {

    private LinearLayout container;
    private LeftPresenter leftPresenter = new LeftPresenter(this);
    private RightFragment rightFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rank_left_fg, container, false);
        initView(view);
        leftPresenter.start();
        return view;
    }

    private void initView(View view) {
        container = ((LinearLayout) view.findViewById(R.id.container));
    }


    @Override
    public void initCategory(final List<Category> list) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        for (int i = 0; i < list.size(); i++) {
            View view = inflater.inflate(R.layout.rank_left_fg_item, container, false);

            final TextView cate_name = (TextView) view.findViewById(R.id.cate_name);
            final View cate_line = view.findViewById(R.id.cate_line);

            cate_name.setText(list.get(i).getCategory());
            final int finalI = i;
            cate_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateColor(finalI);
                    Log.d("google-my:", "onClick: id:::[][][] --> " + list.get(finalI).getId());
                    rightFragment.updateRv(list.get(finalI).getId());
                }

            });

            container.addView(view);
        }

        View view = container.getChildAt(0);

        final TextView cate_name = (TextView) view.findViewById(R.id.cate_name);
        final View cate_line = view.findViewById(R.id.cate_line);

        cate_name.setTextColor(getResources().getColor(R.color.colorPrimary));
        cate_line.setVisibility(View.VISIBLE);

    }

    private void updateColor(int finalI) {
        int count = container.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = container.getChildAt(i);
            TextView cate_name = (TextView) view.findViewById(R.id.cate_name);
            View cate_line =  view.findViewById(R.id.cate_line);

            if(i == finalI){
                cate_name.setTextColor(getResources().getColor(R.color.colorPrimary));
                cate_line.setVisibility(View.VISIBLE);
            }else
            {
                cate_name.setTextColor(getResources().getColor(android.R.color.black));
                cate_line.setVisibility(View.GONE);
            }
        }

        }

    public void setRightFragment(RightFragment rightFragment)
    {
        this.rightFragment = rightFragment;
    }


}



