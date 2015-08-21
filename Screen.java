import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.KeyListener;
import java.awt.event.*;

public class Screen extends JComponent
{
    private JFrame frame;

    private static float interpolation = 0;
    private static Vector v1 = new Vector((int)(Global.WIDTH/2 - 17),300);//position of ball
    private static Vector v2 = new Vector(250,505);//position of slime1
    private static Vector v3 = new Vector(650,505);//position of slime2
    
  

    private Ball b; 
    private Slime s1;
    private Slime s2;
    

    private Background back;
    

    public Screen(JFrame f)
    {
        super();

        frame = f;
        frame.setSize(Global.WIDTH,Global.HEIGHT);
        back = new Background();

        b = new Ball(this,v1,13); 
        s1 = new Slime(this, v3,Slime.SLIME1,true);        
        s2 = new Slime(this, v2,Slime.SLIME2,false);

        frame.addKeyListener(new Listener(s1));        
        frame.addKeyListener(new Listener(s2));   
    }

    public void paintComponent(Graphics g)
    {
        back.draw(g);
        b.draw(g);
        s1.draw(g);
        s2.draw(g); 
    }

    public float getInterp()
    {
        return interpolation;
    }
    public void run()
    {
        Sound.playBackground();
        
        while(true)
        {
            updateVariables();
            repaint();            
            try{Thread.sleep(37);}catch(InterruptedException e){}   //40

            //This value would probably be stored elsewhere.
            final double GAME_HERTZ = 480;
            //Calculate how many ns each frame should take for our target game hertz.
            final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
            //At the very most we will update the game this many times before a new render.
            //If you're worried about visual hitches more than perfect timing, set this to 1.
            final int MAX_UPDATES_BEFORE_RENDER = 5;
            //We will need the last update time.
            double lastUpdateTime = System.nanoTime();
            //Store the last time we rendered.
            double lastRenderTime = System.nanoTime();

            //If we are able to get as high as this FPS, don't render again.
            final double TARGET_FPS = 60;
            final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

            //Simple way of finding FPS.
            int lastSecondTime = (int) (lastUpdateTime / 1000000000);

            
                double now = System.nanoTime();
                int updateCount = 0;

                //if (!paused)
                //{
                    //Do as many game updates as we need to, potentially playing catchup.
                    while( now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER )
                    {
                        updateVariables();
                        lastUpdateTime += TIME_BETWEEN_UPDATES;
                        updateCount++;
                    }

                    //If for some reason an update takes forever, we don't want to do an insane number of catchups.
                    //If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
                    if ( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
                    {
                        lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                    }

                    //Render. To do so, we need to calculate interpolation for a smooth render.
                    float i = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES) );
                    interpolation = i;
                    repaint();
                    lastRenderTime = now;

                    

                    //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                    while ( now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES)
                    {
                        Thread.yield();

                        //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
                        //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
                        //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
                        try {Thread.sleep(1);} catch(Exception e) {} 

                        now = System.nanoTime();
                    }
                //}
            
        }
    }

    public void checkCollide(Slime s)
    {
        
        
        if(b.getCenterY()>s.getCenterY())
        {
            if(b.getCenterX()+b.getRadius()+b.v.x+b.a.x>s.getNodes(0).getPos().x+s.v.x&&b.getCenterX()-b.getRadius()+b.v.x+b.a.x<=s.getNodes(s.nodeNumber-1).getPos().x+s.v.x)
            {
                b.setVelocity(b.v.scale(b.v.addVectors(b.v,s.v),1));
                
                b.p.y = b.getCenterY();
                b.v.y*=-1.0;
                
                Sound.pauseBackground();
            }
        }
        
        /*else
        {
            double minDistance = b.getRadius()*b.getRadius();
            double minimum = minDistance;
            Vector normal = new Vector(1,0);
            for(int i=1;i<s.getNodes().length;i++)
            {
                CollisionNode c1 = s.getNodes()[i-1];
                CollisionNode c2 = s.getNodes()[i];
                
                double C = ((((-1)*(c2.getPos().y-c1.getPos().y))/((-1)*(c2.getPos().x-c1.getPos().x)))*c1.getPos().x)+c1.getPos().y;
                double A = ((c2.getPos().y-c1.getPos().y)/(c2.getPos().x-c1.getPos()));
                
                distance = (Math.abs(A*b.getCenterX() + (-1)*b.getCenterY() + C))/(Math.sqrt(A*A+1));
                
                if(distance<minimum)
                {
                    normal.x = (-1)*(1/A);
                    double magnitude = (Math.sqrt((normal.x*normal.x)+1));
                    normal.x = normal.x/magnitude;
                    normal.y = normal.y/magnitude;
                }
            }
        }*/
        else
        {
            double minDistance = (b.getRadius()*b.getRadius());
            double minimum = minDistance;
            int minIndex = 0;
            for(int i=0;i<s.getNodes().length;i++)
            {
                CollisionNode c = s.getNodes()[i];
                double curDistance = Math.pow((b.getCenterX()-c.getPos().x+s.v.x+s.a.x+b.v.x+b.a.x),2)+Math.pow((b.getCenterY()-c.getPos().y+s.v.y+s.a.y+b.a.y+b.v.y),2);
                if(curDistance<minimum)
                {
                    minimum = curDistance;
                    minIndex = i;
                }
                
                
            }
            
            double delta = Math.sqrt(minimum/minDistance);
            CollisionNode c = s.getNodes(minIndex);
            if(delta<1.0)
                {
                    b.setVelocity(b.v.reflect(b.v,c.getNormal()));
                    if(s.v.getMagnitude()!=0)
                    {
                        Vector unitSlimeVelocity = Vector.scale(s.v,(1.0/s.v.getMagnitude()));
                        Vector dotProduct = Vector.scale(unitSlimeVelocity,(unitSlimeVelocity.x*c.getNormal().x+unitSlimeVelocity.y*c.getNormal().y));
                        dotProduct = Vector.scale(dotProduct,s.v.getMagnitude());
                        b.setVelocity(b.v.addVectors(b.v,dotProduct));
                    }
                    
                    
                    b.p.x = 250;//b.v.x*(b.getRadius()+b.a.x+b.v.x)*(1.0-delta)/(Math.sqrt((b.v.x*b.v.x)+(b.v.y*b.v.y)))+c.getPos().x;
                    b.p.y = 250;//b.v.y*(b.getRadius()+b.a.y+b.v.y)*(1.0-delta)/(Math.sqrt((b.v.x*b.v.x)+(b.v.y*b.v.y)))+c.getPos().y;
                    
                    Sound.soundEffects();
                }
        }
    }

    public void updateVariables()
    {
        
        b.updateVariables();
        s1.updateVariables();
        s2.updateVariables();
        
        checkCollide(s1);
        checkCollide(s2);
    }

    public Slime getSlime1()
    {
        return s1;
    }

    public Slime getSlime2()
    {
        return s2;
    }
}