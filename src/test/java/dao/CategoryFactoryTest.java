package dao;

import model.categories.Category;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryFactoryTest {
    private Category testCategory;
    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setType(TypeCategoryDAO.getInstanse().getTransferType());
        testCategory.setName("test");
    }

    @AfterEach
    void tearDown() {
        new CategoryDAO().delete(testCategory);
    }

    @Test
    void getInstance() {
        Assert.assertNotNull(CategoryFactory.getInstance());
    }

    @Test
    void getCategory() {
        Assert.assertEquals(testCategory.getName(),
                CategoryFactory.getInstance().getCategory(testCategory.getType(),testCategory.getName()).getName());
    }
}