package com.kornasdominika.passwordwallet.presenter.interfaces;

public interface IAddPassword {

    boolean addNewPasswordToWallet(int uid, String password, String webAddr, String description,
                                   String login);
}
