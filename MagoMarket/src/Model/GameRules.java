package Model;

import Controller.GameWindow;

import java.io.IOException;

public class GameRules {

    Point calcD(Point start, Point end) {
        double dx = (Math.abs(end.x - start.x));
        double dy = (Math.abs(end.y - start.y));
        return new Point(dx, dy);
    }

    public void mouseMoved(Point event, GameWindow gameWindow) {
        if (!gameWindow.getCards().get(gameWindow.getMoveCard()).isVisible()) {
            gameWindow.getCards().get(gameWindow.getMoveCard()).setVisible(true);
        }
        double x = 0, y = 0;
        findClosestGlass(event, gameWindow);
        String pos = String.valueOf((int) gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getPosition().x) + String.valueOf((int) gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getPosition().y);
        switch (pos) {
            case "31":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutX() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth() / 4;
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutY() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth();
                rotateCard(gameWindow, 6, 2);
                break;
            case "43":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutX() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth();
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutY() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth() / 4;
                rotateCard(gameWindow, 2, 0);
                break;
            case "24":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutX() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth() / 4;
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutY() + gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth();
                rotateCard(gameWindow, 0, 4);
                break;
            case "12":
                x = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutX() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth();
                y = gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getLayoutY() - gameWindow.getCards().get(gameWindow.getChips().get(gameWindow.getClosestFindGlassId()).getCardId()).getImage().getWidth() / 4;
                rotateCard(gameWindow, 4, 6);
                break;
        }
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutX(x);
        gameWindow.getCards().get(gameWindow.getMoveCard()).setLayoutY(y);
    }

    public void moveReleased(GameWindow gameWindow) throws IOException {
        gameWindow.getCards().get(gameWindow.getMoveCard()).toBack();
        gameWindow.getClient().addCard(gameWindow.getMoveCard(), (int) gameWindow.getCards().get(gameWindow.getMoveCard()).getLayoutX(), (int) gameWindow.getCards().get(gameWindow.getMoveCard()).getLayoutY(), (int) gameWindow.getCards().get(gameWindow.getMoveCard()).getRotate());
        gameWindow.setMoveCard(false);
        gameWindow.getNewCardBtn().setDisable(false);
    }

    public void commandMove (int id, int x, int y, GameWindow gameWindow) {
        gameWindow.getChips().get(id).setLayoutX(x);
        gameWindow.getChips().get(id).setLayoutY(y);
        gameWindow.getChips().get(id).setDefault();
        gameWindow.getChips().get(id).setBusy(false);
    }

    public void mouseReleased(Point event, GameWindow gameWindow) throws IOException {
        boolean f = false;
        for (int i = 0; i < 4; i++) {
            Chip chip = gameWindow.getChips().get(i);
            if (chip.isSelected) {
                f = true;
                Point point = event.getPosition(false, gameWindow);
                gameWindow.getClient().send("GAME MOVE " + i + " " + (int)point.x + " " + (int)point.y);
            }
        }
        if (!f) {
            for (int i = 0; i < 4; i++) {
                Chip chip = gameWindow.getChips().get(i);
                if (!chip.isBusy && (event.x > chip.getLayoutX()) && (event.x < chip.getLayoutX() + chip.getImage().getWidth()) && (event.y > chip.getLayoutY()) && (event.y < chip.getLayoutY() + chip.getImage().getWidth())) {
                    chip.setClicked();
                    gameWindow.getClient().send("GAME BUSY " + i);
                }
            }
        }

//        gameWindow.getClient().sendClick((int)event.x, (int)event.y);
    }

    private void rotateCard(GameWindow gameWindow, int x, int y) {
        while (gameWindow.getCards().get(gameWindow.getMoveCard()).getMap()[x][y] != 20) {
            gameWindow.getCards().set(gameWindow.getMoveCard(), rotateMap(gameWindow.getCards().get(gameWindow.getMoveCard())));
        }
    }

    private Card rotateMap(Card card) {
        int[][] map = new int[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                map[i][j] = card.getMap()[6 - j][i];
            }
        }
        // ---------------------- ТУТ МОСТЫ
        int b = card.bridges;
        if (card.bridges != 0) {
            int bridge = b;
            b = 0;
            while (bridge != 0) {
                int a = bridge % 10000;
                int a1 = a % 100;
                a = a / 100;
                a = 10000 + 1000 * (a % 10) + 100 * (3 - a / 10) + 10 * (a1 % 10) + (3 - a1 / 10);
                b = b * 100000 + a;
                bridge = bridge / 100000;
            }
        }
        card.bridges = b;
        // --------------------- ТУТ МОСТЫ
        card.setMap(map);
        card.setRotate(card.getRotate() + 90);
        return card;
    }

    private void findClosestGlass(Point event, GameWindow gameWindow) {
        double min = gameWindow.getPane().getPrefWidth();
        double x, y;
        for (int i = 0; i < gameWindow.getFindGlasses().size(); i++) {
            int chipId = gameWindow.getFindGlasses().get(i);
            x = gameWindow.getChips().get(chipId).getLayoutX();
            y = gameWindow.getChips().get(chipId).getLayoutY();
            double length = Math.sqrt(Math.pow((event.x - x), 2) + Math.pow((event.y - y), 2));
            if (length < min) {
                min = length;
                gameWindow.setClosestFindGlassId(chipId);
            }
        }
    }
}
