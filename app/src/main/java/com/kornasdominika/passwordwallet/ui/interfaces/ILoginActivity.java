package com.kornasdominika.passwordwallet.ui.interfaces;

public interface ILoginActivity {

    void showMessageForUser(String message);

    void startWalletActivity(int user);

    void disableButton();

    void enableButton();
}
