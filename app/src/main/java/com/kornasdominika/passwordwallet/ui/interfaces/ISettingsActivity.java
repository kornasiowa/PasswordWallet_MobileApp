package com.kornasdominika.passwordwallet.ui.interfaces;

public interface ISettingsActivity {

    void showMessageForUser(String message);

    void finishActivity();

    int getLoggedUserId();
}
