package com.juns.wechat.chat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.juns.wechat.App;

/**
 * Created by zhangtao on 2017/6/30.
 */

public class ShareUtils {

    private static final String SHARE_NAME = "WeChat";
    private static SharedPreferences sp;

    static {
        sp = App.getContext().getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
    }

    public static void putString(String key,String value){
        sp.edit().putString(key,value).apply();
    }

    public static String getString(String key,String defValue){
        return sp.getString(key,defValue);
    }

    public static void putInt(String key,int value){
        sp.edit().putInt(key,value);
    }

    public static int getInt(String key,int defValue){
        return sp.getInt(key,defValue);
    }

    public static void putBoolean(String key,boolean value){
        sp.edit().putBoolean(key,value);
    }

    public static boolean getBoolean(String key,boolean defValue){
        return sp.getBoolean(key,defValue);
    }
}
