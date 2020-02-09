package controllers.operations;

import dao.BillDAO;
import dao.OperationDAO;
import dao.TypeCategoryDAO;
import exceptions.EqualsBills;
import exceptions.FromBillNotChoosed;
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
import util.MsgWindow;

import java.util.List;

public class TransferFormState implements IState {
    ControllerOperationForm form;
    Operation operation;

    public TransferFormState(ControllerOperationForm form, Operation operation) {
        this.form = form;
        this.operation = operation;
    }

    @Override
    public TypeCategory getType() {
        return TypeCategoryDAO.getInstanse().getTransferType();
    }

    @Override
    public Operation getOperation() {
        return operation;
    }

    @Override
    public FXMLLoader getWindowLoader(Stage mainWindow) {
        return new FXMLLoader(getClass().getClassLoader().getResource("res/transfer.fxml"));
    }

    @Override
    public void setBills() throws Exception {
        if(form.billToField == null)
            throw new Exception("Нет поля для счетов назначения на форме");
        if(form.billFromField == null)
            throw new Exception("Нет поля для счетов списания на форме");
        List<Bill> billList = new BillDAO().getAll();
        form.billFromField.setItems(FXCollections.observableArrayList(billList));
        if(billList != null)
            if(operation == null) {
                form.billFromField.setValue(billList.get(0));
                form.billToField.setValue(billList.get(0));
            }else {
                form.billFromField.setValue(operation.getFromBill());
                form.billToField.setValue(operation.getToBill());
            }
    }

    @Override
    public void saveBills(OperationBuilder operationBuilder) throws FromBillNotChoosed, ToBillNotChoosed, EqualsBills {
        Bill billFrom = form.billFromField.getValue();
        Bill billTo = form.billToField.getValue();
        if(billFrom == null){
            throw new FromBillNotChoosed();
        }
        if(billTo == null){
            throw new ToBillNotChoosed();
        }
        if(billFrom.equals(billTo))
            throw new EqualsBills();
        operationBuilder.setFromBill(billFrom);
        operationBuilder.setToBill(billTo);
    }

    @Override
    public boolean addOperation(Operation operation) {
        try {
            operation.executeIsNeed();
            OperationDAO.getInstance().saveOrUpdate(operation);
            return true;
        } catch (NegativeBalanceException e) {
            if (MsgWindow.showAsk("На вашем счете недостаточно средств. Добавить перевод?")) {
                OperationDAO.getInstance().saveOrUpdate(operation);
                return true;
            }
        }
        return false;
    }
}
