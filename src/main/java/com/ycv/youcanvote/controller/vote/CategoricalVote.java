package com.ycv.youcanvote.controller.vote;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.model.Party;
import com.ycv.youcanvote.entity.VotingSession;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class CategoricalVote implements Vote {

    private final VotingSession votingSession;


    private final boolean isPreferential;

    private SingleSelectionField<Candidate> candidateSelect;

    @FXML
    private VBox titleSpace;

    @FXML
    private VBox formSpace;

    @FXML
    private Button confirm;

    @FXML
    private Button blank;


    private CategoricalVote(VotingSession session, boolean isPreferential) {
        this.votingSession = session;
        this.candidateSelect = Field.ofSingleSelectionType(votingSession.getCandidatesList()).label("Preferenza");
        this.isPreferential = isPreferential;
    }

    public static CategoricalVote createCategoricalVote(VotingSession session) {
        return new CategoricalVote(session, false);
    }

    public static CategoricalVote createPreferentialVote(VotingSession session) {
        return new CategoricalVote(session, true);
    }

    @FXML
    public void initialize() {
        Label voteTitle = new Label("Voto Categorico: " + votingSession.getName());
        voteTitle.setStyle("-fx-font-size: 25; -fx-text-fill: rgb(25, 69, 107);");

        confirm.setOnAction(e -> {
            if (!isPreferential) {
                ConfirmVote confirmVote = new ConfirmVote(this);
                try {
                    SceneController.switchScene(confirm, "confirmVote.fxml", confirmVote);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                PreferentialVote preferentialVote = new PreferentialVote(((Party) candidateSelect.getSelection()), votingSession);
                try {
                    SceneController.switchScene(confirm, "vote.fxml", preferentialVote);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        blank.setOnAction(event -> this.blankVote());

        titleSpace.getChildren().add(voteTitle);
        this.candidateSelect = Field.ofSingleSelectionType(votingSession.getCandidatesList()).label("Preferenza");
        Form form = Form.of(
                Group.of(
                        candidateSelect
                )
        );
        formSpace.getChildren().add(new FormRenderer(form));
    }

    public void confirmVote() {
        System.out.println(candidateSelect.getSelection() + "\n" + votingSession.toString());
    }
    private void blankVote() {
        System.out.println("Blank vote");
        Stage thisStage = (Stage) formSpace.getScene().getWindow();
        thisStage.close();
    }
    public Node render() {
        Label selected = new Label("Il candidato selezionato è: " + candidateSelect.getSelection().toString());
        selected.setStyle("-fx-font-size: 20");
        return selected;
    }

}
