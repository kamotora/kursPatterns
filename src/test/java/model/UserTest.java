package model;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;


public class UserTest {
    private User user;
    @Before
    public void setUp()  {
        user = new User();
    }

    @After
    public void tearDown()  {
        user = null;
    }

    @Test
    public void checkPassword() {
        String password = "123!@#";
        user.hashAndSetPass(password);
        Assert.assertTrue(user.checkPassword(password));
    }
}
