package Model;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Ellipse;

public class draw implements EventHandler<KeyEvent> {

    private double x, y;
    GraphicsContext g;

    public draw(GraphicsContext g) {
        this.g = g;
        x = g.getCanvas().getWidth()/2;
        y = g.getCanvas().getHeight()/2;
    }

    @Override
    public void handle(KeyEvent event) {
        System.out.println(event.getCode());
        System.out.println(event.getCode());
        switch (String.valueOf(event.getCode())) {
            case "LEFT":
                x -= 60;
                rePaint(g);
                break;
            case "RIGHT":
                x += 60;
                rePaint(g);
                break;
            case "UP":
                y -= 60;
                rePaint(g);
                break;
            case "DOWN":
                y += 60;
                rePaint(g);
                break;
        }
    }
    private void rePaint (GraphicsContext g){
        g.fillOval(x, y, 60, 60);
    }
}
