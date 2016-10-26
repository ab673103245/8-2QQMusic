package org.sang.a8_1qqmusic.showMusic.presenter;

import org.sang.a8_1qqmusic.BasePresenter;
import org.sang.a8_1qqmusic.showMusic.model.ILeftData;
import org.sang.a8_1qqmusic.showMusic.model.ILeftDataImpl;
import org.sang.a8_1qqmusic.showMusic.view.fragment.ILeftView;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class LeftPresenter implements BasePresenter{

    private ILeftView iLeftView;
    private ILeftData iLeftData;

    public LeftPresenter(ILeftView iLeftView) {
        this.iLeftView = iLeftView;
        iLeftData = new ILeftDataImpl();
    }

    @Override
    public void start() {
        iLeftView.initCategory(iLeftData.getCategory());
    }
}
