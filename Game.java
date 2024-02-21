

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Game 
{
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run()
            {
                GameFrame gameFrame = new GameFrame();
       
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setSize(800,600);
                gameFrame.setLocationRelativeTo(null);
                gameFrame.setVisible(true);   
            }  
        });
    }    
}
