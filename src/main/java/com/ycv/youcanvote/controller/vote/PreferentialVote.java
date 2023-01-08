package com.ycv.youcanvote.controller.vote;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.MultiSelectionField;
import com.dlsc.formsfx.view.controls.SimpleCheckBoxControl;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.model.Party;
import com.ycv.youcanvote.entity.VotingSession;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class PreferentialVote implements Vote {

    private final Party choice;

    private final VotingSession votingSession;

    private MultiSelectionField<Candidate> members;

    @FXML
    private VBox titleSpace;

    @FXML
    private VBox formSpace;

    @FXML
    private VBox root;

    @FXML
    private Button confirm;

    @FXML
    private Button blank;

    public PreferentialVote(Party choice, VotingSession session) {
        this.choice = choice;
        this.votingSession = session;
    }

    public void initialize() {
        Label voteTitle = new Label("Voto Preferenziale per scelta: " + choice.name());
        voteTitle.setStyle("-fx-font-size: 25; -fx-text-fill: rgb(25, 69, 107);");
        confirm.setOnAction(e -> {
            ConfirmVote confirmVote = new ConfirmVote(this);
            try {
                SceneController.switchScene(confirm, "confirmVote.fxml", confirmVote);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        blank.setOnAction(event -> this.blankVote());
        this.members = Field.ofMultiSelectionType(choice.getMembers())
                .render(new SimpleCheckBoxControl<>());
        Form form = Form.of(
                com.dlsc.formsfx.model.structure.Group.of(
                    members
                )
        );
        Button back = new Button("Indietro");
        back.setOnAction( e -> {
            try {
                SceneController.switchScene(back, "vote.fxml", votingSession.getVote());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        HBox p = new HBox();
        p.setPadding(new Insets(60.0));
        back.getStyleClass().add("btn");
        back.setStyle("-fx-font-size: 20");

        p.getChildren().add(back);

        root.getChildren().add(p);
        titleSpace.getChildren().add(voteTitle);
        formSpace.getChildren().add(new FormRenderer(form));
    }

    private void blankVote() {
        System.out.println("Blank vote");
        Stage thisStage = (Stage) formSpace.getScene().getWindow();
        thisStage.close();
    }

    @Override
    public void confirmVote() {
        System.out.println(votingSession.getName() + ": " + choice + "\n" + members.getSelection());
    }

    @Override
    public Node render() {
        Text selection = new Text();
        selection.setStyle("-fx-font-size: 20");
        StringBuilder out = new StringBuilder();
        out.append(String.format("Il candidato selezionato è: %s\nCon preferenza per: ", choice));
        for(Candidate pref: members.getSelection()){
            out.append(String.format("\n%s ", pref.name()));
        }
        selection.setText(
                out.toString()
        );
        return selection;
    }

}
