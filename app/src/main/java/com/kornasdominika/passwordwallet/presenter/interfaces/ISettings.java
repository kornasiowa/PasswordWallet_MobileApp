package com.kornasdominika.passwordwallet.presenter.interfaces;

import android.database.Cursor;

public interface ISettings {

    boolean changeUserMasterPassword(int uid, String oldPassword, String newPassword);

    Cursor getUserLogs(int uid);

    void resetLoginAttempts(int lid);
}
