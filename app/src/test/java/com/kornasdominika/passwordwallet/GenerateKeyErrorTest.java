package com.kornasdominika.passwordwallet;

import com.kornasdominika.passwordwallet.encryption.AES;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@RunWith(Parameterized.class)
public class GenerateKeyErrorTest {

    private AES aes;

    private String masterPassword;

    public GenerateKeyErrorTest(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {"test"},
                {""},
                {"123456781234567"}};

        return Arrays.asList(data);
    }

    @Before
    public void init(){
        aes = new AES();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateKey_ExpectedError_TooShortPassword() throws Exception {
        aes.generateKey(masterPassword);
    }

}
