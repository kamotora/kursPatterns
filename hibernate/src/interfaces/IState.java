package interfaces;

import exceptions.EqualsBills;
import exceptions.FromBillNotChoosed;
import exceptions.ToBillNotChoosed;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.Operation;
import model.OperationBuilder;
import model.categories.TypeCategory;

public interface IState {
    public TypeCategory getType();
    public Operation getOperation();
    public FXMLLoader getWindowLoader(Stage mainWindow);
    public void setBills() throws Exception;
    public void saveBills(OperationBuilder operationBuilder) throws FromBillNotChoosed, ToBillNotChoosed, EqualsBills;
    public boolean addOperation(Operation operation);
}
