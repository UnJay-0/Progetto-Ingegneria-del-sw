package com.ycv.youcanvote.controller.operator;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.SingleSelectionField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.entity.VotingSession.TypeOfVote;
import com.ycv.youcanvote.model.VotingSessionBuilder;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;

public class VotingSessionType {

    private final VotingSessionBuilder build;

    private SingleSelectionField<TypeOfVote> selection;

    @FXML
    private VBox formSpace;

    @FXML
    private Button back;

    @FXML
    private Button forward;

    public VotingSessionType(){
        build = new VotingSessionBuilder();
    }

    @FXML
    private void initialize() {
        Label label = new Label("Seleziona la tipologia di voto");
        label.setStyle("-fx-font-size: 27; -fx-fill: rgb(25, 69, 107);");
        selection = Field.ofSingleSelectionType(Arrays.asList(TypeOfVote.values()));
        Form form = Form.of(
                Group.of(
                        selection
                )
        );

        back.setOnAction(event -> {
            Node node = (Node) event.getSource();
            try {
                SceneController.switchScene(node, "homeGestore.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        forward.setOnAction(event -> {
            Node node = (Node) event.getSource();
            build.setType(selection.getSelection());
            VotingSessionName nameController = new VotingSessionName(build);
            try {
                SceneController.switchScene(node, "createVotingSession.fxml", nameController);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        formSpace.getChildren().addAll(label, new FormRenderer(form));
    }
}
