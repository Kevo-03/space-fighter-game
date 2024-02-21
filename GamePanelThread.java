import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.util.Random;





public class GamePanelThread extends JPanel implements KeyListener, ActionListener,Runnable
{
    //Providing the connection between classes in order to inform that the game is over
    public interface GameOverInformer
    {
        void onGameOver();
    }
    
    private GameOverInformer gameOverInformer;
    private int score = 0;
    private int SCORE_PER_ENEMY = 10;

    private int initialPlayerHealth = 100;
    private int playerHealth = initialPlayerHealth;
    private int characterX = 350;  
    private int characterY = 430;  
    private int PLAYER_SPEED = 10;
    private double HEALTH_BAR_RATIO = 100.0;
    

    private List<Rectangle> bulletsR;  
    private List<Rectangle> bulletsL;
    private int BULLET_SPEED = 5;
    private List<Enemy> enemyJets;
    private int ENEMY_PASS_DMG = 5;
    private int ENEMY_CRASH_DAMAGE = 10;
    private int ENEMY_BULLET_DAMAGE = 5;

    private int SPAWN_DELAY = 1200;
    private int ENEMY_SHOOT_DELAY = 1000;

    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    private Image jetImage = new ImageIcon(getClass().getResource("falcon2.png")).getImage();
    private Image bulletImage = new ImageIcon(getClass().getResource("laser1.png")).getImage();
    private Image enemyJetImage = new ImageIcon(getClass().getResource("2yenijet.png")).getImage();
    private Image background = new ImageIcon(getClass().getResource("bg.png")).getImage();
    private Image enemyBulletImage = new ImageIcon(getClass().getResource("laser2.png")).getImage();
    private Image[] explosionAnimation = new Image[9];
    private int backgroundY1 = 0;
    private int backgroundY2 = -background.getHeight(null);

    private boolean running = false;
    private Thread gameThread;

    private Timer spawnTimer;
    private Timer enemyShootTimer;

