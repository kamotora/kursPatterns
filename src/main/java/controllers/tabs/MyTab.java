package controllers.tabs;

import dao.CategoryDAO;
import dao.OperationDAO;
import dao.TypeCategoryDAO;
import exceptions.NegativeBalanceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Operation;
import model.Period;
import model.User;
import model.categories.Category;
import model.categories.TypeCategory;
import util.MsgWindow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class MyTab extends Tab {
    @FXML protected TableView<Operation> tableView;
    @FXML protected PieChart chart;
    @FXML protected Label todayLabel;
    @FXML protected Label weekLabel;
    @FXML protected Label monthLabel;
    @FXML protected Label allLabel;
    @FXML protected Label msg;

    protected OperationDAO operationDAO;
    protected User currentUser;
    protected CategoryDAO categoryDAO;
    protected TypeCategoryDAO typeCategoryDAO;

    public void createTable(){
        operationDAO = OperationDAO.getInstance();
        categoryDAO = new CategoryDAO();
        typeCategoryDAO = TypeCategoryDAO.getInstanse();
        // столбец
        TableColumn<Operation, LocalDate> dateColumnExpand = new TableColumn<>("Дата");
        // определяем фабрику для столбца с привязкой к свойству
        dateColumnExpand.setCellValueFactory(new PropertyValueFactory<>("dateWithoutTime"));
        // добавляем столбец
        tableView.getColumns().add(dateColumnExpand);

        TableColumn<Operation, model.categories.Category> categoryColumnExpand = new TableColumn<>("Категория");
        categoryColumnExpand.setCellValueFactory(new PropertyValueFactory<>("category"));
        tableView.getColumns().add(categoryColumnExpand);

        TableColumn<Operation, BigDecimal> sumColumnExpand = new TableColumn<>("Сумма");
        sumColumnExpand.setCellValueFactory(new PropertyValueFactory<>("sum"));
        tableView.getColumns().add(sumColumnExpand);

        TableColumn<Operation, Period> periodColumnExpand = new TableColumn<>("Повторять");
        periodColumnExpand.setCellValueFactory(new PropertyValueFactory<>("period"));
        tableView.getColumns().add(periodColumnExpand);
    }

    public void update(TypeCategory typeCategory) {
        ObservableList<Operation> oper = FXCollections.observableArrayList(operationDAO.getOperationsByType(currentUser, typeCategory));
        tableView.setItems(oper);

        List<Category> categories = categoryDAO.getCategoryByType(typeCategory);
        chart.getData().clear();
        chart.setLabelsVisible(true);

        double stat[] = {0.0,0.0,0.0,0.0};

        for(Category category : categories){
            Set<Operation> operations = category.getOperations();
            if(!operations.isEmpty()){
                BigDecimal sum = new BigDecimal(0);
                for(Operation operation: operations) {
                    double cur = operation.getSum().doubleValue();

                    LocalDate date = operation.getDate().toLocalDateTime().toLocalDate();
                    if(date.isEqual(LocalDate.now())){
                        stat[0] += cur;
                    }
                    if(date.isAfter(LocalDate.now().minusWeeks(1))) {
                        stat[1] += cur;
                    }

                    if(date.isAfter(LocalDate.now().minusMonths(1))) {
                        stat[2] += cur;
                    }
                    stat[3] += cur;
                    todayLabel.setText(String.format("%.2f руб.", stat[0]));
                    weekLabel.setText(String.format("%.2f руб.", stat[1]));
                    monthLabel.setText(String.format("%.2f руб.", stat[2]));
                    allLabel.setText(String.format("%.2f руб.", stat[3]));

                    try {
                        operation.executeIsNeed();
                        OperationDAO.getInstance().update(operation);
                    }catch (NegativeBalanceException e) {
                        msg.setText("Для счёта "+operation.getFromBill().getName()+" расходы превышают доходы");
                    }
                    sum = sum.add(BigDecimal.valueOf(cur));
                }
                chart.getData().add(new PieChart.Data(category.getName(),sum.doubleValue()));
            }
        }
    }

    public void setTableView(TableView<Operation> tableView) {
        this.tableView = tableView;
    }

    public void setChart(PieChart chart) {
        this.chart = chart;
    }

    public void setTodayLabel(Label todayLabel) {
        this.todayLabel = todayLabel;
    }

    public void setWeekLabel(Label weekLabel) {
        this.weekLabel = weekLabel;
    }

    public void setMonthLabel(Label monthLabel) {
        this.monthLabel = monthLabel;
    }

    public void setAllLabel(Label allLabel) {
        this.allLabel = allLabel;
    }

    public void setMsg(Label msg) {
        this.msg = msg;
    }


    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Operation getSelected(){
        TableView.TableViewSelectionModel<Operation> selectionModel = null;

        selectionModel = tableView.getSelectionModel();
        Operation selected = selectionModel.getSelectedItem();
        if(selected == null){
            MsgWindow.showErrorWindow("Выберите строку в таблице для редактирования");
            return null;
        }
        return selected;
    }

}
