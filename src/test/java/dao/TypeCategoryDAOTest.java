package dao;

import model.categories.TypeCategory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeCategoryDAOTest {

    @Test
    void getInstanse() {
        Assert.assertNotNull(TypeCategoryDAO.getInstanse());
    }

    @Test
    void find() {
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
    void getAll3Count() {
        Assert.assertEquals(3, TypeCategoryDAO.getInstanse().getAll().size());
    }

    @Test
    void getProfitTypeNotNull() {
        Assert.assertNotNull(TypeCategoryDAO.getInstanse().getProfitType());
    }

    @Test
    void getExpandTypeNotNull() {
        Assert.assertNotNull(TypeCategoryDAO.getInstanse().getExpandType());
    }

    @Test
    void getTransferTypeNotNull() {
        Assert.assertNotNull(TypeCategoryDAO.getInstanse().getTransferType());
    }
}