package model.categories;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "typecategory")
public class TypeCategory {
    private String name;
    private int pkType;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    @Column(name = "pk_type", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public int getPkType() {
        return pkType;
    }

    public void setPkType(int pkType) {
        this.pkType = pkType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeCategory that = (TypeCategory) o;
        return pkType == that.pkType &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pkType);
    }

    @Override
    public String toString() {
        return name;
    }
}
