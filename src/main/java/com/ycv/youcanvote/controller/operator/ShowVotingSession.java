package com.ycv.youcanvote.controller.operator;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.model.VotingSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;


public class ShowVotingSession {

    private final VotingSession selected;

    @FXML
    private Label sessionName;

    @FXML
    private Text description;

    @FXML
    private Label status;

    @FXML
    private Label date;

    @FXML
    private Label nVote;

    @FXML
    private Button back;

    @FXML
    private Button close;

    @FXML
    private Button open;

    @FXML
    private VBox formSpace;

    public ShowVotingSession(VotingSession selected) {
        this.selected = selected;
    }

    @FXML
    private void initialize() {
        sessionName.setText(selected.getName());
        description.setText(selected.getDescription());
        status.setText("Stato: " + (selected.isOpen() ? "Aperta" : "Chiusa"));
        date.setText("Data di apertura: " + selected.getDateOfCreation());
        nVote.setText("Numero di voti: " + selected.nVote());
        back.setOnAction(event -> {
            try {
                SceneController.switchScene(back, "manageVotingSession.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        if(selected.isOpen()) {
            open.setDisable(true);
        } else {
            open.setDisable(false);
            close.setDisable(true);
        }
        close.setOnAction(event -> {
            selected.close();
            close.setDisable(true);
            open.setDisable(false);
            status.setText("Stato: Chiusa");
        });
        Form form = Form.of(
                Group.of(
                        Field.ofMultiSelectionType(selected.getCandidates())
                )
        );

        formSpace.getChildren().add(new FormRenderer(form));

    }
}
