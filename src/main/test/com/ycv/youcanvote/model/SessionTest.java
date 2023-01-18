package com.ycv.youcanvote.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    @Test
    @DisplayName("Log in")
    void setUser() {
        assertAll(
                () -> assertThrows(WrongCredentialsException.class,
                        () -> Session.getInstance().setUser
                                ("", "", false)),
                () -> assertThrows(WrongCredentialsException.class,
                        () -> Session.getInstance().setUser
                                ("123456789abcdef", "", false)),
                () -> assertThrows(WrongCredentialsException.class,
                        () -> Session.getInstance().setUser
                                ("234567890abcdef", "ciao", true)),
                () -> assertDoesNotThrow(() -> Session.getInstance().setUser
                        ("123456789abcdef", "XBJV4T68taylix", true))
        );
    }

    @Test
    @DisplayName("Log out")
    void removeUser() {
        try {
            Session.getInstance().setUser
                    ("123456789abcdef", "XBJV4T68taylix", true);
        } catch (WrongCredentialsException e) {
            throw new RuntimeException(e);
        }
        Session.getInstance().removeUser();
        assertAll(
                () -> assertFalse(Session.getInstance().userLogged())
        );
    }

    @Test
    @DisplayName("get logged user")
    void getUser() {
        try {
            Session.getInstance().setUser
                    ("123456789abcdef", "XBJV4T68taylix", true);
        } catch (WrongCredentialsException e) {
            throw new RuntimeException(e);
        }
        assertAll(
                () -> assertTrue(Session.getInstance().userLogged())
        );
    }
}