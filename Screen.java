import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.KeyListener;
import java.awt.event.*;

public class Screen extends JComponent
{
    private JFrame frame;

    private static float interpolation = 0;
    private static final Vector v1 = new Vector((int)(Global.WIDTH/2 - 17),300);//position of ball
    private static final Vector v2 = new Vector(250,505);//position of slime1
    private static final Vector v3 = new Vector(650,505);//position of slime2

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
        while(true)
        {
            updateVariables();
            repaint();            
            try{Thread.sleep(37);}catch(InterruptedException e){}   //40
            
            
            //We will need the last update time.
            double lastUpdateTime = System.nanoTime();
            //Store the last time we rendered.
            double lastRenderTime = System.nanoTime();

            //Simple way of finding FPS.
            int lastSecondTime = (int) (lastUpdateTime / 1000000000);

            double now = System.nanoTime();
            int updateCount = 0;

                //if (!paused)
                //{
                    //Do as many game updates as we need to, potentially playing catchup.
                    while( now - lastUpdateTime > Global.TIME_BETWEEN_UPDATES && updateCount < Global.MAX_UPDATES_BEFORE_RENDER )
                    {
                        updateVariables();
                        lastUpdateTime += Global.TIME_BETWEEN_UPDATES;
                        updateCount++;
                    }

                    //If for some reason an update takes forever, we don't want to do an insane number of catchups.
                    //If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
                    if ( now - lastUpdateTime > Global.TIME_BETWEEN_UPDATES)
                    {
                        lastUpdateTime = now - Global.TIME_BETWEEN_UPDATES;
                    }

                    //Render. To do so, we need to calculate interpolation for a smooth render.
                    float i = Math.min(1.0f, (float) ((now - lastUpdateTime) / Global.TIME_BETWEEN_UPDATES) );
                    interpolation = i;
                    repaint();
                    lastRenderTime = now;

                    

                    //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                    while ( now - lastRenderTime < Global.TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < Global.TIME_BETWEEN_UPDATES)
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

    /*public void checkCollide1(Slime s)
    {
        double bdx = b.v.x;
        double sdx = s.v.x;
        double bdy = b.v.y;
        double sdy = s.v.y;
        if(b.centerY()>s.centerY())//if ball is under slime
        {
            if(b.rightX()+bdx>s.leftX()+sdx  &&
                b.leftX()+bdx<=s.rightX()+sdx)//if they intersect 
            {
                b.setVelocity(b.v.addVectors(b.v,s.v));
                b.p.y = b.centerY();
                b.v.y*=-1;
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
                
                distance = (Math.abs(A*b.centerX() + (-1)*b.centerY() + C))/(Math.sqrt(A*A+1));
                
                if(distance<minimum)
                {
                    normal.x = (-1)*(1/A);
                    double magnitude = (Math.sqrt((normal.x*normal.x)+1));
                    normal.x = normal.x/magnitude;
                    normal.y = normal.y/magnitude;
                }
            }
        }*/
        /*else
        {
            final double r2 = (b.getRadius()*b.getRadius());
            double minimum = r2;
            
            int minIndex = 0;
            for(int i=0;i<s.getNodes().length;i++)
            {
                Vector cp = s.getNode(i).getPos();
                double curDistance = Math.pow((b.centerX()-cp.x+sdx+bdx),2)+Math.pow((b.centerY()-cp.y+sdy+bdy),2);//d^2
                if(curDistance<minimum)
                {
                    minimum = curDistance;
                    minIndex = i;
                }
            }
            
            double delta = Math.sqrt(minimum/r2);
            CollisionNode c = s.getNode(minIndex);
            if(delta<1)//collided since d2<r2
                {
                    b.setVelocity(Vector.reflect(b.v,c.getNormal()));
                    
                    double slimeSpeed = s.v.getMagnitude();
                    if(slimeSpeed>0)
                    {
                        Vector unitSlimeV = s.v.unitVector();
                        Vector normal = c.getNormal();
                        Vector dotProduct = unitSlimeV.scale(unitSlimeV.x*normal.x+ unitSlimeV.y*normal.y);
                        dotProduct = dotProduct.scale(slimeSpeed);
                        b.setVelocity(Vector.addVectors(b.v,dotProduct));
                    }
                    
                    
                    b.p.x = 250;//b.v.x*(b.getRadius()+b.a.x+b.v.x)*(1.0-delta)/(Math.sqrt((b.v.x*b.v.x)+(b.v.y*b.v.y)))+c.getPos().x;
                    b.p.y = 250;//b.v.y*(b.getRadius()+b.a.y+b.v.y)*(1.0-delta)/(Math.sqrt((b.v.x*b.v.x)+(b.v.y*b.v.y)))+c.getPos().y;
                    
                    
                }
        }
    }*/
    
    /*
     */
    public void checkCollide(Slime s)
    {
        double bdx = b.v.x;
        double bdy = b.v.y;
        double sdx = s.v.x;
        double sdy = s.v.y;
        
        //if ball is under the slime
        if (b.centerY()>s.centerY())
        {
            if(b.rightX()+bdx>s.leftX()+sdx  &&
                b.leftX()+bdx<=s.rightX()+sdx)//if ball hits bottom of slime
            {
                
            }
        }
        else //finds the closest collision node and collides with it if it intersects
        {
            final double R2 = Math.pow(b.getRadius(),2);
            double minimum = R2;
            int minIndex = 0;
            for(int i=0;i<Slime.NODE_NUM;i++)
            {
                Vector cp = s.getNode(i).getPos();
                Vector distanceAtCollision = new Vector(b.centerX()+bdx-(cp.x+sdx), b.centerY()+bdy-(cp.y+sdy));
                double d2 = distanceAtCollision.r2();
                //System.out.println(d2);
                if(d2<minimum)
                {
                    minimum = d2;
                    minIndex = i;
                }
            }
            
            CollisionNode c = s.getNode(minIndex);
            if(minimum<R2)//collided since d2<r2
            {
                b.setVelocity(Vector.reflect(b.v,c.getNormal()));
                
                double slimeSpeed = s.v.getMagnitude();
                if(slimeSpeed>0)
                {
                    
                }
                
                b.setPos(new Vector(250,250));
                //b.v.x*(b.getRadius()+b.a.x+b.v.x)*(1.0-delta)/(Math.sqrt((b.v.x*b.v.x)+(b.v.y*b.v.y)))+c.getPos().x;
                //b.v.y*(b.getRadius()+b.a.y+b.v.y)*(1.0-delta)/(Math.sqrt((b.v.x*b.v.x)+(b.v.y*b.v.y)))+c.getPos().y;
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