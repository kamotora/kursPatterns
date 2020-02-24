package controllers;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import util.MsgWindow;

import java.io.IOException;

public class ControllerLogin {
    private Stage window;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    public void login(){
        String login = loginField.getText();
        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByLogin(login);
        if(user == null){
            MsgWindow.showErrorWindow("Такой пользователь не найден");
            return;
        }
        try {
            if (user.checkPassword(passwordField.getText())){
                window.close();
                new Controller(user).showWidnow();
            }
            else{
                MsgWindow.showErrorWindow("Пароль неверный");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            MsgWindow.showErrorWindow("Ошибка!\n"+e);
        }
    }
    public void close(){
        window.close();
    }
    public void registry(){
        new ControllerRegistry(null).showWindow(window);
    }

    public void showWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
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
        window.setTitle("Вход");
        window.setScene(new Scene(root));
        window.show();
    }
}
