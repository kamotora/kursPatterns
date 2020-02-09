package dao;

import model.Limit;
import model.Operation;
import model.categories.Category;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LimitDAOTest {
    private static Limit testLimit;
    private static LimitDAO limitDAO;
    private static Operation testOperation;
    @BeforeAll
    static void beforeAll() {
        limitDAO = new LimitDAO();
        testLimit = new Limit();
        testLimit.setUser(new UserDAO().findByLogin("test"));
        testLimit.setDatestart(Date.valueOf(LocalDate.now().minusDays(2)));
        testLimit.setDateend(Date.valueOf(LocalDate.now()));
        testLimit.setSum(BigDecimal.valueOf(9999));
        limitDAO.saveOrUpdate(testLimit);
        testOperation = new Operation();
        testOperation.setDate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        testOperation.setUser(testLimit.getUser());
        testOperation.setSum(BigDecimal.valueOf(9999));
        try {
            testOperation.setCategory(new CategoryDAO().getCategoryByType(TypeCategoryDAO.getInstanse().getProfitType()).get(0));
        }catch (ArrayIndexOutOfBoundsException e){
            Assert.fail("Добавьте по 1 категории каждого типа");
        }
        OperationDAO.getInstance().saveOrUpdate(testOperation);
    }

    @AfterAll
    static void afterAll() {
        limitDAO.delete(testLimit);
        OperationDAO.getInstance().delete(testOperation);
    }

    @Test
    void getAll() {
        Assert.assertNotNull(limitDAO.getAll());
    }

    @Test
    void find() {
        Assert.assertEquals(testLimit.getPkLimit(), limitDAO.find(testLimit.getPkLimit()).getPkLimit());
    }

    @Test
    void getOperationsByLimit() {
        List<Operation> list = limitDAO.getOperationsByLimit(testLimit);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(testOperation.getPkOperation(), list.get(0).getPkOperation());
    }
}