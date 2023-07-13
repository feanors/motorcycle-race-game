package GUI;


import Data.UserDatabase;
import GameObjects.Enemies;
import GameObjects.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;

public class GamePanel extends JPanel implements Runnable {

    private Player player;
    private Enemies enemies;

    private int curvature = 0;
    private int animationCounterForRoadSides;

    private long points = 0;
    private String username;
    private UserDatabase database;

    private int straightRoadTimer = 0;
    private int deepestTurnCounter = 0;
    private int engineSoundCycle = 10;

    private boolean clearLeftTurn = false;
    private boolean turnLeftFlag = false;

    private boolean clearRightTurn = false;
    private boolean turnRightFlag = false;

    private boolean gamePaused = false;
    private boolean gamePlaying = true;

    private int timeMultipliedBySixty = 3600;
    private int enemyBlockingDegree = 0;

    private JLabel mainIMG;
    private JButton continueButton;
    private JLabel lostLabel;

    GamePanel(String username, UserDatabase database, JLabel mainIMG) {
        this.username = username;
        this.database = database;

        this.mainIMG = mainIMG;
        continueButton = new JButton("Continue");

        player = new Player();
        enemies = new Enemies(player);
    }

    public void run() {

        ///////////////////////////////////// 3-2-1 COUNTER ///////////////////////////////////////

        repaint();
        JLabel countdownLabel = new JLabel();

        add(countdownLabel);
        countdownLabel.setBounds(544, 1120, 200, 200);
        countdownLabel.setOpaque(false);

        countdownLabel.setIcon(new ImageIcon(getClass().getResource("Img/3.png")));
        sleepFor(1000);
        countdownLabel.setIcon(new ImageIcon(getClass().getResource("Img/2.png")));
        sleepFor(1000);
        countdownLabel.setIcon(new ImageIcon(getClass().getResource("Img/1.png")));
        sleepFor(1000);
        countdownLabel.setIcon(new ImageIcon(getClass().getResource("Img/go.png")));
        sleepFor(1000);

        remove(countdownLabel);

        ///////////////////////////////////// COUNTDOWN COMPLETE ///////////////////////////////////

        while (gamePlaying) {

            if(!gamePaused) {

                player.update(); /// boolean is for testing whether the car crashed or not
                enemies.update(curvature);
                repaint();

                if(!player.didCrash()) {
                    engineSoundPlayer();
                    pointCounter();
                    timeCount();
                    adjustRoad();
                }

                if(player.didCrash()) {
                    if (player.getCrashCounter() > 35) {
                        stopGame();
                    }
                }

                if (enemies.collidesWithPlayer() || player.didCollideWithSides()) {
                    player.crashFlag(true);
                }
            }

            sleepFor(16); // 60fps approx
        }

        database.submitGameResult(username,points);
    }

    private void stopGame() {
        gamePlaying = false;
        displayDefeatPanel();

    }

    private void displayDefeatPanel() {
        lostLabel = new JLabel();
        add(lostLabel);
        lostLabel.setIcon(new ImageIcon(getClass().getResource("Img/lost.png")));
        lostLabel.setBounds(560,100,200,100);
        lostLabel.setOpaque(false);

        continueButton = new JButton("Continue");
        add(continueButton);
        continueButton.setMaximumSize(new Dimension(100,50));
        continueButton.setPreferredSize(new Dimension(100,50));
        continueButton.setMinimumSize(new Dimension(100,50));
        continueButton.setVisible(true);

        continueButton.setOpaque(false);
        continueButton.setBounds(800,20,100,50);

        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(lostLabel);
                remove(continueButton);

                setVisible(false);
                mainIMG.setVisible(true);

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawRoad(g);
        drawUpperBackground(g);
        enemies.draw(g);
        player.draw(g);
        drawLabels(g);
    }

    private void engineSoundPlayer() {
        if (engineSoundCycle == 0) {
            player.sound();
            engineSoundCycle = 120;
        } else {
            engineSoundCycle--;
        }
    }

