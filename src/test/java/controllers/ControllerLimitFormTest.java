package controllers;

import dao.CategoryDAO;
import dao.OperationDAO;
import dao.TypeCategoryDAO;
import dao.UserDAO;
import exceptions.UserAlreadyExistsException;
import model.Limit;
import model.Operation;
import model.User;
import model.categories.Category;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ControllerLimitFormTest {
    private static User userTest;
    private static Limit testLimit;
    private static OperationDAO operationDAO;
    private static List<Operation> operations;

    /**
     * Добавим несколько операций пользователю test для дальнейших тестов
     * */
    @BeforeClass
    public static void createOperations() throws UserAlreadyExistsException {
        testLimit = new Limit();
        CategoryDAO categoryDAO = new CategoryDAO();
        operationDAO = OperationDAO.getInstance();
        Category expandCategory = categoryDAO.getCategoryByType(TypeCategoryDAO.getInstanse().getExpandType()).get(0);
        try {
            userTest = new UserDAO().findByLogin("test");
        }catch (Exception e){
            userTest = new UserDAO().addUser("test", "test");
        }

        operations = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < 10; i++){
            Operation operation = new Operation();
            operation.setUser(userTest);
            int curSum = random.nextInt(1000);
            operation.setSum(BigDecimal.valueOf(curSum));

            operation.setCategory(expandCategory);

            operation.setDate(Timestamp.valueOf(LocalDateTime.now()));
            operationDAO.saveOrUpdate(operation);
            operations.add(operation);
        }
    }

    /**
     * Удалим созданные операции
     * */
    @AfterClass
    public static void deleteOperations(){
        for(Operation operation:operations)
            operationDAO.delete(operation);
    }

    /**
     * Проверка правильности подсчёта суммы расходов за указанный период
     * */
    @Test
    public void getSumOperationsByPeriod(){
        ControllerLimitForm controllerLimitForm = new ControllerLimitForm(testLimit,userTest);
        double sum = 0;
        for(Operation operation:operations)
            sum += operation.getSum().doubleValue();
        //Assert.assertThrows(Exception.class, () -> controllerLimitForm.getSumOperationsByPeriod(LocalDate.now(),LocalDate.now().minusDays(1)));
        try{
            Assert.assertEquals(sum,controllerLimitForm.getSumOperationsByPeriod(LocalDate.now().minusDays(1),LocalDate.now()), 1e-6);
        }catch (Exception e){
            Assert.fail(e.toString());
        }
    }
}