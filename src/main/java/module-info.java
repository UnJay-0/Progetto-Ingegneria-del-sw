module com.ycv.youcanvote {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.scenicview.scenicview;
    requires transitive javafx.web;
    requires transitive javafx.swing;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.jetbrains.annotations;

    opens com.ycv.youcanvote to javafx.fxml;
    exports com.ycv.youcanvote;
    exports com.ycv.youcanvote.model;
    opens com.ycv.youcanvote.model to javafx.fxml;
    exports com.ycv.youcanvote.controller;
    opens com.ycv.youcanvote.controller to javafx.fxml;
    exports com.ycv.youcanvote.controller.home;
    opens com.ycv.youcanvote.controller.home to javafx.fxml;
    exports com.ycv.youcanvote.controller.vote;
    opens com.ycv.youcanvote.controller.vote to javafx.fxml;
    exports com.ycv.youcanvote.controller.operator;
    opens com.ycv.youcanvote.controller.operator to javafx.fxml;
    opens com.ycv.youcanvote.entity;
    exports com.ycv.youcanvote.entity;
}