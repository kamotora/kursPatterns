package model.categories;

import model.Operation;
import model.User;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "category")
@NamedQueries({
        @NamedQuery(name = Category.AUTOCOMPLETE, query = "select c from "+"Category"+" c " +
                "where c.name like :filter order by c.name"),
        @NamedQuery(name = Category.FIND_BY_TYPE, query = "from Category where type.pkType = :pkType")
})

public class Category {
    public static final String AUTOCOMPLETE = "Category.autocomplete";
    public static final String FIND_BY_TYPE = "Category.findByType";

    protected String name;
    protected int pkCategory;
    protected byte[] icon;
    protected TypeCategory type;
    protected Set<Operation> operations = new HashSet<>();
    @Basic
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    @Column(name = "pk_category", nullable = false)
    @GeneratedValue
    public int getPkCategory() {
        return pkCategory;
    }

    public void setPkCategory(int pkCategory) {
        this.pkCategory = pkCategory;
    }

    @Basic
    @Column(name = "icon", nullable = true)
    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return pkCategory == category.pkCategory &&
                Objects.equals(name, category.name) &&
                Arrays.equals(icon, category.icon);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, pkCategory);
        result = 31 * result + Arrays.hashCode(icon);
        return result;
    }

    /**
     * Тип категории
     */
    @ManyToOne
    @JoinColumn(name = "type")
    public TypeCategory getType() {
        return type;
    }

    public void setType(TypeCategory type) {
        this.type = type;
    }

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    @Override
    public String toString() {
        return name;
    }
}
