package com.ycv.youcanvote.model;

import com.ycv.youcanvote.entity.User;
import com.ycv.youcanvote.entity.Vote;
import com.ycv.youcanvote.entity.VotingSession;

import java.util.Map;
import java.util.SortedMap;


public class Results {

    private Candidate winner;

    private final int affluence;

    private final int nVotes;

    private final int nBlankVotes;

    private final Map<Candidate, Integer> ranking;

    public Results(VotingSession votingSession,
                   Map<Candidate, Integer> ranking,
                   Candidate winner ) {
        this.nVotes = votingSession.nVote();
        this.nBlankVotes = votingSession.nBlankVotes();
        this.ranking = ranking;
        this.affluence = (this.nVotes * 100) / User.nUser();
        this.winner = winner;
        System.out.println(votingSession);
        System.out.println(ranking);
    }

    public int getAffluence() {
        return affluence;
    }

    public int getBlankVotes() {
        return nBlankVotes;
    }

    public int getVotes() {
        return nVotes;
    }

    public Candidate getWinner() {
        return winner;
    }

    public Candidate getHighestVote() {
        return  ranking.entrySet().iterator().next().getKey();
    }

    public String getWinnerText() {
        if (winner != null) {
            return "Il vincente è " + winner.name();
        }
        else {
            return "Il candidato con il maggior numero di voti è " +  getHighestVote() +
                    "che però non può essere eletto vincitore poiché non ha raggiunto la maggioranza assoluta";
        }
    }
}
