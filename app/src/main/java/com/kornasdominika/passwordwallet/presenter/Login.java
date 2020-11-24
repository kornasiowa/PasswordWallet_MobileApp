package com.kornasdominika.passwordwallet.presenter;

import android.content.Context;
import android.os.Handler;

import com.kornasdominika.passwordwallet.encryption.HMAC;
import com.kornasdominika.passwordwallet.encryption.SHA512;
import com.kornasdominika.passwordwallet.model.HashDTO;
import com.kornasdominika.passwordwallet.model.Log;
import com.kornasdominika.passwordwallet.presenter.interfaces.ILogin;
import com.kornasdominika.passwordwallet.repository.DatabaseManager;
import com.kornasdominika.passwordwallet.ui.MainActivity;
import com.kornasdominika.passwordwallet.ui.interfaces.ILoginActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Login implements ILogin {

    private ILoginActivity loginActivity;

    private DatabaseManager databaseManager;

    private int numberOfLoginAttempts;
    private boolean loginPermission = true;

    public Login(ILoginActivity iLoginActivity, Context context) {
        this.loginActivity = iLoginActivity;
        this.databaseManager = new DatabaseManager(context);
    }

    public Login() {
    }

    /**
     * Checks whether the user has blocked the login option by entering the wrong password 2 or more times.
     * @param ip user ip address simulation
     * @param login username
     * @param password user password
     */
    @Override
    public void loginIntoWallet(String ip, String login, String password) {
        int user = getUser(login);

        if (user != -1) {
            if (loginPermission) {
                authorizeUser(user, ip, password);
            }
        } else {
            loginActivity.showMessageForUser("Login failed");
        }

    }

    /**
     * Unlocks the ability to log
     */
    @Override
    public void refreshIP() {
        loginPermission = true;
        loginActivity.enableButton();
    }

    /**
     * Gets current user id by login
     * @param login user login
     * @return user id
     */
    private int getUser(String login) {
        databaseManager.open();
        int uid = databaseManager.findUserByLogin(login);
        databaseManager.close();
        return uid;
    }

    /**
     * Checks whether the user has the ability to log in and then performs authorization
     * @param user user id
     * @param ip user ip address simulation
     * @param password user password
     */
    private void authorizeUser(int user, String ip, String password) {
        if(getNumberOfLoginAttempts(user, ip) >= 4){
            loginPermission = false;
            loginActivity.showMessageForUser("Your IP address is permanently blocked");
        } else {
            databaseManager.open();
            HashDTO hashDTO = databaseManager.getUserDataToAuthorization(user);
            databaseManager.close();
            if (hashDTO != null) {
                String inputHash = getInputPasswordHash(password, hashDTO.getKey(), hashDTO.isHash());
                String databaseHash = hashDTO.getHash();
                if (databaseHash.equals(inputHash)) {
                    loginActivity.startWalletActivity(user);
                    putIntoLogsWhenSuccess(user, ip);
                } else {
                    blockLoginShowingMessage(user, ip);
                    putIntoLogsWhenFailed(user, ip);
                }
            }
        }
    }

    /**
     * Allows to get a hash of the entered password in the login form
     * @param password  password string from login form
     * @param key       User key from Database
     * @param algorithm User algorithm from Database
     * @return input password hash
     */
    private String getInputPasswordHash(String password, String key, int algorithm) {
        if (algorithm == 1) {
            return SHA512.calculateSHA512(SHA512.pepper + key + password);
        } else
            return HMAC.calculateHMAC(password, key);
    }

    /**
     * Gets current date and time in specific format
     * @return current date
     */
    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Gets the number of invalid login attempts of the user
     * @param user user id
     * @param ip user ip address simulation
     * @return number of invalid login attempts
     */
    private int getNumberOfLoginAttempts(int user, String ip) {
        databaseManager.open();
        int trials = databaseManager.getLoginAttempts(user, ip);
        databaseManager.close();
        return trials;
    }

    /**
     * Blocks login option depending on the number of incorrect login attempts
     * @param numberOfLoginAttempts number of invalid login attempts
     * @return -4 if user permanently blocked ip address,
     * -2 if user blocked ip address for 10 sec,
     * -3 if user blocked ip address for 20 sec,
     * -1 if user did not blocked ip address yet
     */
    public int blockLoginChangingFlag(int numberOfLoginAttempts) {
        loginPermission = true;

        if (numberOfLoginAttempts >= 3) {
            loginPermission = false;
            loginActivity.disableButton();
            return -4;
        } else {
            if (numberOfLoginAttempts == 1) {
                loginPermission = false;
                loginActivity.disableButton();
                new Handler().postDelayed(() -> {
                    loginPermission = true;
                    loginActivity.enableButton();
                }, 5000);
                return -2;
            } else if (numberOfLoginAttempts == 2) {
                loginPermission = false;
                loginActivity.disableButton();
                new Handler().postDelayed(() -> {
                    loginPermission = true;
                    loginActivity.enableButton();
                }, 10000);
                return -3;
            } else
                return -1;
        }
    }

    /**
     * Shows message for user
     * @param user user id
     * @param ip user ip address simulation
     * @return false when login blocked
     * true when login unblocked
     */
    private boolean blockLoginShowingMessage(int user, String ip) {
        numberOfLoginAttempts = getNumberOfLoginAttempts(user, ip);
        int blockType = blockLoginChangingFlag(numberOfLoginAttempts);
        if (blockType == -1) {
            loginActivity.showMessageForUser("Login failed");
            return false;
        } else if (blockType == -2) {
            loginActivity.showMessageForUser("You have 2 failed login attempts. You must wait 5 seconds to try again");
            return false;
        } else if (blockType == -3) {
            loginActivity.showMessageForUser("You have 3 failed login attempts. You must wait 10 seconds to try again");
            return false;
        } else if (blockType == -4) {
            loginActivity.showMessageForUser("You have at least 4 failed login attempts. Your IP address is permanently blocked");
            return false;
        }
        return true;
    }

    /**
     * Insert into database information about login attempt when authorization was successful
     * @param user user id
     * @param ip user ip address simulation
     */
    private void putIntoLogsWhenSuccess(int user, String ip) {
        databaseManager.open();
        int result = databaseManager.findLogByUserAndIp(user, ip);

        if (result != -1) {
            databaseManager.updateLoginAttempts(result, getCurrentDate(), true, 0);
        } else {
            databaseManager.insertIntoLogs(new Log(user, ip, getCurrentDate(), true, 0));
        }
        databaseManager.close();
    }

    /**
     * Insert into database information about login attempt when authorization was failed
     * @param user user id
     * @param ip user ip address simulation
     */
    private void putIntoLogsWhenFailed(int user, String ip) {
        databaseManager.open();
        int result = databaseManager.findLogByUserAndIp(user, ip);

        if (result != -1) {
            databaseManager.updateLoginAttempts(result, getCurrentDate(), false, numberOfLoginAttempts + 1);
        } else {
            databaseManager.insertIntoLogs(new Log(user, ip, getCurrentDate(), false, 1));
        }
        databaseManager.close();
    }

}
