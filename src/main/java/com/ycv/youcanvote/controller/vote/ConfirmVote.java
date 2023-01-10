package com.ycv.youcanvote.controller.vote;



import com.ycv.youcanvote.controller.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfirmVote {
    @FXML
    private Button cancelButton;

    @FXML
    private Button voteButton;

    @FXML
    private VBox voteSpace;

    private final Voting voting;

    public ConfirmVote(Voting voting) {
        this.voting = voting;
    }

    @FXML
    public void initialize() {
        voteSpace.getChildren().add(voting.render());
        voteButton.setOnAction(e -> confirmVote());
        cancelButton.setOnAction(e -> cancelVote());

    }

    private void confirmVote() {
        voting.confirmVote();
        Stage thisStage = (Stage) voteSpace.getScene().getWindow();
        thisStage.close();
    }

    private void cancelVote() {
        try {
            SceneController.switchScene(cancelButton, "voting.fxml", voting);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
