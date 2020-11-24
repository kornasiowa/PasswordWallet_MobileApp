package com.kornasdominika.passwordwallet.presenter;

import android.content.Context;

import com.kornasdominika.passwordwallet.encryption.EncryptionKey;
import com.kornasdominika.passwordwallet.encryption.HMAC;
import com.kornasdominika.passwordwallet.encryption.SHA512;
import com.kornasdominika.passwordwallet.model.User;
import com.kornasdominika.passwordwallet.other.Vaildator;
import com.kornasdominika.passwordwallet.presenter.interfaces.IRegister;
import com.kornasdominika.passwordwallet.repository.DatabaseManager;
import com.kornasdominika.passwordwallet.ui.interfaces.IRegisterActivity;

import static android.widget.Toast.makeText;

public class Register implements IRegister {

    private IRegisterActivity registerActivity;

    private DatabaseManager databaseManager;

    Vaildator vaildator;

    public Register(IRegisterActivity iRegisterActivity, Context context) {
        this.registerActivity = iRegisterActivity;
        this.databaseManager = new DatabaseManager(context);
        vaildator = new Vaildator();
    }

    public Register(Vaildator vaildator) {
        this.vaildator = vaildator;
    }

    /**
     * Check if User with entered login exist into Database
     * If not new User is registered and saved into Databse
     *
     * @param login     User login
     * @param password  User password
     * @param algorithm selected algorithm
     * @return Returns 1 if user was registered successful
     * or 0 if registration failed
     * or -1 if user with same login already exists
     */
    @Override
    public int saveNewUserIntoDatabase(String login, String password, String password2, String algorithm) {
        int result;
        if (!registerValidation(login, password, password2, algorithm)) {
            result = 0;
        } else {
            databaseManager.open();
            result = databaseManager.insertIntoUser(generateUserDataToSave(login, password, algorithm));
            databaseManager.close();
        }

        if (result == 1) {
            registerActivity.showMessageForUser("User registered successfully! You can log in now");
            registerActivity.startLoginActivity();
            return 1;
        } else if (result == 0) {
            registerActivity.showMessageForUser("Registration failed");
            return 0;
        } else {
            registerActivity.showMessageForUser("The given login is already in use");
            return -1;
        }
    }

    /**
     * Validate User data input into registration form
     *
     * @param login      User login
     * @param password   User password
     * @param password2  User password repeat
     * @param encryption selected algorithm
     * @return false if any data incorrect
     */
    public boolean registerValidation(String login, String password, String password2, String encryption) {
        if (!vaildator.nameValidation(login)) {
            return false;
        } else if (!(login.length() > 0)) {
            return false;
        } else if (!(password.length() > 0)) {
            return false;
        } else if (password.length() < 8) {
            return false;
        } else if (!password.equals(password2)) {
            return false;
        } else if (encryption == null) {
            return false;
        } else
            return true;
    }

    /**
     * Allows to generate User hash and encryption key to save into Database
     *
     * @param login     User login
     * @param password  entered password
     * @param algorithm selected algorithm
     * @return User object ready to save into Database
     */
    private User generateUserDataToSave(String login, String password, String algorithm) {
        String hash,
                salt;
        boolean isHash;

        if (algorithm.equals("SHA512")) {
            salt = EncryptionKey.generateKey(16);
            hash = SHA512.calculateSHA512(SHA512.pepper + salt + password);
            isHash = true;
        } else {
            salt = EncryptionKey.generateKey(16);
            hash = HMAC.calculateHMAC(password, salt);
            isHash = false;
        }
        return new User(login, hash, salt, isHash);
    }
}
