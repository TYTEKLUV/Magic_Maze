package Model;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class CanvasListener implements EventHandler<MouseEvent> {

    private EventType eventType;
    private GraphicsContext g;
    public static String type = "";
    public static ArrayList<Card> queue = new ArrayList<>();
    public static Card activeCard;
    private int gridW = 1;
    public CanvasListener (EventType<MouseEvent> eventType, GraphicsContext g) {
        this.eventType = eventType;
        this.g = g;
        drawGrid(g);
    }

    @Override
    public void handle(MouseEvent event) {
//        switch (String.valueOf(event.getEventType())){
//            case "MOUSE_CLICKED":
//                break;
//            case "":
//                break;
//        }
       mouseClicked(event);
    }

    private void mouseClicked (MouseEvent event) {
        switch (CanvasListener.type) {
            case "floor" :
                drawFloor(event, g);
                break;
            case "wall" :
                drawWall(event, g);
                break;
        }
    }

    private void drawFloor (MouseEvent event, GraphicsContext g) {
        double x = Math.ceil(event.getX()/60);
        double y = Math.ceil(event.getY()/60);
        System.out.println((int)x + " " + (int)y);
        g.setStroke(Color.WHITE);
        g.setLineWidth(2);
        g.strokeRect(x * 60 - 60 + 6, y * 60 - 60 + 6, 60, 60);
        if (activeCard.floor[(int)x - 1][(int)y - 1] == 0) {
            g.setFill(Color.rgb(74, 74, 74));
            activeCard.floor[(int)x - 1][(int)y - 1] = 1;
        }
        else {
            g.setFill(Color.rgb(76, 93, 99));
            activeCard.floor[(int)x - 1][(int)y - 1] = 0;
        }
        g.fillRect(x * 60 - 60 +  7, y * 60 - 60 + gridW + 6, 58, 58);
    }

    private void drawWall (MouseEvent event, GraphicsContext g) {
        double x = Math.ceil(event.getX()/60 * 100) / 100;
        double y = Math.ceil(event.getY()/60 * 100) / 100;
        System.out.println(x + " " + y);
        if (y > Math.floor(y) + 0.5) {
            g.setStroke(Color.WHITE);
            g.setLineWidth(2);
            g.strokeRect(Math.floor(x) * 60 + 6, Math.ceil(y) * 60 - 8 + 6, 60, 8 );
            g.setFill(Color.rgb(76, 93, 99));
            g.fillRect(Math.floor(x) * 60 + gridW + 6, Math.ceil(y) * 60 - 6 + 6, 60 - 2, 6 - gridW);
        }
        else {
            System.out.println("else");
            g.setStroke(Color.WHITE);
            g.setLineWidth(2);
            g.strokeRect(Math.floor(x) * 60 + 6, Math.ceil(y) * 60 - 60 + 6, 60, 6 + 2);
            g.setFill(Color.rgb(76, 93, 99));
            g.fillRect(Math.floor(x) * 60 + gridW + 8, Math.ceil(y) * 60 - 60 + gridW + 6, 60 - 4, 6);
        }
    }

    private void drawGrid (GraphicsContext g) {
        g.setStroke(Color.WHITE);
        for (int i = 0; i < 5; i ++) {
            g.strokeLine(i * 60 + 6, 6, i * 60 + 6, g.getCanvas().getHeight() - 6);
        }
        for (int i = 0; i < 5; i ++) {
            g.strokeLine(6, i * 60 + 6, g.getCanvas().getHeight() - 6, i * 60 + 6);
        }
    }

}
