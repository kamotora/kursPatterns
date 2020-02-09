package dao;

import exceptions.DublicateCategory;
import model.categories.Category;
import model.categories.TypeCategory;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDAOTest {
    private static List<Category> categoryList;
    private static CategoryDAO categoryDAO;
    private static TypeCategory profitTypeCategory,expandTypeCategory, transferTypeCategory;
    private static String nameCategory;
    @BeforeAll
    public static void beforeAll() {
        profitTypeCategory = TypeCategoryDAO.getInstanse().getProfitType();
        expandTypeCategory = TypeCategoryDAO.getInstanse().getExpandType();
        transferTypeCategory = TypeCategoryDAO.getInstanse().getTransferType();
        categoryList = new ArrayList<>();
        categoryDAO = new CategoryDAO();
        nameCategory = "testCategory";
        try{
            for (int i = 0; i < 10 ; i++){
                TypeCategory curType;
                String curName = nameCategory+i;
                if(i % 3 == 0)
                    curType = profitTypeCategory;
                else if(i % 3 == 1)
                    curType = expandTypeCategory;
                else
                    curType = transferTypeCategory;
                Category category = categoryDAO.getCategory(curType, curName);
                if(category == null)
                    category = categoryDAO.addCategory(curType,curName);
                else
                    Assert.fail("Тестовые категории остались в базе. Нужно удалить");
                categoryList.add(category);
            }

        }catch (DublicateCategory dublicateCategory){
            Assert.fail("Тестовые категории остались в базе. Нужно удалить");
        }
    }

    @AfterAll
    public static void afterAll() {
        for(Category category: categoryList)
            categoryDAO.delete(category);
    }

    @Test
    void find() {
        Assert.assertEquals(categoryList.get(0), categoryDAO.find(categoryList.get(0).getPkCategory()));
    }

    @Test
    void getAll() {
        Assert.assertNotNull(categoryDAO.getAll());
    }


    @Test
    void getCategoryByType() {
        Assert.assertNotNull(categoryDAO.getCategoryByType(profitTypeCategory));
        Assert.assertNotNull(categoryDAO.getCategoryByType(expandTypeCategory));
        Assert.assertNotNull(categoryDAO.getCategoryByType(transferTypeCategory));
    }

    @Test
    void getCategory() throws DublicateCategory {
        for(Category category: categoryList)
            Assert.assertEquals(category, categoryDAO.getCategory(category.getType(), category.getName()));
    }

    @Test
    void addCategory() throws DublicateCategory {
        for(Category category: categoryList)
            Assert.assertThrows(DublicateCategory.class, () -> categoryDAO.addCategory(category.getType(), category.getName()));
    }
}