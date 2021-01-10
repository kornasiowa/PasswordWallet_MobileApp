package com.kornasdominika.passwordwallet.presenter;

import android.content.Context;

import com.kornasdominika.passwordwallet.encryption.AES;
import com.kornasdominika.passwordwallet.model.HashDTO;
import com.kornasdominika.passwordwallet.model.Password;
import com.kornasdominika.passwordwallet.presenter.interfaces.IEditPassword;
import com.kornasdominika.passwordwallet.repository.DatabaseManager;
import com.kornasdominika.passwordwallet.ui.interfaces.IEditPasswordActivity;

import java.security.Key;

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
    public void updatePassword(Password newPassword) {
        newPassword.setPassword(getEncryptedPassword(newPassword.password, hashDTO.getHash()));

        databaseManager.open();
        boolean result = databaseManager.updatePassword(newPassword);
        databaseManager.close();
        if (result) {
            editPasswordActivity.showMessageForUser("Password has been updated successfully");
            editPasswordActivity.finishActivity();
        } else {
            editPasswordActivity.showMessageForUser("Password updating error");
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
}
