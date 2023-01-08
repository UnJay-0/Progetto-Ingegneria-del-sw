package com.ycv.youcanvote.controller.operator;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.MultiSelectionField;
import com.dlsc.formsfx.view.controls.SimpleCheckBoxControl;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.controller.vote.Vote;
import com.ycv.youcanvote.entity.VotingSession;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VotingSessionSelection {
    @FXML
    private Button exitButton;
    @FXML
    private Button continueButton;
    @FXML
    private VBox formSpace;
    @FXML
    private Text greetings;

    // TODO: Con Entità
//    @FXML
//    public void initialize() {
//        Session session = Session.getInstance();
//        greetings.setText("Benvenuto " + session.getUser().getName());
//
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
//        MultiSelectionField<VotingSession> votingSessionSelection = Field.ofMultiSelectionType(
//                Arrays.asList(test, test2, test3, test4)).render(new SimpleCheckBoxControl<>());
//        Form selection = Form.of(
//                Group.of(
//                        votingSessionSelection
//                )
//        );
//        formSpace.getChildren().add(new FormRenderer(selection));
//
//        exitButton.setOnAction(e -> {
//            try {
//                SceneController.switchScene(exitButton, "home.fxml");
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//
//        });
//
//        continueButton.setOnAction( e -> voteSelected(continueButton, votingSessionSelection));
//    }

    public void voteSelected(Node source, MultiSelectionField<VotingSession> fields){
        fields.persist();
        Stage thisStage = (Stage) source.getScene().getWindow();
        for(VotingSession vs: fields.getSelection()) {
            Vote controller = vs.getVote();
            try {
                SceneController.loadPopup(thisStage, "vote.fxml", controller);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
