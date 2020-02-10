package dao;

import exceptions.UserAlreadyExistsException;
import model.Operation;
import model.User;
import model.categories.Category;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OperationDAOTest {
    private static User userTest;
    private static OperationDAO operationDAO;
    private static List<Operation> operations;
    private static Category categoryExpend, categoryProfit, categoryTransfer;
    /**
     * Добавим пользователя test:test, если его нет
     * Создадим несколько операций каждого типа, добавим их в бд для дальнейших тестов
     */
    @BeforeClass
    public static void beforeAll() throws UserAlreadyExistsException {
        UserDAO userDAO = new UserDAO();
        String login = "test";
        String password = "test";
        if(userDAO.findByLogin(login) == null)
            userDAO.addUser(login,password);
        userTest = userDAO.findByLogin(login);
        if(userTest == null)
            Assert.fail("Проверить класс UserDAO. Ошибка при добавлении user");
        operationDAO = OperationDAO.getInstance();
        CategoryDAO categoryDAO = new CategoryDAO();
        try{
            categoryExpend = categoryDAO.getCategoryByType(TypeCategoryDAO.getInstanse().getExpandType()).get(0);
            categoryProfit = categoryDAO.getCategoryByType(TypeCategoryDAO.getInstanse().getProfitType()).get(0);
            categoryTransfer = categoryDAO.getCategoryByType(TypeCategoryDAO.getInstanse().getTransferType()).get(0);
        }
        catch (ArrayIndexOutOfBoundsException e){
            Assert.fail("Для теста должна быть хотя бы 1 категория каждого типа");
            return;
        }

        for (Operation operation: operationDAO.getAllByUser(userTest))
            operationDAO.delete(operation);
        operations = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < 10; i++){
            Operation operation = new Operation();
            operation.setUser(userTest);
            int curSum = random.nextInt(1000);
            operation.setSum(BigDecimal.valueOf(curSum));

            if(i % 3 == 0)
                operation.setCategory(categoryExpend);
            if(i % 3 == 1)
                operation.setCategory(categoryProfit);
            if(i % 3 == 2)
                operation.setCategory(categoryTransfer);

            operation.setDate(Timestamp.valueOf(LocalDateTime.now()));
            operationDAO.saveOrUpdate(operation);
            operations.add(operation);
        }
    }

    /**
     * Удалим созданные операции
     * */
    @AfterClass
    public static void afterAll() {
        for(Operation operation: operations)
            operationDAO.delete(operation);
    }


    @Test
    public void getInstanceDAOIsNotNull() {
        Assert.assertNotNull(OperationDAO.getInstance());
    }

    @Test
    public void getAllByUser() {
        Assert.assertEquals(operations.size(), operationDAO.getAllByUser(userTest).size());
    }

    @Test
    public void getExpands() {
        int size = 0;
        for(Operation operation : operations)
            if(operation.getCategory().getType().equals(TypeCategoryDAO.getInstanse().getExpandType()))
                size++;
        Assert.assertEquals(size, operationDAO.getExpands(userTest).size());
    }

    @Test
    public void getExpandsFromDate() {
        int size = 0;
        for(Operation operation : operations)
            if(operation.getCategory().getType().equals(TypeCategoryDAO.getInstanse().getExpandType()))
                size++;
        Assert.assertEquals(size, operationDAO.getExpandsFromDate(userTest, Date.valueOf(LocalDate.now().minusDays(1))).size());
    }

    @Test
    public void getProfits() {
        int size = 0;
        for(Operation operation : operations)
            if(operation.getCategory().getType().equals(TypeCategoryDAO.getInstanse().getProfitType()))
                size++;
        Assert.assertEquals(size, operationDAO.getProfits(userTest).size());
    }

    @Test
    public void getTransfers() {
        int size = 0;
        for(Operation operation : operations)
            if(operation.getCategory().getType().equals(TypeCategoryDAO.getInstanse().getTransferType()))
                size++;
        Assert.assertEquals(size, operationDAO.getTransfers(userTest).size());
    }

    @Test
    public void getOperationsByType() {
        int size = 0;
        for(Operation operation : operations)
            if(operation.getCategory().getType().equals(TypeCategoryDAO.getInstanse().getTransferType()))
                size++;
        Assert.assertEquals(size, operationDAO.getOperationsByType(userTest,TypeCategoryDAO.getInstanse().getTransferType()).size());
    }

    @Test
    public void getOperationsByCategory() {
        int size = 0;
        for(Operation operation : operations)
            if(operation.getCategory().equals(categoryProfit))
                size++;
        Assert.assertEquals(size, operationDAO.getOperationsByCategory(userTest,categoryProfit).size());
    }

    /**
     * Добавим и удалим операцию, её не должно быть в базе после удаления
     * */
    @Test
    public void delete() {
        Operation operation = new Operation();
        operation.setUser(userTest);
        operation.setSum(BigDecimal.valueOf(1111));
        operation.setCategory(categoryTransfer);
        operation.setDate(Timestamp.valueOf(LocalDateTime.now()));
        operationDAO.saveOrUpdate(operation);

        operationDAO.delete(operation);

        Assert.assertNull(operationDAO.find(operation.getPkOperation()));
    }
}