    public GamePanelThread() 
    {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        

        bulletsR = new ArrayList<>();
        bulletsL = new ArrayList<>();
        enemyJets = new ArrayList<>();

        for(int i = 1; i <= 8; i++)
        {
            explosionAnimation[i] = new ImageIcon(getClass().getResource("explosion" + i + ".png")).getImage();
        }

        spawnTimer = new Timer(SPAWN_DELAY, new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                if (enemyJets.size() < 10000) 
                { 
                    spawnEnemyJet();
                }
            }
        });
    }

    //Having a seperate function to start the timer to avoid spawning enemies before selecting play game
    public void startTimer()
    {
        spawnTimer.start();
    }

    public void stopTimer()
    {
        spawnTimer.stop();
    }

    private void spawnEnemyJet() 
    {
        Random rand = new Random();
        int panelWidth = getWidth(); 
        int x = rand.nextInt(panelWidth - enemyJetImage.getWidth(null));
        int y = -enemyJetImage.getHeight(null); 
       
        Enemy enemy = new Enemy(x, y, enemyJetImage);
        enemyJets.add(enemy);
        enemyShootTimer = new Timer(ENEMY_SHOOT_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(enemy.getBullets().size() < 10000)
                enemy.shoot();
            } 
        });
        enemyShootTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        g.drawImage(background, 0, backgroundY1, this.getWidth(), background.getHeight(null), null);
        g.drawImage(background, 0, backgroundY2, this.getWidth(), background.getHeight(null), null);
        g.drawImage(jetImage, characterX, characterY, null);

        for (Enemy jet : enemyJets) 
        {
            if(jet.isExploding())
            {
                //Drawing explosion animation
                int frameIndex = jet.getExplosionFrame();
                g.drawImage(explosionAnimation[frameIndex], jet.getX(), jet.getY(), null);
            }
            else
            {
                g.drawImage(jet.getImage(), jet.getX(), jet.getY(), this);
            }
        }
    
        for (Rectangle bullet : bulletsR) 
        {
            g.drawImage(bulletImage, bullet.x, bullet.y, null);
        }

        for (Rectangle bullet : bulletsL) 
        {
            g.drawImage(bulletImage, bullet.x, bullet.y, null);
        }

        for (Enemy enemy : enemyJets) 
        {
            for (Rectangle bullet : enemy.getBullets()) 
            {
                g.drawImage(enemyBulletImage,bullet.x, bullet.y, null);
            }
        }

        drawHealthBar(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
       
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) 
        {
            moveUp = true;
        } 
        else if (key == KeyEvent.VK_DOWN) 
        {
            moveDown = true;
        } 
        else if (key == KeyEvent.VK_LEFT) 
        {
            moveLeft = true;
        } 
        else if (key == KeyEvent.VK_RIGHT) 
        {
            moveRight = true;
        } 
        else if (key == KeyEvent.VK_A) 
        {
            if(bulletsL.size() < 10000)
            {
                Rectangle bulletl = new Rectangle(characterX - 10, characterY, 10, 10);
                bulletsL.add(bulletl);
            }
        }
        else if (key == KeyEvent.VK_D)
        {
            if(bulletsR.size() < 10000)
            {
                Rectangle bulletr = new Rectangle(characterX + 15, characterY, 10, 10);
                bulletsR.add(bulletr);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) 
    {
        
    }

    @Override
    public void keyReleased(KeyEvent e) 
    {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) 
        {
            moveUp = false;
        } 
        if (key == KeyEvent.VK_DOWN) 
        {
            moveDown = false;
        } 
        if (key == KeyEvent.VK_LEFT) 
        {
            moveLeft = false;
        } 
        if (key == KeyEvent.VK_RIGHT) 
        {
            moveRight = false;
        } 
    }

    public void shootBullet()
    {
        
            Iterator<Rectangle> iteratorR = bulletsR.iterator();
            while (iteratorR.hasNext()) 
            {
                Rectangle bullet = iteratorR.next();
                bullet.y -= BULLET_SPEED;  

                if (bullet.y + bullet.height < 0) 
                {
                    iteratorR.remove();
                }
            }

            Iterator<Rectangle> iteratorL = bulletsL.iterator();
            while (iteratorL.hasNext()) 
            {
                Rectangle bullet = iteratorL.next();
                bullet.y -= BULLET_SPEED;  

                if (bullet.y + bullet.height < 0) 
                {
                    iteratorL.remove();
                }
            }
        
    }

    public void moveEnemy()
    {
        Iterator<Enemy> it = enemyJets.iterator();
        while (it.hasNext()) 
        {
            Enemy jet = it.next();
            jet.move();
    
            
            if (jet.getY() > getHeight()) 
            {
                it.remove();
                playerHealth -= ENEMY_PASS_DMG;
                if(playerHealth <= 0)
                {
                    //gameOver();
                    return;
                }
            }
        }
    }

    public void moveJet()
    {
        if(moveUp)
        {
            characterY = Math.max(characterY - PLAYER_SPEED, 0);
        }
        if(moveDown)
        {
            characterY = Math.min(characterY + PLAYER_SPEED, getHeight() - (jetImage.getHeight(null) + 10));
        }
        if(moveLeft)
        {
            characterX = Math.max(characterX - PLAYER_SPEED, 0);
        }
        if(moveRight)
        {
            characterX = Math.min(characterX + PLAYER_SPEED, getWidth() - jetImage.getWidth(null));
        }
    }

    //Dynamically drawing background so that it feels like jets are really moving
    public void updateBg()
    {
        //Using two different Y coordinates for background and drawing two different backgrounds with them
        backgroundY1 += 1;
        backgroundY2 += 1;

        //When one background gets out of panel bounds move that one to the back of the other one to create a dynamic background
        if (backgroundY1 > getHeight()) 
        {
            backgroundY1 = backgroundY2 - background.getHeight(null);
        }
        if (backgroundY2 > getHeight()) 
        {
            backgroundY2 = backgroundY1 - background.getHeight(null);
        }
    }

    public void detectCollision()
    {
        if(bulletsR.size() != 0)
        {
            Iterator<Rectangle> bulletIteratorR = bulletsR.iterator();
            while(bulletIteratorR.hasNext())
            {
                Rectangle bulletr = bulletIteratorR.next();
                for (Enemy jet : enemyJets) 
                {
                    if (jet.getBounds().intersects(bulletr)) 
                    {
                        jet.hit(); 
                        bulletIteratorR.remove(); 
                        break; 
                    }
                }
            }
        }
        
        if(bulletsL.size() != 0)
        {
            Iterator<Rectangle> bulletIteratorL = bulletsL.iterator();
            while(bulletIteratorL.hasNext())
            {
                Rectangle bulletl = bulletIteratorL.next();
                for (Enemy jet : enemyJets) 
                {
                    if (jet.getBounds().intersects(bulletl)) 
                    {
                        jet.hit(); 
                        bulletIteratorL.remove(); 
                        break; 
                    }
                }
            }
        }
    }

    public void checkPlayerCollision()
    {
        Rectangle playerBounds = new Rectangle(characterX, characterY, jetImage.getWidth(null), jetImage.getHeight(null));
        for(Enemy enemy : enemyJets)
        {
            if(playerBounds.intersects(enemy.getBounds()) && !enemy.isMarked())
            {
                playerHealth -= ENEMY_CRASH_DAMAGE;
                enemy.crushedWithPlayer();
                if(playerHealth <= 0)
                {
                    gameOver();
                    //return;
                }
            }
        }
    }

    public void detectEnemyBulletCollision(Enemy enemy)
    {
        if(enemy.getBullets().size() != 0)
        {
            Rectangle playerBounds = new Rectangle(characterX, characterY, jetImage.getWidth(null), jetImage.getHeight(null));
            Iterator<Rectangle> bulletIterator = enemy.getBullets().iterator();
            while(bulletIterator.hasNext()) 
            {
                Rectangle bullet = bulletIterator.next();
                if(playerBounds.intersects(bullet)) 
                {
                    playerHealth -= ENEMY_BULLET_DAMAGE; 
                    bulletIterator.remove();
                    if(playerHealth <= 0)
                    {
                        gameOver();
                        //return;
                    }
                }
            }
        }
    }

    public void removeJet()
    {
        Iterator<Enemy> jetIterator = enemyJets.iterator();
        while (jetIterator.hasNext()) 
        {
            Enemy jet = jetIterator.next();
            if (jet.isExplosionComplete()) 
            {
                jetIterator.remove();
                score += SCORE_PER_ENEMY;
            }
        }
    }

    private void drawHealthBar(Graphics g)
    {
        int barWidth = 60;
        int barHeight = 3;
        //Moving the health bar alongside player
        int x = characterX ;
        int y = characterY + jetImage.getHeight(null) + 5;

        g.setColor(Color.gray);
        g.fillRect(x, y, barWidth, barHeight);

        //Changing the color of the health bar in different health intervals to provide a better feedback to player
        if((initialPlayerHealth >= playerHealth) && (((initialPlayerHealth*70)/100) < playerHealth))
        {
            g.setColor(Color.green);
        }
        else if((((initialPlayerHealth*70)/100) >= playerHealth) && (((initialPlayerHealth*40)/100) < playerHealth))
        {
            g.setColor(Color.ORANGE);
        }
        else 
        {
            g.setColor(Color.RED);
        }
        int healthBarFill = (int) ((playerHealth / HEALTH_BAR_RATIO) * barWidth);
        g.fillRect(x, y, healthBarFill, barHeight);

        g.setColor(Color.black);
        g.drawRect(x, y, barWidth, barHeight);
    }

    public void setGameOverInformer(GameOverInformer informer) 
    {
        this.gameOverInformer = informer;
    }

    public int getScore()
    {
        return score;
    }

    //Changing damage values,timer delays and player health depending on the selected difficulty 
    public void setDifficulty(int difficulty)
    {
        if(difficulty == 1)//Easy
        {
            SCORE_PER_ENEMY /= 2;
            initialPlayerHealth *= 2;
            playerHealth = initialPlayerHealth;
            HEALTH_BAR_RATIO *= 2;
            ENEMY_PASS_DMG -= 2;
            ENEMY_CRASH_DAMAGE /= 2;
            ENEMY_BULLET_DAMAGE -= 2;
            SPAWN_DELAY *= 4;
        }
        else if(difficulty == 2)//Normal(Default difficulty so no changes)
        {
            return;
        }
        else if(difficulty == 3)//Hard
        {
            SCORE_PER_ENEMY *= 2;
            ENEMY_PASS_DMG *= 2;
            ENEMY_CRASH_DAMAGE *= 2;
            ENEMY_BULLET_DAMAGE *= 2;
        }
        else//Torment
        {
            SCORE_PER_ENEMY *= 2;
            initialPlayerHealth /= 2;
            playerHealth = initialPlayerHealth;
            HEALTH_BAR_RATIO /= 2;
            ENEMY_PASS_DMG *= 2;
            ENEMY_CRASH_DAMAGE *= 2;
            ENEMY_BULLET_DAMAGE *= 2;
            SPAWN_DELAY /= 2;
            ENEMY_SHOOT_DELAY /= 2;
        }
    }

    //Changing images depending on the selected side
    public void rebelOrEmpire(int side)
    {
        if(side == 1)//Empire(since empire is default no changes is made with images)
        {
           return;
        }
        else //Rebels(changing images accordingly to the rebels)
        {
            jetImage = new ImageIcon(getClass().getResource("3yenijet.png")).getImage();
            bulletImage = new ImageIcon(getClass().getResource("laser4.png")).getImage();
            enemyJetImage = new ImageIcon(getClass().getResource("falcon3.png")).getImage();
            enemyBulletImage = new ImageIcon(getClass().getResource("laser3.png")).getImage();
        }
    }

    private void gameOver()
    {
        stopTimer();
        JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.ERROR_MESSAGE);
        String message = "Your Score : " + getScore();
        JOptionPane.showMessageDialog(this, message);
        if (gameOverInformer != null) 
        {
            //Reseting the game and game frame to the initial states
            gameOverInformer.onGameOver();
        }
        stopGame();
    }

    public void startGame() 
    {
        if (gameThread == null || !running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void stopGame() {
        running = false;
        try 
        {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Updating the panel
    public void update()
    {
        updateBg();
        moveJet();
        shootBullet();
        moveEnemy();
        detectCollision();
        checkPlayerCollision();
        for (Enemy jet : enemyJets) 
        {
            jet.update();
        }
        removeJet();
        for (Enemy enemy : enemyJets) 
        {
            enemy.updateBullets(); 
            detectEnemyBulletCollision(enemy); 
        }
        repaint();
    }

    @Override
    public void run()
    {
        
        
        while(running)
        {
            
            try 
            {
                update();
                
                Thread.sleep(10);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
                stopTimer();
                JOptionPane.showMessageDialog(this, "Thread throwed ConcurrentModificationException", "Interrupted", JOptionPane.ERROR_MESSAGE);
                String message = "Your Score : " + getScore();
                JOptionPane.showMessageDialog(this, message);
                if (gameOverInformer != null) 
                {
                    gameOverInformer.onGameOver();
                }
                stopGame();
            }
        }
        
    }
        
}


