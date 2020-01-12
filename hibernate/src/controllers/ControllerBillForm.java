package controllers;

import dao.BillDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Bill;
import util.MsgWindow;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerBillForm implements Initializable {
    private Stage curWindow;
    private Bill bill;
    @FXML
    private TextField sumField;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea noteField;
    ControllerBillForm(Bill bill){
        this.bill = bill;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(bill != null){
            nameField.setText(bill.getName());
            noteField.setText(bill.getNote());
            sumField.setText(String.format("%.2f",bill.getSum()));
            sumField.setEditable(false);
        }
    }

    public void save(){
        boolean isNewRow = false;
        if(bill == null) {
            bill = new Bill();
            isNewRow = true;
        }
        if(nameField.getText().isEmpty()){
            MsgWindow.showErrorWindow("Введите название!");
            return;
        }
        bill.setName(nameField.getText());
        try{
            bill.setSum(BigDecimal.valueOf(Double.parseDouble(sumField.getText())));
        }catch (NumberFormatException exp){
            MsgWindow.showErrorWindow("Стоимость указана не верно");
            return;
        }
        bill.setNote(noteField.getText());
        if(isNewRow) {
            new BillDAO().save(bill);
        }
        else
            new BillDAO().save(bill);
        MsgWindow.showInfoWindow("Успешно!");
        curWindow.close();
    }


    public void showWindow(Stage mainWindow){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("res/bill.fxml"));
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
        if(bill == null)
            curWindow.setTitle("Добавление нового счёта");
        else
            curWindow.setTitle("Изменение счёта");
        curWindow.setScene(new Scene(root));
        curWindow.initModality(Modality.WINDOW_MODAL);
        curWindow.initOwner(mainWindow);
        curWindow.showAndWait();
    }
}
