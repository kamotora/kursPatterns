package controllers.operations;

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
        // При данном состоянии не выполняется никаких действий
        throw new UnsupportedOperationException("При данном состоянии не выполняется никаких действий");
    }

    @Override
    public void saveBills(OperationBuilder operationBuilder){
        // При данном состояние не выполняется никаких действий
        throw new UnsupportedOperationException("При данном состояние не выполняется никаких действий");
    }

    @Override
    public boolean addOperation(Operation operation) {
        return false;
    }
}
