package bwg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load( getClass().getClassLoader().getResource("app.fxml"));
        primaryStage.setTitle("SC");

        primaryStage.initStyle(StageStyle.UNDECORATED);
        final Delta dragDelta = new Delta(0, 0);
        root.setOnMousePressed(mouseEvent -> {
            dragDelta.setX(primaryStage.getX() - mouseEvent.getScreenX());
            dragDelta.setY(primaryStage.getY() - mouseEvent.getScreenY());
        });
        root.setOnMouseDragged(mouseEvent -> {
            primaryStage.setX(mouseEvent.getScreenX() + dragDelta.getX());
            primaryStage.setY(mouseEvent.getScreenY() + dragDelta.getY());
        });

        Scene scene = new Scene (root, primaryStage.getWidth(), primaryStage.getHeight()+50, Color.TRANSPARENT);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("style/css.css").toExternalForm());
        scene.getWindow().setOpacity(.80);

        //root.toFront();
        primaryStage.setAlwaysOnTop(true);
        primaryStage.toFront();
        primaryStage.requestFocus();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
