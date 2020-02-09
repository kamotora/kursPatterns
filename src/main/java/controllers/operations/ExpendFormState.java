package controllers.operations;

import dao.BillDAO;
import dao.OperationDAO;
import dao.TypeCategoryDAO;
import exceptions.FromBillNotChoosed;
import exceptions.NegativeBalanceException;
import interfaces.IState;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.Bill;
import model.Operation;
import model.OperationBuilder;
import model.categories.TypeCategory;
import util.MsgWindow;

import java.util.List;

public class ExpendFormState implements IState {
    ControllerOperationForm form;
    Operation operation;

    public ExpendFormState(ControllerOperationForm form, Operation operation) {
        this.form = form;
        this.operation = operation;
    }

    @Override
    public TypeCategory getType() {
        return TypeCategoryDAO.getInstanse().getExpandType();
    }

    @Override
    public Operation getOperation() {
        return operation;
    }

    @Override
    public FXMLLoader getWindowLoader(Stage mainWindow) {
        return new FXMLLoader(getClass().getClassLoader().getResource("res/expend.fxml"));
    }

    @Override
    public void setBills() throws Exception {
        if(form.billFromField == null)
            throw new Exception("Нет поля для счетов списания на форме");
        List<Bill> billList = new BillDAO().getAll();
        form.billFromField.setItems(FXCollections.observableArrayList(billList));
        if(billList != null)
            if(operation == null)
                form.billFromField.setValue(billList.get(0));
            else
                form.billFromField.setValue(operation.getFromBill());
    }

    @Override
    public void saveBills(OperationBuilder operationBuilder) throws FromBillNotChoosed {
        Bill billFrom = form.billFromField.getValue();
        if(billFrom == null){
            throw new FromBillNotChoosed();
        }
        operationBuilder.setFromBill(billFrom);
    }

    @Override
    public boolean addOperation(Operation operation) {
        try {
            operation.executeIsNeed();
            OperationDAO.getInstance().saveOrUpdate(operation);
            return true;
        } catch (NegativeBalanceException e) {
            if (MsgWindow.showAsk("На вашем счете недостаточно средств. Добавить расход?")) {
                OperationDAO.getInstance().saveOrUpdate(operation);
                return true;
            }
        }
        return false;
    }
}
