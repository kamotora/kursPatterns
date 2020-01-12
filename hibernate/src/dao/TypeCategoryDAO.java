package dao;

import model.categories.TypeCategory;
import util.HibernateSessionFactory;

import java.util.List;

public class TypeCategoryDAO extends DAO<TypeCategory> {
    @Override
    public TypeCategory find(int id) {
        return HibernateSessionFactory.getSession().find(TypeCategory.class, id);
    }

    public List<TypeCategory> getAll() {
        List<TypeCategory> res = HibernateSessionFactory.getSession().createQuery("from TypeCategory ",
                TypeCategory.class).getResultList();
        return res;
    }

    public TypeCategory getProfitType(){
        TypeCategory res = find(1);
        return res;
    }

    public TypeCategory getExpandType(){
        TypeCategory res = find(2);
        return res;
    }

    public TypeCategory getTransferType(){
        TypeCategory res = find(3);
        return res;
    }
}
