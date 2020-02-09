package model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class UserTest {
    private User user;
    @Before
    public void setUp() throws Exception {
        user = new User();
    }

    @After
    public void tearDown() throws Exception {
        user = null;
    }

    @Test
    public void checkPassword() {
        String password = "123!@#";
        user.hashAndSetPass(password);
        Assert.assertTrue(user.checkPassword(password));
    }
}
