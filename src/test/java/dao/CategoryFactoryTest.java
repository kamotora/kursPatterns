package dao;

import model.categories.Category;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

    /**
     * Должен создаваться объект фабрики (singletone), Объект фабрики not Null
     * */
    @Test
    public void getInstance() {
        Assert.assertNotNull(CategoryFactory.getInstance());
    }

    /**
     * Должны получить именно ту категорию, которую запросили
     * */
    @Test
    public void getCategory() {
        Assert.assertEquals(testCategory.getName(),
                CategoryFactory.getInstance().getCategory(testCategory.getType(),testCategory.getName()).getName());
    }
}