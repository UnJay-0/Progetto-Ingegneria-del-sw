package com.ycv.youcanvote.controller.results;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.MultiSelectionField;
import com.dlsc.formsfx.view.controls.SimpleCheckBoxControl;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.controller.vote.Voting;
import com.ycv.youcanvote.entity.VotingSession;
import com.ycv.youcanvote.model.Session;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HomeResults {
    @FXML
    private Button exitButton;
    @FXML
    private Button continueButton;
    @FXML
    private VBox formSpace;

    private MultiSelectionField<VotingSession> votingSessionSelection;

    @FXML
    private void initialize() {
        List<VotingSession> votingSessions = VotingSession.getCloseVotingSession();
        votingSessionSelection = Field.ofMultiSelectionType(votingSessions)
                .render(new SimpleCheckBoxControl<>());
        Form selection = Form.of(
                Group.of(
                        votingSessionSelection
                )
        );
        formSpace.getChildren().add(new FormRenderer(selection));

        exitButton.setOnAction(e -> {
            Session.getInstance().removeUser();
            try {
                SceneController.switchScene(exitButton, "home.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        continueButton.setOnAction( e -> resultsSelected(continueButton, votingSessionSelection));
    }

    public void resultsSelected(Node source, MultiSelectionField<VotingSession> fields){
        fields.persist();
        Stage thisStage = (Stage) source.getScene().getWindow();
        for(VotingSession vs: fields.getSelection()) {
            ShowResults rs = new ShowResults(vs.getTypeOfVote().getResults(vs, vs.getResultMod()), vs);
            try {
                SceneController.loadPopup(thisStage, "showResults.fxml", rs);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
