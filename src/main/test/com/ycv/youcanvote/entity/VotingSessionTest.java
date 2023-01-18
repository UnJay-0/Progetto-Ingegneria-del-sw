package com.ycv.youcanvote.entity;

import com.ycv.youcanvote.model.Answer;
import com.ycv.youcanvote.model.Candidate;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VotingSessionTest {

    @Test
    void close() {
        List<Candidate> candidates = Arrays.asList(new Answer("favorevole"), new Answer("contrario"));
        VotingSession session = new VotingSession(
                "test", "Test test test test",  candidates, "Referendum", "Con quorum");
        session.close();
        assertFalse(session.isOpen());
    }
}