package com.sdin.tourstamprally.sqlite;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String _TABLENAME0 = "alram";
        public static final String USER_PHONE = "user_phone";
        public static final String emailAlram = "emailAlram";
        public static final String pushAlram = "pushAlram";
        public static final String smsAlram = "smsAlram";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +USER_PHONE+" text not null , "
                +emailAlram+" integer not null , "
                +pushAlram+" integer not null , "
                +smsAlram+" integer not null );";
    }
}
