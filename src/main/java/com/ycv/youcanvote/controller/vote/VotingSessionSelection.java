package com.ycv.youcanvote.controller.vote;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.MultiSelectionField;
import com.dlsc.formsfx.view.controls.SimpleCheckBoxControl;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.entity.VoteStory;
import com.ycv.youcanvote.entity.VotingSession;
import com.ycv.youcanvote.model.Session;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
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

    private MultiSelectionField<VotingSession> votingSessionSelection;

    @FXML
    public void initialize() {
        Session session = Session.getInstance();
        greetings.setText("Benvenuto " + session.getUser().getName());

        List<VotingSession> votingSessions = VotingSession.getOpenVotingSession();
        VoteStory.getVoteStoryByUser(Session.getInstance().getUser().getCf()).forEach(
                voteStory -> {
                    votingSessions.remove(voteStory.getVotingSessionByVsId());
                }
        );


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

        continueButton.setOnAction( e -> voteSelected(continueButton, votingSessionSelection));
    }

    public void voteSelected(Node source, MultiSelectionField<VotingSession> fields){
        fields.persist();
        Stage thisStage = (Stage) source.getScene().getWindow();
        for(VotingSession vs: fields.getSelection()) {
            Voting controller = vs.getVote();
            try {
                SceneController.loadPopup(thisStage, "vote.fxml", controller);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        List<VotingSession> votingSessions = VotingSession.getOpenVotingSession();
        VoteStory.getVoteStoryByUser(Session.getInstance().getUser().getCf()).forEach(
                voteStory -> {
                    votingSessions.remove(voteStory.getVotingSessionByVsId());
                }
        );
        votingSessionSelection.items(votingSessions);
    }
}
