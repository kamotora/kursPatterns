package dao;

import model.Bill;
import model.Period;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class PeriodDAOTest {
    private static PeriodDAO periodDAO;
    private static Period period;
    @BeforeAll
    static void beforeAll() {
        periodDAO = new PeriodDAO();
        period = new Period();
        period.setName("test");
        periodDAO.saveOrUpdate(period);
    }

    @AfterAll
    static void afterAll() {
        periodDAO.delete(period);
    }

    @Test
    void getAll() {
        Assert.assertNotNull(periodDAO.getAll());
    }

    @Test
    void find() {
        Assert.assertEquals(period, periodDAO.find(period.getPkPeriod()));
    }
}