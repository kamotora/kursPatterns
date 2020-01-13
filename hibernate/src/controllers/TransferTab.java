package controllers;

import exceptions.NegativeBalanceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Bill;
import model.Operation;
import model.categories.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class TransferTab extends MyTab implements IObserver{
    @Override
    public void createTable(){
        super.createTable();
        TableColumn<Operation, Bill> billFromColumnExpend = new TableColumn<>("Счёт списания");
        billFromColumnExpend.setCellValueFactory(new PropertyValueFactory<>("fromBill"));
        tableView.getColumns().add(billFromColumnExpend);

        TableColumn<Operation, Bill> billToColumn = new TableColumn<>("Счёт зачисления");
        billToColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        tableView.getColumns().add(billToColumn);

    }
    @Override
    public void update() {
        super.update(typeCategoryDAO.getTransferType());
    }
}
