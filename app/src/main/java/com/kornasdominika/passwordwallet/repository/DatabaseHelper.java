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
            "FOREIGN KEY(id_user) REFERENCES user(id))";
    private static final String CREATE_TABLE_LOGS = "CREATE TABLE " + LOGS_TABLE +
            "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id_user INTEGER  NOT NULL, " +
            "ip VARCHAR(256) NOT NULL, " +
            "time VARCHAR(256) NOT NULL, " +
            "isLastSuccess bool NOT NULL, " +
            "failedAttempts INTEGER NOT NULL, " +
            "FOREIGN KEY(id_user) REFERENCES user(id))";
    private static final int DB_VERSION = 1;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PASSWORD);
        db.execSQL(CREATE_TABLE_LOGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
