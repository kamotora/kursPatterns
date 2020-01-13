package dao;

import exceptions.UserAlreadyExistsException;
import model.User;
import util.HibernateSessionFactory;

import javax.persistence.NoResultException;
import java.util.List;


public class UserDAO extends DAO<User> {

    public User find(int id) {
        return HibernateSessionFactory.getSession().find(User.class, id);
    }

    public List<User> getAll() {
        return HibernateSessionFactory.getSession().createNamedQuery(User.ALL_USERS, User.class).getResultList();
    }

    public void addUser(String login, String password) throws Exception {
        if (findByLogin(login) != null) {
            throw new UserAlreadyExistsException();
        }
        User user = new User();
        user.setLogin(login);
        user.hashAndSetPass(password);
        save(user);
    }

    /**
     * Получение пользователя по логину из БД
     *
     * @param login логин
     * @return Пользователь или null если пользователь не найден
     */
    public User findByLogin(String login) {
        try {
            return HibernateSessionFactory.getSession().createNamedQuery(User.FIND_BY_LOGIN, User.class).
                    setParameter("login", login).getSingleResult();
        } catch (NoResultException ignored) {
            return null;
        }
    }
}