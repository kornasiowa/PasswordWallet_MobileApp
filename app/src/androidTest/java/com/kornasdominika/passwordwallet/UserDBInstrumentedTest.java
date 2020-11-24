package com.kornasdominika.passwordwallet;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.kornasdominika.passwordwallet.model.User;
import com.kornasdominika.passwordwallet.repository.DatabaseManager;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.junit.runner.notification.RunListener;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class UserDBInstrumentedTest extends RunListener {

    Context context;
    static DatabaseManager databaseManager;

    @Parameterized.Parameter
    public static User user;

    @Parameterized.Parameters
    public static Object[] data() {
        User user1 = new User("test1", "test1", "salt1", false);
        User user2 = new User("test2", "test2", "salt2", true);
        return new Object[][]{
                {user1}, {user2}
        };
    }


    @Before
    public void init() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        databaseManager = new DatabaseManager(context);
        databaseManager.open();
    }

    @AfterClass
    public static void cleanUp() {
        databaseManager.dbClear();
        databaseManager.close();
    }

    @Test
    public void testAFindUserByLogin_UserNotFound_NewUser() {
        int result = databaseManager.findUserByLogin(user.login);
        assertEquals(result, -1);
    }

    @Test
    public void testBInsertIntoUser_InsertSuccessful_NewUser() {
        int result = databaseManager.insertIntoUser(user);
        assertEquals(1, result);
    }

    @Test
    public void testCFindUserByLogin_UserFound_ExistingUser() {
        int result = databaseManager.findUserByLogin(user.login);
        assertTrue(result > 0);
    }

    @Test
    public void testDInsertIntoUser_InsertFail_ExistingUser() {
        int result = databaseManager.insertIntoUser(user);
        assertEquals(-1, result);
    }

}