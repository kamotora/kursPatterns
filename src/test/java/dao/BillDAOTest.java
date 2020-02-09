package dao;

import model.Bill;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BillDAOTest {
    private static BillDAO billDAO;
    private static Bill bill;
    @BeforeAll
    static void beforeAll() {
        billDAO = new BillDAO();
        bill = new Bill();
        bill.setName("test");
        bill.setSum(BigDecimal.valueOf(111));
        billDAO.saveOrUpdate(bill);
    }

    @AfterAll
    static void afterAll() {
        billDAO.delete(bill);
    }

    @Test
    void getAll() {
        Assert.assertNotNull(billDAO.getAll());
    }

    @Test
    void find() {
        Assert.assertNotNull(billDAO.find(bill.getPkBill()));
        Assert.assertEquals(bill.getPkBill(), billDAO.find(bill.getPkBill()).getPkBill());
    }
}