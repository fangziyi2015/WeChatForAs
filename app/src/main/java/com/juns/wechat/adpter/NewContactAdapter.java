package com.juns.wechat.adpter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.juns.wechat.App;
import com.juns.wechat.R;
import com.juns.wechat.bean.Contact;
import com.juns.wechat.common.Utils;

import java.util.List;

/**
 * Created by zhangtao on 2017/6/30.
 */

public class NewContactAdapter extends BaseAdapter {
    private List<Contact> mContacts;
    private LayoutInflater mLayoutInflater;

    private static final String TAG = "NewContactAdapter";
    private TextView mTv_add;

    public NewContactAdapter(List<Contact> contacts,Context context){
        this.mContacts = contacts;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.layout_item_newfriend,parent,false);
        if (convertView != null){
            TextView tv_name = (TextView) convertView.findViewById(R.id.txt_name);
            TextView tv_reason = (TextView) convertView.findViewById(R.id.txt_msg);
            mTv_add = (TextView) convertView.findViewById(R.id.txt_add);

            tv_name.setText(mContacts.get(position).getName());
            tv_reason.setText(mContacts.get(position).getReason());

            mTv_add.setTag(position);
            mTv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    Contact contact = mContacts.get(position);
                    final String name = contact.getName();
                    Log.i(TAG, "onClick: name:"+name);
                    // 同意添加
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().contactManager().acceptInvitation(name);
                                handler.sendEmptyMessage(1);
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(0);
                            }
                        }
                    }).start();
                }
            });
        }


        return convertView;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Utils.showShortToast(App.getContext(),"已同意好友申请");
                    mTv_add.setText("已同意");
                    break;
                case 0:
                    Utils.showShortToast(App.getContext(),"同意好友失败");
                    break;

            }
        }
    };

}
