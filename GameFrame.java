import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;


public class GameFrame extends JFrame implements GamePanelThread.GameOverInformer
{
    private JMenuBar menu;
    private boolean loggedIn = false;
    private String activePlayer;
    private String diffficultyMenu = "SELECT GAME DIFFICULTY\n" +
                                     "1.EASY\n" +
                                     "2.NORMAL\n" +
                                     "3.HARD\n" +
                                     "4.TORMENT\n";
    private String pickSidesMenu = "SELECT YOUR SIDE\n" +
                                   "1.EMPIRE(DEFAULT)\n" +
                                   "2.REBELS\n";
   
    GamePanelThread game;

    public GameFrame()
    {
        super("CSE212 Jet Fighter");
        JMenu fileMenu = new JMenu("File");

        JMenuItem register = new JMenuItem("Register");
        fileMenu.add(register);

        JMenuItem login = new JMenuItem("Login");
        fileMenu.add(login);

        JMenuItem play = new JMenuItem("Play Game");
        fileMenu.add(play);

        JMenuItem score = new JMenuItem("Scoretable");
        fileMenu.add(score);

        JMenuItem exit = new JMenuItem("Exit");
        fileMenu.add(exit);

        JMenu helpMenu = new JMenu("Help");

        JMenuItem about = new JMenuItem("About");
        helpMenu.add(about);

        menu = new JMenuBar();
        menu.add(fileMenu);
        menu.add(helpMenu);
        setJMenuBar(menu);

        game = new GamePanelThread();
        game.setGameOverInformer(this);
        add(game,BorderLayout.CENTER);
            

        
        play.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event)
                {
                    if(loggedIn)
                    {
                        String getDiff = JOptionPane.showInputDialog(diffficultyMenu);
                        int diff = Integer.parseInt(getDiff);
                        game.setDifficulty(diff);
                        String pickSides = JOptionPane.showInputDialog(pickSidesMenu);
                        int side = Integer.parseInt(pickSides);
                        game.rebelOrEmpire(side);
                        game.requestFocusInWindow();
                        game.startGame();
                        game.startTimer();
                    }
                    else 
                    {
                        JOptionPane.showMessageDialog(null, "Please Login to Play!", "Not Logged In", JOptionPane.ERROR_MESSAGE);
                    }
                }  
            }
        );

        register.addActionListener(
            new ActionListener() 
            {
                public void actionPerformed(ActionEvent event)
                {
                    JFrame regFrame = new JFrame("Register");
                    JPanel regPanel = new JPanel();
                    regPanel.setPreferredSize(new Dimension(400,180));
                    regPanel.setLayout(null);
                    JTextField nameField = new JTextField(20);
                    JPasswordField passwordField = new JPasswordField(20);
                    JLabel nameLabel = new JLabel("Name");
                    JLabel passLabel = new JLabel("Password");
                    JButton regButton = new JButton("Register");
                    JButton helpButton = new JButton("Help");
                    regPanel.add(nameLabel);
                    regPanel.add(nameField);
                    regPanel.add(passLabel);
                    regPanel.add(passwordField);
                    regPanel.add(regButton);
                    regPanel.add(helpButton);
                    
                    nameLabel.setBounds(50, 20, 50, 30);
                    passLabel.setBounds(50,60,100,30);
                    nameField.setBounds(180, 20, 120, 30);
                    passwordField.setBounds(180, 60, 120, 30);
                    regButton.setBounds(100, 100, 100, 30);
                    helpButton.setBounds(200, 100, 100, 30);
                    
                    regButton.addActionListener(
                        new ActionListener() {
                            public void actionPerformed(ActionEvent event)
                            {
                                String name = nameField.getText();
                                char password[] = passwordField.getPassword();
                                String passwordStr = new String(password);
                                String info = name + "," + passwordStr + "\n";
                                try
                                {
                                    FileWriter writer = new FileWriter("UserInfos.txt",true);
                                    BufferedWriter bufferedWriter = new BufferedWriter(writer);
                                    bufferedWriter.write(info);
                                    bufferedWriter.newLine();
                                    JOptionPane.showMessageDialog(null,"Registered Successfully");
                                    bufferedWriter.close();
                                    regFrame.dispose();
                                }
                                catch(IOException e)
                                {
                                    e.printStackTrace();
                                }
                               
                            }
                        }
                    );
                    helpButton.addActionListener(
                        new ActionListener() {
                            public void actionPerformed(ActionEvent event)
                            {
                                JOptionPane.showMessageDialog(null,"Register with your name and password");
                            }   
                        }
                    );

                    regFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    regFrame.add(regPanel);
                    regFrame.setSize(400,200);
                    regFrame.setLocationRelativeTo(null);
                    regFrame.setVisible(true);
                    
                }
            }
        );

        login.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event)
                {
                    JFrame logFrame = new JFrame("Login");
                    JPanel logPanel = new JPanel();
                    logPanel.setPreferredSize(new Dimension(400,180));
                    logPanel.setLayout(null);
                    JTextField nameField = new JTextField(20);
                    JPasswordField passwordField = new JPasswordField(20);
                    JLabel nameLabel = new JLabel("Name");
                    JLabel passLabel = new JLabel("Password");
                    JButton logButton = new JButton("Login");
                    JButton helpButton = new JButton("Help");
                    logPanel.add(nameLabel);
                    logPanel.add(nameField);
                    logPanel.add(passLabel);
                    logPanel.add(passwordField);
                    logPanel.add(logButton);
                    logPanel.add(helpButton);
                    
                    nameLabel.setBounds(50, 20, 50, 30);
                    passLabel.setBounds(50,60,100,30);
                    nameField.setBounds(180, 20, 120, 30);
                    passwordField.setBounds(180, 60, 120, 30);
                    logButton.setBounds(100, 100, 100, 30);
                    helpButton.setBounds(200, 100, 100, 30);

                    logButton.addActionListener(
                        new ActionListener() {
                            public void actionPerformed(ActionEvent event)
                            {
                                String name = nameField.getText();
                                char password[] = passwordField.getPassword();
                                String passwordStr = new String(password);
                                try
                                {
                                    FileReader reader = new FileReader("UserInfos.txt");
                                    BufferedReader bufferedReader = new BufferedReader(reader);

                                    String line;
                                    while ((line = bufferedReader.readLine()) != null) 
                                    {
                                        String[] fields = line.split(",");
                                        if (fields.length == 2) {
                                            String nameCheck = fields[0];
                                            String passwordCheck = fields[1];
                                            if(name.equals(nameCheck) && passwordStr.equals(passwordCheck))
                                            {
                                                loggedIn = true;
                                                activePlayer = name;
                                                break;
                                            }
                                        }
                                    }

                                    bufferedReader.close();
                                    if(loggedIn)
                                    {
                                        JOptionPane.showMessageDialog(null,"Logged in Successfully");
                                    }
                                    else 
                                    {
                                        JOptionPane.showMessageDialog(null, "Account not Found,Please Register!", "Invalid Account", JOptionPane.ERROR_MESSAGE);
                                    }
                                    logFrame.dispose();
                                }
                                catch(IOException e)
                                {
                                    e.printStackTrace();
                                }
                            }   
                        }
                    );
                    helpButton.addActionListener(
                        new ActionListener() {
                            public void actionPerformed(ActionEvent event)
                            {
                                JOptionPane.showMessageDialog(null,"Login with your name and password");
                            }   
                        }
                    );

                    logFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    logFrame.add(logPanel);
                    logFrame.setSize(400,200);
                    logFrame.setLocationRelativeTo(null);
                    logFrame.setVisible(true);
                }   
            }
        );

        about.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event)
                {
                    JOptionPane.showMessageDialog(null, "Kıvanç Onat Türker\n20210702053");
                }   
            }
        );

        exit.addActionListener(
            new ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    System.exit(0);
                }
            }

        );

        score.addActionListener(
            new ActionListener() {
                String scores = "";
                public void actionPerformed(ActionEvent event)
                {
                    JFrame scoreFrame = new JFrame("Score Table");
                    JPanel scorePanel = new JPanel();
                    TextArea scoresArea = new TextArea(20,20);
                    scorePanel.setPreferredSize(new Dimension(250,450));
                    scorePanel.setLayout(null);
                    scorePanel.add(scoresArea);
                    scoresArea.setBounds(15, 8, 200, 400);

                    try 
                    {
                        FileReader reader = new FileReader("UserScores.txt");
                        BufferedReader bufferedReader = new BufferedReader(reader);

                        String line;
                        while ((line = bufferedReader.readLine()) != null) 
                        {
                            scores += line + "\n";
                        }
                        bufferedReader.close();
                        scoresArea.setText(scores);
                        scores = "";

                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                    scoreFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    scoreFrame.add(scorePanel);
                    scoreFrame.setSize(250,450);
                    scoreFrame.setLocationRelativeTo(null);
                    scoreFrame.setVisible(true);
                }   
            }
        );
    }

    //Interface function to reset the game and writing the score to scoretable when the game is over
    public void onGameOver() 
    {
        int score = game.getScore();
        resetGame();
        try
        {
            String userScore = activePlayer + "," + score;
            FileWriter writer = new FileWriter("UserScores.txt",true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(userScore);
            bufferedWriter.newLine();
            bufferedWriter.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        loggedIn = false; 
        activePlayer = null;
        
    }

    //Reseting the game
    private void resetGame() 
    {
        remove(game);
        game = new GamePanelThread();
        game.setGameOverInformer(this);
        add(game, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

}