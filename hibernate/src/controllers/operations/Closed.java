package controllers.operations;

import exceptions.EqualsBills;
import exceptions.FromBillNotChoosed;
import exceptions.ToBillNotChoosed;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.Operation;
import model.OperationBuilder;
import model.categories.TypeCategory;

public class Closed implements IState {
    @Override
    public TypeCategory getType() {
        return null;
    }

    @Override
    public Operation getOperation() {
        return null;
    }

    @Override
    public FXMLLoader getWindowLoader(Stage mainWindow) {
        return null;
    }

    @Override
    public void setBills() throws Exception {

    }

    @Override
    public void saveBills(OperationBuilder operationBuilder) throws FromBillNotChoosed, ToBillNotChoosed, EqualsBills {

    }

    @Override
    public boolean addOperation(Operation operation) {
        return false;
    }
}
