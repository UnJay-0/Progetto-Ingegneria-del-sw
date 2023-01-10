package com.ycv.youcanvote.controller.operator;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.entity.Party;
import com.ycv.youcanvote.entity.VotingSession;
import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.model.Session;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ShowParties {

    private SingleSelectionField<Party> partiesSelection;

    @FXML
    private VBox formSpace;

    @FXML
    private Label resultText;
    private List<Party> parties;


    @FXML
    private void initialize() {
        this.parties = Party.getParty();
        partiesSelection = Field.ofSingleSelectionType(this.parties)
                .label("Partito/Gruppo selezionato");

        Form form = Form.of(
                Group.of(
                        partiesSelection
                )
        );

        formSpace.getChildren().add(new FormRenderer(form));
    }
    @FXML
    private void createParty() {
        try {
            SceneController.switchScene(formSpace, "showParty.fxml", new ShowParty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void modifyParty() {
        Party toModify = partiesSelection.getSelection();
        ShowParty controller = new ShowParty(toModify);
        try {
            SceneController.switchScene(formSpace, "showParty.fxml", controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteParty() {
        resultText.setText("");
        boolean isUsed = false;
        for(VotingSession v : VotingSession.getVotingSession()) {
            for(Candidate c : v.getCandidatesList()) {
                if(c.equals(this.partiesSelection.getSelection())) {
                    isUsed = true;
                    break;
                }
            }
            if(isUsed) {
                break;
            }
        }
        if(isUsed) {
            resultText.setText("Il Partito è presente in una sessione di voto");
            return;
        }
        List<Candidate> candidates = partiesSelection.getSelection().getMembers();
        for (Candidate el : candidates) {
            EntityManager entityManager = Session.getInstance().getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.remove(el);
            entityManager.getTransaction().commit();
        }
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(partiesSelection.getSelection());
        entityManager.getTransaction().commit();
        this.parties.remove(partiesSelection.getSelection());
        partiesSelection.items(this.parties);
    }

    @FXML
    private void back() {
        try {
            SceneController.switchScene(formSpace, "homeGestore.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
