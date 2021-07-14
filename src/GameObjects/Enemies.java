package GameObjects;

import java.awt.*;
import java.util.ArrayList;

public class Enemies {

    private ArrayList<Enemy> enemies;

    public boolean startHunting;
    private Player playerToHunt;

    public Enemies(Player playerToHunt) {
        this.playerToHunt = playerToHunt;

        enemies = new ArrayList<>();
        enemies.add(new Enemy(playerToHunt,240, 610,350,220, 200,720));
        enemies.add(new Enemy(playerToHunt,300,660,350,230,860,720));
        enemies.add(new Enemy(playerToHunt,1200,660,350,255,260,850));
        enemies.add(new Enemy(playerToHunt,660,660,350,245,700,600));

    }

    public void turnLeftFlag(boolean b) {
        for (Enemy e : enemies) {
            e.turnLeftFlag(b);
        }
    }

    public void turnRightFlag(boolean b) {
        for (Enemy e : enemies) {
            e.turnRightFlag(b);
        }
    }

    public void update(int curvature) {
        for (Enemy e : enemies) {
            e.update(curvature);
        }
    }

    public boolean collidesWithPlayer() {
        for (Enemy e : enemies) {
            if (e.doesCollideWithPlayer()) {
                return true;
            }
        }

        return false;
    }

    public void draw(Graphics g) {
        for (Enemy e : enemies) {
            e.draw(g);
        }
    }

    public void incrementBlockingDegree() {
        for (Enemy e : enemies) {
            e.incrementBlockingDegree();
        }
    }

}
