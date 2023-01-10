package com.ycv.youcanvote.entity;

import com.ycv.youcanvote.model.Session;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

/**
 * OVERVIEW:
 * The instance of User are used to store information about a user of
 * the system. A user is composed by a name, surname, birthday, place of birth
 * and the rule (elector or operator)
 * <br/><br/>
 *
 * REP INV:
 * <br/>
 * name != null && name != ""
 * <br/>
 * surname != null && surname != ""
 * <br/>
 * birthday != null
 * <br/>
 * placeOfBirth != null
 * <br/><br/>
 * password != null && password != ""
 * <br/><br/>
 *
 * ABS FUN:
 * <br/>
 * ABS(User) -> {name, surname, birthday, placeOfBirth, rule}
 */
@Entity
@Table(name="\"User\"")
@NamedQuery(name = "User.byId", query="SELECT e FROM User e WHERE e.cf=?1")
public class User {


    @Id
    @Column(name = "cf")
    private String cf;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "surname")
    private String surname;
    @Basic
    @Column(name = "date_of_birth")
    private Date birthday;
    @Basic
    @Column(name = "place_of_birth")
    private String placeOfBirth;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "is_operator")
    private Boolean isOperator;
    @OneToMany(mappedBy = "userByCf")
    private Collection<VoteStory> voteStoriesByCf;


    public static User getUserById(String id) {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();

        User user = entityManager.createNamedQuery("User.byId", User.class)
                .setParameter(1, id)
                .getSingleResult();


        entityManager.getTransaction().commit();

        return  user;
    }
    public String getCf() {
        return cf;
    }

    private void setCf(String cf) {
        this.cf = cf;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    private void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateOfBirth() {
        return birthday;
    }

    private void setDateOfBirth(Date dateOfBirth) {
        this.birthday = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    private void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public Boolean getOperator() {
        return isOperator;
    }

    private void setOperator(Boolean operator) {
        isOperator = operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(cf, user.cf) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(birthday, user.birthday) && Objects.equals(placeOfBirth, user.placeOfBirth) && Objects.equals(password, user.password) && Objects.equals(isOperator, user.isOperator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cf, name, surname, birthday, placeOfBirth, password, isOperator);
    }

    public Collection<VoteStory> getVoteStoriesByCf() {
        return voteStoriesByCf;
    }

    private void setVoteStoriesByCf(Collection<VoteStory> voteStoriesByCf) {
        this.voteStoriesByCf = voteStoriesByCf;
    }

    public String toString() {
        return name + " " + surname +
                "\n" + birthday + ", " + placeOfBirth +
                "\nGestore: " + (isOperator ? "Sì" : "No");
    }
}
