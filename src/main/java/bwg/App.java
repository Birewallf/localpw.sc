package bwg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("app.fxml")));
        primaryStage.setTitle("SC");

        primaryStage.initStyle(StageStyle.UNDECORATED);
        final Delta dragDelta = new Delta(0, 0);
        root.setOnMousePressed(mouseEvent -> {
            root.requestFocus();
            dragDelta.setX(primaryStage.getX() - mouseEvent.getScreenX());
            dragDelta.setY(primaryStage.getY() - mouseEvent.getScreenY());
        });
        root.setOnMouseDragged(mouseEvent -> {
            primaryStage.setX(mouseEvent.getScreenX() + dragDelta.getX());
            primaryStage.setY(mouseEvent.getScreenY() + dragDelta.getY());
        });

        Scene scene = new Scene (root, primaryStage.getWidth(), primaryStage.getHeight()+50, Color.TRANSPARENT);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("style/css.css")).toExternalForm());
        scene.getWindow().setOpacity(.87);

        //root.toFront();
        primaryStage.setAlwaysOnTop(true);
        primaryStage.toFront();
        primaryStage.requestFocus();

        primaryStage.getIcons().add(new Image("icon.png"));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
