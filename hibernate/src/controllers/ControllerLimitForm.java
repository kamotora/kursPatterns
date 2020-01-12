package controllers;

import dao.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Bill;
import model.Limit;
import model.Operation;
import model.User;
import util.MsgWindow;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerLimitForm implements Initializable {
    private Limit limit;
    private Stage curWindow;
    @FXML
    private DatePicker dateStartField;
    @FXML
    private DatePicker dateEndField;
    @FXML
    private TextField sumField;
    @FXML
    private TextArea noteField;
    @FXML
    private Button saveButton;
    @FXML
    private Label infoLabel;
    private User currentUser;
    ControllerLimitForm(Limit Limit, User currentUser){
        this.limit = Limit;
        this.currentUser = currentUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<model.categories.Category> categoryList = new CategoryDAO().getCategoryByType(new TypeCategoryDAO().getExpandType());
        List<Bill> billList = new BillDAO().getAll();
        if(limit != null){
            Date datestart = limit.getDatestart();
            dateStartField.setValue(datestart.toLocalDate());

            Date dateEnd = limit.getDatestart();
            dateEndField.setValue(dateEnd.toLocalDate());

            sumField.setText(String.valueOf(limit.getSum()));
            noteField.setText(limit.getNode());
            saveButton.setText("Изменить");
        }
        else {
            dateStartField.setValue(LocalDate.now());
            dateEndField.setValue(LocalDate.now().plusMonths(1));
            saveButton.setText("Добавить");
            //sumField.setText("0.0");
        }
    }

    public void save(){
        Date dateStart = Date.valueOf(dateStartField.getValue());
        Date dateFinish = Date.valueOf(dateEndField.getValue());
        String note = noteField.getText();
        BigDecimal sum;
        try{
            sum = BigDecimal.valueOf(Double.parseDouble(sumField.getText()));
        }catch (NumberFormatException exp){
            MsgWindow.showErrorWindow("Стоимость указана не верно");
            return;
        }
        if(limit == null){
            limit = new Limit();
            limit.setUser(currentUser);
        }
        limit.setDatestart(dateStart);
        limit.setDateend(dateFinish);
        limit.setSum(sum);
        if(note != null && !note.isEmpty())
            limit.setNode(note);
        new LimitDAO().saveOrUpdate(limit);
        MsgWindow.showInfoWindow("Выполнено!");
        curWindow.close();
    }

    public void updateMsg(){
        LocalDate startDate = dateStartField.getValue();
        LocalDate finishDate = dateEndField.getValue();
        if(startDate != null && finishDate != null){
            if(finishDate.isBefore(startDate)){
                infoLabel.setText("Дата окончания не может быть раньше даты начала");
                return;
            }
            Period period = Period.between(startDate,finishDate);
            LocalDate curStartDate = LocalDate.now().minus(period);
            List<Operation> operations = new OperationDAO().getExpandsFromDate(currentUser,Date.valueOf(curStartDate));
            double sum = 0;
            for(Operation operation : operations)
                sum += operation.getSum().doubleValue();
            infoLabel.setText("За аналогичный период с "+curStartDate+" по сегодня вы потратили: "+String.format("%.2f",sum));
            sumField.setText(String.format("%.2f",sum));
        }
    }

    public void showWindow(Stage mainWindow){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("res/limit.fxml"));
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
        if(limit == null)
            curWindow.setTitle("Добавление лимита");
        else
            curWindow.setTitle("Изменение лимита");
        curWindow.setScene(new Scene(root));
        curWindow.initModality(Modality.WINDOW_MODAL);
        curWindow.initOwner(mainWindow);
        curWindow.showAndWait();
    }

    public void close(){
        curWindow.close();
    }
}
