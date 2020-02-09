package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class MsgWindow {
    static Alert alert;
    private static MsgWindow window = null;

    public MsgWindow() {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Инфо");
        alert.setHeaderText(null);
        alert.setContentText("Hello World!");
    }

    public static void showInfoWindow(String msg){
        getInstance();
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setTitle("Инфо");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void showErrorWindow(String msg){
        getInstance();
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static boolean showAsk(String ask){
        getInstance();
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Вопрос");
        alert.setContentText(ask);
        Optional<ButtonType> option = alert.showAndWait();

        return option.isPresent() && option.get() == ButtonType.OK;
    }

    public static MsgWindow getInstance(){
        if(window == null)
            window = new MsgWindow();
        return window;
    }

}
