package com.ycv.youcanvote.controller.operator;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.model.Individual;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ShowCandidate {

    private final String candidateName;

    private StringField name;

    private boolean hasBeenDeleted = false;

    private boolean hasBeenSet = false;

    @FXML
    private VBox formSpace;

    @FXML
    private Button delete;

    @FXML
    private Button confirm;

    public ShowCandidate(){
        candidateName = "";
    }

    public ShowCandidate(Candidate toModify) {
        candidateName = toModify.name();
    }

    @FXML
    private void initialize(){
        name = Field.ofStringType(candidateName).label("Nome");

        Form form = Form.of(
                Group.of(
                    name
                )
        );

        delete.setOnAction(event -> {
            Stage stage = (Stage) delete.getScene().getWindow();
            hasBeenDeleted = true;
            stage.close();
        });

        confirm.setOnAction(event -> {
            Stage stage = (Stage) confirm.getScene().getWindow();
            hasBeenSet = true;
            stage.close();
        });

        formSpace.getChildren().add(new FormRenderer(form));
    }

    public Candidate getCandidate() {
        return new Individual(name.getValue());
    }

    public boolean hasBeenDeleted() {
        return hasBeenDeleted;
    }

    public boolean hasBeenSet() {
        return hasBeenSet;
    }

}
