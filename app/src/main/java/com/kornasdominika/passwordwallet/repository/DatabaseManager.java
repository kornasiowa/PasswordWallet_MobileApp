package com.kornasdominika.passwordwallet.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.kornasdominika.passwordwallet.model.HashDTO;
import com.kornasdominika.passwordwallet.model.Log;
import com.kornasdominika.passwordwallet.model.Password;
import com.kornasdominika.passwordwallet.model.User;

/**
 * Class which is a interface between SQL Database and business logic of app
 * It contains reference to SQLiteDatabase, ApplicationContext and DatabaseHelper
 *
 * @see DatabaseHelper
 */
public class DatabaseManager {

    private DatabaseHelper dbHelper;

    private SQLiteDatabase database;

    private Context context;

    public DatabaseManager(Context c) {
        context = c;
    }

    /**
     * Method that allows to open connetion to Database. It assigns WritableDatabase of current Applicaiton to object of class SQLiteDatabase
     *
     * @return the current DatabaseManager
     * @throws SQLException
     */
    public DatabaseManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * Method that allows to close connetion to Database.
     */
    public DatabaseManager close() {
        dbHelper.close();
        return this;
    }

    /**
     * Method allows to clear user table in Database.
     */
    public void dbClear(){
        database.delete("user", null, null);
    }

