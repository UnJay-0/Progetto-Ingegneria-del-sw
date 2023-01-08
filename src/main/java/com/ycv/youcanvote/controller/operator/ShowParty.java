package com.ycv.youcanvote.controller.operator;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.model.Party;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowParty {


    private final String partyName;
    private final List<Candidate> partyMembers;
    private StringField name;

    private SingleSelectionField<Candidate> membersSelection;

    @FXML
    private VBox formSpace;

    @FXML
    private Button back;

    @FXML
    private Button confirm;

    @FXML
    private Button add;

    @FXML
    private Button modify;

    public ShowParty() {
        this.partyName = "";
        this.partyMembers = new ArrayList<Candidate>();
    }

    public ShowParty(Party toModify) {
        this.partyName = toModify.name();
        this.partyMembers = toModify.getMembers();
    }

    @FXML
    private void initialize() {
        name = Field.ofStringType(this.partyName).label("Nome");
        membersSelection = Field.ofSingleSelectionType(this.partyMembers);

        Form form = Form.of(
                Group.of(
                        name,
                        membersSelection
                )
        );

        formSpace.getChildren().add(new FormRenderer(form));


        back.setOnAction(event -> {
            Node node = (Node) event.getSource();
            try {
                SceneController.switchScene(node, "showParties.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        confirm.setOnAction(event -> {
//            Party newParty = new Party(membersSelection.getItems(), name.getValue());
            System.out.println(name.getValue());
            System.out.println(membersSelection.getItems());
        });

        modify.setOnAction(event -> {
            Stage stage = (Stage) modify.getScene().getWindow();
            Candidate toModify = membersSelection.getSelection();
            ShowCandidate controller = new ShowCandidate(toModify);
            try {
                SceneController.loadPopup(stage, "showCandidate.fxml", controller);
                partyMembers.remove(toModify);
                if(!controller.hasBeenDeleted()) {
                    partyMembers.add(controller.getCandidate());
                    membersSelection.items(partyMembers);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        add.setOnAction(event -> {
            Stage stage = (Stage) modify.getScene().getWindow();
            ShowCandidate controller = new ShowCandidate();
            try {
                SceneController.loadPopup(stage, "showCandidate.fxml", controller);
                if(!controller.hasBeenDeleted() && controller.hasBeenSet()) {
                    partyMembers.add(controller.getCandidate());
                    membersSelection.items(partyMembers);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
