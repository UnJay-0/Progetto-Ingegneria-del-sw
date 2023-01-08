package com.ycv.youcanvote.controller.operator;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.model.*;
import com.ycv.youcanvote.entity.VotingSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManageVotingSession {

    private SingleSelectionField<VotingSession> sessionSelection;

    @FXML
    private VBox formSpace;

    @FXML
    private Button back;

    @FXML
    private void initialize() {
//        // TEST -------------------------------------------
//        // RANKED VOTE
//        List<Candidate> candidates = new ArrayList<>();
//        candidates.add(new Individual("BENEE"));
//        candidates.add(new Individual("Birthh"));
//        candidates.add(new Individual("070 Shake"));
//        VotingSession test = new VotingSession(
//                "Voto per bellezza artistica",
//                "bellezza artistica",
//                candidates,
//                VotingSession.TypeOfVote.RANKEDVOTE.toString()
//        );
//
//        // CATEGORICAL VOTE
//        List<Candidate> candidates2 = new ArrayList<>();
//        candidates2.add(new Individual("Dario Moccia"));
//        candidates2.add(new Individual("Mangaka96"));
//        candidates2.add(new Individual("Marco Merrino"));
//        VotingSession test2 = new VotingSession(
//                "Voto per bellezza filosofica",
//                "bellezza filosofica",
//                candidates2,
//                VotingSession.TypeOfVote.CATEGORICALVOTE.toString()
//        );
//
//        // PREFERENTIAL CATEGORICAL VOTE
//        Party artisti = new Party(candidates, "Musicians");
//        Party streamers = new Party(candidates2, "Streamers");
//        List<Candidate> candidates3 = new ArrayList<>();
//        candidates3.add(artisti);
//        candidates3.add(streamers);
//        VotingSession test3 = new VotingSession("the best influencer", "who's the best influencer among these celebs?", candidates3, VotingSession.TypeOfVote.PREFERENTIALVOTE.toString());
//
//        // REFERENDUM
//        List<Candidate> candidates4 = new ArrayList<>();
//        candidates4.add(new Answer("Sì"));
//        candidates4.add(new Answer("No"));
//        VotingSession test4 = new VotingSession("Free candy", "Should they be free candy for all?", candidates4, VotingSession.TypeOfVote.REFERENDUM.toString());
//
//        // TEST --------------------------------------------
//
//        // TODO: ottenimento sessioni di voto DAO
//        sessionSelection = Field.ofSingleSelectionType(Arrays.asList(test, test2, test3, test4))
//                .label("Seleziona una sessione di voto");
//
//        Form form = Form.of(
//                Group.of(
//                        sessionSelection
//                )
//        );
//
//        back.setOnAction(event -> {
//            try {
//                SceneController.switchScene(back, "homeGestore.fxml");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        formSpace.getChildren().add(new FormRenderer(form));

    }

    @FXML
    private void openVotingSession() {
//        VotingSession selected = sessionSelection.getSelection();
//        ShowVotingSession controller =  new ShowVotingSession(selected);
//        try {
//            SceneController.switchScene(formSpace, "showVotingSession.fxml", controller);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
