package com.ycv.youcanvote.controller.vote;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.MultiSelectionField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.entity.Vote;
import com.ycv.youcanvote.entity.VoteStory;
import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.entity.VotingSession;
import com.ycv.youcanvote.model.Session;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankedVoting implements Voting {
    private final List<Candidate> candidates;
    private final VotingSession votingSession;

    private MultiSelectionField<Candidate> candidateSelect;

    @FXML
    private VBox titleSpace;

    @FXML
    private VBox formSpace;

    @FXML
    private Button confirm;

    @FXML
    private Button blank;

    public RankedVoting(VotingSession votingSession) {
        this.candidates = votingSession.getCandidatesList();
        this.votingSession = votingSession;
    }

    @FXML
    public void initialize() {
        Label voteTitle = new Label("Voto Ordinale: " + votingSession.getName());
        voteTitle.setStyle("-fx-font-size: 25; -fx-text-fill: rgb(25, 69, 107); ");
        HBox formControlSpace = new HBox();
        formControlSpace.setAlignment(Pos.CENTER);
        VBox buttonSpace = new VBox();
        buttonSpace.setAlignment(Pos.CENTER);
        candidateSelect = Field.ofMultiSelectionType(candidates);
        Button up = new Button("↑");
        up.getStyleClass().add("btn");
        Button down = new Button("↓");
        down.getStyleClass().add("btn");
        up.setOnAction(e -> moveUpCandidate());
        down.setOnAction(e -> moveDownCandidate());



        confirm.setOnAction(e -> {
            ConfirmVote confirmVote = new ConfirmVote(this);
            try {
                SceneController.switchScene(confirm, "confirmVote.fxml", confirmVote);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });



        blank.setOnAction(event -> this.blankVote());

        HBox padding2 = new HBox();
        padding2.setPadding(new Insets(10, 0, 10, 0));
        Form form = Form.of(
                Group.of(
                        candidateSelect
                )
        );

        titleSpace.getChildren().add(voteTitle);
        buttonSpace.getChildren().addAll(up, padding2, down);
        formControlSpace.getChildren().addAll(new FormRenderer(form), buttonSpace);
        formSpace.getChildren().add(formControlSpace);
    }

    public Node render() {
        Text selection = new Text();
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < candidates.size(); i++ ){
            out.append(String.format("%d - %s\n", i+1, candidates.get(i).toString()));
        }
        selection.setText(out.toString());
        selection.setStyle("-fx-font-size: 17; -fx-text-fill: rgb(25, 69, 107);");
        HBox box = new HBox(selection);
        box.setAlignment(Pos.CENTER);
        return selection;
    }


    private String selectionToString() {
        StringBuilder str = new StringBuilder();
        for(Candidate c : candidates) {
            str.append(c.toString()).append(";");
        }
        return str.toString();
    }
    @Override
    public void confirmVote() {
        Vote vote = new Vote(
                selectionToString(),
                this.votingSession
        );
        Vote.saveVote(vote);
        VoteStory voteStory = new VoteStory(Session.getInstance().getUser(), votingSession);
        VoteStory.saveVoteStory(voteStory);
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

    public String toString() {
        return candidates.toString();
    }

    private void moveUpCandidate() {
        List<Candidate> selection = new ArrayList<>(this.candidateSelect.getSelection());
        for(Candidate selected: selection) {
            int down = this.candidates.indexOf(selected);
            if (this.candidates.size() > 1) {
                if (down != 0) {
                    int up = down - 1;
                    Collections.swap(candidates, up, down);
                } else {
                    for (int i = 1; i < this.candidates.size(); i++) {
                        Collections.swap(candidates,i - 1, i);
                    }
                }
                this.candidateSelect.items(this.candidates);
            }
        }
    }


    private void moveDownCandidate() {
        List<Candidate> selection = new ArrayList<>(this.candidateSelect.getSelection());
        for(Candidate selected: selection) {
            int up = this.candidates.indexOf(selected);
            if (this.candidates.size() > 1) {
                if (up != this.candidates.size() - 1) {
                    int down = up + 1;
                    Collections.swap(candidates, up, down);
                } else {
                    for (int i = this.candidates.size() - 2; i >= 0; i--) {
                        Collections.swap(candidates,i + 1, i);
                    }
                }
                this.candidateSelect.items(this.candidates);
            }
        }
    }
}
