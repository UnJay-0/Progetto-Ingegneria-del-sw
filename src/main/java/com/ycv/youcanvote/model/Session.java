package com.ycv.youcanvote.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import com.ycv.youcanvote.entity.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;


/**
 * OVERVIEW:
 * Session is a mutable singleton used to store information about the current session
 * The instance of this class will store the user logged and the open and closed
 * voting sessions.
 */
public class Session {
    private static Session current = null;
    private User user = null;
    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    private Session() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     *
     * @return the instance of Session if already instantiated, otherwise
     *         a new one
     */
    public static Session getInstance() {
        if (current == null) {
            current = new Session();
        }
        return current;
    }

    /**
     *
     * @return true if the user has logged in, false otherwise
     */
    public boolean userLogged() {
        return user != null;
    }

    /**
     * This method  is used to log in a user.
     * @param cf ID of the user, must be non-null and not empty.
     * @param pwd password of the user, must be non-null and not empty.
     * @param forOperator must be true if the user is an operator otherwise false.
     * @throws WrongCredentialsException if the credentials are wrong.
     */
    public void setUser(String cf, String pwd, boolean forOperator) throws WrongCredentialsException {
        if (!userLogged()) {
            try {
                User user = Objects.requireNonNull(User.getUserById(cf));
            } catch (NullPointerException e) {
                throw new WrongCredentialsException("codice fiscale o password sbagliata");
            }

            if(user.getPassword().equals(pwdToSHA256(pwd)) &&
                    (user.getOperator() || (!forOperator && !user.getOperator()))) {
                this.user = user;
            } else {
                throw new WrongCredentialsException("codice fiscale o password sbagliata");
            }
        }
    }

    private String pwdToSHA256(String pwd) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(
                pwd.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * This method is used to log out the logged user.
     * if there isn't a logged user nothing will happen.
     */
    public void removeUser() {
        user = null;
    }

    public User getUser() {
        return user;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}

