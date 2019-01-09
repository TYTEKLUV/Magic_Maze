package Controller;

import Model.PlayerList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;

public class RoomMenu extends ControllerFXML{
    @FXML
    private ListView<String> playersList;

    @FXML
    private Button readyButton;

    @FXML
    private Button startButton;

    ObservableList<String> items =FXCollections.observableArrayList ();

    @FXML
    void Ready(ActionEvent event) throws IOException {
        if (readyButton.getText().equals("Готов")){
            readyButton.setText("Не готов");
            try {
                getMain().getClientStarter().commandHandler("ready");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            readyButton.setText("Готов");
            getMain().getClientStarter().commandHandler("nready");
        }
        updateList();
        //сменить статус на готов/не готов
    }

    @FXML
    void Start(ActionEvent event) throws IOException {
        getMain().getClientStarter().commandHandler("start");
    }

    public Button getStartButton() {
        return startButton;
    }

    public void updateList() {
        items.clear();
        PlayerList players = getMain().getClientStarter().getPlayers();
        for (int i = 0; i < players.size(); i++) {
            items.add(players.get(i).forList());
        }
        playersList.setItems(items);
    }
}
