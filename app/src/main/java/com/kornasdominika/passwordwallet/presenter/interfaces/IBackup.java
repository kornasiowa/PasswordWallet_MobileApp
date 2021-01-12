package com.kornasdominika.passwordwallet.presenter.interfaces;

import android.database.Cursor;

import com.kornasdominika.passwordwallet.model.DataChange;

public interface IBackup {

    Cursor getAllUserFunctionsStatistics(int uid);

    Cursor getAllUserFunctionsStatistics(int uid, String type);

    DataChange getDataChangeToRestore(int fid);

    String showDecryptedPassword(int uid, String encryptedPassword);

    void restoreSelectedRecord(String action);
}
