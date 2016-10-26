package org.sang.a8_1qqmusic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.sang.a8_1qqmusic.util.LoginUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.tencent.qq.QQ;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void login(View view) {
        // 一点击登录按钮：
        // 立即把信息传入sp中，然后就刷新Fragment的UI，再次点击Relative即无效。
        QQ qq = new QQ(this);
        // 判断用户是否已经获得授权
        if(qq.isAuthValid())
        {
            // 清除用户授权,但在这里不需要，只是后面需要
//            qq.removeAccount();

            // 如果用户已经获得授权,就把他的信息保存在sp里,sp是由一个工具类提供，实际上sp对象只有一个就够了，不过sp的初始化需要一个Context,那就弄一个工具类吧
            PlatformDb db = qq.getDb();

            LoginUtil.saveData(db,LoginActivity.this);

        }else
        {
            // 引导用户登录 qq.authorize();
            qq.showUser(null);
        }

        qq.setPlatformActionListener(new PlatformActionListener() {
            // 登录成功时回调
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                PlatformDb db = platform.getDb();
                // 登录成功之后，往sp中存储数据
                LoginUtil.saveData(db,LoginActivity.this);


            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();
        // 在Activity中的onPause()方法中，调用finish掉本页面
        // 只要本页面不是在用户的最前面的界面，就finish掉本页，然后重新进来时，会调用onResume，在那里刷新Fragment的UI即可
        this.finish();
    }
}
