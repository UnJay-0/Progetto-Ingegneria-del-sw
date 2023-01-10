package com.ycv.youcanvote.controller.operator;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.StringField;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.entity.Party;
import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.entity.Individual;
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

    private Candidate toModify;
    private Party party;

    public ShowCandidate(Party party){
        candidateName = "";
        this.party = party;
    }

    public ShowCandidate(Candidate toModify, Party party) {
        this.toModify = toModify;
        this.party = party;
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
        if (this.toModify == null)
            return new Individual(name.getValue(), party);
        else if(!this.hasBeenDeleted){
            toModify.alterName(name.getValue());
            return toModify;
        } else {
            return toModify;
        }
    }

    public boolean hasBeenDeleted() {
        return hasBeenDeleted;
    }

    public boolean hasBeenSet() {
        return hasBeenSet;
    }

}
