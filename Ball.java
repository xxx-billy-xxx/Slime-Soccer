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
        g.drawImage(image,(int)p.x, (int)p.y, (int)r*2, (int)r*2, null);
        //g.setColor(Color.WHITE);
        //g.fillOval((int)p.x, (int)p.y, (int)r*2,(int)r*2);
    }
    
    public double rightX()
    {
        return centerX()+r;
    }
    
    public double leftX()
    {
        return centerX()-r;
    }
    
    public double topY()
    {
        return centerY()-r;
    }
    
    public double bottomY()
    {
        return centerY()+r;
    }
    
    public double centerX()
    {
        return p.x+r;
    }
    
    public double centerY()
    {
        return p.y+r;
    }
    
    public Vector center()
    {
        return new Vector(centerX(),centerY());
    }
    
    public double getRadius()
    {
        return r;
    }
    
    public double r2()
    {
        return r*r;
    }

    public void updateVariables()
    {
        boundVelocity();
        double ipx = p.x;
        double ipy = p.y;
            
        a.y = Global.YDECELERATION_BALL;
        //STEP 1
        //UPDATES POSITION AND VELOCITY
        double futureX = p.x+v.x;
        if((futureX > Global.LEFT_BOUND+r) && (futureX < Global.RIGHT_BOUND+6*r)) //Establishes Left and Right bounds
            p = Vector.addVectors(p,v);
        else //Stops you from moving past the bounds
        {
            p.y+=v.y;
            v.x*=-1.0;
        }
        

        //STEP 2
        //BOUNDS Y POSITION
        if(p.y>Global.FLOOR) //Creates a floor
        {
            p.y = Global.FLOOR;
            v.y*=-.85;
        }
        else if(p.y<0)//creates ceiling
        {
            p.y=0;
            v.y*=-.85;
        }

        //STEP 3 X ACCELERATION
        double vxsign = (v.x!=0) ? v.x/Math.abs(v.x) : 0;
        /*double avx = Math.abs(v.x);
        if(avx>0)
        {
            if(avx>1)a.x=-.05;
            else if(avx>.5)a.x=-.025;
            else if(avx>.1)a.x=-.001;
            else if(avx>.01)a.x=-.0001;
            else if(avx>.0001)a.x=-.0001;
        }*/
        a.x=-vxsign*Global.XDECELERATION_BALL;
        
        //v.x+=a.x;
        v.y+=a.y;
        if (Math.abs(v.x)>=Math.abs(a.x))
            v.x+=a.x;
        else v.x=0;
        
        
       
        //STEP 4 CHECK BOUNDS
        boundVelocity();
    }
    
    private void boundVelocity()
    {
        double vxsign = v.x==0 ? 0 : v.x/Math.abs(v.x);
        double vysign = v.y==0 ? 0 : v.y/Math.abs(v.y);
        
        if(Math.abs(v.x)>Global.MAX_X_VELOCITY_BALL)
            v.x=vxsign*Global.MAX_X_VELOCITY_BALL;
        if(Math.abs(v.y)>Global.MAX_Y_VELOCITY_BALL)
            v.y=vysign*Global.MAX_Y_VELOCITY_BALL;
    }
}