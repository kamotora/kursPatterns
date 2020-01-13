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

public class ProfitTab extends MyTab implements IObserver{
    @Override
    public void createTable(){
        super.createTable();
        TableColumn<Operation, Bill> billFromColumnExpend = new TableColumn<>("Счёт списания");
        billFromColumnExpend.setCellValueFactory(new PropertyValueFactory<>("fromBill"));
        tableView.getColumns().add(billFromColumnExpend);

    }
    @Override
    public void update() {
        super.update(typeCategoryDAO.getProfitType());
    }

}
