package model;

import dao.UserDAO;
import junit.framework.TestCase;
import model.categories.Category;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import util.TestBCrypt;

import static org.junit.jupiter.api.Assertions.*;


public class OperationBuilderTest {

    @Test
    public void testCreate() {
        OperationBuilder operationBuilder = new OperationBuilder();
        Assert.assertThrows("Не указан user",Exception.class, operationBuilder::create);
        operationBuilder.setUser(new UserDAO().findByLogin("test"));
        Assert.assertThrows("Не указана категория",Exception.class, operationBuilder::create);
        operationBuilder.setCategory(new Category());
        Assert.assertThrows("Не указан счёт",Exception.class, operationBuilder::create);
        Bill bill = new Bill();
        operationBuilder.setFromBill(bill);
        operationBuilder.setToBill(bill);
        Assert.assertThrows("Одинаковые счета",Exception.class, operationBuilder::create);
        operationBuilder.setToBill(null);
        try {
            Assert.assertNotNull(operationBuilder.create());
        }catch (Exception e){
            Assert.fail("Непротестированное исключение");
            e.printStackTrace();
        }

    }
}