package com.ycv.youcanvote.model;


import java.util.Date;
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
 *
 * ABS FUN:
 * <br/>
 * ABS(User) -> {name, surname, birthday, placeOfBirth, rule}
 */
public class User {
    private final String name;
    private final String surname;
    private final Date birthday;
    private final String placeOfBirth;

    private final Boolean isOperator;

    /**
     *
     * @param name name of the user, must be non-null and not an empty string.
     * @param surname surname of user, must be non-null and not an empty string.
     * @param birthday birthday of user, must be non-null.
     * @param placeOfBirth place of birth of the user, must be non-null and not an empty string.
     * @param isOperator must be true if the user is an operator otherwise false.
     */
    public User(String name, String surname, Date birthday, String placeOfBirth, Boolean isOperator){
        if (!name.trim().equals(""))
            this.name = Objects.requireNonNull(name, "The name can't be null");
        else
            throw new IllegalArgumentException("the name can't be empty");


        if (!surname.trim().equals(""))
            this.surname = Objects.requireNonNull(surname, "The surname can't be null");
        else
            throw new IllegalArgumentException("the surname can't be empty");


        this.birthday = Objects.requireNonNull(birthday, "The birthday can't be null");

        if (!placeOfBirth.trim().equals(""))
            this.placeOfBirth = Objects.requireNonNull(placeOfBirth, "The place of birth can't be null");
        else
            throw new IllegalArgumentException("the place of birth can't be empty");

        this.isOperator = isOperator;

    }

    /**
     *
     * @return the name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the surname of the user.
     */
    public String getSurname() {
        return surname;
    }

    /**
     *
     * @return the birthday of the user.
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     *
     * @return the place of birth of the user
     */
    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    /**
     *
     * @return true if the user is an operator, false otherwise.
     */
    public Boolean getOperator() {
        return isOperator;
    }
}
