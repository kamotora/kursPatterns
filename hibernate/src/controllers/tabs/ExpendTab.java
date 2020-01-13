package controllers.tabs;

import interfaces.IObserver;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Bill;
import model.Operation;

public class ExpendTab extends MyTab implements IObserver {
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
