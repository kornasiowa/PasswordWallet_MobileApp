package com.kornasdominika.passwordwallet.presenter.interfaces;

import android.app.AlertDialog;
import android.database.Cursor;

import com.kornasdominika.passwordwallet.model.Password;

public interface IWallet {

    Cursor getUserPasswords(int uid);

    String showDecryptedPassword(int uid, String encryptedPassword);

    void sharePassword(String username, Password sharedPassword, AlertDialog alertDialog);

    void deletePassword(int id, String message, String message2);

    String getUserLogin(int uid);
}
