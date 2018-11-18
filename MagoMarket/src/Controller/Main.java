package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../View/FieldEditor.fxml"));
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root, Color.TRANSPARENT);
        primaryStage.setMinWidth(scene.getWidth());
        primaryStage.setMinHeight(scene.getHeight());
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        System.out.println();
        //primaryStage.setMinWidth(878);
        primaryStage.show();
    }
}
