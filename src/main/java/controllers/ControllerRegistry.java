package controllers;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.User;
import util.MsgWindow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerRegistry implements Initializable {
    //Если !=null, меняем пароль для него
    private User user;
    private Stage window;
    @FXML
    private Button button;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField oldPassword;
    @FXML
    private PasswordField newPassword1;
    @FXML
    private PasswordField newPassword2;
    ControllerRegistry(User user){
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(user == null){
            button.setText("Создать");
            oldPassword.setStyle("-fx-control-inner-background: LightGrey;");
            oldPassword.setEditable(false);
        }
        else{
            button.setText("Изменить");
            loginField.setText(user.getLogin());
            loginField.setStyle("-fx-control-inner-background: LightGrey;");
            loginField.setEditable(false);
        }

    }

    public void execute(){
        UserDAO  userDAO = new UserDAO();
        if(newPassword1.getText().compareTo(newPassword2.getText()) != 0){
            MsgWindow.showErrorWindow("Новые пароли не совпадают");
            return;
        }
        if(user != null){
            if(!user.checkPassword(oldPassword.getText())){
                MsgWindow.showErrorWindow("Старый пароль не такой");
                return;
            }
            user.hashAndSetPass(newPassword1.getText());
            userDAO.update(user);
        }
        else{
            try{
                userDAO.addUser(loginField.getText(), newPassword1.getText());
            }catch (Exception e){
                e.printStackTrace();
                MsgWindow.showErrorWindow("Ошибка!\n"+e);
                return;
            }
        }
        MsgWindow.showInfoWindow("Успех!");
        window.close();
    }

    public void showWindow(Stage mainWindow){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/registry.fxml"));
        window = new Stage();
        loader.setController(this);
        Parent root;
        try{
            root = loader.load();
        }catch (IOException ex){
            MsgWindow.showErrorWindow("Не получилось открыть новое окно");
            ex.printStackTrace();
            return;
        }
        if(user == null)
            window.setTitle("Добавление нового пользователя");
        else
            window.setTitle("Смена пароля");
        window.setScene(new Scene(root));
        window.initModality(Modality.WINDOW_MODAL);
        window.initOwner(mainWindow);
        window.show();
    }
    public void close(){
        window.close();
    }
}
