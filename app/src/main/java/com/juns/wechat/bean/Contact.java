package com.juns.wechat.bean;

/**
 * Created by zhangtao on 2017/6/30.
 */

public class Contact {
    private String name;
    private String reason;

    public Contact(String name, String reason) {
        this.name = name;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
