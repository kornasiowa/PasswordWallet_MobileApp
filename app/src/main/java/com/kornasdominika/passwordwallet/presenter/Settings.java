package com.kornasdominika.passwordwallet.presenter;

import android.content.Context;
import android.database.Cursor;

import com.kornasdominika.passwordwallet.encryption.AES;
import com.kornasdominika.passwordwallet.encryption.EncryptionKey;
import com.kornasdominika.passwordwallet.encryption.HMAC;
import com.kornasdominika.passwordwallet.encryption.SHA512;
import com.kornasdominika.passwordwallet.model.HashDTO;
import com.kornasdominika.passwordwallet.presenter.interfaces.ISettings;
import com.kornasdominika.passwordwallet.repository.DatabaseManager;
import com.kornasdominika.passwordwallet.ui.interfaces.ISettingsActivity;

import java.security.Key;

public class Settings implements ISettings {

    private ISettingsActivity settingsActivity;

    private DatabaseManager databaseManager;

    private AES aes;

    private HashDTO hashDTO;

    private String oldHash;

    public Settings(ISettingsActivity settingsActivity, Context context) {
        this.settingsActivity = settingsActivity;
        this.databaseManager = new DatabaseManager(context);
        this.aes = new AES();
    }

    /**
     * Allows to change user master password
     * Call authorization method
     * Call change password methods
     *
     * @param uid         user id
     * @param oldPassword user current password
     * @param newPassword user new password
     * @return true if operation was successful
     */
    @Override
    public boolean changeUserMasterPassword(int uid, String oldPassword, String newPassword) {
        databaseManager.open();
        if (currentPasswordUserAuthorization(uid, oldPassword)) {
            if (saveNewMasterPassword(uid, newPassword)) {
                settingsActivity.showMessageForUser("New password saved");
                databaseManager.close();
                settingsActivity.finishActivity();
                return true;
            } else {
                settingsActivity.showMessageForUser("Update password error");
                databaseManager.close();
                return false;
            }
        }
        return false;
    }

    /**
     * Check if current password entered into change password form is correct user password
     *
     * @param uid         user id
     * @param oldPassword current password entered into change password form
     * @return true if authorization was successful
     */
    private boolean currentPasswordUserAuthorization(int uid, String oldPassword) {
        hashDTO = databaseManager.getUserDataToAuthorization(uid);
        if (hashDTO != null) {
            String inputHash = getPasswordHash(oldPassword, hashDTO.getKey(), hashDTO.isHash());
            oldHash = hashDTO.getHash();
            if (oldHash.equals(inputHash)) {
                return true;
            } else {
                settingsActivity.showMessageForUser("Current password incorrect");
                return false;
            }
        }
        return false;
    }

    /**
     * Allows to get a hash of the entered password in the change password form
     *
     * @param password  password string from change password form
     * @param key       User key
     * @param algorithm User algorithm from Database
     * @return input password hash
     */
    private String getPasswordHash(String password, String key, int algorithm) {
        if (algorithm == 1) {
            return SHA512.calculateSHA512(SHA512.pepper + key + password);
        } else
            return HMAC.calculateHMAC(password, key);
    }

    /**
     * Generate new user master password hash and key and save it into Database
     * Call method that update all user passwords with new encryption key
     *
     * @param uid         user id
     * @param newPassword new password to be hash
     * @return true if update was successful
     */
    private boolean saveNewMasterPassword(int uid, String newPassword) {
        String newKey = EncryptionKey.generateKey(16);
        String newHash = getPasswordHash(newPassword, newKey, hashDTO.isHash());

        if (databaseManager.updateUserMasterPassword(uid, newHash, newKey)) {
            changeAllUserPasswords(uid, newHash);
            return true;
        }
        return false;
    }

    /**
     * Update all user passwords into Database
     * Decrypt password with old master password hash and encrypt it with new master password hash
     *
     * @param uid     user id
     * @param newHash new master password hash
     */
    private void changeAllUserPasswords(int uid, String newHash) {
        Cursor cursor = databaseManager.getAllUserPasswords(uid);
        cursor.moveToPosition(-1);
        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String password = cursor.getString(4);
                String decrypted = getDecryptedPassword(password, oldHash);
                String newEncrypted = getEncryptedPassword(decrypted, newHash);
                databaseManager.updatePassword(id, newEncrypted);
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * Allows to get decrypted password with AES algorithm
     *
     * @param password       to be decrypted
     * @param masterPassword User master password to be decryption key
     * @return decrypted password string
     */
    private String getDecryptedPassword(String password, String masterPassword) {
        try {
            Key key = aes.generateKey(masterPassword);
            return AES.decrypt(password, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }

    /**
     * Allows to get encrypted password with AES algorithm
     *
     * @param password       to be encrypted
     * @param masterPassword User master password to be encryption key
     * @return encrypted password string
     */
    private String getEncryptedPassword(String password, String masterPassword) {
        try {
            Key key = aes.generateKey(masterPassword);
            return AES.encrypt(password, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }

    @Override
    public Cursor getUserLogs(int uid) {
        databaseManager.open();
        return databaseManager.getAllUserLogs(uid);
    }

    @Override
    public void resetLoginAttempts(int lid) {
        databaseManager.updateLoginAttemptsNumber(lid, 0);
    }
}
