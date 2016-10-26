package org.sang.a8_1qqmusic.localMusic.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.sang.a8_1qqmusic.R;
import org.sang.a8_1qqmusic.localMusic.view.adapter.MyVpAdapter;
import org.sang.a8_1qqmusic.localMusic.view.fragment.LocalArtistFragment;
import org.sang.a8_1qqmusic.localMusic.view.fragment.LocalMusicFragment;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);

        initView();

    }

    private void initView() {

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        List<Fragment> fragments = new ArrayList<>();

        fragments.add(new LocalMusicFragment());
        fragments.add(new LocalArtistFragment());

        String[] titles = new String[]{"歌曲","歌手"};

        viewPager.setAdapter(new MyVpAdapter(getSupportFragmentManager(),fragments,titles));

        tabLayout.setupWithViewPager(viewPager);

    }
}
