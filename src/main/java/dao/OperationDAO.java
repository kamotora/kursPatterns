package dao;

import interfaces.IObservable;
import interfaces.IObserver;
import model.Operation;
import model.User;
import model.categories.Category;
import model.categories.TypeCategory;
import util.HibernateSessionFactory;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OperationDAO extends DAO<Operation> implements IObservable {

    static OperationDAO instance = null;
    private HashMap<TypeCategory, List<IObserver>> observers = new HashMap<>();

    private OperationDAO() {
    }

    public static OperationDAO getInstance() {
        if(instance == null)
            instance = new OperationDAO();
        return instance;
    }
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
    public List<Operation> getOperationsByType(User user, TypeCategory typeCategory){
        return HibernateSessionFactory.getSession().createQuery("FROM Operation where user = :user and category.type.pkType = :pktype",Operation.class)
                .setParameter("user", user)
                .setParameter("pktype",typeCategory.getPkType())
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

    public void addObserver(IObserver observer, TypeCategory typeCategory){
        List<IObserver> list = observers.get(typeCategory);
        if(list == null)
            list = new ArrayList<>();
        list.add(observer);
        observers.put(typeCategory,list);
    }

    public void notifyObservers(TypeCategory typeCategory){
        if(observers.get(typeCategory) == null)
            return;
        for(IObserver iObserver : observers.get(typeCategory))
            iObserver.update();
    }

    @Override
    public void saveOrUpdate(Operation entity) {
        super.saveOrUpdate(entity);
        notifyObservers(entity.getCategory().getType());
    }

    public void deleteAllByUser(User user){
        List<Operation> operations = getAllByUser(user);
        for(Operation operation : operations)
            delete(operation);
    }

    @Override
    public void delete(Operation entity) {
        super.delete(entity);
        notifyObservers(entity.getCategory().getType());
    }
}
