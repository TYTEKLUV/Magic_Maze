package Game.Model;

import Game.Server.Room;
import javafx.stage.Stage;

public class ControllerFXML {
    private Stage stage;
    private Room main;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Room getMain() {
        return main;
    }

    public void setMain(Room main) {
        this.main = main;
    }
}
