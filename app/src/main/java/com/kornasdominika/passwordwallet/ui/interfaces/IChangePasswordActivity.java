package com.kornasdominika.passwordwallet.ui.interfaces;

public interface IChangePasswordActivity {

    void showMessageForUser(String message);

    void finishActivity();

    int getLoggedUserId();
}
