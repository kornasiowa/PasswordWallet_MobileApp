package com.kornasdominika.passwordwallet.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Class which represents Database tables
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    static final String USER_TABLE = "user";
    static final String PASSWORD_TABLE = "password";
    static final String LOGS_TABLE = "logs";
    static final String FUNCTION_TABLE = "function";
    static final String DATA_CHANGE_TABLE = "data_change";

    private static final String DB_NAME = "PASSWORDWALLET.DB";
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + USER_TABLE +
            "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "login VARCHAR(30) NOT NULL, " +
            "password_hash VARCHAR(512) NOT NULL, " +
            "salt VARCHAR(20) NOT NULL, " +
            "isPasswordKeptAsHash bool NOT NULL) ";

    private static final String CREATE_TABLE_PASSWORD = "CREATE TABLE " + PASSWORD_TABLE +
            "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "password VARCHAR(256) NOT NULL, " +
            "id_user INTEGER NOT NULL, " +
            "web_address VARCHAR(256) NOT NULL, " +
            "description VARCHAR(256) NOT NULL, " +
            "login VARCHAR(30) NOT NULL, " +
            "owner INTEGER, " +
            "mid INTEGER, " +
            "FOREIGN KEY(id_user) REFERENCES user(id))";

    private static final String CREATE_TABLE_LOGS = "CREATE TABLE " + LOGS_TABLE +
            "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_user INTEGER  NOT NULL, " +
            "ip VARCHAR(256) NOT NULL, " +
            "time VARCHAR(256) NOT NULL, " +
            "isLastSuccess bool NOT NULL, " +
            "failedAttempts INTEGER NOT NULL, " +
            "FOREIGN KEY(id_user) REFERENCES user(id))";

    private static final String CREATE_TABLE_FUNCTION = "CREATE TABLE " + FUNCTION_TABLE +
            "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "uid INTEGER  NOT NULL, " +
            "time VARCHAR(56) NOT NULL, " +
            "functionName VARCHAR(56) NOT NULL, " +
            "FOREIGN KEY(uid) REFERENCES user(id))";

    private static final String CREATE_TABLE_DATA_CHANGE = "CREATE TABLE " + DATA_CHANGE_TABLE +
            "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "uid INTEGER  NOT NULL, " +
            "rid INTEGER  NOT NULL, " +
            "fid INTEGER  NOT NULL, " +
            "actionType VARCHAR(56) NOT NULL, " +
            "previousValue VARCHAR(300), " +
            "presentValue VARCHAR(300), " +
            "time VARCHAR(56) NOT NULL, " +
            "isRestored INTEGER NOT NULL, " +
            "FOREIGN KEY(uid) REFERENCES user(id))";

    private static final int DB_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PASSWORD);
        db.execSQL(CREATE_TABLE_LOGS);
        db.execSQL(CREATE_TABLE_FUNCTION);
        db.execSQL(CREATE_TABLE_DATA_CHANGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
