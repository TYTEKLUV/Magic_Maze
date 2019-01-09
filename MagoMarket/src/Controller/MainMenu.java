package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MainMenu extends ControllerFXML {
    @FXML
    private Button play;

    @FXML
    private TextField nickField;
    private  Alert alert = new Alert(Alert.AlertType.INFORMATION);


    @FXML
    void StartClick(ActionEvent event) throws IOException {
        if (!nickField.getText().equals("")){
            getMain().getClientStarter().commandHandler("nick " + nickField.getText());
            getStage().setScene(getMain().getGameMenu().getStage().getScene());
        } else {
            alert.setTitle("Ошибка!");
            alert.setHeaderText(null);
            alert.setContentText("Введите никнейм!");
            alert.showAndWait();
        }
    }
}
