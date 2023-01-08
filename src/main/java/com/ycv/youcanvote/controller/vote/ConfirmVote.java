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

    private final Vote vote;

    public ConfirmVote(Vote vote) {
        this.vote = vote;
    }

    @FXML
    public void initialize() {
        voteSpace.getChildren().add(vote.render());
        voteButton.setOnAction(e -> confirmVote());
        cancelButton.setOnAction(e -> cancelVote());

    }

    private void confirmVote() {
        vote.confirmVote();
        Stage thisStage = (Stage) voteSpace.getScene().getWindow();
        thisStage.close();
    }

    private void cancelVote() {
        try {
            SceneController.switchScene(cancelButton, "vote.fxml", vote);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
