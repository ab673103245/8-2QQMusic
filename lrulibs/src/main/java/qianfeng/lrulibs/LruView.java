package qianfeng.lrulibs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import qianfeng.lrulibs.bean.LrcBean;


/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class LruView extends View {

    private List<LrcBean> list;

    // C语言层有一个MediaPlayer，java层有一个MediaPlayer，两者有时候会出现不一致，所以希望MediaPlayer的最外面应该有一层try-catch来包裹
    private MediaPlayer mediaPlayer;

    private Paint gPaint;

    private Paint hPaint;

    private int width;

    private int height;

    private int currentPosition;

    private int lastPosition;

    private int mode = 0;

    private static final int  KARAOKE = 1;


    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }


    public void init() {
        currentPosition = 0;
        lastPosition = 0;
        // 要是更完善就加上这行，画布也置为原来的位置
        setScrollY(0);
        invalidate();
    }

    public void setList(List<LrcBean> list) {
        this.list = list;
    }

    public LruView(Context context) {
        this(context, null);
    }

    public LruView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LruView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LruView);

        mode = ta.getInt(R.styleable.LruView_mode,0);

        ta.recycle();

        gPaint = new Paint();
        gPaint.setAntiAlias(true);
        gPaint.setColor(getResources().getColor(android.R.color.white));
        gPaint.setTextSize(36);
        gPaint.setTextAlign(Paint.Align.CENTER);


        hPaint = new Paint();
        hPaint.setAntiAlias(true);
        hPaint.setColor(getResources().getColor(R.color.green));
        hPaint.setTextSize(36);
        hPaint.setTextAlign(Paint.Align.CENTER);


    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (width == 0 || height == 0) {
            width = getWidth();
            height = getHeight();
        }

        if (list == null || list.size() == 0) {
            canvas.drawText("暂无歌词", width / 2, height / 2, gPaint);
            return;
        }

        getCurrentPosition();

//        drawLru1(canvas);

        // 在显示下一个高亮 之前就 缓慢拖动画布,在500毫秒内，每隔100毫秒，拖动80/5px的距离,只不过这个值要叠加
//        setScrollY();
        int currentMillis = mediaPlayer.getCurrentPosition();

        long start = list.get(currentPosition).getStart();

        //   lastPostion并不是没有用的，它是有用的！！！！           这里它就有用了 lastPosition * 80
        // 我的本意是：绘制变颜色的高亮的歌词是一瞬间每隔100毫秒就完成的事，而我要让它慢慢上来，我高亮的歌词是必须显示在这控件的正中央，所以要让高亮好的歌词慢慢上来(达到控件中央)，上一行再慢慢上去(离开控件中央,往上)
        long y = currentMillis - start > 500 ? currentPosition * 80 : (lastPosition * 80 + (currentPosition - lastPosition) * 80 * (currentMillis - start) / 500);

        setScrollY((int) y);

        // 怎样知道该换行了呢？当setScrollY移动的距离等于 currentPosition * 80 的时候，我就知道下一行歌词要上来了
        if (getScrollY() == currentPosition * 80) {
            lastPosition = currentPosition;
        }

//        drawLru2(canvas);
        drawLrc3(canvas, currentMillis);

        // 隔100毫秒重新调用一次onDraw()方法,重绘
        postInvalidateDelayed(100);

    }

//    private void drawLru3(Canvas canvas, int currentMillis) {
//        for (int i = 0; i < list.size(); i++) {
//            canvas.drawText(list.get(i).getText(), width / 2, height / 2 + 80 * i, gPaint);
//        }
//
//        LrcBean lrcBean = list.get(currentPosition);
////        canvas.drawText(lrcBean.getText(), width / 2, height / 2 * 80 * currentPosition, hPaint);
//
//        float measureTextWidth = hPaint.measureText(lrcBean.getText());
//
//        int bitmapWidth = (int) ((currentMillis - lrcBean.getStart() * 1.0f / (lrcBean.getEnd() - lrcBean.getStart()) * measureTextWidth);
//
//        if (bitmapWidth > 0) {
//            Bitmap TextBitmap = Bitmap.createBitmap(bitmapWidth, 80, Bitmap.Config.ARGB_8888);
//            Canvas textCanvas = new Canvas(TextBitmap);
//
//            textCanvas.drawText(lrcBean.getText(), measureTextWidth / 2, 80, hPaint);
//            // Bitmap又是一个单元, height / 2 + 80 * (currentPosition - 1) 是为了让歌词变绿的时候是，轮到下一句的时候，让这一句变绿
//            // -----   这是我要显示的歌词，位于中间？
//            // ---- 这是下一句歌词
//            // ----
//            textCanvas.drawBitmap(TextBitmap, (width - measureTextWidth) / 2, height / 2 + 80 * (currentPosition - 1), null);
//        }
//
//
//    }

    private void drawLrc3(Canvas canvas, long currentMillis) {
        if (mode == 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i == currentPosition) {
                    canvas.drawText(list.get(i).getText(), width / 2, height / 2 + 80 * i, hPaint);
                } else {
                    canvas.drawText(list.get(i).getText(), width / 2, height / 2 + 80 * i, gPaint);
                }
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                canvas.drawText(list.get(i).getText(), width / 2, height / 2 + 80 * i, gPaint);
            }
