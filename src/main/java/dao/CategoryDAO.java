package dao;

import exceptions.DublicateCategory;
import model.categories.Category;
import model.categories.TypeCategory;
import util.HibernateSessionFactory;

import javax.persistence.NoResultException;
import java.util.List;

public class CategoryDAO extends DAO<Category> {
    public Category find(int id) {
        return HibernateSessionFactory.getSession().find(Category.class, id);
    }

    public List<Category> getAll() {
        return HibernateSessionFactory.getSession().createQuery("from Category",
                Category.class).getResultList();
    }


    public List<Category> getCategoryByType(TypeCategory typeCategory){
        try {
            return HibernateSessionFactory.getSession().createNamedQuery(Category.FIND_BY_TYPE, Category.class)
                    .setParameter("pkType",typeCategory.getPkType()).getResultList();
        }catch (NoResultException ex){
            return null;
        }
    }
    public Category getCategory(TypeCategory typeCategory,String name) throws DublicateCategory {
        List<Category> res = HibernateSessionFactory.getSession().createQuery("from Category  where name = :name and type = :type", Category.class)
                .setParameter("name",name)
                .setParameter("type",typeCategory)
                .getResultList();
        if(res.isEmpty())
            return null;
        if(res.size() > 1)
            throw new DublicateCategory();
        return res.get(0);
    }
    public Category addCategory(TypeCategory typeCategory, String name) throws DublicateCategory {
        Category category = getCategory(typeCategory,name);
        if(category == null) {
            category = new Category();
            category.setName(name);
            category.setType(typeCategory);
            saveOrUpdate(category);
        }
        else
            throw new DublicateCategory();
        return category;
    }
}