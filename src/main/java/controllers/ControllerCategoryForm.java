package controllers;

import dao.CategoryDAO;
import dao.CategoryFactory;
import dao.TypeCategoryDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.categories.Category;
import model.categories.TypeCategory;
import util.MsgWindow;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerCategoryForm implements Initializable {
    private TypeCategory typeCategory ;
    private Stage curWindow;
    private Category category;
    @FXML
    private ChoiceBox<TypeCategory> typeField;
    @FXML
    private TextField nameField;
    ControllerCategoryForm(){
        this.category = null;
        this.typeCategory = null;
    }
    ControllerCategoryForm(Category category){
        this.category = category;
    }
    public ControllerCategoryForm(TypeCategory typeCategory){
        this.typeCategory = typeCategory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<TypeCategory> types = TypeCategoryDAO.getInstanse().getAll();
        typeField.setItems(FXCollections.observableArrayList(types));
        if(category != null){
            nameField.setText(category.getName());
            typeField.setValue(category.getType());
        }else if(typeCategory != null){
            typeField.setValue(typeCategory);
            typeField.setDisable(true);
        }
            else{
            typeField.setValue(types.get(0));
        }
    }

    public void save(){
        if(nameField.getText().isEmpty()){
            MsgWindow.showErrorWindow("Введите название!");
            return;
        }
        if(typeField.getValue() == null)
        {
            MsgWindow.showErrorWindow("Введите дату!");
            return;
        }
        CategoryFactory.getInstance().getCategory(typeField.getValue(),nameField.getText());
        MsgWindow.showInfoWindow("Успешно!");
        curWindow.close();
    }
    public void close(){
        curWindow.close();
    }
    public void showWindow(Stage mainWindow){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/category.fxml"));
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
        if(category == null)
            curWindow.setTitle("Добавление новой категории");
        else
            curWindow.setTitle("Изменение категории");
        curWindow.setScene(new Scene(root));
        curWindow.initModality(Modality.WINDOW_MODAL);
        curWindow.initOwner(mainWindow);
        curWindow.showAndWait();
    }
}
