package model;

import org.junit.Assert;
import org.junit.Test;


public class UserTest {
    @Test
    public void checkPassword() {
        User user = new User();
        String password = "123!@#";
        user.hashAndSetPass(password);
        Assert.assertTrue(user.checkPassword(password));
    }
}
