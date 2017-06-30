package com.juns.wechat.view.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.juns.wechat.App;
import com.juns.wechat.Constants;
import com.juns.wechat.MainActivity;
import com.juns.wechat.R;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;
import com.juns.wechat.zxing.WeChatHelper;

import org.apache.http.message.BasicNameValuePair;

import static com.juns.wechat.chat.utils.CommonUtils.isNetWorkConnected;

//登陆
public class LoginActivity extends BaseActivity implements OnClickListener {
	private Button btn_login, btn_register;
	private EditText et_username, et_password;

	private boolean isAutoLogin = false;
	private static final String TAG = "LoginActivity";

	@Override
	protected void setContentView() {
		if (WeChatHelper.getInstance().isLoggedIn()) {
			isAutoLogin = true;
			openActivity(LoginActivity.this, MainActivity.class);
			return;
		}
		setContentView(R.layout.activity_login);
	}

	@Override
	protected void initView() {
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setEnabled(true);
		btn_register = (Button) findViewById(R.id.btn_qtlogin);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_confirm_password);
		setWeChatTitle("登陆");
	}

	@Override
	protected void setListener() {
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		findViewById(R.id.tv_wenti).setOnClickListener(this);
	}


	@Override
	public void myOnClick(View v, int id) {
		super.myOnClick(v, id);
		switch (v.getId()) {
			case R.id.tv_wenti:
				Utils.openActivity(LoginActivity.this, WebViewActivity.class,
						new BasicNameValuePair(Constants.Title, "帮助"),
						new BasicNameValuePair(Constants.URL,
								"http://weixin.qq.com/"));
				break;
			case R.id.btn_qtlogin:
				openActivity(LoginActivity.this,RegisterActivity.class);
				break;
			case R.id.btn_login:
				login();
				break;
			default:
				break;
		}
	}

	// 登陆
	private void login() {
		if (!isNetWorkConnected(this)) {
			Utils.showShortToast(this,getResources().getString(R.string.network_isnot_available));
			return;
		}

		final String userName = et_username.getText().toString().trim();
		final String userPwd = et_password.getText().toString().trim();

		if (TextUtils.isEmpty(userName)){
			Utils.showShortToast(this,"账号不能为空！");
			return;
		}

		if (TextUtils.isEmpty(userPwd)){
			Utils.showShortToast(this,"密码不能为空！");
			return;
		}

		showProgressDialog("正在登陆中...");

		new Thread(new Runnable() {
			@Override
			public void run() {
				EMClient.getInstance().login(userName, userPwd, new EMCallBack() {
					@Override
					public void onSuccess() {
						// 加载所有群组和会话
						EMClient.getInstance().groupManager().loadAllGroups();
						EMClient.getInstance().chatManager().loadAllConversations();

						// 更新当前接入点的昵称
						boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
								App.currentUserNick.trim());
						if (!updatenick) {
							Log.e("LoginActivity", "update current user nick fail");
						}


						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Utils.showShortToast(LoginActivity.this,"登陆服务器成功！");
								if (!LoginActivity.this.isFinishing()){
									dismissProgressDialog();
								}
							}
						});

						openActivity(LoginActivity.this,MainActivity.class);
						finish(LoginActivity.this);
					}

					@Override
					public void onError(int i, String s) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Utils.showShortToast(LoginActivity.this,"登陆失败，请重新登陆！");
								if (!LoginActivity.this.isFinishing()){
									dismissProgressDialog();
								}
							}
						});
					}

					@Override
					public void onProgress(int i, String s) {
						Log.d(TAG, "onProgress: ");
					}
				});
			}
		}).start();

	}


}
