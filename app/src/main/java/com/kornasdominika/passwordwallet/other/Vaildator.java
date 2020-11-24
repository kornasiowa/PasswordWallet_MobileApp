package com.kornasdominika.passwordwallet.other;

public class Vaildator {

    /**
     * Rules for username: Must start with the alphabet
     * Only allow alphabet, dots and underscore
     * Minimum 2 chars are requires
     *
     * @param username
     * @return true if username consistent with regex
     */
    public boolean nameValidation(String username) {
        return username.matches("^([a-zA-Z])+([\\w.])+$");
    }

    public boolean ipValidation(String ip) {
        return ip.matches("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    }
}
