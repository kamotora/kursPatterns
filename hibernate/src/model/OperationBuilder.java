package model;

import util.MsgWindow;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OperationBuilder {

    private Timestamp date;
    private int pkOperation;
    private BigDecimal sum;
    private String node;
    private Timestamp nextexecute;
    private byte[] icon;
    private model.categories.Category category;
    private Bill fromBill;
    private Bill toBill;
    private User user;
    private Period period;
    private Operation operation;

    public OperationBuilder() {
        operation = null;
    }


    public OperationBuilder(Operation operation) {
        this.operation = operation;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setDate(LocalDate date) {
        this.date = Timestamp.valueOf(date.atStartOfDay());
    }
    public void setPkOperation(int pkOperation) {
        this.pkOperation = pkOperation;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public void setSum(String sum) {
        try{
            this.sum = BigDecimal.valueOf(Double.parseDouble(sum));
        }catch (NumberFormatException exp){
            MsgWindow.showErrorWindow("Стоимость указана не верно");
        }
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void setNextExecute() {
        LocalDate localDate = date.toLocalDateTime().toLocalDate();
        if (period == null)
            return;
        switch (period.getPkPeriod()){
            case 1:
                nextexecute = Timestamp.valueOf(localDate.plusDays(1).atStartOfDay());
                break;
            case 2:
                nextexecute = Timestamp.valueOf(localDate.plusWeeks(1).atStartOfDay());
                break;
            case 3:
                nextexecute = Timestamp.valueOf(localDate.plusMonths(1).atStartOfDay());
                break;
            case 4:
                nextexecute = null;
                break;
            default:
                System.out.println("Неизвестный период");
        }
    }

    public void setNextExecute(Timestamp nextexecute) {
        this.nextexecute = nextexecute;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public void setCategory(model.categories.Category category) {
        this.category = category;
    }

    public void setFromBill(Bill fromBill) {
        this.fromBill = fromBill;
    }

    public void setToBill(Bill toBill) {
        this.toBill = toBill;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void setPeriod(Period period) {
        this.period = period;
    }

    public Operation create() throws Exception {
        if(period != null){
            switch (period.getPkPeriod()){
                case 1:
                    nextexecute = Timestamp.valueOf(date.toLocalDateTime().plusDays(1));
                case 2:
                    nextexecute = Timestamp.valueOf(date.toLocalDateTime().plusWeeks(1));
                    break;
                case 3:
                    nextexecute = Timestamp.valueOf(date.toLocalDateTime().plusMonths(1));
                    break;
                case 4:
                    nextexecute = null;
                    break;
                default:
                    throw new Exception("Неизвестный тип периода");
            }
        }

        if(date == null)
            date = Timestamp.valueOf(LocalDateTime.now());

        if(user == null)
            throw new Exception("Пользователь не указан");
        if(category == null)
            throw new Exception("Категория не указана");
        if(toBill == null && fromBill == null)
            throw new Exception("Счёт не указан");
        if(toBill == fromBill)
            throw new Exception("Одинаковые счета");

        if(operation == null)
            operation = new Operation(date,sum,node,nextexecute,category,fromBill,toBill,user,period);
        else {
            operation.setUser(user);
            operation.setDate(date);
            operation.setCategory(category);
            operation.setFromBill(fromBill);
            operation.setToBill(toBill);
            operation.setSum(sum);
            operation.setPeriod(period);
            operation.setNextExecuteDateByPeriod();
            operation.setNode(node);
        }
        return operation;
    }

    public boolean isNewOperation(){
        return operation == null;
    }
}
