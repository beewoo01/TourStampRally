package com.sdin.tourstamprally.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

public class DbOpenHelper extends SQLiteOpenHelper{

    public DbOpenHelper(Context context){
        super(context, "bsrDB", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql =  "CREATE TABLE alram (" +
                " user_Phone TEXT," +
                " email_Alram INTEGER,"+
                " push_Alram INTEGER,"+
                " sms_Alram INTEGER"+
                " );";

        //Log.wtf("DbOpenHelper", "onCreate");

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE if exists alram";

        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
