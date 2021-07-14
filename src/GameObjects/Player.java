package GameObjects;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;


public class Player extends Motorcycle {
    private boolean accelerateFlag;
    private boolean isTurningLeft;
    private boolean isTurningRight;
    private boolean didCrash;

    private boolean wheelToggle = true;

    private int crashCounter = 0;
    private int leftAnimationCounter = 0;
    private int rightAnimationCounter = 0;


    public Player() {
        setImage("Img/Player/a.png");
        setX(580);
        setY(800);
    }

    public void update() {
        if(!didCrash)
            move();
        else {
            crashCounter++;
            explosionAnimation();
        }
    }

    private void move() {

        if (accelerateFlag) {
            accelerate();
        } else {
            deAccelerate();
        }

        if (isTurningLeft) {
            moveLeft();
        } else if (isTurningRight) {
            moveRight();
        } else {
            wheelMovement();
        }
    }

    private void moveLeft() {
        setX(getX() - 10);
        setImage("Img/Player/b2-l.png");
    }

    private void moveLeftAnimation() {

    }

    private void moveRight() {
        setX(getX() + 10);
        setImage("Img/Player/b2-r.png");
    }

    private void accelerate() {
        if(getSpeed() < 250) {
            setSpeed(getSpeed()+1);
        }
    }

    private void deAccelerate() {
        if (getSpeed() > 0) {
            setSpeed(getSpeed()-1);
        }
    }

    public void accelerateFlag(boolean b) {
        accelerateFlag = b;
    }
    public void turnRightFlag(boolean b) {
        isTurningRight = b;
    }
    public void turnLeftFlag(boolean b) {
        isTurningLeft = b;
    }

    private void wheelMovement() {
        if(getSpeed() > 0 && wheelToggle) {
            setImage("Img/Player/a.png");
            wheelToggle = false;
        } else if (getSpeed() > 0 && !wheelToggle) {
            setImage("Img/Player/b.png");
            wheelToggle = true;
        }
    }

    public int getCrashCounter() {
        return crashCounter;
    }

    public void crashFlag(boolean b) {
        didCrash = true;
        setSpeed(0);
    }

    public boolean didCrash() {
        return didCrash;
    }

    public boolean didCollideWithSides() {
        if (getX() < 30 || getX() > 1200) {
            return true;
        }
        return false;
    }

    public void sound() {
        float volLevel;

        if (getSpeed() == 0) {
            volLevel = -60;
        } else if (getSpeed() < 50) {
            volLevel = -18;
        } else if (getSpeed() < 100) {
            volLevel = -16;
        } else if (getSpeed() < 150) {
            volLevel = -14;
        } else if (getSpeed() < 200) {
            volLevel = -12;
        } else {
            volLevel = -10;
        }

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("BikeSound/vin.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volLevel);
            clip.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void slowDownBike() {
        setSpeed(50);
    }

    private void playExplosionSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("BikeSound/explosionSound2.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            clip.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void explosionAnimation() {
        if(crashCounter == 1) {
            playExplosionSound();
        }
        if(crashCounter < 5) {
            setImage("Img/Explosion/1.png");
        } if(crashCounter < 10) {
            setImage("Img/Explosion/2.png");
        } else if(crashCounter < 15) {
            setImage("Img/Explosion/3.png");
        } else if(crashCounter < 20) {
            setImage("Img/Explosion/4.png");
        } else if(crashCounter < 25) {
            setImage("Img/Explosion/5.png");
        } else if(crashCounter < 30) {
            setImage("Img/Explosion/6.png");
        }
    }
}
