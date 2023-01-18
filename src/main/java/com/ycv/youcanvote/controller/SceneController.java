package com.ycv.youcanvote.controller;

import com.ycv.youcanvote.YcvApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public final class SceneController {

    private SceneController(){}

    public static <T> void loadPopup(Stage owner, String sceneName, T controller) throws IOException {
        final Stage dialog = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(
                YcvApplication.class.getResource(sceneName));

        fxmlLoader.setController(controller);


        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);
        Scene dialogScene = new Scene(fxmlLoader.load(), 1000, 800);
        dialogScene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
    public static void switchScene(Node source, String sceneName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                YcvApplication.class.getResource(sceneName));
        Parent view = fxmlLoader.load();
        sceneLoader(source, view);
    }

    public static <T> void switchScene(Node source, String sceneName, T controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                YcvApplication.class.getResource(sceneName));
        fxmlLoader.setController(controller);
        Parent view = fxmlLoader.load();
        sceneLoader(source, view);
    }

    public static void sceneLoader(Node source, Parent newView) {
        Scene thisScene = (Scene) source.getScene();
        thisScene.setRoot(newView);
    }
}
