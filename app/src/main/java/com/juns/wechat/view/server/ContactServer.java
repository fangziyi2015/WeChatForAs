package com.juns.wechat.view.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.juns.wechat.bean.Contact;
import com.juns.wechat.common.Utils;
import com.juns.wechat.db.DataBaseOpenHelper;

/**
 * Created by zhangtao on 2017/6/30.
 */

public class ContactServer extends Service {

    private static final String TAG = "ContactServer";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {

            }

            @Override
            public void onContactDeleted(String s) {

            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                Utils.showShortToast(ContactServer.this,"收到好友添加请求！");
                Log.d(TAG, "onContactInvited: 收到好友请求");
                DataBaseOpenHelper helper = new DataBaseOpenHelper(ContactServer.this,1);
                Contact contact = new Contact(username,reason);
                helper.addContact(contact);
            }

            @Override
            public void onFriendRequestAccepted(String s) {

            }

            @Override
            public void onFriendRequestDeclined(String s) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
