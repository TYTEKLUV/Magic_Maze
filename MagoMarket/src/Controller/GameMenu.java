package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class GameMenu extends ControllerFXML{
    @FXML
    private Button createButton;

    @FXML
    private Button connectButton;

    @FXML
    private ListView<?> roomsList;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField playersCount;

    @FXML
    private TextField roomName;

    @FXML
    void Connect(ActionEvent event) {

    }

    @FXML
    void CreateGame(ActionEvent event) {

    }

    @FXML
    void Refresh(ActionEvent event) {

    }

}
