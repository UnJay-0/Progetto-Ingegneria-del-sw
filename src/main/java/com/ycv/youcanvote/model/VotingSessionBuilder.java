package com.ycv.youcanvote.model;

import com.ycv.youcanvote.entity.VotingSession;
import jakarta.persistence.EntityManager;

import java.util.List;

public class VotingSessionBuilder {
    private String name;

    private String description;

    private VotingSession.TypeOfVote type;

    private List<Candidate> candidateList;

    private boolean partyVoting;

    private VotingSession.ResultMod resultMod;

    public VotingSessionBuilder() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(VotingSession.TypeOfVote type) {
        this.type = type;
    }


    public void setCandidateList(List<Candidate> candidateList) {
        this.candidateList = candidateList;
    }

    public void setResultMod(VotingSession.ResultMod resultMod) {
        this.resultMod = resultMod;
    }

    public void setPartyVoting(boolean partyVoting) {
        this.partyVoting = partyVoting;
    }

    public boolean isPartyVoting() {
        return partyVoting;
    }

    public VotingSession.TypeOfVote getType() {
        return this.type;
    }

    public VotingSession getVotingSession() {
        return new VotingSession(name, description, candidateList, type.toString(), resultMod.toString());
    }

    public void saveVotingSession() {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(getVotingSession());
        entityManager.getTransaction().commit();
    }
}
