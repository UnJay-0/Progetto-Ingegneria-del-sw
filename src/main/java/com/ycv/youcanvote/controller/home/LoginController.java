package com.ycv.youcanvote.controller.home;

import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import com.ycv.youcanvote.YcvApplication;
import com.ycv.youcanvote.model.Session;
import com.ycv.youcanvote.model.WrongCredentialsException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;


public class LoginController {

    @FXML
    private VBox loginFormSpace;
    @FXML
    private Label resultText;
    @FXML
    private Button submit;

    private StringField codiceFiscale;
    private PasswordField password;
    private final Boolean forOperator;



    public LoginController(Boolean forOperator) {
        this.forOperator = forOperator;
    }

    @FXML
    public void initialize() {

        submit.setOnAction(e -> this.submit());

        StringProperty cf = new SimpleStringProperty("");
        StringProperty pwd = new SimpleStringProperty("");
        StringField codiceFiscale = Field.ofStringType(cf)
                .label("Codice Fiscale")
                .required("");
        PasswordField password = Field.ofPasswordType(pwd)
                .label("Password")
                .required("")
                .bind(pwd);

        this.codiceFiscale = codiceFiscale;
        this.password = password;

        Form loginForm = Form.of(
                Group.of(
                        codiceFiscale,
                        password
                )
        ).title("Login");

        ImageView logo = new ImageView(new Image(Objects.requireNonNull(YcvApplication.class.getResourceAsStream("logo_small_icon_only.png"))));

        loginFormSpace.getChildren().add(logo);
        loginFormSpace.getChildren().add(new FormRenderer(loginForm));
        loginFormSpace.setAlignment(Pos.CENTER);

    }

    public void submit() {
        codiceFiscale.persist();
        password.persist();
        Session session = Session.getInstance();
        try {
            if (codiceFiscale.getValue().equals("") || password.getValue().equals("")) {
                throw new WrongCredentialsException("Codice fiscale o password non possono essere vuoti");
            }
            session.setUser(codiceFiscale.getValue(), password.getValue(), forOperator);
            Stage stage = (Stage) submit.getScene().getWindow();
            stage.close();
        } catch (WrongCredentialsException e) {
            wrongCredentials(e.getMessage());
            codiceFiscale.reset();
            password.reset();
        }
    }

    public void wrongCredentials(String error) {
        resultText.setText(error);
    }
}
