import java.awt.event.*;

public class Listener implements KeyListener
{
    private final int left;
    private final int right;
    private final int up;
    private final int down;
 
    
    private final Slime slime;
    
    public Listener(Slime s)
    {
        slime = s;
        int slimeType = slime.getWhichSlime();
        if (slimeType == Slime.SLIME1)
        {
            left = KeyEvent.VK_LEFT;
            right = KeyEvent.VK_RIGHT;
            up = KeyEvent.VK_UP;
            down = KeyEvent.VK_DOWN;
        }
        else // (slimeType==Global.SLIME2)
        {
            left = KeyEvent.VK_A;
            right = KeyEvent.VK_D;
            up = KeyEvent.VK_W;
            down = KeyEvent.VK_S;
        }
    }
    
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        Vector a = slime.getAcceleration();
        Vector v = slime.getVelocity();
        if (key==left)
        {
            Thread t1 = new Thread()
            {
                public void run()
                {
                    slime.setLeft();
                }
            };
            t1.start();
        }
        else if (key==right)
        {
            Thread t1 = new Thread()
            {
                public void run()
                {
                    slime.setRight();
                }
            };
            t1.start();
        }        
        else if (key==up && slime.getPosition().y == Global.FLOOR)
        {
            Thread t1 = new Thread()
            {
                public void run()
                {
                    v.y = -Global.JUMP_SPEED;
                    a.y = 0;
                    if(slime.getDirection() == Slime.LEFT)
                        slime.setLeft();
                    else if(slime.getDirection() == Slime.RIGHT)
                        slime.setRight();
                    //a.y = Global.GRAVITY_GOING_DOWN;
                }
            };
            t1.start();
            
        }
        else if (key==down)
        {
            //Don't think anything should happen here ever
        }
      
    }
    
    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();
        Vector a = slime.getAcceleration();
        
        if (key==left)
        {
            Thread t1 = new Thread()
            {
                public void run()
                {
                    a.x = Global.XDECELERATION;
                    slime.setStill();
                    //a.y = Global.GRAVITY_GOING_DOWN;
                }
            };
            t1.start();
             //It's in here instead of out there so that you can tap the jump key, let go, and not start declerating
        }
        else if (key==right)
        {
            a.x = -Global.XDECELERATION;
            slime.setStill();
        }
        if (key==up)
        {
            //nothing needs to be here I think
        }
        else if (key==down)
        {
            //Don't think anything should happen here ever
        }
    }
    
    public void keyTyped(KeyEvent e){}
}