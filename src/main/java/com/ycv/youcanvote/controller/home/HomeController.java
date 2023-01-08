package com.ycv.youcanvote.controller.home;

import com.ycv.youcanvote.controller.SceneController;
import com.ycv.youcanvote.model.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;


import javafx.stage.Stage;

import java.io.IOException;


public class HomeController {
    private void logUser(boolean forOperator, Stage thisStage) {
        try{
            LoginController loginController = new LoginController(forOperator);
            SceneController.loadPopup(thisStage, "login.fxml", loginController);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    protected void areaGestori(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Session session = Session.getInstance();

        logUser(true, thisStage);

        if (session.userLogged()) {
            try {
                SceneController.switchScene(node, "homeGestore.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @FXML
    protected void vota(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        Session session = Session.getInstance();

        logUser(false, thisStage);

        if (session.userLogged()){
            try {
                SceneController.switchScene(node, "votingSessionSelection.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
