package Controller;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GameMenu extends ControllerFXML{
    @FXML
    private Button createButton;

    @FXML
    private Button connectButton;

    @FXML
    private TextField playersCount;

    @FXML
    private TextField roomName;

    @FXML
    private TextField roomNameConnect;

    private  Alert alert = new Alert(Alert.AlertType.INFORMATION);

    @FXML
    void Connect(ActionEvent event) throws IOException {
        //подключиться
        //выдать ошибку
        //сменить окно
        if (!roomNameConnect.getText().equals("")){
            getMain().getClientStarter().commandHandler("connect " + roomNameConnect.getText());
        } else {
            alert.setTitle("Ошибка!");
            alert.setHeaderText(null);
            alert.setContentText("Введите имя комнаты!");
            alert.showAndWait();
        }

    }

    @FXML
    void CreateGame(ActionEvent event) throws IOException {
        System.out.println("Создана");
        getMain().getClientStarter().commandHandler("connect " + roomName.getText() + " " + playersCount.getText());
        getMain().getMainMenu().getStage().setScene(getMain().getRoomMenu().getStage().getScene());
        //создать комнату
        //сменить окно
    }
}
