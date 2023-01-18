package com.ycv.youcanvote.controller.operator;

import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.model.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.IOException;

public class HomeGestore {
    @FXML
    private Label greetText;

    public void initialize() {
        greetText.setText("Benvenuto " + Session.getInstance().getUser().getName());
    }
    @FXML
    private void createVotingSession(ActionEvent event) {
        Node node = (Node) event.getSource();
        VotingSessionType type = new VotingSessionType();
        try {
            SceneController.switchScene(node, "createVotingSession.fxml", type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void manageVotingSession(ActionEvent event) {
        Node node = (Node) event.getSource();
        try {
            SceneController.switchScene(node, "manageVotingSession.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void manageCandidates(ActionEvent event) {
        Node node = (Node) event.getSource();
        try {
            SceneController.switchScene(node, "showParties.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void back(ActionEvent event) {
        Node node = (Node) event.getSource();
        try {
            Session.getInstance().removeUser();
            SceneController.switchScene(node, "home.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
