package com.juns.wechat.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.juns.wechat.R;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;

//注册
public class RegisterActivity extends BaseActivity implements OnClickListener {
	private Button btn_register;
	private EditText et_username, et_password, et_confirm_password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_register);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initView() {
		setWeChatTitle("注册");
		btn_register = (Button) findViewById(R.id.btn_register);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
	}


	@Override
	protected void setListener() {
		setUpBackListener();
		btn_register.setOnClickListener(this);
	}

	@Override
	public void myOnClick(View v, int id) {
		super.myOnClick(v, id);
		switch (v.getId()) {
			case R.id.btn_register:
				// 注册
				register();
				break;
			default:
				break;
		}
	}

	/**
	 * 注册
	 */
	private void register() {
		final String userName = et_username.getText().toString().trim();
		final String userPwd = et_password.getText().toString().trim();
		String confirmPwd = et_confirm_password.getText().toString().trim();

		if (TextUtils.isEmpty(userName)){
			Utils.showShortToast(this,"用户名不能为空");
			et_username.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(userPwd)){
			Utils.showShortToast(this,"密码不能为空");
			et_password.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(confirmPwd)){
			Utils.showShortToast(this,"请输入确认密码");
			et_confirm_password.requestFocus();
			return;
		}

		if (!userPwd.equals(confirmPwd)){
			Utils.showShortToast(this,"两次密码不一致，请重新确认");
			et_confirm_password.requestFocus();
			return;
		}

		showProgressDialog("正在注册...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					EMClient.getInstance().createAccount(userName,userPwd);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Utils.showShortToast(RegisterActivity.this,"注册用户成功!");
							if (!RegisterActivity.this.isFinishing()){
								dismissProgressDialog();
							}
						}
					});

					openActivity(RegisterActivity.this,LoginActivity.class);
					finish(RegisterActivity.this);
				} catch (HyphenateException e) {
					//e.printStackTrace();
					if (!RegisterActivity.this.isFinishing()){
						dismissProgressDialog();
					}

					final int errorCode = e.getErrorCode();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(errorCode== EMError.NETWORK_ERROR){
								Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
							}else if(errorCode == EMError.USER_ALREADY_EXIST){
								Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
							}else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
								Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
							}else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
								Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		}).start();
	}

}
