package com.ycv.youcanvote.model;

public class Answer implements Candidate {
    private final String answer;

    public Answer(String answer){
        this.answer = answer;
    }

    @Override
    public String name() {
        return answer;
    }

    public String toString() {
        return this.answer;
    }
}
