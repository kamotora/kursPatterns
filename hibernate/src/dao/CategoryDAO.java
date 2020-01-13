package dao;

import model.categories.Category;
import model.categories.TypeCategory;
import util.HibernateSessionFactory;

import java.util.List;

public class CategoryDAO extends DAO<Category> {
    public Category find(int id) {
        return HibernateSessionFactory.getSession().find(Category.class, id);
    }

    public List<Category> getAll() {
        return HibernateSessionFactory.getSession().createQuery("from Category",
                Category.class).getResultList();
    }

    public List<Category> autocomplete(String name) {
        return HibernateSessionFactory.getSession().createNamedQuery(Category.AUTOCOMPLETE, Category.class).
                setParameter("filter", name + "%").getResultList();
    }

    /**
     * Добавляем новую категорию и сохраняем её
     *
     * @param name название категории
     */
    public void add(String name) {
        Category category = new Category();
        category.setName(name);
        save(category);
    }

    public List<Category> getCategoryByType(TypeCategory typeCategory){
        return HibernateSessionFactory.getSession().createNamedQuery(Category.FIND_BY_TYPE, Category.class)
                .setParameter("pkType",typeCategory.getPkType()).getResultList();
    }
    public Category getCategory(TypeCategory typeCategory,String name){
        return HibernateSessionFactory.getSession().createQuery("from Category  where name = :name and type = :type", Category.class)
                .setParameter("name",name)
                .setParameter("type",typeCategory)
                .getSingleResult();
    }
    public Category addCategory(TypeCategory typeCategory, String name){
        Category category = getCategory(typeCategory,name);
        if(category == null) {
            category = new Category();
            category.setName(name);
            category.setType(typeCategory);
            saveOrUpdate(category);
        }
        return category;
    }
}