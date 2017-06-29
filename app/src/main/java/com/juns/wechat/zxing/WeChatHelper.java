package com.juns.wechat.zxing;

import com.hyphenate.chat.EMClient;

/**
 * Created by zhangtao on 2017/6/29.
 */

public class WeChatHelper {

    private static WeChatHelper mWeChatHelper;

    public synchronized static WeChatHelper getInstance() {
        if (mWeChatHelper == null) {
            mWeChatHelper = new WeChatHelper();
        }
        return mWeChatHelper;
    }

    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }
}
