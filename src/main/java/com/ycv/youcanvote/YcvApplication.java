package com.ycv.youcanvote;

import com.ycv.youcanvote.entity.*;
import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.model.Session;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.kordamp.bootstrapfx.BootstrapFX;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.scenicview.ScenicView;

public class YcvApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        TypedQuery<User> getUserByCf = entityManager.createNamedQuery(
                "User.byId", User.class);
        getUserByCf.setParameter(1, "123456789abcdef");
        TypedQuery<VotingSession> getVsById = entityManager.createNamedQuery(
                "VotingSession.byId", VotingSession.class
        );
        getVsById.setParameter(1, 1);
        Vote vote = new Vote("1", getVsById.getSingleResult());
        VoteStory voteStory = new VoteStory(getUserByCf.getSingleResult(), getVsById.getSingleResult());

        entityManager.persist(vote);
        entityManager.persist(voteStory);

        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();



        Session session = Session.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader(
                YcvApplication.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("YouCanVote!");
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<>() {
            @Override
            public void handle(WindowEvent t) {
                session.getEntityManager().close();
                session.getEntityManagerFactory().close();
                System.out.println("CLOSING");
            }
        });
        stage.show();
        stage.getScene().getRoot().requestFocus();
        ScenicView.show(scene);


    }

    public static void main(String[] args) {
        launch();
    }
}
