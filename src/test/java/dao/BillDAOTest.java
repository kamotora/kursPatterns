package dao;

import model.Bill;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

public class BillDAOTest {
    private static BillDAO billDAO;
    private static Bill bill;
    @BeforeClass
    public static void beforeAll() {
        billDAO = new BillDAO();
        bill = new Bill();
        bill.setName("test");
        bill.setSum(BigDecimal.valueOf(111));
        //TODO Убрать
        try {
            billDAO.save(bill);
        }catch (Exception e){
            billDAO.update(bill);
        }
    }

    @AfterClass
    public static void afterAll() {
        billDAO.delete(bill);
    }

    @Test
    public void getAll() {
        Assert.assertNotNull(billDAO.getAll());
    }

    @Test
    public void find() {
        Assert.assertNotNull(billDAO.find(bill.getPkBill()));
        Assert.assertEquals(bill.getPkBill(), billDAO.find(bill.getPkBill()).getPkBill());
    }
}