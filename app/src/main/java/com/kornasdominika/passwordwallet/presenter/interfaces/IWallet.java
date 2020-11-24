package com.kornasdominika.passwordwallet.presenter.interfaces;

import android.database.Cursor;

public interface IWallet {

    Cursor getUserPasswords(int uid);

    String showDecryptedPassword(int uid, String encryptedPassword);
}
