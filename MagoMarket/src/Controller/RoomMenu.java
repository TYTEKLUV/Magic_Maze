package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class RoomMenu extends ControllerFXML{
    @FXML
    private ListView<?> playersList;

    @FXML
    private Button readyButton;

    @FXML
    private Button startButton;

    ObservableList<String> items =FXCollections.observableArrayList ();

    @FXML
    void Ready(ActionEvent event) {
        if (readyButton.getText().equals("Готов")){
            readyButton.setText("Не готов");
        } else {
            readyButton.setText("Готов");
        }
        //сменить статус на готов/не готов
    }

    @FXML
    void Start(ActionEvent event) {
        //Заупустить игру
    }
}
