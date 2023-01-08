package com.ycv.youcanvote.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Date;
import java.util.List;

/**
 * OVERVIEW:
 * Session is a mutable singleton used to store information about the current session
 * The instance of this class will store the user logged and the open and closed
 * voting sessions.
 */
public class Session {
    private static Session current = null;
    private User user = null;

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

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
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method  is used to log in a user.
     * @param cf ID of the user, must be non-null and not empty.
     * @param pwd password of the user, must be non-null and not empty.
     * @param forOperator must be true if the user is an operator otherwise false.
     * @throws WrongCredentialsException if the credentials are wrong.
     */
    public void setUser(String cf, String pwd, boolean forOperator) throws WrongCredentialsException{
        if (!userLogged()) {
            // TODO: impostare il DAO
            user = new User("jonathan", "agyekum",
                    new Date(938275890), "Como", forOperator);
        }
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

    public VotingSession getActiveVotingSessions(){
        //TODO: DAO PER VOTING SESSIONS
        return null;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}

