package model;

import util.BCrypt;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = User.ALL_USERS, query = "from User"),
        @NamedQuery(name = User.FIND_BY_LOGIN, query = "from User where login = :login")
})

@Table(name = "users")
public class User {
    public static final String ALL_USERS = "User.allUsers";
    public static final String FIND_BY_LOGIN = "User.findByLogin";
    private int pkUser;
    private String login;
    private String pass;

    @Id
    @Column(name = "pk_user", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public int getPkUser() {
        return pkUser;
    }

    public void setPkUser(int pkUser) {
        this.pkUser = pkUser;
    }

    @Basic
    @Column(name = "login", nullable = true, length = -1)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "pass", nullable = true, length = -1)
    public String getPass() {
        return pass;
    }

    public void setPass(String hashedPass) {
        //this.pass = Password.getSaltedHash(password);
        this.pass = hashedPass;
    }

    public void hashAndSetPass(String password){
        this.pass = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password) {
        //return Password.check(password, this.pass);
        return BCrypt.checkpw(password, this.pass);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return pkUser == user.pkUser &&
                Objects.equals(login, user.login) &&
                Objects.equals(pass, user.pass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkUser, login, pass);
    }

    @Override
    public String toString() {
        return login;
    }
}
