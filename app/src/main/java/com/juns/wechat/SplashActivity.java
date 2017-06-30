package com.juns.wechat;

import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.util.EasyUtils;
import com.juns.wechat.chat.VideoCallActivity;
import com.juns.wechat.chat.VoiceCallActivity;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;
import com.juns.wechat.view.activity.LoginActivity;
import com.juns.wechat.zxing.WeChatHelper;

public class SplashActivity extends BaseActivity {

	private static final int sleepTime = 2000;
	private static final String TAG = "SplashActivity";

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_splash);
	}

	@Override
	protected void initView() {
		RelativeLayout splash_root = (RelativeLayout) findViewById(R.id.splash_root);
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		splash_root.startAnimation(animation);
	}

	@Override
	protected void onStart() {
		super.onStart();
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (WeChatHelper.getInstance().isLoggedIn()){
					Log.i(TAG, "run: 登陆");
					long start = System.currentTimeMillis();
					EMClient.getInstance().chatManager().loadAllConversations();
					EMClient.getInstance().groupManager().loadAllGroups();
					long costTime = System.currentTimeMillis() - start;
					//wait
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
//
					String topActivityName = EasyUtils.getTopActivityName(EMClient.getInstance().getContext());
					if (topActivityName != null && (topActivityName.equals(VideoCallActivity.class.getName()) || topActivityName.equals(VoiceCallActivity.class.getName()))) {
						// nop
						// avoid main screen overlap Calling Activity
						Log.i(TAG, "avoid main screen overlap Calling Activity");
					} else {
						//enter main screen
						Utils.openActivity(SplashActivity.this,MainActivity.class);
						Log.i(TAG, "run: ===================");
					}

				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						Log.d(TAG, "run: "+e.getMessage());
					}

					Utils.openActivity(SplashActivity.this,LoginActivity.class);
					finish(SplashActivity.this);
				}
			}
		}).start();
	}
}
