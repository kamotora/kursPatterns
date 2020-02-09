package dao;

import exceptions.DublicateCategory;
import model.categories.Category;
import model.categories.TypeCategory;
import util.MsgWindow;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryFactory {
    private ArrayList<Category> categories;
    private static CategoryFactory instance;
    CategoryDAO categoryDAO;
    private CategoryFactory(){
        categories  = new ArrayList<>();
        categoryDAO = new CategoryDAO();
    }


    public static CategoryFactory getInstance() {
        if(instance == null)
            instance = new CategoryFactory();
        return instance;
    }
    /**
     * Если есть такая категория в массиве или базе, возвращает существующую.
     * Иначе - создание новой категории с указанными данными, добавление в бд и массив и возврат её
     * @param typeCategory тип искомой категории
     * @param name Название искомой категории
     * */
    public Category getCategory(TypeCategory typeCategory, String name){
        Category category = null;
        for(Category cat : categories)
            if(cat.getName().equals(name) && cat.getType().equals(typeCategory)){
                category = cat;
                break;
            }
        if(category == null) {
            try {

                category = categoryDAO.getCategory(typeCategory, name);
                if(category == null){
                    category = categoryDAO.addCategory(typeCategory,name);
                }
                categories.add(category);

            }catch (DublicateCategory e){
                MsgWindow.showErrorWindow(e.toString());
                e.printStackTrace();
            }
        }
        return category;
    }
}
