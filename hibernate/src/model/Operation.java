package model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class Operation {
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
    private Timestamp dateExecute;
    private LocalDate dateWithoutTime;

    public Operation() {
    }

    public Operation(Timestamp date, BigDecimal sum, String node, Timestamp nextexecute, model.categories.Category category, Bill fromBill, Bill toBill, User user, Period period){
        this.sum = sum;
        this.node = node;
        this.nextexecute = nextexecute;
        this.category = category;
        this.fromBill = fromBill;
        this.toBill = toBill;
        this.user = user;
        this.period = period;
        setDate(date);
    }
    @Basic
    @Column(name = "date", nullable = false)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
        dateWithoutTime = date.toLocalDateTime().toLocalDate();
    }

    @Id
    @Column(name = "pk_operation", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public int getPkOperation() {
        return pkOperation;
    }

    public void setPkOperation(int pkOperation) {
        this.pkOperation = pkOperation;
    }

    @Basic
    @Column(name = "sum", nullable = false, precision = 2)
    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    @Basic
    @Column(name = "node", nullable = true, length = -1)
    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    @Basic
    @Column(name = "nextexecute", nullable = true)
    public Timestamp getNextexecute() {
        return nextexecute;
    }

    public void setNextexecute(Timestamp nextexecute) {
        this.nextexecute = nextexecute;
    }

    @Basic
    @Column(name = "icon", nullable = true)
    public byte[] getIcon() {
        return icon;
    }



    /**
     * Категория операции
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "category")
    public model.categories.Category getCategory() {
        return category;
    }

    /**
     * Ссылка на счет, с которого сделан перевод
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "bill_from")
    public Bill getFromBill() {
        return fromBill;
    }

    /**
     * Ссылка на счет, на который сделан перевод
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "bill_to")
    public Bill getToBill() {
        return toBill;
    }

    /**
     * Пользователь, совершивший операцию
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "pk_user")
    public User getUser() {
        return user;
    }

    /**
     * Период повторения операции, если она периодическая
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "period")
    public Period getPeriod() {
        return period;
    }

    /**
     * Дата, когда операция была учтена
     */
    @Basic
    @Column(name = "dateExecute", nullable = true)
    public Timestamp getDateExecute() {
        return dateExecute;
    }
    public void setDateExecute(Timestamp dateExecute) {
        this.dateExecute = dateExecute;
    }


    @Transient
    public LocalDate getDateExecuteWithoutTime() {
        if(dateExecute == null)
            return null;
        return dateExecute.toLocalDateTime().toLocalDate();
    }

    @Transient
    public LocalDate getNextDateExecuteWithoutTime() {
        if(nextexecute == null)
            return null;
        return nextexecute.toLocalDateTime().toLocalDate();
    }

    @Transient
    public LocalDate getDateWithoutTime() {
        return dateWithoutTime;
    }
    @Transient
    public void setNextExecuteDateByPeriod(){
        if (period == null)
            return;
        switch (period.getPkPeriod()){
            case 1:
                nextexecute = Timestamp.valueOf(getDateWithoutTime().plusDays(1).atStartOfDay());
                break;
            case 2:
                nextexecute = Timestamp.valueOf(getDateWithoutTime().plusWeeks(1).atStartOfDay());
                break;
            case 3:
                nextexecute = Timestamp.valueOf(getDateWithoutTime().plusMonths(1).atStartOfDay());
                break;
            case 4:
                nextexecute = null;
                break;
            default:
                System.out.println("Неизвестный период");
        }
    }
    @Transient
    public boolean isNeedExecuteNow(){
        LocalDate todayDate = LocalDate.now();
        if(getDateExecuteWithoutTime() == null && (getDateWithoutTime().isBefore(todayDate) || getDateWithoutTime().isEqual(todayDate)))
            return true;
        if(getDateExecuteWithoutTime() != null){
            if(getDateExecuteWithoutTime().isEqual(todayDate))
                return false;
            return getNextDateExecuteWithoutTime() != null && (getNextDateExecuteWithoutTime().isEqual(todayDate) || getNextDateExecuteWithoutTime().isBefore(todayDate) );
        }
            return false;
    }
    public void setDateWithoutTime(LocalDate dateWithoutTime) {
        this.dateWithoutTime = dateWithoutTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return pkOperation == operation.pkOperation &&
                Objects.equals(date, operation.date) &&
                Objects.equals(sum, operation.sum) &&
                Objects.equals(node, operation.node) &&
                Objects.equals(nextexecute, operation.nextexecute) &&
                Arrays.equals(icon, operation.icon);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(date, pkOperation, sum, node, nextexecute);
        result = 31 * result + Arrays.hashCode(icon);
        return result;
    }

}
