package com.kornasdominika.passwordwallet;

import com.kornasdominika.passwordwallet.other.ValidatorAdapter;
import com.kornasdominika.passwordwallet.presenter.Register;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;


@RunWith(Parameterized.class)
public class RegisterFormIncorrectPasswordTest {

    private String login,
            password,
            password2,
            algorithm;


    public RegisterFormIncorrectPasswordTest(String login, String password, String password2, String algorithm) {
        this.login = login;
        this.password = password;
        this.password2 = password2;
        this.algorithm = algorithm;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {"test", "test123", "test123", "SHA512"},
                {"test", "test", "test", "SHA512"},
                {"test", "test1234", "", "SHA512"},
                {"test", "test1234", "test", "SHA512"},
                {"test", "test123", "test", "SHA512"}};

        return Arrays.asList(data);
    }

    @Test
    public void testRegisterValidation_False_IncorrectPasswordsGiven() {
        Register instance = new Register(new ValidatorAdapter());

        boolean result = instance.registerValidation(login, password, password2, algorithm);
        String res = (result) ? "valid" : "invalid";
        System.out.println("Password: " + password + " \nRepeat password: " + password2);
        System.out.println(" Passwords are " + res + "\n");
        assertFalse("Result", result);
    }
}
