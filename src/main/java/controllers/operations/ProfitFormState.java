package controllers.operations;

import dao.BillDAO;
import dao.OperationDAO;
import dao.TypeCategoryDAO;
import exceptions.NegativeBalanceException;
import exceptions.ToBillNotChoosed;
import interfaces.IState;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.Bill;
import model.Operation;
import model.OperationBuilder;
import model.categories.TypeCategory;

import java.util.List;

public class ProfitFormState implements IState {
    ControllerOperationForm form;
    Operation operation;

    public ProfitFormState(ControllerOperationForm form, Operation operation) {
        this.form = form;
        this.operation = operation;
    }

    @Override
    public TypeCategory getType() {
        return TypeCategoryDAO.getInstanse().getProfitType();
    }

    @Override
    public Operation getOperation() {
        return operation;
    }

    @Override
    public FXMLLoader getWindowLoader(Stage mainWindow) {
        return  new FXMLLoader(getClass().getClassLoader().getResource("res/profit.fxml"));
    }

    @Override
    public void setBills() throws Exception {
        if(form.billToField == null)
            throw new Exception("Нет поля для счетов назначения на форме");
        List<Bill> billList = new BillDAO().getAll();
        form.billToField.setItems(FXCollections.observableArrayList(billList));
        if(billList != null)
            if(operation == null)
                form.billToField.setValue(billList.get(0));
            else
                form.billToField.setValue(operation.getToBill());
    }

    @Override
    public void saveBills(OperationBuilder operationBuilder) throws ToBillNotChoosed {
        Bill billTo = form.billToField.getValue();
        if(billTo == null){
            throw new ToBillNotChoosed();
        }
        operationBuilder.setToBill(billTo);
    }

    @Override
    public boolean addOperation(Operation operation) {
        try {
            operation.executeIsNeed();
            OperationDAO.getInstance().saveOrUpdate(operation);
            return true;
        } catch (NegativeBalanceException e) {
            System.out.println("Такого исключения тут быть не должно");
            e.printStackTrace();
        }
        return false;
    }
}
