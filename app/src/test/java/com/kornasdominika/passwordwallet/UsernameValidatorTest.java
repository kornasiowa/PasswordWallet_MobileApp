package com.kornasdominika.passwordwallet;

import com.kornasdominika.passwordwallet.other.Vaildator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class UsernameValidatorTest {

    private Vaildator vaildator;

    private String arg;
    private Boolean expectedValidation;

    public UsernameValidatorTest(String arg, Boolean expectedValidation) {
        this.arg = arg;
        this.expectedValidation = expectedValidation;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {"k", false},  // it's too short
                {"1an kowalski", false},  // it's not allowed to begin name with number
                {"J@n kowalski", false},  // it's not allowed to have special character other than ._
                {".jan kowalski", false}, // it's not allowed to begin with special character
                {"Jan Kowalsky", false},  // it's not allowed to have space

                {"jan_kowalsk1", true},
                {"Jan.Kowalski", true},
                {"J_Kowalski_Nowak", true},
                {"Jan", true}};

        return Arrays.asList(data);
    }

    @Before
    public void init(){
        vaildator = new Vaildator();
    }

    @Test
    public void testUsernameValidation_IfCorrect_UsernameGiven() {
        Boolean result = vaildator.nameValidation(this.arg);
        String res = (result) ? "valid" : "invalid";
        System.out.println("Username " + arg + " is " + res);
        assertEquals("Result", this.expectedValidation, result);
    }

}