package com.ycv.youcanvote;

import com.ycv.youcanvote.model.Session;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import java.io.IOException;


import org.scenicview.ScenicView;

public class YcvApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Session session = Session.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader(
                YcvApplication.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("YouCanVote!");
        stage.setScene(scene);
        stage.setOnCloseRequest(t -> {
            session.getEntityManager().close();
            session.getEntityManagerFactory().close();
            System.out.println("CLOSING");
        });
        stage.show();
        stage.getScene().getRoot().requestFocus();
        ScenicView.show(scene);


    }

    public static void main(String[] args) {
        launch();
    }
}