//        canvas.drawText(list.get(currentPosition).getText(), width / 2, height / 2 + 80 * currentPosition, hPaint);
            LrcBean lrcBean = list.get(currentPosition);
            float highLineWidth = gPaint.measureText(lrcBean.getText());
            int bitmapWidth = (int) ((currentMillis - lrcBean.getStart()) * 1.0f / (lrcBean.getEnd() - lrcBean.getStart()) * highLineWidth);
            if (bitmapWidth > 0) {
                Bitmap textBitmap = Bitmap.createBitmap(bitmapWidth, 82, Bitmap.Config.ARGB_8888);
                Canvas textCanvas = new Canvas(textBitmap);
                textCanvas.drawText(lrcBean.getText(), highLineWidth / 2, 80, hPaint);
                canvas.drawBitmap(textBitmap, (width - highLineWidth) / 2, height / 2 + 80 * (currentPosition - 1), null);
            }
        }
    }

    private void drawLru2(Canvas canvas) {// 滚动方式

        for (int i = 0; i < list.size(); i++) {
            // i随着currentPosition 也是隔100毫秒更新一次
            // 这里每隔100毫秒都是整个画布重绘一次
            if (i == currentPosition) {
                // 一开始显示歌词的时候，就在屏幕的中央
                // 如果滑出屏幕是看不到的，不过没关系，我们可以拖动画布来让高亮的歌词显示出来
                canvas.drawText(list.get(i).getText(), width / 2, height / 2 + 80 * i, hPaint);
            } else {
                canvas.drawText(list.get(i).getText(), width / 2, height / 2 + 80 * i, gPaint);
            }
        }

    }

    private void drawLru1(Canvas canvas) {
        String text = list.get(currentPosition).getText();
        canvas.drawText(text, width / 2, height / 2, hPaint);


        // 画10行，如果屏幕显示不了那么多，那么它就不显示出来。
        for (int i = 1; i < 10; i++) {
            int index = currentPosition - i;
            if (index > -1) {
                canvas.drawText(list.get(index).getText(), width / 2, height / 2 - 80 * i, gPaint);
            }
        }
        for (int i = 1; i < 10; i++) {
            int index = currentPosition + i;
            if (index < list.size()) {
                canvas.drawText(list.get(index).getText(), width / 2, height / 2 + 80 * i, gPaint);
            }
        }
    }

    private void getCurrentPosition() {

        // 有一种情况是：当MediaPlayer的java层和C层出现状态不一致时，这个getCurrentPosition有可能在MediaPlayer没准备好之前，就开始调用这个getCurrentPostion方法，就会出现异常。
        // 解决的方式是：try-catch，并且在catch到异常的时候，进行处理，就是延迟100毫秒重新调用一次onDraw方法，使其绘制视图

        try {
            long currentTime = mediaPlayer.getCurrentPosition();

            for (int i = 0; i < list.size(); i++) {
                if (currentTime < list.get(0).getStart()) {
                    // list集合的第一行及在网络下载的第一行之前的数据，全部归为list集合里面的显示的第一行。
                    // 我的list集合做了处理，显示的第一行文本并不是从网络下载
                    currentPosition = 0;
                    return;
                }

                if (currentTime > list.get(list.size() - 1).getStart()) {
                    currentPosition = list.size() - 1;
                    return;
                }

                if (currentTime >= list.get(i).getStart() && currentTime < list.get(i).getEnd()) {
                    currentPosition = i;
                    return;
                }


            }
        } catch (Exception e) {
            // 在捕获到异常后，
            // 隔100毫秒重新绘制视图，这样就总有一个时刻，会进入到onDraw方法里面，进行重新绘制视图
            postInvalidateDelayed(100);
        }
    }
}
