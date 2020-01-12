package dao;

import model.Bill;
import org.hibernate.Session;
import util.HibernateSessionFactory;

import java.util.List;

public class BillDAO extends DAO<Bill> {
    public List<Bill> getAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        List<Bill> res = session.createQuery("from Bill",
                Bill.class).getResultList();
        session.close();
        return res;
    }

    @Override
    public Bill find(int id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Bill res = session.find(Bill.class,id);
        session.close();
        return res;
    }
}
