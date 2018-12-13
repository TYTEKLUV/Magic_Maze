package Game.Model;

import Game.Server.OmegaServer;
import javafx.stage.Stage;

public class ControllerFXML {
    private Stage stage;
    private OmegaServer main;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public OmegaServer getMain() {
        return main;
    }

    public void setMain(OmegaServer main) {
        this.main = main;
    }
}
