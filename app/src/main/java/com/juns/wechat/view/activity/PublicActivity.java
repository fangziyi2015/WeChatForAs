package com.juns.wechat.view.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.juns.wechat.R;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.BaseActivity;

//公共页面
public class PublicActivity extends BaseActivity implements OnClickListener {

	private EditText mEt_contact;
	private Button mBtn_find_contact;
	private Button mBtn_add_contact;
	private TextView mTv_contact;
	private RelativeLayout mRl_root;
	private String mFindContact;
	private EditText mEt_add_reason;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_public);
	}

	@Override
	protected void initView() {
		setWeChatTitle("添加好友");
		mEt_contact = (EditText) findViewById(R.id.et_contact);
		mEt_add_reason = (EditText) findViewById(R.id.et_add_reason);
		mBtn_find_contact = (Button) findViewById(R.id.btn_find_contact);
		mBtn_add_contact = (Button) findViewById(R.id.btn_add_contact);
		mTv_contact = (TextView) findViewById(R.id.tv_contact);
		mRl_root = (RelativeLayout) findViewById(R.id.rl_root);

	}

	@Override
	protected void setListener() {
		setUpBackListener();
		mBtn_find_contact.setOnClickListener(this);
		mBtn_add_contact.setOnClickListener(this);
	}

	@Override
	public void myOnClick(View v, int id) {
		super.myOnClick(v, id);
		switch (id){
			case R.id.btn_find_contact:
				findContact();
				break;
			case R.id.btn_add_contact:
				addContact();
				break;
		}
	}


	private void findContact() {
		mFindContact = mEt_contact.getText().toString();
		if (TextUtils.isEmpty(mFindContact)){
			Utils.showShortToast(this,"请输入好友昵称");
			return;
		}

		mRl_root.setVisibility(View.VISIBLE);
		mTv_contact.setText(mFindContact);
	}

	private void addContact() {
		final String reason = mEt_add_reason.getText().toString();
		// 如果添加的是当前用户，则不能添加
		if(EMClient.getInstance().getCurrentUser().equals(mFindContact)){
			Utils.showShortToast(this,getResources().getString(R.string.not_add_myself));
			return;
		}

		if(EMClient.getInstance().contactManager().getBlackListUsernames().contains(mFindContact)){
			Utils.showShortToast(this,getResources().getString(R.string.user_already_in_contactlist));
			return;
		}

		showProgressDialog("正在向该好友发出请求...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					EMClient.getInstance().contactManager().addContact(mFindContact,EMClient.getInstance().getCurrentUser()
							+ (TextUtils.isEmpty(reason) ? "请求加你为好友！" : reason));
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Utils.showShortToast(PublicActivity.this,"发送请求成功,等待对方验证！");
							dismissProgressDialog();
						}
					});
				} catch (HyphenateException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Utils.showShortToast(PublicActivity.this,"请求添加好友失败!");
							dismissProgressDialog();
						}
					});
				}
			}
		}).start();

	}

}
