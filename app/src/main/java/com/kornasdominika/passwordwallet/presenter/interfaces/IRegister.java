package com.kornasdominika.passwordwallet.presenter.interfaces;

public interface IRegister {

    int saveNewUserIntoDatabase(String login, String password, String password2, String algorithm);
}
