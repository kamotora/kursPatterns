package controllers.tabs;

import interfaces.IObserver;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Bill;
import model.Operation;

public class TransferTab extends MyTab implements IObserver {
    @Override
    public void createTable(){
        super.createTable();
        TableColumn<Operation, Bill> billFromColumnExpend = new TableColumn<>("Счёт списания");
        billFromColumnExpend.setCellValueFactory(new PropertyValueFactory<>("fromBill"));
        tableView.getColumns().add(billFromColumnExpend);

        TableColumn<Operation, Bill> billToColumn = new TableColumn<>("Счёт зачисления");
        billToColumn.setCellValueFactory(new PropertyValueFactory<>("toBill"));
        tableView.getColumns().add(billToColumn);

    }
    @Override
    public void update() {
        super.update(typeCategoryDAO.getTransferType());
    }
}
