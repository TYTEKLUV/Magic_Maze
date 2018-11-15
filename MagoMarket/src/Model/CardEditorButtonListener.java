package Model;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class CardEditorButtonListener implements EventHandler<MouseEvent> {

    String type;
    GraphicsContext g;

    public CardEditorButtonListener (String type, GraphicsContext g) {
        this.type = type;
        this.g = g;
    }

    @Override
    public void handle(MouseEvent event) {
        switch (type) {
            case "floor":
                CanvasListener.type = "floor";
                break;
            case "wall":
                CanvasListener.type = "wall";
                break;
            case "door":
                CanvasListener.type = "door";
                break;
        }
    }

}
