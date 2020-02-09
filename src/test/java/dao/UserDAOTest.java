package dao;

import exceptions.UserAlreadyExistsException;
import model.User;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.Extensions;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private static UserDAO userDAO;
    private static String login;
    private static String password;

    /*
    * Добавим пользователя test:test
    * */
    @BeforeAll
    static void beforeAll() throws UserAlreadyExistsException {
        userDAO = new UserDAO();
        login = "test";
        password = "test";
        if(userDAO.findByLogin(login) == null)
            userDAO.addUser(login,password);
    }


    @org.junit.jupiter.api.Test
    void getAll() {
        Assert.assertNotNull(userDAO.getAll());
    }

    /*
     * Добавление уже существующего пользователя
     * */
    @org.junit.jupiter.api.Test
    void addUserExistingLogin() {
        Assert.assertThrows(UserAlreadyExistsException.class,()-> userDAO.addUser(login,password));
    }

    @org.junit.jupiter.api.Test
    void findByLogin() {
        Assert.assertNotNull(userDAO.findByLogin(login));
    }
}