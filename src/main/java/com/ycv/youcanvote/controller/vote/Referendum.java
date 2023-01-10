package com.ycv.youcanvote.controller.vote;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.entity.VoteStory;
import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.entity.VotingSession;
import com.ycv.youcanvote.entity.Vote;
import com.ycv.youcanvote.model.Session;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Referendum implements Voting {
    private final VotingSession votingSession;

    private SingleSelectionField<Candidate> candidateSelect;

    @FXML
    private VBox titleSpace;

    @FXML
    private VBox formSpace;

    @FXML
    private Button confirm;

    @FXML
    private Button blank;

    public Referendum(VotingSession session) {
        this.votingSession = session;
    }

    @FXML
    public void initialize() {
        Label voteTitle = new Label("Referendum: " + votingSession.getName());
        voteTitle.setStyle("-fx-font-size: 25; -fx-text-fill: rgb(25, 69, 107);");

        Text question = new Text(votingSession.getDescription());
        question.setStyle("-fx-font-size: 20; -fx-text-fill: rgb(25, 69, 107);");


        confirm.setOnAction(e -> {
            ConfirmVote confirmVote = new ConfirmVote(this);
            try {
                SceneController.switchScene(confirm, "confirmVote.fxml", confirmVote);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });

        blank.setOnAction(event -> this.blankVote());

        titleSpace.getChildren().add(voteTitle);
        titleSpace.getChildren().add(question);
        this.candidateSelect = Field.ofSingleSelectionType(votingSession.getCandidatesList());
        Form form = Form.of(
                Group.of(
                        candidateSelect
                )
        );
        formSpace.getChildren().add(new FormRenderer(form));
    }

    private void blankVote() {
        Vote vote = new Vote(
                "Bianca",
                this.votingSession
        );
        Vote.saveVote(vote);
        VoteStory voteStory = new VoteStory(Session.getInstance().getUser(), votingSession);
        VoteStory.saveVoteStory(voteStory);
        Stage thisStage = (Stage) formSpace.getScene().getWindow();
        thisStage.close();
    }

    @Override
    public void confirmVote() {
        Vote vote = new Vote(
                this.candidateSelect.getSelection().toString(),
                this.votingSession
        );
        Vote.saveVote(vote);
        VoteStory voteStory = new VoteStory(Session.getInstance().getUser(), votingSession);
        VoteStory.saveVoteStory(voteStory);
    }

    @Override
    public Node render() {
        Label selected = new Label("La risposta selezionata è: " + candidateSelect.getSelection().toString());
        selected.setStyle("-fx-font-size: 20");
        return selected;
    }

}
