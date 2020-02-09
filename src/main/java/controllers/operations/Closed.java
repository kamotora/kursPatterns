package controllers.operations;

import exceptions.EqualsBills;
import exceptions.FromBillNotChoosed;
import exceptions.ToBillNotChoosed;
import interfaces.IState;
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
    public void setBills() {

    }

    @Override
    public void saveBills(OperationBuilder operationBuilder){

    }

    @Override
    public boolean addOperation(Operation operation) {
        return false;
    }
}
