package com.ycv.youcanvote.model;

import java.util.ArrayList;
import java.util.List;

public class Party implements  Candidate{
    private final List<Candidate> partyMembers;
    private final String name;

    public Party(List<Candidate> groupMembers, String name) {
        this.partyMembers = groupMembers;
        this.name = name;
    }

    public List<Candidate> getMembers() {
        return new ArrayList<>(partyMembers);
    }


    @Override
    public String name() {
        return this.name;
    }

    public String toString() {
        return name;
    }
}
