package controllers.operations;

import controllers.ControllerBillForm;
import controllers.ControllerCategoryForm;
import dao.*;
import exceptions.EqualsBills;
import exceptions.FromBillNotChoosed;
import exceptions.ToBillNotChoosed;
import interfaces.IState;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import model.categories.Category;
import util.DateUtil;
import util.MsgWindow;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public class ControllerOperationForm{
    Stage curWindow;
    @FXML DatePicker dateField;
    @FXML ChoiceBox<Bill> billToField;
    @FXML ChoiceBox<Bill> billFromField;
    @FXML ChoiceBox<Category> categoryField;
    @FXML ChoiceBox<Period> periodField;
    @FXML TextField sumField;
    @FXML TextArea noteField;
    @FXML Button saveButton;
    private IState state;
    private Operation operation;
    private User currentUser;
    public ControllerOperationForm(IState state, User user){
        this.state = state;
        this.operation = state.getOperation();
        this.currentUser = user;
    }


    public void save() {
        Timestamp date = Timestamp.valueOf(dateField.getValue().atStartOfDay());
        Category category = categoryField.getValue();
        if(category == null){
            MsgWindow.showErrorWindow("Категория не выбрана");
            return;
        }
        Period period= periodField.getValue();
        if(period == null){
            MsgWindow.showErrorWindow("Период повторения не выбран");
            return;
        }
        String note = noteField.getText();
        BigDecimal sum;
        try{
            sum = BigDecimal.valueOf(Double.parseDouble(sumField.getText()));
        }catch (NumberFormatException exp){
            MsgWindow.showErrorWindow("Стоимость указана не верно");
            return;
        }
        OperationBuilder operationBuilder = new OperationBuilder(operation);
        try {
            state.saveBills(operationBuilder);
        } catch (FromBillNotChoosed | ToBillNotChoosed | EqualsBills ex) {
            MsgWindow.showErrorWindow(ex.toString());
        }
        operationBuilder.setUser(currentUser);
        operationBuilder.setDate(date);
        operationBuilder.setCategory(category);
        operationBuilder.setSum(sum);
        operationBuilder.setPeriod(period);
        operationBuilder.setNextExecute();
        operationBuilder.setNode(note);
        try {
            if(state.addOperation(operationBuilder.create())) {
                MsgWindow.showInfoWindow("Выполнено!");
                curWindow.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgWindow.showErrorWindow(e.toString());
        }
    }

    public void addBill(){
        ControllerBillForm controllerBillForm = new ControllerBillForm(null);
        controllerBillForm.showWindow(curWindow);
        try {
            state.setBills();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCategory(){
        ControllerCategoryForm controllerCategoryForm = new ControllerCategoryForm(state.getType());
        controllerCategoryForm.showWindow(curWindow);
        updateCategoryList();
    }

    public void updateCategoryList(){
        List<Category> categoryList = new CategoryDAO().getCategoryByType(state.getType());
        categoryField.setItems(FXCollections.observableArrayList(categoryList));
        if(!categoryList.isEmpty())
            categoryField.setValue(categoryList.get(0));
    }
    public void showWindow(Stage mainWindow){
        operation = state.getOperation();
        FXMLLoader loader = state.getWindowLoader(mainWindow);
        loader.setController(this);
        Parent root;
        try{
            root = loader.load();
        }catch (IOException ex){
            MsgWindow.showErrorWindow("Не получилось открыть новое окно");
            ex.printStackTrace();
            return;
        }
        curWindow = new Stage();
        if(operation == null)
            curWindow.setTitle("Добавление "+state.getType().getName()+"a");
        else
            curWindow.setTitle("Изменение "+state.getType().getName()+"a");
        curWindow.setScene(new Scene(root));
        curWindow.initModality(Modality.WINDOW_MODAL);
        curWindow.initOwner(mainWindow);
        updateCategoryList();
        List<Period> periodList = new PeriodDAO().getAll();
        try {
            state.setBills();
        } catch (Exception e) {
            e.printStackTrace();
        }
        periodField.setItems(FXCollections.observableArrayList(periodList));
        if(!periodList.isEmpty())
            periodField.setValue(periodList.get(3));
        if(operation != null){
            Timestamp date = operation.getDate();
            dateField.setValue(LocalDate.of(DateUtil.getYear(date),DateUtil.getMonth(date), DateUtil.getDay(date)));
            sumField.setText(String.valueOf(operation.getSum()));
            noteField.setText(operation.getNode());
            saveButton.setText("Изменить");
            categoryField.setValue(operation.getCategory());
            if (operation.getPeriod() != null)
                periodField.setValue(operation.getPeriod());
        }
        else {
            dateField.setValue(LocalDate.now());
            saveButton.setText("Добавить");
        }

        curWindow.showAndWait();
    }

    public void close(){
        curWindow.close();
    }

    public void setState(IState state) {
        this.state = state;
    }
}
