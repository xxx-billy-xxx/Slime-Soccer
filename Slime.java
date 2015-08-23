import java.awt.*;

public class Slime extends Moveable
{    
    private int direction;
    private boolean isLeft;
    private int whichSlime; //final int whichSlime;
    
    private double ea;//half major axis 
    private double eb;//half minor axis
    
    private CollisionNode[] nodes;//used to determine collisions
    private Line[] sides;//also used to determine collisions
    
    public static final int NODE_NUM = 49;//number of nodes to be used, dont make it a multiple of 10
    
    private static final Vector DEFAULT_ELLIPSE_SIZE = new Vector(90,70);
    
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int STILL = 3;
    
    public static final int SLIME1 = 0;
    public static final int SLIME2 = 1;
    
    public Slime(Screen s, Vector pos, int slimeNum, boolean initiallyLeft)
    {
        super(s, pos);
        direction = STILL;
        whichSlime = slimeNum;
        isLeft = initiallyLeft;
        ea = DEFAULT_ELLIPSE_SIZE.x/2;
        eb = DEFAULT_ELLIPSE_SIZE.y/2;
        
        //intializing collision nodes
        nodes = new CollisionNode[NODE_NUM];
        for(int i=0;i<NODE_NUM;i++)
        {
            double angle = i*Math.PI/NODE_NUM;
            nodes[i] = new CollisionNode(new Vector(centerX()-Math.cos(angle)*ea,centerY()-(Math.sin(angle)*eb)),this);
        }
        
        sides = new Line[NODE_NUM-1];
        for (int i = 0; i < NODE_NUM-1; i++)
        {
            sides[i] = new Line(nodes[i],nodes[i+1]);
        }
    }
    
    public void draw(Graphics g)
    {
        g.setColor(Color.CYAN);
        g.fillArc((int)p.x, (int)p.y, (int)(2*ea), (int)(2*eb), 0, 180);      
        
        g.setColor(Color.BLACK);
        g.drawArc((int)p.x, (int)p.y, (int)(2*ea), (int)(2*eb), 0, 180);  
        g.drawLine((int)p.x, (int)p.y + (int)eb, (int)p.x + (int)(2*ea), (int)p.y + (int)eb);
        
        if(!isLeft)
        {
            //Eyes
            g.drawOval((int)p.x+60, (int)p.y+13, 14, 14);
            g.setColor(Color.WHITE);
            g.fillOval((int)p.x+60, (int)p.y+13, 14, 14);
            //Pupils
            g.setColor(Color.BLACK);
            g.fillOval((int)p.x +67, (int)p.y+15, 7, 7);
            //Pupil Speckle
            // g.setColor(Color.WHITE);
            //g.fillOval((int)p.x + 68, (int)p.y + 17, 3, 3);
        }
        else
        {
            //Eyes
            g.drawOval((int)p.x + 14, (int)p.y + 13, 14, 14);
            g.setColor(Color.WHITE);
            g.fillOval((int)p.x + 14, (int)p.y +13, 14, 14);
            //Pupils
            g.setColor(Color.BLACK);
            g.fillOval((int)p.x + 14, (int)p.y + 15, 7, 7);
            //Pupil Speckle
            //g.setColor(Color.WHITE);
            //g.fillOval((int)p.x +15, (int)p.y + 17, 3, 3);
        }
        
    }
    
    public double centerX()
    {
        return p.x + ea;
    }
    
    public double centerY()
    {
        return p.y + eb;
    }
    
    public Vector center()
    {
        return new Vector(centerX(),centerY());
    }
    
