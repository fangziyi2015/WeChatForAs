package com.juns.wechat;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtao on 2017/6/29.
 */

public class ActivityControler {
    private static ActivityControler mControler;
    private static List<Activity> activities;

    static {
        activities = new ArrayList<>();
    }

    public synchronized static ActivityControler getInstance(){
        if (mControler == null){
            mControler = new ActivityControler();
        }
        return mControler;
    }

    private ActivityControler(){

    }

    public void addActivity(Activity activity){
        if (activities != null){
            if (activity != null)
                activities.add(activity);
        }
    }

    public void removeActivity(Activity activity){
        if (activities != null && activities.contains(activity)) {
            activities.remove(activity);
        }
    }

}
