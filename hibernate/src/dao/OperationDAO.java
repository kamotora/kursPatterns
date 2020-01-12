package dao;

import model.Operation;
import model.User;
import model.categories.Category;
import util.HibernateSessionFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


public class OperationDAO extends DAO<Operation> {

    public List<Operation> getAll(){
        return HibernateSessionFactory.getSession().createQuery("FROM Operation",Operation.class).getResultList();
    }

    public List<Operation> getAllByUser(User user){
        return HibernateSessionFactory.getSession().createQuery("FROM Operation where user = "+user.getPkUser(),Operation.class).getResultList();
    }

    public List<Operation> getExpands(User user){
        return HibernateSessionFactory.getSession().createQuery("FROM Operation where user = :user and category.type.pkType = :pkExpandType",Operation.class)
                .setParameter("user", user)
                .setParameter("pkExpandType",2)
                .getResultList();
    }

    public List<Operation> getExpandsFromDate(User user, Date date){
        return HibernateSessionFactory.getSession().createQuery("FROM Operation where user = :user and category.type.pkType = :pkExpandType and date > :startDate",Operation.class)
                .setParameter("user", user)
                .setParameter("pkExpandType",2)
                .setParameter("startDate",date)
                .getResultList();
    }

    public List<Operation> getProfits(User user){
        return HibernateSessionFactory.getSession().createQuery("FROM Operation where user = :user and category.type.pkType = :pkProfitType",Operation.class)
                .setParameter("user", user)
                .setParameter("pkProfitType",1)
                .getResultList();
    }

    public List<Operation> getTransfers(User user){
        return HibernateSessionFactory.getSession().createQuery("FROM Operation where user = :user and category.type.pkType = :pkTransfer",Operation.class)
                .setParameter("user", user)
                .setParameter("pkTransfer",3)
                .getResultList();
    }

    public List<Operation> getOperationsByCategory(User user, Category category){
        return HibernateSessionFactory.getSession().createQuery("FROM Operation where user = :user and category.pkCategory = :pkCategory",Operation.class)
                .setParameter("user", user)
                .setParameter("pkCategory",category.getPkCategory())
                .getResultList();
    }

    @Override
    public Operation find(int id) {
        return HibernateSessionFactory.getSession().find(Operation.class,id);
    }
}
