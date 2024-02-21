
import java.awt.Rectangle;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


public class Enemy
{
    private int enemyX,enemyY;
    private Image image;
    private int hitCount = 0;
    private boolean exploding = false;
    private int HIT_THRESHOLD = 3;//Maximum hits an enemy can take until exploding
    private int totalExplosionFrames = 8;
    private int currentExplosionFrame = 0;
    private int SPEED = 3;
    private boolean marked = false;
    private List<Rectangle> enemyBullets;
    private int BULLET_SPEED = 5;

    public Enemy(int x, int y, Image image)
    {
        setImage(image);
        setX(x);
        setY(y);
        enemyBullets = new ArrayList<>();
    }

    public void move() 
    {
        enemyY += SPEED; 
    }

    public void shoot() 
    {
        Rectangle bullet = new Rectangle(enemyX, enemyY + image.getHeight(null), 10, 10);
        enemyBullets.add(bullet);
       
    }

    public void updateBullets() 
    {
        Iterator<Rectangle> iterator = enemyBullets.iterator();
        while (iterator.hasNext()) 
        {
            Rectangle bullet = iterator.next();
            bullet.y += BULLET_SPEED;
            if (bullet.y > 600)//600 is the height of the game panel 
            {
                iterator.remove();
            }
        }
    }

    public List<Rectangle> getBullets()
    {
        return enemyBullets;
    }

    //Updating explosion animation image
    public void update()
    {
        if(exploding)
        {
            currentExplosionFrame++;
            if(currentExplosionFrame >= totalExplosionFrames)
            {
                currentExplosionFrame = totalExplosionFrames;
            }
        }
        else
        {
           return;
        }
    }

    public void hit() 
    {
        hitCount++;
        if (hitCount >= HIT_THRESHOLD)
        { 
            exploding = true;
        }
    }

    public void crushedWithPlayer()
    {
        if(!marked)
        {
            marked = true;
            exploding = true;
        }
    }

    public boolean isMarked()
    {
        return marked;
    }

    //Informing that explosion animation is completed so that enemy can be removed
    public boolean isExplosionComplete() 
    {
        return currentExplosionFrame >= totalExplosionFrames;
    }

    public Rectangle getBounds() 
    {
        return new Rectangle(enemyX, enemyY, image.getWidth(null), image.getHeight(null));
    }

    public void setX(int x)
    {
        enemyX = x;
    }

    public int getX()
    {
        return enemyX;
    }

    public void setY(int y)
    {
        enemyY = y;
    }

    public int getY()
    {
        return enemyY;
    }

    public void setImage(Image image)
    {
        this.image = image;
    }

    public Image getImage()
    {
        return image;
    }

    public boolean isExploding() 
    {
        return exploding;
    }

    public int getExplosionFrame()
    {
        return currentExplosionFrame;
    }
}
