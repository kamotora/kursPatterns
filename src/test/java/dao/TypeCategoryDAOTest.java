package dao;

import model.categories.TypeCategory;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TypeCategoryDAOTest {

    @Test
    public  void getInstanse() {
        Assert.assertNotNull(TypeCategoryDAO.getInstanse());
    }

    @Test
    public  void find() {
        TypeCategory typeCategory = TypeCategoryDAO.getInstanse().getProfitType();
        Assert.assertEquals(typeCategory, TypeCategoryDAO.getInstanse().find(typeCategory.getPkType()));


        typeCategory = TypeCategoryDAO.getInstanse().getExpandType();
        Assert.assertEquals(typeCategory, TypeCategoryDAO.getInstanse().find(typeCategory.getPkType()));


        typeCategory = TypeCategoryDAO.getInstanse().getTransferType();
        Assert.assertEquals(typeCategory, TypeCategoryDAO.getInstanse().find(typeCategory.getPkType()));
    }

    /**
     * Должно быть 3 типа : доход, расход, перевод
     * */
    @Test
    public void getAll3Count() {
        Assert.assertEquals(3, TypeCategoryDAO.getInstanse().getAll().size());
    }

    @Test
    public void getProfitTypeNotNull() {
        Assert.assertNotNull(TypeCategoryDAO.getInstanse().getProfitType());
    }

    @Test
    public void getExpandTypeNotNull() {
        Assert.assertNotNull(TypeCategoryDAO.getInstanse().getExpandType());
    }

    @Test
    public void getTransferTypeNotNull() {
        Assert.assertNotNull(TypeCategoryDAO.getInstanse().getTransferType());
    }
}