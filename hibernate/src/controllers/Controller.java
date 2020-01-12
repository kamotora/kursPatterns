package controllers;

import dao.BillDAO;
import dao.CategoryDAO;
import dao.OperationDAO;
import dao.TypeCategoryDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Bill;
import model.Operation;
import model.Period;
import model.User;
import model.categories.Category;
import model.categories.TypeCategory;
import util.MsgWindow;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

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
    public Controller(User user) {
        operationDAO = new OperationDAO();
        categoryDAO =  new CategoryDAO();
        typeCategoryDAO = new TypeCategoryDAO();
        billDAO = new BillDAO();
        this.currentUser = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createTables();
        updateInfo();
    }

    public void createTables(){
        // столбец
        TableColumn<Operation, LocalDate> dateColumnExpand = new TableColumn<>("Дата");
        // определяем фабрику для столбца с привязкой к свойству
        dateColumnExpand.setCellValueFactory(new PropertyValueFactory<>("dateWithoutTime"));
        // добавляем столбец
        expendTableView.getColumns().add(dateColumnExpand);
        TableColumn<Operation, LocalDate> dateColumnProfit = new TableColumn<>("Дата");
        dateColumnProfit.setCellValueFactory(new PropertyValueFactory<>("dateWithoutTime"));
        profitTableView.getColumns().add(dateColumnProfit);
        TableColumn<Operation, LocalDate> dateColumnTransfer = new TableColumn<>("Дата");
        dateColumnTransfer.setCellValueFactory(new PropertyValueFactory<>("dateWithoutTime"));
        transferTableView.getColumns().add(dateColumnTransfer);

        TableColumn<Operation, model.categories.Category> categoryColumnExpand = new TableColumn<>("Категория");
        categoryColumnExpand.setCellValueFactory(new PropertyValueFactory<>("category"));
        expendTableView.getColumns().add(categoryColumnExpand);
        TableColumn<Operation, model.categories.Category> categoryColumnProfit = new TableColumn<>("Категория");
        categoryColumnProfit.setCellValueFactory(new PropertyValueFactory<>("category"));
        profitTableView.getColumns().add(categoryColumnProfit);
        TableColumn<Operation, model.categories.Category> categoryColumnTransfer = new TableColumn<>("Категория");
        categoryColumnTransfer.setCellValueFactory(new PropertyValueFactory<>("category"));
        transferTableView.getColumns().add(categoryColumnTransfer);

        TableColumn<Operation, BigDecimal> sumColumnExpand = new TableColumn<>("Сумма");
        sumColumnExpand.setCellValueFactory(new PropertyValueFactory<>("sum"));
        expendTableView.getColumns().add(sumColumnExpand);
        TableColumn<Operation, BigDecimal> sumColumnProfit = new TableColumn<>("Сумма");
        sumColumnProfit.setCellValueFactory(new PropertyValueFactory<>("sum"));
        profitTableView.getColumns().add(sumColumnProfit);
        TableColumn<Operation, BigDecimal> sumColumnTransfer = new TableColumn<>("Сумма");
        sumColumnTransfer.setCellValueFactory(new PropertyValueFactory<>("sum"));
        transferTableView.getColumns().add(sumColumnTransfer);

        TableColumn<Operation, Bill> billFromColumnExpend = new TableColumn<>("Счёт списания");
        billFromColumnExpend.setCellValueFactory(new PropertyValueFactory<>("fromBill"));
        expendTableView.getColumns().add(billFromColumnExpend);
        TableColumn<Operation, Bill> billFromColumnTransfer = new TableColumn<>("Счёт списания");
        billFromColumnTransfer.setCellValueFactory(new PropertyValueFactory<>("fromBill"));
        transferTableView.getColumns().add(billFromColumnTransfer);

        TableColumn<Operation, Bill> billToColumnProfit = new TableColumn<>("Счёт назначения");
        billToColumnProfit.setCellValueFactory(new PropertyValueFactory<>("toBill"));
        profitTableView.getColumns().add(billToColumnProfit);
        TableColumn<Operation, Bill> billToColumnTransfer = new TableColumn<>("Счёт назначения");
        billToColumnTransfer.setCellValueFactory(new PropertyValueFactory<>("toBill"));
        transferTableView.getColumns().add(billToColumnTransfer);

        TableColumn<Operation, Period> periodColumnExpand = new TableColumn<>("Повторять");
        periodColumnExpand.setCellValueFactory(new PropertyValueFactory<>("period"));
        expendTableView.getColumns().add(periodColumnExpand);
        TableColumn<Operation, Period> periodColumnProfit = new TableColumn<>("Повторять");
        periodColumnProfit.setCellValueFactory(new PropertyValueFactory<>("period"));
        profitTableView.getColumns().add(periodColumnProfit);
        TableColumn<Operation, Period> periodColumnTransfer = new TableColumn<>("Повторять");
        periodColumnTransfer.setCellValueFactory(new PropertyValueFactory<>("period"));
        transferTableView.getColumns().add(periodColumnTransfer);

    }

    /**
     * Обновление информации о расходах, доходах, переводах
     * */
    public void updateInfo(){
          /*
          Обновляем таблицы
          */
        ObservableList<Operation> expends = FXCollections.observableArrayList(operationDAO.getExpands(currentUser));
        expendTableView.setItems(expends);

        ObservableList<Operation> profits = FXCollections.observableArrayList(operationDAO.getProfits(currentUser));
        profitTableView.setItems(profits);

        ObservableList<Operation> transfer = FXCollections.observableArrayList(operationDAO.getTransfers(currentUser));
        transferTableView.setItems(transfer);
        /*
          Обновляем графики
          */

        List<Category> categories = categoryDAO.getAll();
        expendChart.getData().clear();
        expendChart.setLabelsVisible(true);

        profitChart.getData().clear();
        profitChart.setLabelsVisible(true);

        transferChart.getData().clear();
        transferChart.setLabelsVisible(true);

        for(Category category : categories){
            Set<Operation> operations = category.getOperations();
            if(!operations.isEmpty()){
                BigDecimal sum = new BigDecimal(0);
                for(Operation operation: operations)
                    sum = sum.add(operation.getSum());
                if(category.getType().equals(typeCategoryDAO.getExpandType()))
                    expendChart.getData().add(new PieChart.Data(category.getName(),sum.doubleValue()));
                else if(category.getType().equals(typeCategoryDAO.getProfitType()))
                    profitChart.getData().add(new PieChart.Data(category.getName(),sum.doubleValue()));
                else if(category.getType().equals(typeCategoryDAO.getTransferType()))
                    transferChart.getData().add(new PieChart.Data(category.getName(),sum.doubleValue()));

            }
        }

        //Обновляем статистику по периодам и выполняем операции, если пришло время
        updateOperations(operationDAO.getAllByUser(currentUser));
    }
    /**
     * Обновляем статистику по периодам и выполняем операции, если пришло время
     * */
    public void updateOperations(List<Operation> operations){
        //Статистика за сегодня, неделю, месяц, все время для каждого типа
        double expands[] = new double[4];
        double profits[] = new double[4];
        double transfers[] = new double[4];
        for(Operation operation : operations){
            /*
              Обновляем статистику за сегодня, неделю, месяц
              */
            TypeCategory type = operation.getCategory().getType();
            double cur = operation.getSum().doubleValue();
            LocalDate date = operation.getDate().toLocalDateTime().toLocalDate();
            if(date.isEqual(LocalDate.now())){
                if(type.equals(typeCategoryDAO.getExpandType()))
                    expands[0] += cur;
                else if(type.equals(typeCategoryDAO.getProfitType()))
                    profits[0] += cur;
                else if(type.equals(typeCategoryDAO.getTransferType()))
                    transfers[0] += cur;
            }
            if(date.isAfter(LocalDate.now().minusWeeks(1)))
            {
                if(type.equals(typeCategoryDAO.getExpandType()))
                    expands[1] += cur;
                else if(type.equals(typeCategoryDAO.getProfitType()))
                    profits[1] += cur;
                else if(type.equals(typeCategoryDAO.getTransferType()))
                    transfers[1] += cur;
            }

            if(date.isAfter(LocalDate.now().minusMonths(1)))
            {
                if(type.equals(typeCategoryDAO.getExpandType()))
                    expands[2] += cur;
                else if(type.equals(typeCategoryDAO.getProfitType()))
                    profits[2] += cur;
                else if(type.equals(typeCategoryDAO.getTransferType()))
                    transfers[2] += cur;
            }

            if(type.equals(typeCategoryDAO.getExpandType())){
                expands[3] += cur;
                todayExpendLabel.setText(String.format("%.2f руб.", expands[0]));
                weekExpendLabel.setText(String.format("%.2f руб.", expands[1]));
                monthExpendLabel.setText(String.format("%.2f руб.", expands[2]));
                allExpendLabel.setText(String.format("%.2f руб.", expands[3]));
            }
            else if(type.equals(typeCategoryDAO.getProfitType())) {
                profits[3] += cur;
                todayProfitLabel.setText(String.format("%.2f руб.", profits[0]));
                weekProfitLabel.setText(String.format("%.2f руб.", profits[1]));
                monthProfitLabel.setText(String.format("%.2f руб.", profits[2]));
                allProfitLabel.setText(String.format("%.2f руб.", profits[3]));
            }
            else if(type.equals(typeCategoryDAO.getTransferType())) {
                transfers[3] += cur;
                todayTransferLabel.setText(String.format("%.2f руб.", transfers[0] ));
                weekTransferLabel.setText(String.format("%.2f руб.", transfers[1] ));
                monthTransferLabel.setText(String.format("%.2f руб.", transfers[2] ));
                allTransferLabel.setText(String.format("%.2f руб.", transfers[3] ));
            }

            /*
              Выполняем операцию, если нужно
              */
            LocalDate executeDate = operation.getDateExecuteWithoutTime();
            LocalDate nextExecuteDate = operation.getNextDateExecuteWithoutTime();
            if(operation.isNeedExecuteNow()){
                BigDecimal resFrom = null, resTo = null;
                if(operation.getFromBill() !=  null)
                    resFrom = operation.getFromBill().getSum().subtract(operation.getSum());
                if(operation.getToBill() !=  null)
                    resTo = operation.getFromBill().getSum().add(operation.getSum());
                if(resFrom != null && resFrom.doubleValue() < 0){
                    if (type == typeCategoryDAO.getExpandType())
                        msgExpend.setText("Ваши расходы превышают доходы!");
                    else if(type == typeCategoryDAO.getTransferType())
                        msgTransfer.setText("Для счёта "+operation.getFromBill().getName()+" расходы превышают доходы");
                }
                if(resFrom != null){
                    operation.getFromBill().setSum(resFrom);
                    billDAO.update(operation.getFromBill());
                }
                if(resTo != null) {
                    operation.getToBill().setSum(resTo);
                    billDAO.update(operation.getToBill());
                }
                if(nextExecuteDate != null && (nextExecuteDate.isBefore(LocalDate.now()) || nextExecuteDate.isEqual(LocalDate.now()))){
                    operation.setNextExecuteDateByPeriod();
                    operation.setDate(operation.getNextexecute());
                }
                operation.setDateExecute(Timestamp.valueOf(LocalDateTime.now()));
                operationDAO.update(operation);
            }
        }
    }
    public void addExpend(ActionEvent actionEvent) throws IOException {
        ControllerExpendForm controllerExpendForm = new ControllerExpendForm(null,currentUser);
        controllerExpendForm.showWindow(mainWindow);
        updateInfo();
    }
    public void addProfit(ActionEvent actionEvent) throws IOException {
        ControllerProfitForm controllerProfitForm = new ControllerProfitForm(null,currentUser);
        controllerProfitForm.showWindow(mainWindow);
        updateInfo();
    }
    public void addTransfer(ActionEvent actionEvent) throws IOException {
        ControllerTransferForm controllerTransferForm = new ControllerTransferForm(null,currentUser);
        controllerTransferForm.showWindow(mainWindow);
        updateInfo();
    }
    public void addCategory(ActionEvent actionEvent) {
        ControllerCategoryForm controllerCategoryForm = new ControllerCategoryForm();
        controllerCategoryForm.showWindow(mainWindow);

    }

    public void addLimit(ActionEvent actionEvent){
        new ControllerLimitForm(null,currentUser).showWindow(mainWindow);
    }

    public Operation getSelected(TypeCategory type){
        TableView.TableViewSelectionModel<Operation> selectionModel = null;
        if(type.equals(typeCategoryDAO.getExpandType()))
            selectionModel = expendTableView.getSelectionModel();
        else if(type.equals(typeCategoryDAO.getProfitType()))
            selectionModel = profitTableView.getSelectionModel();
        else if(type.equals(typeCategoryDAO.getTransferType()))
            selectionModel = transferTableView.getSelectionModel();
        Operation selected = selectionModel.getSelectedItem();
        if(selected == null){
            MsgWindow.showErrorWindow("Выберите строку в таблице для редактирования");
            return null;
        }
        return selected;
    }

    public void changeExpand(){
        new ControllerExpendForm(getSelected(typeCategoryDAO.getExpandType()),currentUser).showWindow(mainWindow);
        updateInfo();
    }

    public void deleteExpand(){
        Operation forDelete = getSelected(typeCategoryDAO.getExpandType());
        if(MsgWindow.showAsk("Вы точно хотите удалить эту запись?"))
            operationDAO.delete(forDelete);
        updateInfo();
    }

    public void changeProfit(){
        new ControllerProfitForm(getSelected(typeCategoryDAO.getProfitType()),currentUser).showWindow(mainWindow);
        updateInfo();
    }

    public void deleteProfit(){
        Operation forDelete = getSelected(typeCategoryDAO.getProfitType());
        if(MsgWindow.showAsk("Вы точно хотите удалить эту запись?"))
            operationDAO.delete(forDelete);
        updateInfo();
    }

    public void changeTransfer(){
        new ControllerTransferForm(getSelected(typeCategoryDAO.getTransferType()),currentUser).showWindow(mainWindow);
        updateInfo();
    }

    public void deleteTransfer(){
        Operation forDelete = getSelected(typeCategoryDAO.getTransferType());
        if(MsgWindow.showAsk("Вы точно хотите удалить эту запись?"))
            operationDAO.delete(forDelete);
        updateInfo();
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
