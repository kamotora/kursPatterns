package model;

import dao.LimitDAO;
import dao.TypeCategoryDAO;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "limits")
public class Limit {
    private int pkLimit;
    private Date datestart;
    private Date dateend;
    private BigDecimal sum;
    private String node;
    private User user;

    @Id
    @Column(name = "pk_limit", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public int getPkLimit() {
        return pkLimit;
    }

    public void setPkLimit(int pkLimit) {
        this.pkLimit = pkLimit;
    }

    @Basic
    @Column(name = "datestart", nullable = false)
    public Date getDatestart() {
        return datestart;
    }

    public void setDatestart(Date datestart) {
        this.datestart = datestart;
    }

    @Basic
    @Column(name = "dateend", nullable = false)
    public Date getDateend() {
        return dateend;
    }

    public void setDateend(Date dateend) {
        this.dateend = dateend;
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

    /**
     * Пользователь, для которого действует лимит
     */
    @ManyToOne(optional = true)
    @JoinColumn(name = "pk_user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @Transient
    public LocalDate getDateSt() {
        return datestart.toLocalDate();
    }
    @Transient
    public LocalDate getDateFinish() {
        return dateend.toLocalDate();
    }
    @Transient
    public Double getSumExpand(){
        List<Operation> operationList = new LimitDAO().getOperationsByLimit(this);
        double res = 0;
        for(Operation operation : operationList)
            if(operation.getCategory().getType().equals(new TypeCategoryDAO().getExpandType()))
                res += operation.getSum().doubleValue();
        return res;
    }
    @Transient
    public Double getSumProfit(){
        List<Operation> operationList = new LimitDAO().getOperationsByLimit(this);
        double res = 0;
        for(Operation operation : operationList)
            if(operation.getCategory().getType().equals(new TypeCategoryDAO().getProfitType()))
                res += operation.getSum().doubleValue();
        return res;
    }

    @Transient
    public Double getSumToEnd(){
        return sum.doubleValue() - getSumExpand();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Limit limit = (Limit) o;
        return pkLimit == limit.pkLimit &&
                Objects.equals(datestart, limit.datestart) &&
                Objects.equals(dateend, limit.dateend) &&
                Objects.equals(sum, limit.sum) &&
                Objects.equals(node, limit.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkLimit, datestart, dateend, sum, node);
    }

}
