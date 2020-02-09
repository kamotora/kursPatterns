package dao;

import model.Bill;
import model.Period;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

public class PeriodDAOTest {
    private static PeriodDAO periodDAO;
    private static Period period;
    @BeforeClass
    public static void beforeAll() {
        periodDAO = new PeriodDAO();
        period = new Period();
        period.setName("test");
        periodDAO.saveOrUpdate(period);
    }

    @AfterClass
    public static void afterAll() {
        periodDAO.delete(period);
    }

    @Test
    public void getAll() {
        Assert.assertNotNull(periodDAO.getAll());
    }

    @Test
    public void find() {
        Assert.assertEquals(period, periodDAO.find(period.getPkPeriod()));
    }
}