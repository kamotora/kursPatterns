package dao;

import model.Limit;
import model.Operation;
import util.HibernateSessionFactory;

import java.util.List;

public class LimitDAO extends DAO<Limit> {
    public List<Limit> getAll() {
        return HibernateSessionFactory.getSession().createQuery("from Limit",
                Limit.class).getResultList();
    }

    @Override
    public Limit find(int id) {
        return HibernateSessionFactory.getSession().find(Limit.class,id);
    }

    public List<Operation> getOperationsByLimit(Limit limit){
        return HibernateSessionFactory.getSession().createQuery("from Operation where user = :user and date between :start and :finish ", Operation.class)
                .setParameter("user",limit.getUser())
                .setParameter("start",limit.getDatestart())
                .setParameter("finish",limit.getDateend())
                .getResultList();
    }
}
