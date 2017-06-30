package com.juns.wechat.view.activity;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.juns.wechat.Constants;
import com.juns.wechat.R;
import com.juns.wechat.adpter.NewContactAdapter;
import com.juns.wechat.bean.Contact;
import com.juns.wechat.common.Utils;
import com.juns.wechat.db.DataBaseOpenHelper;
import com.juns.wechat.view.BaseActivity;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

//新朋友
public class NewFriendsListActivity extends BaseActivity implements
        OnClickListener {
    private TextView txt_right;
    private ListView mlistview;
    private View layout_head;
    private static final String TAG = "NewFriendsListActivity";
    private static List<Contact> mContacts;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_listview);
    }

    @Override
    protected void initView() {
        setWeChatTitle("新的朋友");
        txt_right = (TextView) findViewById(R.id.txt_right);
        txt_right.setText("添加朋友");
        mlistview = (ListView) findViewById(R.id.listview);
        layout_head = getLayoutInflater().inflate(
                R.layout.layout_head_newfriend, null);
        mlistview.addHeaderView(layout_head);

        DataBaseOpenHelper helper = new DataBaseOpenHelper(this, 1);
        mContacts = helper.queryContact();
        Log.i(TAG, "initControl: size:" + mContacts.size());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> allContact = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    for (Contact contact : mContacts) {
                        Log.d(TAG, "run: allContact:" + allContact.toString());
                        if (allContact.contains(contact.getName())) {
                            mContacts.remove(contact);
                            Log.i(TAG, "run: ");
                        }
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        mlistview.setAdapter(new NewContactAdapter(mContacts, NewFriendsListActivity.this));
    }

    @Override
    protected void setListener() {
        setUpBackListener();
        txt_right.setOnClickListener(this);
        layout_head.findViewById(R.id.txt_search).setOnClickListener(this);
        layout_head.findViewById(R.id.txt_tel).setOnClickListener(this);
        layout_head.findViewById(R.id.txt_qq).setOnClickListener(this);

    }

    @Override
    public void myOnClick(View v, int id) {
        super.myOnClick(v, id);
        switch (v.getId()) {
            case R.id.txt_right:
                Utils.openActivity(this, PublicActivity.class,
                        new BasicNameValuePair(Constants.NAME, "添加朋友"));
                break;
            case R.id.txt_search:
                Utils.openActivity(this, PublicActivity.class,
                        new BasicNameValuePair(Constants.NAME, "搜索"));
                break;
            case R.id.txt_tel:
                Utils.openActivity(this, AddFromContactActivity.class);
                break;
            case R.id.txt_qq:
                Utils.openActivity(this, PublicActivity.class,
                        new BasicNameValuePair(Constants.NAME, "添加QQ好友"));
                break;
            default:
                break;
        }
    }

}