    public void updateVariables()
    {
        //used for collisionNodes
        double initialPosX = p.x;
        double initialPosY = p.y;
        
        //STEP 1
        //UPDATES POSITION
        if(((p.x+v.x) > Global.LEFT_BOUND) && ((p.x+v.x) < Global.RIGHT_BOUND   )) //Establishes Left and Right bounds
        {
            p = Vector.addVectors(p,v);
            v = Vector.addVectors(v,a);
        }
        else //Stops you from moving past the bounds
        {
            p.y = p.y + v.y;
            v = Vector.addVectors(v,a);
        }
        
        
        //STEP 2
        //UPDATES Y ACCELERATION
        
        //Jump starts out as constant velocity
        if(p.y<Global.PEAK) //Applies heavy acceleration just before the peak of the Slime's jump
        {
            a.y = Global.GRAVITY_GOING_UP;
        }
       
        if(p.y>Global.FLOOR) //Creates a floor so my slimes don't fall into the pits of hell
        {
            p.y = Global.FLOOR;
            v.y = 0;
        }
        
        if (v.y >= 0)//makes sure gravity is less when slime is past peak
            a.y = Global.GRAVITY_GOING_DOWN;
          
            
            
        //STEP 3
        //UPDATES X ACCELERATION
        double vsign = v.x==0 ? 0 : v.x/Math.abs(v.x);//this is either 1 or -1 for use in keeping the direction constant
        
        /*if(direction==LEFT && v.x<0) //Prevents derpy turnaround accelerations 
            a.x = -Global.XACCELERATION;
        else if(direction==RIGHT && v.x>0)
            a.x = Global.XACCELERATION;
        else if(v.x<0 && direction ==STILL)
            a.x = Global.XDECELERATION;
        else if(v.x>0 && direction ==STILL)
            a.x = -Global.XDECELERATION;  */
        if (direction==LEFT)
        {
            if (v.x <= 0)
                a.x = -Global.XACCELERATION;
            else //youre still moving right
                a.x = -Global.XDECELERATION-Global.XACCELERATION;
        }
        else if (direction==RIGHT)
        {
            if (v.x >= 0)
                a.x = Global.XACCELERATION;
            else //youre still moving left
                a.x = Global.XDECELERATION+Global.XACCELERATION;
        }
        else //direction==STILL
        {
            if (v.x==0)
                a.x = 0;
            else 
                a.x = vsign * -Global.XDECELERATION;
        }
        
        if(Math.abs(v.x) <= Global.XDECELERATION && Math.abs(a.x) == Global.XDECELERATION)
            {
                a.x = 0;
                v.x = 0;
            }
        
        if (Math.abs(v.x)>Global.MAX_X_VELOCITY)
            v.x = Global.MAX_X_VELOCITY * vsign;//sets the velocity to the max.
        
            
        //Make sure this is after all position updates
        for(CollisionNode c : nodes)
        {
            c.getPos().x += p.x-initialPosX;
            c.getPos().y += p.y-initialPosY;
        }
    }
    
    public boolean contains(Vector pos)
    {
        Vector v = Vector.subtractVectors(pos,center());
        if (Math.abs(v.x)>=ea || pos.y>centerY())
            return false;
        
        double y = eb*Math.sqrt(1-v.x*v.x/Math.pow(ea,2));
        return Math.abs(v.y)<y;
    }
    
    public void setLeft()
    {
        direction = LEFT;
        isLeft = true;
    }
    
    public void setRight()
    {
        direction = RIGHT;
        isLeft = false;
    }
    
    public void setStill()
    {
        direction = STILL;
    }
    
    public int getDirection()
    {
        return direction;
    }
    
    public int getWhichSlime()
    {
        return whichSlime;
    }
    
    public CollisionNode[] getNodes()
    {
        return nodes;
    }
    
    public Line[] getSides()
    {
        return sides;
    }
    
    public Line getSide(int i)
    {
        return sides[i];
    }
    
    public CollisionNode getNode(int i)
    {
        return nodes[i];
    }
    
    public double leftX()
    {
        return nodes[0].getPos().x;
    }
    
    public double rightX()
    {
        return nodes[nodes.length-1].getPos().x;
    }
    
    public double a()
    {
        return ea;
    }
    
    public double b()
    {
        return eb;
    }
}