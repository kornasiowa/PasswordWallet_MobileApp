package com.kornasdominika.passwordwallet;

import com.kornasdominika.passwordwallet.other.Vaildator;
import com.kornasdominika.passwordwallet.presenter.Register;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(Parameterized.class)
public class RegisterFormCorrectDataTest {

    private String login,
            password,
            password2,
            algorithm;


    public RegisterFormCorrectDataTest(String login, String password, String password2, String algorithm) {
        this.login = login;
        this.password = password;
        this.password2 = password2;
        this.algorithm = algorithm;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {"test", "test1234", "test1234", "SHA512"},
                {"Jan.Kowalski", "qwerty123", "qwerty123", "HMAC"}};

        return Arrays.asList(data);
    }

    @Test
    public void testRegisterValidation_True_CorrectDataGiven() {
        Register instance = new Register(new Vaildator() {
            public boolean nameValidation(String username) {
                return true;
            }
        });

        boolean result = instance.registerValidation(login, password, password2, algorithm);
        String res = (result) ? "valid" : "invalid";
        System.out.println("Login: " + login + " \nPassword: " + password + " \nRepeat password: " + password2 + " \nAlgorithm: " + algorithm);
        System.out.println(" Data is " + res + "\n");
        assertTrue("Result", result);
    }

}