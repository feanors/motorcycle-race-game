package GameObjects;

import javax.swing.*;
import java.awt.*;

abstract class Motorcycle {
    private int x;
    private int y;
    private Image image;

    private int speed = 0;

    public int getSpeed() {
        return speed;
    }

    void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    void setY(int y) {
        this.y = y;
    }

    private Image getImage() {
        return image;
    }

    void setImage(String s) {
        this.image = new ImageIcon(getClass().getResource(s)).getImage();
    }

    Rectangle getBikeBounds() {
        return new Rectangle(getX()+20, getY()+40,getImage().getWidth(null)-40, getImage().getHeight(null)-60);
    }

    public void draw(Graphics g) {
        g.drawImage(getImage(),getX(),getY(),null);
    }
}
