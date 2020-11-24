package com.kornasdominika.passwordwallet.presenter;

import android.content.Context;

import com.kornasdominika.passwordwallet.encryption.AES;
import com.kornasdominika.passwordwallet.model.Password;
import com.kornasdominika.passwordwallet.presenter.interfaces.IAddPassword;
import com.kornasdominika.passwordwallet.repository.DatabaseManager;
import com.kornasdominika.passwordwallet.ui.interfaces.IAddPasswordActivity;

import java.security.Key;

public class AddPassword implements IAddPassword {

    private IAddPasswordActivity addPasswordActivity;

    private DatabaseManager databaseManager;

    private AES aes;

    public AddPassword(IAddPasswordActivity iAddPassword, Context context) {
        this.addPasswordActivity = iAddPassword;
        this.databaseManager = new DatabaseManager(context);
        aes = new AES();
    }

    /**
     * Allows to add new Password object into Databse
     *
     * @param uid         user id
     * @param password    password to be encrypted
     * @param webAddr
     * @param description
     * @param login
     * @return Returns true if Password save into Database successful
     */
    @Override
    public boolean addNewPasswordToWallet(int uid, String password, String webAddr, String description,
                                          String login) {
        databaseManager.open();
        String masterPassword = databaseManager.getUserHash(uid);
        databaseManager.close();
        String encryptedPassword = getEncryptedPassword(password, masterPassword);

        if (encryptedPassword.equals("Error")) {
            addPasswordActivity.showMessageForUser("Password adding error");
            return false;
        } else {
            databaseManager.open();
            boolean result = databaseManager.insertIntoPassword(
                    new Password(encryptedPassword, uid, webAddr, description, login));
            databaseManager.close();
            if (result) {
                addPasswordActivity.showMessageForUser("New password saved");
                addPasswordActivity.finishActivity();
                return true;
            } else {
                addPasswordActivity.showMessageForUser("Password adding error");
                return false;
            }
        }
    }

    /**
     * Allows to get encrypted password with AES algorithm
     *
     * @param password       to be encrypted
     * @param masterPassword User master password to be encryption key
     * @return encrypted password string
     */
    public String getEncryptedPassword(String password, String masterPassword) {
        try {
            Key key = aes.generateKey(masterPassword);
            return AES.encrypt(password, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }

}
