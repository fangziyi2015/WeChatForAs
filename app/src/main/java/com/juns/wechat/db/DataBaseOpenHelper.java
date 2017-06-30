package com.juns.wechat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.juns.wechat.bean.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangtao on 2017/6/30.
 */

public class DataBaseOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "WeChat.db";
    private static final String TABLE_NAME = "contact";
    private SQLiteDatabase db;
    private static final String CREATE_CONTACT = "create table " + TABLE_NAME
            + " (id integer primary key autoincrement,"
            + " name varchar(20),"
            + " reason text)";


    public DataBaseOpenHelper(Context context,int version) {
        super(context, DB_NAME, null, version);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addContact(Contact contact){
        ContentValues value = new ContentValues();
        if (contact != null){
            value.put("name",contact.getName());
            value.put("reason",contact.getReason());
        }
        db.insert(TABLE_NAME,null,value);
    }

    public List<Contact> queryContact(){
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String reason = cursor.getString(cursor.getColumnIndex("reason"));
                Contact contact = new Contact(name,reason);
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        return contacts;
    }
}
