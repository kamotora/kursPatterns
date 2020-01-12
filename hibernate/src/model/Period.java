package model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Period {
    private int pkPeriod;
    private String name;

    @Id
    @Column(name = "pk_period", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public int getPkPeriod() {
        return pkPeriod;
    }

    public void setPkPeriod(int pkPeriod) {
        this.pkPeriod = pkPeriod;
    }

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return pkPeriod == period.pkPeriod &&
                Objects.equals(name, period.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkPeriod, name);
    }

    @Override
    public String toString() {
        return name;
    }
}
