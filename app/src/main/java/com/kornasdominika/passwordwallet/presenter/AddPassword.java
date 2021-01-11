package com.kornasdominika.passwordwallet.presenter;

import android.content.Context;

import com.kornasdominika.passwordwallet.encryption.AES;
import com.kornasdominika.passwordwallet.model.DataChange;
import com.kornasdominika.passwordwallet.model.Function;
import com.kornasdominika.passwordwallet.model.Password;
import com.kornasdominika.passwordwallet.presenter.interfaces.IAddPassword;
import com.kornasdominika.passwordwallet.repository.DatabaseManager;
import com.kornasdominika.passwordwallet.ui.interfaces.IAddPasswordActivity;

import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
     * Allows to add new Password object into Database
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
        String encryptedPassword = getEncryptedPassword(password, masterPassword);
        int exists = databaseManager.checkIfPasswordWasInserted(uid, webAddr, login, encryptedPassword);
        databaseManager.close();

        if(exists == -1){
            if (encryptedPassword.equals("Error")) {
                addPasswordActivity.makeButtonEnable();
                addPasswordActivity.showMessageForUser("Password adding error");
                return false;
            } else {
                databaseManager.open();
                int result = databaseManager.insertIntoPassword(
                        new Password(encryptedPassword, uid, webAddr, description, login, -2, -1));
                databaseManager.close();
                if (result != -1) {
                    registerDataChangeAfterAddAction(new Password(result, password, uid, webAddr, description, login, -2, -1));
                    addPasswordActivity.showMessageForUser("New password saved");
                    addPasswordActivity.finishActivity();
                    return true;
                } else {
                    addPasswordActivity.makeButtonEnable();
                    addPasswordActivity.showMessageForUser("Password adding error");
                    return false;
                }
            }
        } else {
            addPasswordActivity.makeButtonEnable();
            addPasswordActivity.showMessageForUser("You already have this password in your wallet");
            return false;
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

    private int registerAction(int uid) {
        String time = getCurrentDate();
        Function function = new Function(uid, time, "Add password");

        databaseManager.open();
        int fid = databaseManager.insertIntoFunction(function);
        databaseManager.close();

        return fid;
    }

    private void registerDataChangeAfterAddAction(Password newPassword) {
        int fid = registerAction(newPassword.uid);

        String time = getCurrentDate();
        String presentData = newPassword.password + "|" + newPassword.webAddress + "|"
                + newPassword.description + "|" + newPassword.login;

        DataChange dataChange
                = new DataChange(newPassword.uid, newPassword.pid, fid,"add", null, presentData, time);

        databaseManager.open();
        databaseManager.insertIntoDataChange(dataChange);
        databaseManager.close();
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
