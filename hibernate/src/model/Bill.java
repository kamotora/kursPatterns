package model;

import javafx.beans.property.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Bill {
    private String name;
    private BigDecimal sum;
    private String note;
    private int pkBill;
    private byte[] icon;

    private Set<Operation> expands = new HashSet<>();
    private Set<Operation> profits = new HashSet<>();

    private Property<BigDecimal> sumExpands;
    private Property<BigDecimal> sumProfits;
    @Basic
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "sum", nullable = true, precision = 2)
    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    @Basic
    @Column(name = "note", nullable = true, length = -1)
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Id
    @Column(name = "pk_bill", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public int getPkBill() {
        return pkBill;
    }

    public void setPkBill(int pkBill) {
        this.pkBill = pkBill;
    }

    @Basic
    @Column(name = "icon", nullable = true)
    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    @OneToMany(mappedBy = "fromBill", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, orphanRemoval = true)
    public Set<Operation> getExpands() {
        return expands;
    }

    public void setExpands(Set<Operation> expands) {
        this.expands = expands;
    }

    @OneToMany(mappedBy = "toBill", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, orphanRemoval = true)
    public Set<Operation> getProfits() {
        return profits;
    }

    public void setProfits(Set<Operation> profits) {
        this.profits = profits;
    }

    @Transient
    public BigDecimal getSumExpands() {
        double res = 0;
        for(Operation operation : expands)
            res += operation.getSum().doubleValue();
        sumExpands = new SimpleObjectProperty<BigDecimal>();
        sumExpands.setValue(BigDecimal.valueOf(res));
        return sumExpands.getValue();
    }

    @Transient
    public BigDecimal getSumProfits() {
        double res = 0;
        for(Operation operation : profits)
            res += operation.getSum().doubleValue();
        sumProfits = new SimpleObjectProperty<BigDecimal>();
        sumProfits.setValue(BigDecimal.valueOf(res));
        return sumProfits.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return pkBill == bill.pkBill &&
                Objects.equals(name, bill.name) &&
                Objects.equals(sum, bill.sum) &&
                Objects.equals(note, bill.note) &&
                Arrays.equals(icon, bill.icon);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, sum, note, pkBill);
        result = 31 * result + Arrays.hashCode(icon);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

}
