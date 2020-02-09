package controllers;

import dao.CategoryDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Bill;
import model.categories.Category;
import model.categories.TypeCategory;
import util.MsgWindow;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerAllCategoriesForm implements Initializable {
    private Stage curWindow;
    private CategoryDAO categoryDAO;
    @FXML
    private TableView<Category> tableView;
    ControllerAllCategoriesForm(){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryDAO = new CategoryDAO();
        createExpendTable();
        updateData();
    }

    public void createExpendTable(){
        TableColumn<Category, TypeCategory> typeColumn = new TableColumn<>("Тип");
        //sumColumn.setCellValueFactory(cellData -> cellData.getValue().sumProperty());
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableView.getColumns().add(typeColumn);
        // столбец
        TableColumn<Category, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.getColumns().add(nameColumn);
    }

    public Category getSelected(){
        TableView.TableViewSelectionModel<Category> selectionModel = tableView.getSelectionModel();
        Category selected = selectionModel.getSelectedItem();
        if(selected == null){
            MsgWindow.showErrorWindow("Выберите строку в таблице для редактирования");
            return null;
        }
        return selected;
    }

    public void updateData(){
        ObservableList<Category> categories = FXCollections.observableArrayList(categoryDAO.getAll());
        tableView.setItems(categories);
    }

    public void addCategory(){
        new ControllerCategoryForm().showWindow(curWindow);
        updateData();
    }

    public void changeCategory(){
        Category category = getSelected();
        if(category == null)
            return;
        new ControllerCategoryForm(category).showWindow(curWindow);
        updateData();
    }

    public void removeCategory(){
        Category category = getSelected();
        if(MsgWindow.showAsk("Вы точно хотите удалить эту запись?"))
            categoryDAO.delete(category);
        updateData();
    }
    public void showWindow(Stage mainWindow){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/allCategories.fxml"));
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
        curWindow.setTitle("Все категории");
        curWindow.setScene(new Scene(root));
        curWindow.initModality(Modality.WINDOW_MODAL);
        curWindow.initOwner(mainWindow);
        curWindow.showAndWait();
    }
}
