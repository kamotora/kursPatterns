package controllers;

import dao.CategoryDAO;
import dao.LimitDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Limit;
import model.categories.Category;
import model.categories.TypeCategory;
import util.MsgWindow;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ControllerAllLimitsForm implements Initializable {
    private Stage curWindow;
    private LimitDAO limitDAO;
    @FXML
    private TableView<Limit> tableView;
    ControllerAllLimitsForm(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        limitDAO = new LimitDAO();
        createExpendTable();
        updateData();
    }

    public void createExpendTable(){
        TableColumn<Limit, LocalDate> dateStartColumn = new TableColumn<>("Дата начала");
        dateStartColumn.setCellValueFactory(new PropertyValueFactory<>("dateSt"));
        tableView.getColumns().add(dateStartColumn);

        TableColumn<Limit, LocalDate> dateFinishColumn = new TableColumn<>("Дата окончания");
        dateFinishColumn.setCellValueFactory(new PropertyValueFactory<>("dateFinish"));
        tableView.getColumns().add(dateFinishColumn);

        TableColumn<Limit, BigDecimal> sumColumn = new TableColumn<>("Сумма лимита");
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        tableView.getColumns().add(sumColumn);

        TableColumn<Limit, Double> sumExpandColumn = new TableColumn<>("Расходов за период");
        sumExpandColumn.setCellValueFactory(new PropertyValueFactory<>("sumExpand"));
        tableView.getColumns().add(sumExpandColumn);
        TableColumn<Limit, Double> sumProfitColumn = new TableColumn<>("Доходов за период");
        sumProfitColumn.setCellValueFactory(new PropertyValueFactory<>("sumProfit"));
        tableView.getColumns().add(sumProfitColumn);
        TableColumn<Limit, Double> sumToEndColumn = new TableColumn<>("Осталось");
        sumToEndColumn.setCellValueFactory(new PropertyValueFactory<>("sumToEnd"));
        tableView.getColumns().add(sumToEndColumn);

        TableColumn<Limit, String> noteColumn = new TableColumn<>("Примечание");
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("node"));
        tableView.getColumns().add(noteColumn);

    }

    public Limit getSelected(){
        TableView.TableViewSelectionModel<Limit> selectionModel = tableView.getSelectionModel();
        Limit selected = selectionModel.getSelectedItem();
        if(selected == null){
            MsgWindow.showErrorWindow("Выберите строку в таблице для редактирования");
            return null;
        }
        return selected;
    }

    public void updateData(){
        ObservableList<Limit> limits = FXCollections.observableArrayList(limitDAO.getAll());
        tableView.setItems(limits);
    }

    public void addLimit(){
        new ControllerCategoryForm().showWindow(curWindow);
        updateData();
    }

    public void changeLimit(){
        Limit limit = getSelected();
        if(limit == null)
            return;
        new ControllerLimitForm(limit,limit.getUser()).showWindow(curWindow);
        updateData();
    }

    public void removeLimit(){
        Limit limit = getSelected();
        if(MsgWindow.showAsk("Вы точно хотите удалить эту запись?"))
            limitDAO.delete(limit);
        updateData();
    }
    public void showWindow(Stage mainWindow){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/allLimits.fxml"));
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
        curWindow.setTitle("Все лимиты");
        curWindow.setScene(new Scene(root));
        curWindow.initModality(Modality.WINDOW_MODAL);
        curWindow.initOwner(mainWindow);
        curWindow.showAndWait();
    }
}
