package Controller;

import Model.CanvasListener;
import Model.CardEditorButtonListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;


public class CardEditorController {
    @FXML
    Button floor, door, wall;
    @FXML
    public Canvas canvas;

    @FXML
    void initialize () {
        canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, new CanvasListener(MouseEvent.MOUSE_CLICKED, canvas.getGraphicsContext2D()));
        initButtons();
    }

    private void initButtons () {
        floor.addEventHandler(MouseEvent.MOUSE_CLICKED, new CardEditorButtonListener("floor", canvas.getGraphicsContext2D()));
        door.addEventHandler(MouseEvent.MOUSE_CLICKED, new CardEditorButtonListener("door", canvas.getGraphicsContext2D()));
        wall.addEventHandler(MouseEvent.MOUSE_CLICKED, new CardEditorButtonListener("wall", canvas.getGraphicsContext2D()));
    }

}
