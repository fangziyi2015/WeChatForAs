package com.juns.wechat.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.ActivityControler;
import com.juns.wechat.R;
import com.juns.wechat.common.Utils;
import com.juns.wechat.dialog.FlippingLoadingDialog;
import com.juns.wechat.net.NetClient;

import org.apache.http.message.BasicNameValuePair;

public abstract class BaseActivity extends Activity implements View.OnClickListener{
	protected Activity context;
	protected NetClient netClient;
	protected FlippingLoadingDialog mLoadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		ActivityControler.getInstance().addActivity(this);
		netClient = new NetClient(this);
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
	 * 绑定控件id
	 */
	protected abstract void initControl();

	/**
	 * 初始化控件
	 */
	protected abstract void initView();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 设置监听
	 */
	protected abstract void setListener();

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

	public FlippingLoadingDialog getLoadingDialog(String msg) {
		if (mLoadingDialog == null)
			mLoadingDialog = new FlippingLoadingDialog(this, msg);
		return mLoadingDialog;
	}
}