package com.ycv.youcanvote.model;

public interface Candidate {
    String name();

    void alterName(String name);

    long getId();
}
