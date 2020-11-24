package com.kornasdominika.passwordwallet;

import com.kornasdominika.passwordwallet.encryption.AES;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;


@RunWith(Parameterized.class)
public class GenerateKeyCorrectTest {

    private AES aes;

    private String masterPassword;

    public GenerateKeyCorrectTest(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {"testtesttesttesttesttesttest"},
                {"1234567812345678"}};

        return Arrays.asList(data);
    }

    @Before
    public void init(){
        aes = new AES();
    }

    @Test
    public void testGenerateKey_KeyNotNull_AtLeast16BytesPassword() throws Exception {
        Key key = aes.generateKey(masterPassword);
        assertNotNull(key);
    }
}
