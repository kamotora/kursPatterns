package controllers;

import dao.CategoryDAO;
import dao.OperationDAO;
import dao.TypeCategoryDAO;
import exceptions.NegativeBalanceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Bill;
import model.Operation;
import model.Period;
import model.User;
import model.categories.Category;
import util.MsgWindow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class ExpendTab extends MyTab implements IObserver{
    @Override
    public void createTable(){
        super.createTable();
        TableColumn<Operation, Bill> billFromColumnExpend = new TableColumn<>("Счёт списания");
        billFromColumnExpend.setCellValueFactory(new PropertyValueFactory<>("fromBill"));
        tableView.getColumns().add(billFromColumnExpend);

    }
    @Override
    public void update() {
        super.update(typeCategoryDAO.getExpandType());
    }
}