    private void timeCount() {
        timeMultipliedBySixty--;
        if(timeMultipliedBySixty == 0) {
            stopGame();
        }
    }

    synchronized void pauseGame() {
        gamePaused = true;
    }
    synchronized void continueGame() {
        gamePaused = false;
    }

    private void pointCounter() {
        points +=  ( player.getSpeed() / 10 );
    }

    Player getPlayer() {
        return player;
    }

    private void sleepFor(long x) {
        try {
            Thread.currentThread().sleep(x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void drawLabels(Graphics g) {
        g.setFont(new Font(Font.SERIF,1,40));


        g.drawImage(new ImageIcon(getClass().getResource("Img/scorev1.png")).getImage(), 350,20,null);
        g.setColor(Color.BLUE);
        g.drawString(Long.toString(points),350,100);

        g.drawImage(new ImageIcon(getClass().getResource("Img/timev1.png")).getImage(), 700,20,null);
        g.setColor(Color.yellow);
        g.drawString(Integer.toString(timeMultipliedBySixty / 60),700,100);

        g.setColor(Color.WHITE);
        g.drawImage(new ImageIcon(getClass().getResource("Img/speedv1.png")).getImage(), 1050,20,null);
        g.drawString(Integer.toString(player.getSpeed()) + " KM/H",1050,100);


        g.setColor(Color.black);
        g.drawRoundRect(9,19,154,42,50,50);
        g.setColor(Color.WHITE);
        g.fillRoundRect(10,20,150,40,50,50);

        g.setFont(new Font(Font.SANS_SERIF,1,24));
        g.setColor(Color.RED);
        g.drawString("DIFFICULTY",15,48);
        g.setFont(new Font(Font.SANS_SERIF,1,40));
        g.setColor(Color.PINK);
        g.drawString(Integer.toString(enemyBlockingDegree),9,100);

    }

    private void drawRoad(Graphics g) {

        side(g);
        Graphics2D g2 = (Graphics2D) g;

        drawDrivableArea(g2);
        fillSides(g2);
    }

    private void side(Graphics g) {
        animationCounterForRoadSides =  (animationCounterForRoadSides % 150) + ( player.getSpeed() / 10 ) + (player.getSpeed() == 0 ? 0 : 1) ;

        g.setColor(Color.WHITE);
        g.fillRect(0,360,1280,600);

        g.setColor(new Color(150,50,50));
        g.fillRect(0,210+ animationCounterForRoadSides,1280,50+ animationCounterForRoadSides);
        g.fillRect(0,360+(animationCounterForRoadSides *4),1280,200+(animationCounterForRoadSides *4));

    }

    private void drawDrivableArea(Graphics2D g2) {
        GeneralPath closedRoad = new GeneralPath();
        g2.setColor(Color.GRAY);
        closedRoad.moveTo(40,961);
        closedRoad.curveTo(40,961,330-curvature,660,620+curvature,360);
        closedRoad.lineTo(660+curvature,360);
        closedRoad.curveTo(660+curvature,360,950-curvature,660,1240,961);
        closedRoad.lineTo(40,961);
        g2.fill(closedRoad);
    }


    private void fillSides(Graphics2D g2) {
        GeneralPath closedSidesL = new GeneralPath();
        g2.setColor(new Color(100,150,100));

        closedSidesL.moveTo(0,960);
        closedSidesL.curveTo(0,960,305-curvature,660,610+curvature,360);
        closedSidesL.lineTo(0,360);
        closedSidesL.closePath();
        g2.fill(closedSidesL);

        GeneralPath closedSidesR = new GeneralPath();


        closedSidesR.moveTo(1280,960);
        closedSidesR.curveTo(1280,960,975-curvature,660+(curvature < 0 ? 0 : curvature /30),670+curvature,360);
        closedSidesR.lineTo(1280,360);
        closedSidesR.closePath();
        g2.fill(closedSidesR);


    }

    private void drawUpperBackground(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(-2000,0,10000,360);

        g.setColor(Color.YELLOW);
        g.fillArc(100-(player.getX()-520),0,200,200,0,360);

        g.drawImage(new ImageIcon(getClass().getResource("Img/bulut.png")).getImage(), 300-(player.getX()-520),250,null);
        g.drawImage(new ImageIcon(getClass().getResource("Img/bulut.png")).getImage(), 700-(player.getX()-520),100,null);
        g.drawImage(new ImageIcon(getClass().getResource("Img/bulut.png")).getImage(), 1000-(player.getX()-520),260,null);
        g.drawImage(new ImageIcon(getClass().getResource("Img/bulut.png")).getImage(), -1000-(player.getX()-520),250,null);
        g.drawImage(new ImageIcon(getClass().getResource("Img/bulut.png")).getImage(), 800-(player.getX()-520),270,null);
        g.drawImage(new ImageIcon(getClass().getResource("Img/bulut.png")).getImage(), 1700-(player.getX()-520),60,null);
        g.drawImage(new ImageIcon(getClass().getResource("Img/bulut.png")).getImage(), 1400-(player.getX()-520),270,null);
        g.drawImage(new ImageIcon(getClass().getResource("Img/bulut.png")).getImage(), -200-(player.getX()-520),250,null);
        g.drawImage(new ImageIcon(getClass().getResource("Img/bulut.png")).getImage(), -400-(player.getX()-520),50,null);
    }


    private void adjustRoad() {

        if (straightRoadTimer < 200000) {

            straightRoadTimer+=player.getSpeed();

        } else if (straightRoadTimer > 200000) {

            straightRoadTimer=200000;
            if (!turnRightFlag)
                turnLeftFlag = true;

        } else if (turnLeftFlag) {

            enemies.turnLeftFlag(true);

            if(curvature > -600 && !clearLeftTurn){

                if(player.getSpeed() != 0)
                    curvature-=5;
                player.setX(player.getX()+ (player.getSpeed()/40) );

            }else if(curvature == -600  && deepestTurnCounter < 75000) {

                clearLeftTurn=true;
                deepestTurnCounter+=player.getSpeed();
                player.setX(player.getX()+ (player.getSpeed()/22) );

            } else if (deepestTurnCounter >= 75000) {

                if(player.getSpeed() != 0)
                    curvature+=5;

                player.setX(player.getX()+ (player.getSpeed()/40) );

                if (curvature == 0) {

                    enemies.turnLeftFlag(false);
                    turnLeftFlag = false;
                    clearLeftTurn = false;
                    turnRightFlag = true;

                    deepestTurnCounter = 0;
                    straightRoadTimer = 0;
                }
            }
        } else if (turnRightFlag) {

            enemies.turnRightFlag(true);

            if (curvature < 600 && !clearRightTurn) {

                player.setX(player.getX() - (player.getSpeed()/40) );
                if(player.getSpeed() != 0)
                    curvature+=5;

            } else if (curvature >= 600 && deepestTurnCounter < 75000) {

                player.setX(player.getX() - (player.getSpeed()/22) );
                clearRightTurn = true;
                deepestTurnCounter+=player.getSpeed();

            } else if (deepestTurnCounter >= 75000) {

                if(player.getSpeed() != 0)
                    curvature-=5;
                player.setX(player.getX() - (player.getSpeed()/40) );
                if (curvature == 0) {

                    enemies.turnRightFlag(false);
                    turnLeftFlag = true;
                    clearRightTurn = false;
                    turnRightFlag = false;

                    deepestTurnCounter = 0;
                    straightRoadTimer = 0;
                    timeMultipliedBySixty += 3600;

                    enemies.incrementBlockingDegree();
                    enemyBlockingDegree++;

                }
            }
        }
    }
}
