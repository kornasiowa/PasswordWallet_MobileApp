package com.kornasdominika.passwordwallet.presenter.interfaces;

import com.kornasdominika.passwordwallet.model.Password;

public interface IEditPassword {

    String getCurrentPassword(int uid, String currentPassword);

    void updatePassword(Password password);
}
