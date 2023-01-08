package com.ycv.youcanvote.model;

public record Individual(String name) implements Candidate {

    public String toString() {
        return name;
    }
}
