package controllers;

import dao.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Bill;
import model.Operation;
import model.Period;
import model.User;
import model.categories.Category;
import util.DateUtil;
import util.MsgWindow;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerProfitForm implements Initializable {
    private Operation operation;
    private Stage curWindow;
    @FXML
    private DatePicker dateField;
    @FXML
    private ChoiceBox<Bill> billToField;
    @FXML
    private ChoiceBox<Category> categoryField;
    @FXML
    private ChoiceBox<Period> periodField;
    @FXML
    private TextField sumField;
    @FXML
    private TextArea noteField;
    @FXML
    private Button saveButton;
    private User currentUser;
    ControllerProfitForm(Operation operation, User currentUser){
        this.operation = operation;
        this.currentUser = currentUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Category> categoryList = new CategoryDAO().getCategoryByType(new TypeCategoryDAO().getProfitType());
        List<Bill> billList = new BillDAO().getAll();
        List<Period> periodList = new PeriodDAO().getAll();
        categoryField.setItems(FXCollections.observableArrayList(categoryList));
        billToField.setItems(FXCollections.observableArrayList(billList));
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
            billToField.setValue(operation.getFromBill());
            if (operation.getPeriod() != null)
                periodField.setValue(operation.getPeriod());
        }
        else {
            dateField.setValue(LocalDate.now());
            if(!categoryList.isEmpty())
                categoryField.setValue(categoryList.get(0));
            if(!billList.isEmpty())
                billToField.setValue(billList.get(0));
            saveButton.setText("Добавить");
            //sumField.setText("0.0");
        }
    }

    public void save(){
        Timestamp date = Timestamp.valueOf(dateField.getValue().atStartOfDay());
        Bill billTo = billToField.getValue();
        if(billTo == null){
            MsgWindow.showErrorWindow("Счёт не выбран");
            return;
        }

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
        boolean isNeedUpdate = true;
        if(operation == null){
            operation = new Operation();
            operation.setUser(currentUser);
            isNeedUpdate = false;
        }
        operation.setDate(date);
        operation.setCategory(category);
        operation.setToBill(billTo);
        operation.setSum(sum);
        operation.setPeriod(period);
        operation.setNextExecuteDateByPeriod();
        if(!note.isEmpty())
            operation.setNode(note);

        //Прибавляем ко счету
        BigDecimal res = billTo.getSum();
        if(date.toLocalDateTime().toLocalDate().isBefore(LocalDate.now()) || date.toLocalDateTime().toLocalDate().isEqual(LocalDate.now()) ){
            res = res.add(sum);
        }
        billTo.setSum(res);
        operation.setDateExecute(Timestamp.valueOf(LocalDateTime.now()));
        new BillDAO().update(billTo);
        if(isNeedUpdate)
            new OperationDAO().update(operation);
        else
            new OperationDAO().save(operation);

        MsgWindow.showInfoWindow("Выполнено!");
        curWindow.close();
    }

    public void addBill(){
        ControllerBillForm controllerBillForm = new ControllerBillForm(null);
        controllerBillForm.showWindow(curWindow);
        initialize(null,null);
    }

    public void addCategory(){
        ControllerCategoryForm controllerCategoryForm = new ControllerCategoryForm(new TypeCategoryDAO().getProfitType());
        controllerCategoryForm.showWindow(curWindow);
        initialize(null,null);
    }

    public void showWindow(Stage mainWindow){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("res/profit.fxml"));
        curWindow = new Stage();
        loader.setController(this);
        Parent root;
        try{
            root = loader.load();
        }catch (IOException ex){
            MsgWindow.showErrorWindow("Не получилось открыть новое окно");
            ex.printStackTrace();
            return;
        }
        if(operation == null)
            curWindow.setTitle("Добавление дохода");
        else
            curWindow.setTitle("Изменение дохода");
        curWindow.setScene(new Scene(root));
        curWindow.initModality(Modality.WINDOW_MODAL);
        curWindow.initOwner(mainWindow);
        curWindow.showAndWait();
    }

    public void close(){
        curWindow.close();
    }
}
