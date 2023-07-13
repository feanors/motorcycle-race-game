package GUI;

import Data.UserDatabase;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class GameFrame extends JFrame {

    private UserDatabase database;
    private String currentUser;

    private GamePanel gamePanel;
    private Thread gameThread;

    private boolean gameIsRunning = false;
    private boolean gamePaused = false;

    private JLabel mainImgLabel;

    GameFrame() {
        super("Super-HangON");
        playThemeSong();
        setJMenuBar(createMenuBar());
        setupFramesBasics();
        drawMainImage();
        database = new UserDatabase();

    }

    private void setupFramesBasics() {
        setSize(1280,1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);
        setFocusable(true);
        setResizable(false);
    }

    private void setupGamePanel() {
        addKeyListenerForGamePanel();
        gameIsRunning = true;
        mainImgLabel.setVisible(false);
        gamePanel = new GamePanel(currentUser,database,mainImgLabel);
        gameThread = new Thread(gamePanel);
        gamePanel.setOpaque(false);
        gamePanel.setBounds(0,0,getWidth(),getHeight());
        gamePanel.setVisible(true);
        add(gamePanel);
        gameThread.start();

    }

    private void restartGame() {
        if(gameIsRunning) {
            remove(gamePanel);
            gamePanel = null;
            setupGamePanel();
        }
    }

    private void drawMainImage() {
        mainImgLabel = new JLabel();
        add(mainImgLabel);
        mainImgLabel.setIcon(new ImageIcon(getClass().getResource("Img/mainImg.png")));
        mainImgLabel.setBounds(0,0,1280,960);
        mainImgLabel.setOpaque(false);

    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createAndReturnGameMenu());
        menuBar.add(createAndReturnUserMenu());
        menuBar.add(createAndReturnQuitMenu());

        return menuBar;
    }

    private JMenu createAndReturnGameMenu() {
        JMenu gameMenu = new JMenu("Game");

        JMenuItem gameStartItem = new JMenuItem("Start");
        gameStartItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentUser != null) {
                    if (!gameIsRunning) {
                        setupGamePanel();
                    } else {
                        restartGame();
                    }
                }
                else
                    new LoginFrame(database, new NewUserable() {
                        @Override
                        public void newUser(String username) {
                            currentUser = username;
                            setupGamePanel();
                        }
                    });
            }
        });
        gameMenu.add(gameStartItem);

        JMenuItem gamePauseItem = new JMenuItem("Pause");
        gamePauseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gameIsRunning && !gamePaused) {
                    gamePaused = true;
                    gamePanel.pauseGame();
                }
            }
        });

        gameMenu.add(gamePauseItem);

        JMenuItem gameContinueItem = new JMenuItem("Continue");
        gameContinueItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gameIsRunning && gamePaused ) {
                    gamePanel.continueGame();
                    gamePaused = false;
                }
            }
        });
        gameMenu.add(gameContinueItem);

        JMenuItem gameRestartItem = new JMenuItem("Restart");
        gameRestartItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gameIsRunning) {
                    restartGame();
                }
            }
        });
        gameMenu.add(gameRestartItem);

        return gameMenu;
    }

    private JMenu createAndReturnUserMenu() {
        JMenu userMenu = new JMenu("User");

        JMenuItem userLoginItem = new JMenuItem("Login");
        userLoginItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame(database, new NewUserable() {
                    @Override
                    public void newUser(String username) {
                        currentUser = username;
                    }
                });
            }
        });
        userMenu.add(userLoginItem);

        JMenuItem userRankingsItem = new JMenuItem("Rankings");
        userRankingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame rankingsFrame = new JFrame("Rankings");
                rankingsFrame.setVisible(true);
                rankingsFrame.setSize(200,100);
                rankingsFrame.add(new JScrollPane(new JTextArea(database.getTopTenRankings())));
            }
        });
        userMenu.add(userRankingsItem);

        return userMenu;
    }

    private JMenu createAndReturnQuitMenu() {
        JMenu quitMenu = new JMenu("Quit");

        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                database.storeData();
                System.exit(1);
            }
        });
        quitMenu.add(quitItem);

        return quitMenu;
    }

    private void addKeyListenerForGamePanel() {
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {

                if(e.getKeyCode() == KeyEvent.VK_UP && gameIsRunning) {
                    gamePanel.getPlayer().accelerateFlag(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT && gameIsRunning) {
                    gamePanel.getPlayer().turnLeftFlag(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT && gameIsRunning) {
                    gamePanel.getPlayer().turnRightFlag(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE && gameIsRunning) {
                    gamePanel.getPlayer().slowDownBike();
                }
            }
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_UP && gameIsRunning) {
                    gamePanel.getPlayer().accelerateFlag(false);
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT && gameIsRunning) {
                    gamePanel.getPlayer().turnLeftFlag(false);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT && gameIsRunning) {
                    gamePanel.getPlayer().turnRightFlag(false);
                }
            }
        });
    }

    private void playThemeSong() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("ThemeSong/themeSong.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {

        new GameFrame();
    }
}
