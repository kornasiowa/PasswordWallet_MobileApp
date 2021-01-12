package com.kornasdominika.passwordwallet.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;

import com.kornasdominika.passwordwallet.encryption.AES;
import com.kornasdominika.passwordwallet.model.DataChange;
import com.kornasdominika.passwordwallet.model.Function;
import com.kornasdominika.passwordwallet.model.Password;
import com.kornasdominika.passwordwallet.presenter.interfaces.IWallet;
import com.kornasdominika.passwordwallet.repository.DatabaseManager;
import com.kornasdominika.passwordwallet.ui.interfaces.IWalletActivity;

import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Wallet implements IWallet {

    private IWalletActivity walletActivity;

    private DatabaseManager databaseManager;

    private AES aes;

    public Wallet(IWalletActivity iWalletActivity, Context context) {
        this.walletActivity = iWalletActivity;
        this.databaseManager = new DatabaseManager(context);
        this.aes = new AES();
    }

    /**
     * Allows to get all logged user passwords saved into Database
     *
     * @param uid user id
     * @return cursor contains user passwords
     */
    @Override
    public Cursor getUserPasswords(int uid) {
        databaseManager.open();
        return databaseManager.getAllUserPasswords(uid);
    }

    /**
     * Gets user master password from Database and uses it to decrypt selected password
     *
     * @param uid               user id
     * @param encryptedPassword encrypted password to be decrypted
     * @return decrypted password
     */
    @Override
    public String showDecryptedPassword(int uid, String encryptedPassword) {
        databaseManager.open();
        String masterPassword = databaseManager.getUserHash(uid);
        databaseManager.close();
        registerAction(uid, "View password");
        return getDecryptedPassword(encryptedPassword, masterPassword);
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

    @Override
    public String getUserLogin(int uid) {
        databaseManager.open();
        String login = databaseManager.getUserLoginById(uid);
        databaseManager.close();

        return login;
    }

    @Override
    public void stopPreviewingPassword(int id) {
        databaseManager.open();
        boolean result = databaseManager.deletePassword(id);
        databaseManager.close();
        if (result) {
            walletActivity.showMessage("You lost the preview of this password");
            walletActivity.setListAdapter();
        } else {
            walletActivity.showMessage("Error");
        }
    }

    @Override
    public void deletePassword(int id, Password deletedPassword) {
        databaseManager.open();
        boolean result = databaseManager.deletePassword(id);
        databaseManager.close();
        if (result) {
            registerDataChangeAfterDeleteAction(deletedPassword);
            walletActivity.showMessage("Password has been deleted successfully");
            walletActivity.setListAdapter();
        } else {
            walletActivity.showMessage("Password deleting error");
        }
    }

    @Override
    public void sharePassword(String username, Password sharedPassword, AlertDialog alertDialog) {
        int recipient = checkIfGivenUserExists(username);
        if (recipient == -1) {
            walletActivity.showMessage("The user with the given name does not exist");
            return;
        } else if (sharedPassword.owner == recipient) {
            walletActivity.showMessage("You cannot share the password with yourself");
            return;
        }

        sharedPassword.setUid(recipient);

        databaseManager.open();
        int result = databaseManager.insertIntoPassword(sharedPassword);
        databaseManager.close();
        if (result != -1) {
            registerAction(sharedPassword.owner, "Share password");
            walletActivity.showMessage("Password has been shared successfully");
        } else {
            walletActivity.showMessage("Password sharing error");
        }
        alertDialog.dismiss();
    }

    private int checkIfGivenUserExists(String username) {
        databaseManager.open();
        int user = databaseManager.findUserByLogin(username);
        databaseManager.close();

        return user;
    }

    private int registerAction(int uid, String action) {
        String time = getCurrentDate();
        Function function = new Function(uid, time, action);

        databaseManager.open();
        int fid = databaseManager.insertIntoFunction(function);
        databaseManager.close();

        return fid;
    }

    private void registerDataChangeAfterDeleteAction(Password deletedPassword) {
        int fid = registerAction(deletedPassword.uid, "Delete password");

        String time = getCurrentDate();
        String previousData = deletedPassword.password + "|" + deletedPassword.webAddress + "|"
                + deletedPassword.description + "|" + deletedPassword.login;

        DataChange dataChange
                = new DataChange(deletedPassword.uid, deletedPassword.pid, fid, "delete", previousData, null, time, 0);

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
