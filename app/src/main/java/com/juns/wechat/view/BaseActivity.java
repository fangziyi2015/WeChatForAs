package com.juns.wechat.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.ActivityControler;
import com.juns.wechat.R;
import com.juns.wechat.common.Utils;
import com.juns.wechat.net.NetClient;

import org.apache.http.message.BasicNameValuePair;

import java.util.Timer;
import java.util.TimerTask;

public class BaseActivity extends Activity implements View.OnClickListener{
	protected Activity context;
	protected NetClient netClient;
	private ProgressDialog mProgressDialog;

	private static final int TIME_OUT = 20 * 1000;
	private Timer mTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
		context = this;
		netClient = new NetClient(this);
		ActivityControler.getInstance().addActivity(this);
		initView();
		initData();
		initControl();
		setListener();
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();

		dismissProgressDialog();
	}

	public void setWeChatTitle(String title){
		TextView tv_title = (TextView) findViewById(R.id.txt_title);
		if (title != null && tv_title != null){
			tv_title.setText(title);
		}
	}

	public void setWeChatTitle(int resId){
		setWeChatTitle(getResources().getString(resId));
	}

	public void setUpBackListener(){
		ImageView iv_back = (ImageView) findViewById(R.id.img_back);
		if (iv_back != null){
			iv_back.setVisibility(View.VISIBLE);
			iv_back.setOnClickListener(this);
		}
	}

	public void showProgressDialog(String message){
		if (mProgressDialog == null){
			mProgressDialog = new ProgressDialog(this);
		}

		mProgressDialog.setMessage(message);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();

		startCountTimeOut();
	}

	public void dismissProgressDialog(){
		if (mProgressDialog != null && mProgressDialog.isShowing()){
			mProgressDialog.dismiss();
			mProgressDialog = null;

			if (mTimer != null){
				mTimer.cancel();
				mTimer = null;
			}
		}
	}

	private void startCountTimeOut(){
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				dismissProgressDialog();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Utils.showShortToast(context,"请求超时，请重试！");
						mTimer.cancel();
						mTimer = null;
					}
				});
			}
		},TIME_OUT);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.img_back:
				Utils.finish(this);
				ActivityControler.getInstance().removeActivity(this);
				break;
			default:
				myOnClick(v,v.getId());
				break;
		}
	}

	public void myOnClick(View v,int id){

	}

	/**
	 * 设置布局
	 */
	protected void setContentView(){}

	/**
	 * 绑定控件id
	 */
	protected void initControl(){}

	/**
	 * 初始化控件
	 */
	protected void initView(){}

	/**
	 * 初始化数据
	 */
	protected void initData(){}

	/**
	 * 设置监听
	 */
	protected void setListener(){}

	/**
	 * 打开 Activity
	 * 
	 * @param activity
	 * @param cls
	 * @param name
	 */
	public void openActivity(Activity activity, Class<?> cls,
							 BasicNameValuePair... name) {
		Utils.openActivity(activity, cls, name);
	}

	/**
	 * 关闭 Activity
	 * 
	 * @param activity
	 */
	public void finish(Activity activity) {
		Utils.finish(activity);
	}

	/**
	 * 判断是否有网络连接
	 */
	public boolean isNetworkAvailable(Context context) {
		return Utils.isNetworkAvailable(context);
	}

}