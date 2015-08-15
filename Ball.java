import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;  
import javax.imageio.ImageIO;

public class Ball extends Moveable
{    
    private double r;
    private Image image;
    
    public Ball(Screen s, Vector vector, double radius)
    {
        super(s, vector);
        r = radius;
        try 
        {                
           image = ImageIO.read(new File("Ball 5.png"));                    
        } 
        catch (IOException ex) {System.out.println("Background wasn't found");}
    }

    public void draw(Graphics g)
    {
        //g.setColor(Color.BLACK);
        //g.drawOval((int)p.x, (int)p.y, (int)r*2,(int)r*2);
        g.drawImage(image,(int)p.x, (int)p.y, null);
        //g.setColor(Color.WHITE);
        //g.fillOval((int)p.x, (int)p.y, (int)r*2,(int)r*2);
    }
    
    public double getCenterX()
    {
        return p.x+r;
    }
    
    public double getCenterY()
    {
        return p.y+r;
    }
    
    public double getRadius()
    {
        return r;
    }

    public void updateVariables()
    {
        //maybe add that the closer slime to the ball will get collision detection first
            
            a.y = .6;
            //STEP 1
            //UPDATES POSITION
            if(((p.x+v.x) > Global.LEFT_BOUND+r) && ((p.x+v.x) < Global.RIGHT_BOUND+6*r)) //Establishes Left and Right bounds
            {
                p = Vector.addVectors(p,v);
                v = Vector.addVectors(v,a);
            }
            else //Stops you from moving past the bounds
            {
                v.x*=-1;
                v = Vector.addVectors(v,a);
            }

            //STEP 2
            //UPDATES Y ACCELERATION
            if(p.y>Global.FLOOR) //Creates a floor so my slimes don't fall into the pits of hell
            {
                p.y = Global.FLOOR;
                v.y*=-.85;
            }
            else if(p.y<0)
            {
                p.y=0;
                v.y*=-.85;
                v.y+=a.y;
            }

            //if(v.y<.0001)v.y=0;

            //STEP 3 X ACCELERATION
            if(v.x>0)
            {
                if(v.x>1)a.x=-.05;
                else if(v.x>.5)a.x=-.025;
                else if(v.x>.1)a.x=-.001;
                else if(v.x>.01)a.x=-.0001;
                else if(v.x>.0001)a.x=-.0001;
            }
            else if(v.x<0)
            {
                if(v.x>-1)a.x=.05;
                else if(v.x>-.5)a.x=.025;
                else if(v.x>-.1)a.x=.001;
                else if(v.x>-.01)a.x=.0001;
                else if(v.x>-.0001)a.x=.0001;
            }
            v.x+=a.x;

            //STEP 4 CHECK BOUNDS
            if(v.x>Global.MAX_X_VELOCITY_BALL)v.x=Global.MAX_X_VELOCITY_BALL;
            else if(v.x<-1*Global.MAX_X_VELOCITY_BALL)v.x=-1*Global.MAX_X_VELOCITY_BALL;
            if(v.y>Global.MAX_Y_VELOCITY_BALL)v.y=Global.MAX_Y_VELOCITY_BALL;
            else if(v.y<-1*Global.MAX_Y_VELOCITY_BALL)v.y=-1*Global.MAX_Y_VELOCITY_BALL;
        
    }

    
}