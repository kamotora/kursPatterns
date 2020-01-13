package controllers;

import controllers.operations.*;
import controllers.tabs.ExpendTab;
import controllers.tabs.ProfitTab;
import controllers.tabs.TransferTab;
import dao.BillDAO;
import dao.CategoryDAO;
import dao.OperationDAO;
import dao.TypeCategoryDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Operation;
import model.User;
import util.MsgWindow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private final CategoryDAO categoryDAO;
    private final BillDAO billDAO;
    private final TypeCategoryDAO typeCategoryDAO;
    private Stage mainWindow;
    private OperationDAO operationDAO;
    private User currentUser;

    @FXML private TableView<Operation> expendTableView;
    @FXML private PieChart expendChart;
    @FXML private Label todayExpendLabel;
    @FXML private Label weekExpendLabel;
    @FXML private Label monthExpendLabel;
    @FXML private Label allExpendLabel;
    @FXML private Label msgExpend;

    @FXML private TableView<Operation> profitTableView;
    @FXML private PieChart profitChart;
    @FXML private Label todayProfitLabel;
    @FXML private Label weekProfitLabel;
    @FXML private Label monthProfitLabel;
    @FXML private Label allProfitLabel;
    @FXML private Label msgProfit;

    @FXML private TableView<Operation> transferTableView;
    @FXML private PieChart transferChart;
    @FXML private Label todayTransferLabel;
    @FXML private Label weekTransferLabel;
    @FXML private Label monthTransferLabel;
    @FXML private Label allTransferLabel;
    @FXML private Label msgTransfer;

    @FXML private ExpendTab expendTab;
    @FXML private ProfitTab profitTab;
    @FXML private TransferTab transferTab;
    private ControllerOperationForm controllerOperationForm;
    public Controller(User user) {
        operationDAO = OperationDAO.getInstance();
        categoryDAO =  new CategoryDAO();
        typeCategoryDAO = TypeCategoryDAO.getInstanse();
        billDAO = new BillDAO();
        this.currentUser = user;
        controllerOperationForm = new ControllerOperationForm(new Closed(), user);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        operationDAO.addObserver(expendTab,typeCategoryDAO.getExpandType());
        expendTab.setCurrentUser(currentUser);
        expendTab.setTodayLabel(todayExpendLabel);
        expendTab.setWeekLabel(weekExpendLabel);
        expendTab.setMonthLabel(monthExpendLabel);
        expendTab.setAllLabel(allExpendLabel);
        expendTab.setChart(expendChart);
        expendTab.setMsg(msgExpend);
        expendTab.setTableView(expendTableView);
        expendTab.createTable();
        expendTab.update();

        operationDAO.addObserver(profitTab,typeCategoryDAO.getProfitType());
        profitTab.setCurrentUser(currentUser);
        profitTab.setTodayLabel(todayProfitLabel);
        profitTab.setWeekLabel(weekProfitLabel);
        profitTab.setMonthLabel(monthProfitLabel);
        profitTab.setAllLabel(allProfitLabel);
        profitTab.setChart(profitChart);
        profitTab.setMsg(msgProfit);
        profitTab.setTableView(profitTableView);
        profitTab.createTable();
        profitTab.update();

        operationDAO.addObserver(transferTab,typeCategoryDAO.getTransferType());
        transferTab.setCurrentUser(currentUser);
        transferTab.setTodayLabel(todayTransferLabel);
        transferTab.setWeekLabel(weekTransferLabel);
        transferTab.setMonthLabel(monthTransferLabel);
        transferTab.setAllLabel(allTransferLabel);
        transferTab.setChart(transferChart);
        transferTab.setMsg(msgTransfer);
        transferTab.setTableView(transferTableView);
        transferTab.createTable();
        transferTab.update();
    }

    public void addExpend(ActionEvent actionEvent) {
        controllerOperationForm.setState(new ExpendFormState(controllerOperationForm,null));
        controllerOperationForm.showWindow(mainWindow);
    }
    public void addProfit(ActionEvent actionEvent){
        controllerOperationForm.setState(new ProfitFormState(controllerOperationForm,null));
        controllerOperationForm.showWindow(mainWindow);
    }
    public void addTransfer(ActionEvent actionEvent){
        controllerOperationForm.setState(new TransferFormState(controllerOperationForm,null));
        controllerOperationForm.showWindow(mainWindow);
    }
    public void addCategory(ActionEvent actionEvent) {
        ControllerCategoryForm controllerCategoryForm = new ControllerCategoryForm();
        controllerCategoryForm.showWindow(mainWindow);
    }

    public void addLimit(ActionEvent actionEvent){
        new ControllerLimitForm(null,currentUser).showWindow(mainWindow);
    }

    public void changeExpand(){
        controllerOperationForm.setState(new ExpendFormState(controllerOperationForm,expendTab.getSelected()));
        controllerOperationForm.showWindow(mainWindow);
    }

    public void deleteExpand(){
        Operation forDelete = expendTab.getSelected();
        if(MsgWindow.showAsk("Вы точно хотите удалить эту запись?"))
            operationDAO.delete(forDelete);
    }

    public void changeProfit(){
        controllerOperationForm.setState(new ProfitFormState(controllerOperationForm,profitTab.getSelected()));
        controllerOperationForm.showWindow(mainWindow);
        }

    public void deleteProfit(){
        Operation forDelete = profitTab.getSelected();
        if(MsgWindow.showAsk("Вы точно хотите удалить эту запись?"))
            operationDAO.delete(forDelete);
    }

    public void changeTransfer(){
        controllerOperationForm.setState(new TransferFormState(controllerOperationForm,transferTab.getSelected()));
        controllerOperationForm.showWindow(mainWindow);
    }

    public void deleteTransfer(){
        Operation forDelete = transferTab.getSelected();
        if(MsgWindow.showAsk("Вы точно хотите удалить эту запись?"))
            operationDAO.delete(forDelete);
    }


    public void changePassword(){
        new ControllerRegistry(currentUser).showWindow(mainWindow);
    }

    public void showAllBills(){
        new ControllerAllBillsForm().showWindow(mainWindow);
    }

    public void showAllCategories(){new ControllerAllCategoriesForm().showWindow(mainWindow);}

    public void showAllLimits(){new ControllerAllLimitsForm().showWindow(mainWindow);}

    public void showWidnow(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("res/sample.fxml"));
        loader.setController(this);
        mainWindow = new Stage();
        Parent root;
        try{
            root = loader.load();
        }catch (IOException ex){
            ex.printStackTrace();
            MsgWindow.showErrorWindow("Не получилось открыть новое окно");
            return;
        }
        mainWindow.setTitle("Домашняя бухгалтерия");
        mainWindow.setScene(new Scene(root));
        mainWindow.show();
    }

}