    /**
     * Method that allows to add new User object to Database only if user with the same login doesn't exists in Database.
     *
     * @param newUser object of User class
     * @return Return -1 if error so user exists or return 1 if user has been added succesfully or return 0 if something gone wrong during adding a new user
     */
    public int insertIntoUser(User newUser) {
        if (findUserByLogin(newUser.login) == -1) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("login", newUser.login);
            contentValues.put("password_hash", newUser.passwordHash);
            contentValues.put("salt", newUser.salt);
            contentValues.put("isPasswordKeptAsHash", newUser.isHash);
            database.insert("user", null, contentValues);

            if (findUserByLogin(newUser.login) != -1)
                return 1;
            else return 0;
        } else return -1;

    }

    /**
     * Method that allows to find User in Database according to login String
     *
     * @param login
     * @return Returns -1 if user doesn't exist in Database or ID of user if present
     */
    public int findUserByLogin(String login) {
        try (Cursor cursor = database.rawQuery("SELECT * FROM user WHERE login LIKE '" + login + "'", null)) {
            cursor.moveToFirst();
            cursor.getCount();
            if (cursor.getCount() == 0) {
                return -1;
            } else return cursor.getInt(0);
        }
    }

    /**
     * Method that allows to get User according to user id
     *
     * @param uid
     * @return Returns HashDTO object if User was find in Database or null if not
     */
    public HashDTO getUserDataToAuthorization(int uid) {
        try (Cursor cursor = database.rawQuery("SELECT * FROM user WHERE id LIKE '" + uid + "'", null)) {
            cursor.moveToFirst();
            cursor.getCount();
            if (cursor.getCount() == 0) {
                return null;
            } else {
                return new HashDTO(cursor.getString(2), cursor.getString(3), cursor.getInt(4));
            }
        }
    }

    /**
     * Method that allows get User password hash according to user id
     *
     * @param uid
     * @return Returns User password hash or null if it was not find in Database
     */
    public String getUserHash(int uid) {
        try (Cursor cursor = database.rawQuery("SELECT password_hash FROM user WHERE id LIKE '" + uid + "'", null)) {
            cursor.moveToFirst();
            cursor.getCount();
            if (cursor.getCount() == 0) {
                return null;
            } else {
                return cursor.getString(0);
            }
        }
    }

    /**
     * Method that allows to add new Password object to Database
     *
     * @param newPassword
     * @return Returns true if User was added to Database successful
     */
    public boolean insertIntoPassword(Password newPassword) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword.password);
        contentValues.put("id_user", newPassword.uid);
        contentValues.put("web_address", newPassword.webAddress);
        contentValues.put("description", newPassword.description);
        contentValues.put("login", newPassword.login);
        database.insert("password", null, contentValues);

        return findPasswordIntoDB(newPassword.uid, newPassword.webAddress, newPassword.login) != -1;
    }

    /**
     * Method that allows find Password object into Database according to passed parameters
     * @param uid
     * @param webAddr
     * @param login
     * @return Returns -1 if Password was not find or its id
     */
    public int findPasswordIntoDB(int uid, String webAddr, String login) {
        try (Cursor cursor = database.rawQuery("SELECT * FROM password WHERE id_user LIKE '" + uid + "' AND web_address LIKE '" + webAddr + "' AND login LIKE '" + login + "'", null)) {
            cursor.moveToFirst();
            cursor.getCount();
            if (cursor.getCount() == 0) {
                return -1;
            } else return cursor.getInt(0);
        }
    }

    /**
     * Method that gets all user passwords according to user id
     * @param uid user id
     * @return cursor with all user passwords
     */
    public Cursor getAllUserPasswords(int uid) {
        Cursor cursor = database.rawQuery("SELECT id as _id, web_address, description, login, password FROM password WHERE id_user LIKE '" + uid + "'", null);
        return cursor;
    }

    /**
     * Allows to change user master password into Database
     *
     * @param uid user id
     * @param newPassword new password hash
     * @param key new salt
     * @return true if update successful
     */
    public boolean updateUserMasterPassword(int uid, String newPassword, String key) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("password_hash", newPassword);
        contentValues.put("salt", key);
        database.update("user", contentValues, "id=" + uid, null);

        return checkIfUpdateSuccessful(uid, newPassword) != -1;
    }

    /**
     * Allows to check if password was changed successful
     *
     * @param uid user id
     * @param newPassword new password hash
     * @return Returns -1 if password was not find into Database or its id
     */
    private int checkIfUpdateSuccessful(int uid, String newPassword) {
        try (Cursor cursor = database.rawQuery("SELECT * FROM user WHERE id LIKE '" + uid + "' AND password_hash LIKE '" + newPassword + "'", null)) {
            cursor.moveToFirst();
            cursor.getCount();
            if (cursor.getCount() == 0) {
                return -1;
            } else return cursor.getInt(0);
        }
    }

    /**
     * Allows to change password param into Password object
     *
     * @param id password id
     * @param newPassword new password string
     */
    public void updatePassword(int id, String newPassword) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);
        database.update("password", contentValues, "id=" + id, null);
    }

    public int findLogByUserAndIp(int uid, String ip) {
        try (Cursor cursor = database.rawQuery("SELECT * FROM logs WHERE id_user LIKE '" + uid + "' AND ip LIKE '" + ip + "'", null)) {
            cursor.moveToFirst();
            cursor.getCount();
            if (cursor.getCount() == 0) {
                return -1;
            } else return cursor.getInt(0);
        }
    }

    public void insertIntoLogs(Log log) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_user", log.uid);
        contentValues.put("ip", log.ip);
        contentValues.put("time", log.time);
        contentValues.put("isLastSuccess", log.isLastSuccess);
        contentValues.put("failedAttempts", log.failedAttempts);
        database.insert("logs", null, contentValues);
    }

    public void updateLoginAttempts(int lid, String time, boolean isLastSuccess, int count) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", time);
        contentValues.put("isLastSuccess", isLastSuccess);
        contentValues.put("failedAttempts", count);
        database.update("logs", contentValues, "id=" + lid, null);
    }


    public int getLoginAttempts(int uid, String ip) {
        try (Cursor cursor = database.rawQuery("SELECT failedAttempts FROM logs WHERE id_user LIKE '" + uid + "' AND ip LIKE '" + ip + "'", null)) {
            cursor.moveToFirst();
            cursor.getCount();
            if (cursor.getCount() == 0) {
                return -1;
            } else {
                return cursor.getInt(0);
            }
        }
    }

    public Cursor getAllUserLogs(int uid) {
        Cursor cursor = database.rawQuery("SELECT id as _id, ip, time, isLastSuccess, failedAttempts FROM logs WHERE id_user LIKE '" + uid + "'", null);
        return cursor;
    }

    public void updateLoginAttemptsNumber(int lid, int count) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("failedAttempts", count);
        database.update("logs", contentValues, "id=" + lid, null);
    }
}
