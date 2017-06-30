package com.juns.wechat.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.juns.wechat.Constants;
import com.juns.wechat.GloableParams;
import com.juns.wechat.R;
import com.juns.wechat.adpter.FriendAdapter;
import com.juns.wechat.bean.User;
import com.juns.wechat.chat.ChatActivity;
import com.juns.wechat.common.Utils;
import com.juns.wechat.view.activity.GroupListActivity;
import com.juns.wechat.view.activity.NewFriendsListActivity;
import com.juns.wechat.view.activity.PublishUserListActivity;
import com.juns.wechat.view.activity.SearchActivity;
import com.juns.wechat.widght.SideBar;

import net.tsz.afinal.FinalDb;

import java.util.List;

//通讯录

public class Fragment_Friends extends Fragment implements OnClickListener,AdapterView.OnItemClickListener
		{
	private Activity ctx;
	private View layout, layout_head;
	private ListView lvContact;
	private SideBar indexBar;
	private TextView mDialogText;
	private WindowManager mWindowManager;
	private static final String TAG = "Fragment_Friends";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout == null) {
			ctx = this.getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.fragment_friends,
					null);
			mWindowManager = (WindowManager) ctx
					.getSystemService(Context.WINDOW_SERVICE);
			initViews();
			initData();
			setOnListener();
		} else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}

	private void initViews() {
		lvContact = (ListView) layout.findViewById(R.id.lvContact);

		mDialogText = (TextView) LayoutInflater.from(getActivity()).inflate(
				R.layout.list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		indexBar = (SideBar) layout.findViewById(R.id.sideBar);
		indexBar.setListView(lvContact);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);
		layout_head = ctx.getLayoutInflater().inflate(
				R.layout.layout_head_friend, null);
		lvContact.addHeaderView(layout_head);

	}

	@Override
	public void onDestroy() {
		mWindowManager.removeView(mDialogText);
		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		initData();
	}

	private void initData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final List<String> mUsernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
					Log.i(TAG, "initData: size:" + mUsernames.toString());
					ctx.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							lvContact.setAdapter(new FriendAdapter(mUsernames,ctx));
						}
					});
				} catch (HyphenateException e) {
					e.printStackTrace();
				}
			}
		}).start();

		//lvContact.setAdapter(new FriendAdapter(mUsernames,ctx));


		if (GloableParams.UserInfos != null) {
//			lvContact.setAdapter(new ContactAdapter(getActivity(),
//					GloableParams.UserInfos));
//			lvContact.setAdapter(new FriendAdapter(mUsernames,ctx));
		} else {
			FinalDb db = FinalDb
					.create(getActivity(), Constants.DB_NAME, false);
			GloableParams.UserInfos = db.findAllByWhere(User.class, "type='N'");
//			lvContact.setAdapter(new ContactAdapter(getActivity(),
//					GloableParams.UserInfos));
//			lvContact.setAdapter(new FriendAdapter(mUsernames,ctx));
			for (User user : GloableParams.UserInfos) {
				GloableParams.Users.put(user.getTelephone(), user);
			}
			// Intent intent = new Intent(getActivity(), UpdateService.class);
			// getActivity().startService(intent);
		}

	}

	private void setOnListener() {
		lvContact.setOnItemClickListener(this);
		layout_head.findViewById(R.id.layout_addfriend).setOnClickListener(this);
		layout_head.findViewById(R.id.layout_search).setOnClickListener(this);
		layout_head.findViewById(R.id.layout_group).setOnClickListener(this);
		layout_head.findViewById(R.id.layout_public).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_search:// 搜索好友及公众号
			Utils.openActivity(getActivity(), SearchActivity.class);
			break;
		case R.id.layout_addfriend:// 新的朋友
			Utils.openActivity(getActivity(), NewFriendsListActivity.class);
			break;
		case R.id.layout_group:// 群聊
			Utils.openActivity(getActivity(), GroupListActivity.class);
			break;
		case R.id.layout_public:// 公众号
			Utils.openActivity(getActivity(), PublishUserListActivity.class);
			break;
		default:
			break;
		}
	}

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String name = "zhangsan";
				Intent intent = new Intent(ctx, ChatActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(Constants.NAME, name);
				bundle.putInt(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
				intent.putExtras(bundle);
				ctx.startActivity(intent);

				Log.d(TAG, "onItemClick: ==========");
			}

//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
////		User user = GloableParams.UserInfos.get(arg2 - 1);
//
////		if (user != null) {
//
//			Intent intent = new Intent(getActivity(), ChatActivity.class);
//			intent.putExtra(Constants.NAME, "");
//			ctx.startActivity(intent);
////			intent.putExtra(Constants.TYPE, ChatActivity.CHATTYPE_SINGLE);
////			intent.putExtra(Constants.User_ID, user.getTelephone());
////			getActivity().startActivity(intent);
////			getActivity().overridePendingTransition(R.anim.push_left_in,
////					R.anim.push_left_out);
////		}
//
//	}
}
