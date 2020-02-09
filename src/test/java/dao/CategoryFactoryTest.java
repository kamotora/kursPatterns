package dao;

import model.categories.Category;
import org.junit.*;
import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryFactoryTest {
    private Category testCategory;
    @Before
    public void setUp() {
        testCategory = new Category();
        testCategory.setType(TypeCategoryDAO.getInstanse().getTransferType());
        testCategory.setName("test");
    }

    @After
    public void tearDown() {
        new CategoryDAO().delete(testCategory);
    }

    @Test
    public void getInstance() {
        Assert.assertNotNull(CategoryFactory.getInstance());
    }

    @Test
    public void getCategory() {
        Assert.assertEquals(testCategory.getName(),
                CategoryFactory.getInstance().getCategory(testCategory.getType(),testCategory.getName()).getName());
    }
}