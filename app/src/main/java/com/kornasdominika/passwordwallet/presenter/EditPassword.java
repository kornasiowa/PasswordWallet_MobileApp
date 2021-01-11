package com.kornasdominika.passwordwallet.presenter;

import android.content.Context;

import com.kornasdominika.passwordwallet.encryption.AES;
import com.kornasdominika.passwordwallet.model.DataChange;
import com.kornasdominika.passwordwallet.model.Function;
import com.kornasdominika.passwordwallet.model.HashDTO;
import com.kornasdominika.passwordwallet.model.Password;
import com.kornasdominika.passwordwallet.presenter.interfaces.IEditPassword;
import com.kornasdominika.passwordwallet.repository.DatabaseManager;
import com.kornasdominika.passwordwallet.ui.interfaces.IEditPasswordActivity;

import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditPassword implements IEditPassword {

    IEditPasswordActivity editPasswordActivity;

    private DatabaseManager databaseManager;

    private AES aes;
    private HashDTO hashDTO;

    public EditPassword(IEditPasswordActivity editPasswordActivity, Context context) {
        this.editPasswordActivity = editPasswordActivity;
        this.databaseManager = new DatabaseManager(context);
        this.aes = new AES();
    }

    @Override
    public String getCurrentPassword(int uid, String currentPassword) {
        databaseManager.open();
        hashDTO = databaseManager.getUserDataToAuthorization(uid);
        databaseManager.close();

        return getDecryptedPassword(currentPassword, hashDTO.getHash());
    }

    @Override
    public void updatePassword(Password oldPassword, Password newPassword) {
        newPassword.setPassword(getEncryptedPassword(newPassword.password, hashDTO.getHash()));

        databaseManager.open();
        int exists = databaseManager.checkIfPasswordWasInserted(newPassword.uid, newPassword.webAddress, newPassword.login, newPassword.password);
        databaseManager.close();

        if (exists == -1) {
            databaseManager.open();
            boolean result = databaseManager.updatePassword(newPassword);
            databaseManager.close();
            if (result) {
                registerDataChangeAfterEditAction(oldPassword, newPassword);
                editPasswordActivity.showMessageForUser("Password has been updated successfully");
                editPasswordActivity.finishActivity();
            } else {
                editPasswordActivity.makeButtonEnable();
                editPasswordActivity.showMessageForUser("Password updating error");
            }
        } else {
            editPasswordActivity.makeButtonEnable();
            editPasswordActivity.showMessageForUser("You already have this password in your wallet");
        }
    }

    private String getDecryptedPassword(String password, String masterPassword) {
        try {
            Key key = aes.generateKey(masterPassword);
            return AES.decrypt(password, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }

    private String getEncryptedPassword(String password, String masterPassword) {
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
        Function function = new Function(uid, time, "Edit password");

        databaseManager.open();
        int fid = databaseManager.insertIntoFunction(function);
        databaseManager.close();

        return fid;
    }

    private void registerDataChangeAfterEditAction(Password oldPassword, Password newPassword) {
        int fid = registerAction(newPassword.uid);

        String time = getCurrentDate();
        String previousData = getDecryptedPassword(oldPassword.password, hashDTO.getHash())
                + "|" + oldPassword.webAddress
                + "|" + oldPassword.description
                + "|" + oldPassword.login;
        String presentData = getDecryptedPassword(newPassword.password, hashDTO.getHash())
                + "|" + newPassword.webAddress
                + "|" + newPassword.description
                + "|" + newPassword.login;

        DataChange dataChange
                = new DataChange(newPassword.uid, newPassword.pid, fid, "edit", previousData, presentData, time);

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
