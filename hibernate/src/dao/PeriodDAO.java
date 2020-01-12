package dao;

import model.Operation;
import model.Period;
import model.categories.Category;
import util.HibernateSessionFactory;

import java.util.List;

public class PeriodDAO extends DAO<Period>{
    public List<Period> getAll() {
        return HibernateSessionFactory.getSession().createQuery("from Period",
                Period.class).getResultList();
    }

    @Override
    public Period find(int id) {
        return HibernateSessionFactory.getSession().find(Period.class,id);
    }
}
