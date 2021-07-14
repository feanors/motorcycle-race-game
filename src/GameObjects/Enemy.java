package GameObjects;

import java.awt.*;

public class Enemy extends Motorcycle {

    private Player playerToHunt;

    private boolean currentlyOnMap = true;
    private boolean wheelToggle = false;
    private int degreeOfBlocking = 0;

    private int timeTillReAppear;
    private final int reAppearTime;

    private boolean turnLeft = false;
    private boolean turnRight = false;

    private int reAppearX;
    private int reAppearY;

    private int maxSpeed;

    Enemy(Player p, int reAppearTime, int xCord, int yCord, int speed, int startCoordX, int startCoordY) {
        setImage("Img/Enemy/m1.png");
        playerToHunt = p;

        this.reAppearTime = reAppearTime;
        timeTillReAppear = reAppearTime;

        maxSpeed = speed;
        setSpeed(0);

        reAppearX = xCord;
        reAppearY = yCord;

        setX(startCoordX);
        setY(startCoordY);

    }

    void update(int curvature) {
        //stayInRoad(curvature);
        if(currentlyOnMap) {
            currentlyOnMap = isEnemyOutOfSite();
            move();
            if(degreeOfBlocking > 0) {
                blockPlayer();
            }
        } else {
            reAppear(curvature);
        }
    }

    private void move() {
        if (turnLeft) {
            turnLeftAnimation();
            setSpeed(getSpeed()-1);
        } else if (turnRight) {
            turnRightAnimation();
            setSpeed(getSpeed()-1);
        } else {
            incrementSpeed();
            if (wheelToggle) {
                if (getY() < 500) {
                    setImage("Img/Enemy/1.png");
                } else {
                    setImage("Img/Enemy/m1.png");
                }
                wheelToggle = false;
            } else {
                if (getY() < 500) {
                    setImage("Img/Enemy/2.png");
                } else {
                    setImage("Img/Enemy/m2.png");
                }
                wheelToggle = true;
            }
        }
    }

    private void turnLeftAnimation() {
        if (getY() < 500) {
            setImage("Img/Enemy/3.png");
        } else {
            setImage("Img/Enemy/m3.png");
        }

        setX(getX()-1);
    }

    private void turnRightAnimation() {
        if (getY() < 500) {
            setImage("Img/Enemy/4.png");
        } else {
            setImage("Img/Enemy/m4.png");
        }

        setX(getX()+1);
    }

    private void blockPlayer(){
        if (playerToHunt.getY() > this.getY()) {
            if (getY() > 480) {
                if (playerToHunt.getX() + 30 < getX()) {
                    setX(getX() - (degreeOfBlocking));
                } else if (playerToHunt.getX() - 30 > getX()) {
                    setX(getX() + (degreeOfBlocking));
                }
            }
        }
    }

    private boolean isEnemyOutOfSite() {
        yCordAccordingToPlayerSpeed();

        return getY() <= 960 && getY() >= 350;
    }

    private void yCordAccordingToPlayerSpeed() {
        setY(getY() + ( (playerToHunt.getSpeed() - this.getSpeed()) / 10 ));
    }

    private void reAppear(int curvature) {
        timeTillReAppear--;
        incrementSpeed();
        if (timeTillReAppear == 0) {
            setY(reAppearY);
            setX(reAppearX + (curvature / 3) );
            currentlyOnMap = true;
            timeTillReAppear = reAppearTime;
        }
    }

    boolean doesCollideWithPlayer() {
        if (playerToHunt.getBikeBounds().intersects(this.getBikeBounds())) {
            return true;
        }
        return false;
    }

    void incrementBlockingDegree() {
        degreeOfBlocking++;
    }

    public void draw(Graphics g) {
        if (currentlyOnMap) {
            super.draw(g);
        }
    }

    void turnLeftFlag(boolean b) {
        turnLeft = b;
    }

    void turnRightFlag(boolean b) {
        turnRight = b;
    }

    @Override
    void setSpeed(int speed) {
        if ( speed >= maxSpeed-20 && speed <= maxSpeed)
            super.setSpeed(speed);
    }

    private void incrementSpeed() {
        if (getSpeed() < maxSpeed) {
            super.setSpeed(getSpeed()+1);
        }
    }

    /*
    hopefully :p
     */
    private void stayInRoad(int curvature) {
        if (getY() < 640) {
            if (getX() < 600 && curvature > 200) {
                setX(getX() + 2);
            } else if (getX() > 600 && curvature < -200) {
                setX(getX() - 2);
            } else if (curvature < 200 && curvature > - 200) {
                if (getX() < 600) {
                    setX(getX()+1);
                } else {
                    setX(getX()-1);
                }
            }
        }
    }
}
