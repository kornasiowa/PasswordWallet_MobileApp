package com.kornasdominika.passwordwallet.presenter;

import android.content.Context;
import android.database.Cursor;

import com.kornasdominika.passwordwallet.encryption.AES;
import com.kornasdominika.passwordwallet.model.DataChange;
import com.kornasdominika.passwordwallet.model.Function;
import com.kornasdominika.passwordwallet.model.Password;
import com.kornasdominika.passwordwallet.presenter.interfaces.IBackup;
import com.kornasdominika.passwordwallet.repository.DatabaseManager;
import com.kornasdominika.passwordwallet.ui.BackupActivity;
import com.kornasdominika.passwordwallet.ui.interfaces.IBackupActivity;

import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Backup implements IBackup {

    private IBackupActivity backupActivity;

    private DatabaseManager databaseManager;

    private DataChange dataChange;

    private AES aes;

    public Backup(IBackupActivity backupActivity, Context context) {
        this.backupActivity = backupActivity;
        this.databaseManager = new DatabaseManager(context);
        this.aes = new AES();
    }

    @Override
    public Cursor getAllUserFunctionsStatistics(int uid) {
        databaseManager.open();
        return databaseManager.getAllUserStatistics(uid);
    }

    @Override
    public Cursor getAllUserFunctionsStatistics(int uid, String type) {
        databaseManager.open();
        return databaseManager.getAllUserStatistics(uid, type);
    }

    @Override
    public DataChange getDataChangeToRestore(int fid) {
        databaseManager.open();
        dataChange = databaseManager.findDataChangeRecord(fid);
        databaseManager.close();

        return dataChange;
    }

    @Override
    public String showDecryptedPassword(int uid, String encryptedPassword) {
        databaseManager.open();
        String masterPassword = databaseManager.getUserHash(uid);
        databaseManager.close();

        return getDecryptedPassword(encryptedPassword, masterPassword);
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

    @Override
    public void restoreSelectedRecord(String action) {
        if (action.equals("Add password")) {
            deletePassword(BackupActivity.SELECTED_ACTION);
        } else if (action.equals("Delete password")) {
            addPassword(BackupActivity.SELECTED_ACTION);
        } else {
            editPassword(BackupActivity.SELECTED_ACTION);
        }
    }

    private void deletePassword(String action) {
        databaseManager.open();
        boolean result = databaseManager.deletePassword(dataChange.rid);
        databaseManager.close();
        if (result) {
            registerDataChangeAfterDeleteAction();
            backupActivity.showMessage("The record was successfully restored");
            backupActivity.setListAdapter(action);
        } else {
            backupActivity.showMessage("An error occurred while restoring the record");
        }
    }

    private void addPassword(String action) {
        String[] passwordValues = dataChange.previousValue.split("\\|");

        databaseManager.open();
        int exists = databaseManager.checkIfPasswordWasInserted(dataChange.uid, passwordValues[1], passwordValues[3], passwordValues[0]);
        databaseManager.close();

        if (exists == -1) {
            databaseManager.open();
            int result = databaseManager.insertIntoPassword(
                    new Password(passwordValues[0], dataChange.uid, passwordValues[1], passwordValues[2], passwordValues[3], -2, -1));
            databaseManager.close();
            if (result != -1) {
                registerDataChangeAfterAddAction();
                backupActivity.showMessage("The record was successfully restored");
                backupActivity.setListAdapter(action);
            } else {
                backupActivity.showMessage("An error occurred while restoring the record");
            }
        } else {
            backupActivity.showMessage("You already have this password in your wallet");
        }
    }

    private void editPassword(String action) {
        String[] passwordValues = dataChange.previousValue.split("\\|");

        databaseManager.open();
        int exists = databaseManager.checkIfPasswordWasInserted(dataChange.uid, passwordValues[1], passwordValues[3], passwordValues[0]);
        Password currentData = databaseManager.getPasswordByPid(dataChange.rid);
        databaseManager.close();

        if (currentData != null) {
            if (exists == -1) {
                databaseManager.open();
                boolean result = databaseManager.updatePassword(
                        new Password(dataChange.rid, passwordValues[0], dataChange.uid, passwordValues[1],
                                passwordValues[2], passwordValues[3], -2, -1));
                databaseManager.close();
                if (result) {
                    registerDataChangeAfterEditAction(currentData);
                    backupActivity.showMessage("The record was successfully restored");
                    backupActivity.setListAdapter(action);
                } else {
                    backupActivity.showMessage("An error occurred while restoring the record");
                }
            } else {
                backupActivity.showMessage("You already have this password in your wallet");
            }
        } else {
            backupActivity.showMessage("You cannot perform this action. Password has been removed");
        }
    }

    private int registerAction(int uid, String action) {
        String time = getCurrentDate();
        Function function = new Function(uid, time, action);

        databaseManager.open();
        int fid = databaseManager.insertIntoFunction(function);
        databaseManager.close();

        return fid;
    }

    private void registerDataChangeAfterDeleteAction() {
        int fid = registerAction(dataChange.uid, "Delete password");

        String time = getCurrentDate();

        DataChange newDataChange = new DataChange(dataChange.uid, dataChange.rid, fid,
                "delete", dataChange.presentValue, null, time, 0);

        databaseManager.open();
        databaseManager.insertIntoDataChange(newDataChange);
        databaseManager.updateIfRecordIsRestored(dataChange.cid);
        databaseManager.close();
    }

    private void registerDataChangeAfterAddAction() {
        int fid = registerAction(dataChange.uid, "Add password");

        String time = getCurrentDate();

        DataChange newDataChange = new DataChange(dataChange.uid, dataChange.rid, fid,
                "add", null, dataChange.previousValue, time, 0);

        databaseManager.open();
        databaseManager.insertIntoDataChange(newDataChange);
        databaseManager.updateIfRecordIsRestored(dataChange.cid);
        databaseManager.close();
    }

    private void registerDataChangeAfterEditAction(Password currentPassword) {
        int fid = registerAction(dataChange.uid, "Edit password");

        String time = getCurrentDate();
        String previousData = currentPassword.password
                + "|" + currentPassword.webAddress
                + "|" + currentPassword.description
                + "|" + currentPassword.login;

        DataChange newDataChange
                = new DataChange(dataChange.uid, dataChange.rid, fid, "edit", previousData, dataChange.previousValue, time, 0);

        databaseManager.open();
        databaseManager.insertIntoDataChange(newDataChange);
        databaseManager.updateIfRecordIsRestored(dataChange.cid);
        databaseManager.close();
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
