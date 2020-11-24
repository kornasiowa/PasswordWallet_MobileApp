package com.kornasdominika.passwordwallet.presenter.interfaces;

public interface ILogin {

    void loginIntoWallet(String ip, String login, String password);

    void refreshIP();
}
