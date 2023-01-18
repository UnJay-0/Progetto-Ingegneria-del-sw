package com.ycv.youcanvote.controller.results;

import com.ycv.youcanvote.entity.User;
import com.ycv.youcanvote.entity.VotingSession;
import com.ycv.youcanvote.model.Results;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ShowResults {
    private final List<Results> results;

    private VotingSession votingSession;

    @FXML
    private Text votingSessionName;

    @FXML
    private Text votingSessionType;

    @FXML
    private Text affluence;

    @FXML
    private Text totalVotes;

    @FXML
    private Text totalBlanks;

    @FXML
    private Text winners;

    public ShowResults(List<Results> results, VotingSession votingSession) {

        this.results = new ArrayList<>(results);
        this.votingSession = votingSession;
    }

    @FXML
    private void initialize() {
        votingSessionName.setText(votingSession.getName());
        votingSessionType.setText(votingSession.getTypeOfVote()
                + " con " + votingSession.getResultMod());
        affluence.setText((votingSession.nVote() * 100 / User.nUser()) + "%");
        totalVotes.setText(votingSession.nVote() + "");
        totalBlanks.setText(votingSession.nBlankVotes() + "");
        winners.setText(results.get(0).getWinnerText());
        if(results.size() > 1) {
            winners.setText(winners.getText() + " con preferenza per il candidato " + results.get(1).getWinner().name());
        }
    }
}
