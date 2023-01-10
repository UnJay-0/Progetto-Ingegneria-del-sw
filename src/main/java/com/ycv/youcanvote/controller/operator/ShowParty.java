package com.ycv.youcanvote.controller.operator;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.entity.Individual;
import com.ycv.youcanvote.entity.VotingSession;
import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.entity.Party;
import com.ycv.youcanvote.model.Session;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowParty {


    private final String partyName;
    private List<Candidate> partyMembers;
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

    @FXML
    private Label resultText;

    private Party toModify;



    public ShowParty() {
        this.partyName = "";
        this.partyMembers = new ArrayList<Candidate>();
    }

    public ShowParty(Party toModify) {
        this.toModify = toModify;
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
            if (toModify == null) {
                Party newParty = new Party(name.getValue());
                EntityManager entityManager = Session.getInstance().getEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(newParty);
                entityManager.getTransaction().commit();
            } else if(name.hasChanged()){
               toModify.alterName(name.getValue());
            }
            Node node = (Node) event.getSource();
            try {
                SceneController.switchScene(node, "showParties.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        modify.setOnAction(event -> {
            resultText.setText("");
            Stage stage = (Stage) modify.getScene().getWindow();
            Candidate toModify = membersSelection.getSelection();
            ShowCandidate controller = new ShowCandidate(toModify, this.toModify);
            try {
                SceneController.loadPopup(stage, "showCandidate.fxml", controller);
                partyMembers.remove(toModify);
                if(!controller.hasBeenDeleted()) {
                    EntityManager entityManager = Session.getInstance().getEntityManager();
                    entityManager.getTransaction().begin();
                    entityManager.persist(controller.getCandidate());
                    entityManager.getTransaction().commit();
                } else {
                    boolean isUsed = false;
                    for(VotingSession v : VotingSession.getVotingSession()) {
                        for(Candidate c : v.getCandidatesList()) {
                            if(c.equals(controller.getCandidate())) {
                                isUsed = true;
                                break;
                            }
                        }
                        if(isUsed) {
                            break;
                        }
                    }
                    if(isUsed) {
                        resultText.setText("Il candidato è presente in una sessione di voto");
                        return;
                    }
                    EntityManager entityManager = Session.getInstance().getEntityManager();
                    entityManager.getTransaction().begin();
                    entityManager.remove(controller.getCandidate());
                    entityManager.getTransaction().commit();
                }
                this.toModify = Party.getPartyById(this.toModify.getPartyId());
                partyMembers = this.toModify.getMembers();

                membersSelection.items(partyMembers);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        add.setOnAction(event -> {
            resultText.setText("");

            if (toModify == null && name.getValue().equals("")) {
                resultText.setText("Inserire il nome del Partito/Gruppo prima di aggiungere candidati");
                return;
            } else if(toModify == null) {
                resultText.setText("");
                this.toModify = new Party(name.getValue());
                EntityManager entityManager = Session.getInstance().getEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(this.toModify);
                entityManager.getTransaction().commit();
            }

            Stage stage = (Stage) modify.getScene().getWindow();
            ShowCandidate controller = new ShowCandidate(this.toModify);


            try {
                SceneController.loadPopup(stage, "showCandidate.fxml", controller);
                if(!controller.hasBeenDeleted() && controller.hasBeenSet()) {
                    EntityManager entityManager = Session.getInstance().getEntityManager();
                    entityManager.getTransaction().begin();
                    entityManager.persist(controller.getCandidate());
                    entityManager.getTransaction().commit();

                    toModify = Party.getPartyById(toModify.getPartyId());
                    partyMembers = toModify.getMembers();
                    membersSelection.items(partyMembers);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
