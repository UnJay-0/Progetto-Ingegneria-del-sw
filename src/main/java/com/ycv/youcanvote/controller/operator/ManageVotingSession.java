package com.ycv.youcanvote.controller.operator;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.entity.VotingSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ManageVotingSession {

    private SingleSelectionField<VotingSession> sessionSelection;

    @FXML
    private VBox formSpace;

    @FXML
    private Button back;

    @FXML
    private void initialize() {

        sessionSelection = Field.ofSingleSelectionType(VotingSession.getVotingSession())
                .label("Seleziona una sessione di voto");

        Form form = Form.of(
                Group.of(
                        sessionSelection
                )
        );

        back.setOnAction(event -> {
            try {
                SceneController.switchScene(back, "homeGestore.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        formSpace.getChildren().add(new FormRenderer(form));

    }

    @FXML
    private void openVotingSession() {
        VotingSession selected = sessionSelection.getSelection();
        ShowVotingSession controller =  new ShowVotingSession(selected);
        try {
            SceneController.switchScene(formSpace, "showVotingSession.fxml", controller);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
