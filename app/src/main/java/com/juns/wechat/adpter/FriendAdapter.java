package com.juns.wechat.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juns.wechat.R;

import java.util.List;

/**
 * Created by zhangtao on 2017/6/30.
 */

public class FriendAdapter extends BaseAdapter {
    private List<String> mContacts;
    private LayoutInflater mLayoutInflater;

    public FriendAdapter(List<String> contacts, Context context){
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
        convertView = mLayoutInflater.inflate(R.layout.contact_item,parent,false);
        if (convertView != null) {
            TextView tv_nick = (TextView) convertView.findViewById(R.id.contactitem_nick);
            tv_nick.setText(mContacts.get(position));
        }
        return convertView;
    }

}
