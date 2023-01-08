package com.ycv.youcanvote.controller.operator;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.model.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VotingSessionCandidates {

    private final VotingSessionBuilder build;

    private SingleSelectionField<Candidate> candidatesSelectionField;

    private List<Candidate> toSelect;

    private final List<Candidate> selectedCandidates;



    @FXML
    private Button back;

    @FXML
    private Button forward;

    @FXML
    private VBox formSpace;

    @FXML
    private VBox addedCandidates;

    public VotingSessionCandidates(VotingSessionBuilder build) {
        this.build = build;
        selectedCandidates = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        // TODO: dao per ottenere i gruppi

        // TEST ---------------------------------------
        List<Candidate> candidates = new ArrayList<>();
        candidates.add(new Individual("BENEE"));
        candidates.add(new Individual("Birthh"));
        candidates.add(new Individual("070 Shake"));
        candidates.add(new Individual("Dominic Fike"));

        List<Candidate> candidates2 = new ArrayList<>();
        candidates2.add(new Individual("Dario Moccia"));
        candidates2.add(new Individual("Mangaka96"));
        candidates2.add(new Individual("Marco Merrino"));
        candidates2.add(new Individual("Voghelita"));

        Party artisti = new Party(candidates, "Musicians");
        Party streamers = new Party(candidates2, "Streamers");
        // TEST ---------------------------------------

        if(build.isPartyVoting()){
            toSelect = new ArrayList<>();
            toSelect.add(artisti);
            toSelect.add(streamers);
            candidatesSelectionField = Field.ofSingleSelectionType(toSelect);
        } else {
            toSelect = new ArrayList<>();
            toSelect.addAll(artisti.getMembers());
            toSelect.addAll(streamers.getMembers());
            candidatesSelectionField = Field.ofSingleSelectionType(toSelect);
        }

        Form form = Form.of(
                Group.of(
                        candidatesSelectionField
                )
        );

        Button addCandidate = new Button("Aggiungi candidato");
        addCandidate.getStyleClass().add("btn");
        addCandidate.setStyle("-fx-font-size: 20");
        addCandidate.setOnAction(event -> {
            addCandidates();
        });

        formSpace.getChildren().addAll(new FormRenderer(form), addCandidate);
        formSpace.setAlignment(Pos.CENTER);

        forward.setOnAction(event -> {
            build.setCandidateList(selectedCandidates);
            System.out.println(build.getVotingSession());
            try {
                SceneController.switchScene(forward, "homeGestore.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        back.setOnAction(event -> {
            VotingSessionName controller = new VotingSessionName(this.build);
            try {
                SceneController.switchScene(back, "createVotingSession.fxml", controller);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        addedCandidates.setAlignment(Pos.CENTER);

    }

    private void addCandidates() {
        Candidate selected = candidatesSelectionField.getSelection();
        Hyperlink candidate = new Hyperlink(selected.name());
        candidate.setTooltip(new Tooltip("Esegui un Click per eliminare il candidato"));
        candidate.getStyleClass().add("clickable");
        candidate.setStyle("-fx-font-size: 25;");
        candidate.setOnAction(event -> {
            addedCandidates.getChildren().remove(candidate);
            selectedCandidates.remove(selected);
            toSelect.add(selected);
            candidatesSelectionField.items(toSelect);
        });
        selectedCandidates.add(selected);
        addedCandidates.getChildren().add(candidate);
        toSelect.remove(selected);
        candidatesSelectionField.items(toSelect);
    }
}
