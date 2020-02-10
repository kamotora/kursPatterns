package dao;

import exceptions.UserAlreadyExistsException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserDAOTest {
    private static UserDAO userDAO;
    private static String login;
    private static String password;

    /*
    * Добавим пользователя test:test
    * */
    @BeforeClass
    public static void beforeAll() throws UserAlreadyExistsException {
        userDAO = new UserDAO();
        login = "test";
        password = "test";
        if(userDAO.findByLogin(login) == null)
            userDAO.addUser(login,password);
    }


    @Test
    public  void getAll() {
        Assert.assertNotNull(userDAO.getAll());
    }

    /*
     * Добавление уже существующего пользователя
     * */
    @Test
    public void addUserExistingLogin() {
        Assert.assertThrows(UserAlreadyExistsException.class,()-> userDAO.addUser(login,password));
    }

    @Test
    public void findByLogin() {
        Assert.assertNotNull(userDAO.findByLogin(login));
    }
}