package com.sdin.tourstamprally.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sdin.tourstamprally.model.AlramState;

public class SQLiteConnector extends DbOpenHelper {

    private final SQLiteDatabase db;
    private final String tableName = "alram";

    public SQLiteConnector(Context context) {
        super(context);
        db = getWritableDatabase();
        //onCreate(db);
    }


    public long insert(AlramState alramState) {
        Log.wtf("insert", "insert");
        /*String sql =
                "insert into alram " +
                        "VALUES ("+ alramState.getUser_phone() + ", " + alramState.getEmail_Alram() +
                        ", " + alramState.getPush_Alram() + ", " + alramState.getSms_Alram() + ")";
        db.execSQL(sql);*/
        long result = db.insert(tableName, null, contentValues(alramState));
        Log.wtf("result", String.valueOf(result));
        return result;
    }

    public void update(AlramState alramState) {
        Log.d("UID?", String.valueOf(alramState.getUser_phone()));
        db.execSQL("UPDATE " + tableName +
                " SET email_Alram = '" + alramState.getEmail_Alram() + "'" +
                ", push_Alram = '" + alramState.getPush_Alram() + "'" +
                ", sms_Alram = '" + alramState.getSms_Alram() + "'" +
                " WHERE user_Phone = '" + alramState.getUser_phone() + "';"
        );
    }

    public AlramState selectAll(String user_phone) {
        String sql = "SELECT * FROM " + tableName + " WHERE user_Phone = " + user_phone;
        Log.wtf("selectAll", "왓따");
        Cursor cursor = db.rawQuery(sql, null);
        String phone = "";
        int email_Alram = 0;
        int push_Alram = 0;
        int sms_Alram = 0;
        while (cursor.moveToNext()) {
            Log.wtf("selectAll", "moveToNext");
            phone = cursor.getString(0);
            email_Alram = cursor.getInt(1);
            push_Alram = cursor.getInt(2);
            sms_Alram = cursor.getInt(2);
            Log.wtf("phone 0", phone);
            Log.wtf("email_Alram 1", String.valueOf(email_Alram));
            Log.wtf("push_Alram 2", String.valueOf(push_Alram));
            Log.wtf("sms_Alram 3", String.valueOf(sms_Alram));
        }

        cursor.close();

        return new AlramState(
                phone,
                email_Alram,
                push_Alram,
                sms_Alram
        );
    }

    public AlramState selectExits(String user_phone) {
        Log.wtf("selectExits user_phone", user_phone);
        String sql = "SELECT * FROM alram WHERE user_Phone ='" + user_phone + "';";
        Cursor cursor = db.rawQuery(sql, null);
        AlramState model = new AlramState();
        if (cursor.moveToFirst()) {
            model.setUser_phone(cursor.getString(0));
            model.setEmail_Alram(cursor.getInt(1));
            model.setPush_Alram(cursor.getInt(2));
            model.setSms_Alram(cursor.getInt(3));
            cursor.close();

        } else {
            insert(new AlramState(user_phone, 0, 0, 0));
            String seSql = "SELECT * FROM alram WHERE user_Phone ='" + user_phone + "';";
            Cursor seCursor = db.rawQuery(seSql, null);
            model = new AlramState();
            if (seCursor.moveToFirst()) {

                model.setUser_phone(seCursor.getString(0));
                model.setEmail_Alram(seCursor.getInt(1));
                model.setPush_Alram(seCursor.getInt(2));
                model.setSms_Alram(seCursor.getInt(3));
            }
            seCursor.close();

        }
        return model;
    }


    private ContentValues contentValues(AlramState alramState) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_Phone", alramState.getUser_phone());
        contentValues.put("email_Alram", alramState.getEmail_Alram());
        contentValues.put("push_Alram", alramState.getPush_Alram());
        contentValues.put("sms_Alram", alramState.getSms_Alram());
        return contentValues;
    }

}
