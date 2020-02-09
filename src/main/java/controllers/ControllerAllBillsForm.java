package controllers;

import dao.BillDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Bill;
import util.MsgWindow;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerAllBillsForm implements Initializable {
    private Stage curWindow;
    private BillDAO billDAO;
    @FXML
    private TableView<Bill> tableView;
    @FXML
    private Label sumLabel;
    ControllerAllBillsForm(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        billDAO = new BillDAO();
        createExpendTable();
        updateData();
    }

    public void createExpendTable(){
        // столбец
        TableColumn<Bill, String> nameColumn = new TableColumn<>("Название");
        // определяем фабрику для столбца с привязкой к свойству
        //nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        // добавляем столбец
        tableView.getColumns().add(nameColumn);


        TableColumn<Bill, BigDecimal> sumColumn = new TableColumn<>("Сумма");
        //sumColumn.setCellValueFactory(cellData -> cellData.getValue().sumProperty());
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        tableView.getColumns().add(sumColumn);

        TableColumn<Bill, BigDecimal> expandColumn = new TableColumn<>("Всего убыло");
        expandColumn.setCellValueFactory(new PropertyValueFactory<>("sumExpands"));
        tableView.getColumns().add(expandColumn);

        TableColumn<Bill, BigDecimal> profitColumn = new TableColumn<>("Всего прибыло");
        profitColumn.setCellValueFactory(new PropertyValueFactory<>("sumProfits"));
        tableView.getColumns().add(profitColumn);

        // столбец
        TableColumn<Bill, String> noteColumn = new TableColumn<>("Примечание");
        //noteColumn.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
        tableView.getColumns().add(noteColumn);
    }

    public Bill getSelected(){
        TableView.TableViewSelectionModel<Bill> selectionModel = tableView.getSelectionModel();
        Bill selected = selectionModel.getSelectedItem();
        if(selected == null){
            MsgWindow.showErrorWindow("Выберите строку в таблице для редактирования");
            return null;
        }
        return selected;
    }

    public void updateData(){
        ObservableList<Bill> bills = FXCollections.observableArrayList(billDAO.getAll());
        tableView.setItems(bills);
        double sumBills = 0.0;
        for(Bill bill : bills)
            sumBills += bill.getSum().doubleValue();
        sumLabel.setText(String.format("%.2f",sumBills));
    }

    public void addBill(){
        new ControllerBillForm(null).showWindow(curWindow);
        updateData();
    }

    public void changeBill(){
        Bill bill = getSelected();
        if(bill == null)
            return;
        new ControllerBillForm(bill).showWindow(curWindow);
        updateData();
    }

    public void removeBill(){
        Bill forDelete = getSelected();
        if(MsgWindow.showAsk("Вы точно хотите удалить эту запись?"))
            billDAO.delete(forDelete);
        updateData();
    }
    public void showWindow(Stage mainWindow){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/allBills.fxml"));
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
        curWindow.setTitle("Все счета");
        curWindow.setScene(new Scene(root));
        curWindow.initModality(Modality.WINDOW_MODAL);
        curWindow.initOwner(mainWindow);
        curWindow.showAndWait();
    }
}
