package com.ycv.youcanvote.model;

public class WrongCredentialsException extends Exception {
    public WrongCredentialsException(){
        super();
    }

    public WrongCredentialsException(String s) {
        super(s);
    }
}
