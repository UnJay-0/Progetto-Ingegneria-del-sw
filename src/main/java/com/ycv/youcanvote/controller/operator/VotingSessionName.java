package com.ycv.youcanvote.controller.operator;
import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.entity.VotingSession;
import com.ycv.youcanvote.model.Answer;
import com.ycv.youcanvote.model.VotingSessionBuilder;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.Arrays;

public class VotingSessionName {

    private final VotingSessionBuilder build;

    private StringField name;

    private StringField description;

    private SingleSelectionField<VotingSession.ResultMod> modSelection;

    private BooleanField partyVoting;

    @FXML
    private VBox formSpace;

    @FXML
    private Button back;

    @FXML
    private Button forward;


    public VotingSessionName(VotingSessionBuilder build) {
        this.build = build;
    }

    @FXML
    public void initialize() {
        name = Field.ofStringType("")
                .label("Nome")
                .multiline(false);
        description = Field.ofStringType("")
                .label("Descrizione")
                .multiline(true);
        modSelection = Field.ofSingleSelectionType(build.getType().getMods(), 1)
                .label("Modalità di vittoria");
        partyVoting = Field.ofBooleanType(true)
                .label("Gruppi/Partiti")
                        .tooltip("Seleziona se i candidati per questa " +
                                "votazione sono gruppi o partiti");
        if (build.getType() == VotingSession.TypeOfVote.PREFERENTIALVOTE
                || build.getType() == VotingSession.TypeOfVote.REFERENDUM ) {
            partyVoting.editable(false);
        }

        back.setOnAction(event -> {
            Node node = (Node) event.getSource();
            VotingSessionType type = new VotingSessionType();
            try {
                SceneController.switchScene(node, "createVotingSession.fxml", type);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        forward.setOnAction(event -> {
            Node node = (Node) event.getSource();
            if (name.hasChanged() && description.hasChanged()) {
                build.setName(name.getValue());
                build.setDescription(description.getValue());
                build.setResultMod(modSelection.getSelection());
                build.setPartyVoting(partyVoting.getValue());
                VotingSessionCandidates controller = new VotingSessionCandidates(build);
                if(build.getType() == VotingSession.TypeOfVote.REFERENDUM) {
                    build.setCandidateList(Arrays.asList(new Answer("Favorevole"), new Answer("Contrario")));
                    build.saveVotingSession();
                    try {
                        SceneController.switchScene(forward, "homeGestore.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        SceneController.switchScene(node, "addCandidates.fxml", controller);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });

        Form form = Form.of(
                Group.of(
                        name,
                        description,
                        modSelection,
                        partyVoting
                )
        );

        formSpace.getChildren().add(new FormRenderer(form));

        if(build.getType() == VotingSession.TypeOfVote.REFERENDUM) {
            forward.setText("Completa e salva");
        }

    }
}
