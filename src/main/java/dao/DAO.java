package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateSessionFactory;

import java.util.List;

/**
 * Data Access Object - слой работы с базой данных
 */
public abstract class DAO<T> {
    public void save(T entity){
        Session session = HibernateSessionFactory.getSession();
        Transaction tx1 = session.beginTransaction();
        session.save(entity);
        tx1.commit();
        session.close();
    }
    public void saveOrUpdate(T entity){
        Session session = HibernateSessionFactory.getSession();
        Transaction tx1 = session.beginTransaction();
        session.saveOrUpdate(entity);
        tx1.commit();
        session.close();
    }
    public void update(T entity) {
        Session session = HibernateSessionFactory.getSession();
        Transaction tx1 = session.beginTransaction();
        session.update(entity);
        tx1.commit();
        session.close();
    }

    public void delete(T entity) {
        Session session = HibernateSessionFactory.getSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(entity);
        tx1.commit();
        session.close();
    }

    abstract public T find(int id);
    abstract public List<T> getAll();
}
