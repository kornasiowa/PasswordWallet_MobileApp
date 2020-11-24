package com.kornasdominika.passwordwallet;

import com.kornasdominika.passwordwallet.presenter.Login;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class LoginAttemptTest {

    private Login login;

    private int arg;
    private int expectedValidation;

    public LoginAttemptTest(int arg, int expectedValidation) {
        this.arg = arg;
        this.expectedValidation = expectedValidation;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {0, -1},
                {1, -2},
                {2, -3},
                {3, -4},
                {4, -4},
                {5, -4}};

        return Arrays.asList(data);
    }

    @Before
    public void init(){
        login = new Login();
    }

    @Test
    public void testBlockLoginChangingFlag_IfCorrect_NumberOfLoginAttemptsGiven() {
        int result = login.blockLoginChangingFlag(this.arg);
        assertEquals("Result", this.expectedValidation, result);
    }

}