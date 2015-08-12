import java.awt.*;

public class Ball extends Moveable
{    
    private double r;
    
    public Ball(Screen s, Vector vector, double radius)
    {
        super(s, vector);
        r = radius;
    }
    
    public void draw(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.drawOval((int)p.x, (int)p.y, (int)r*2,(int)r*2);
    }
    
    public void updateVariables()
    {
        //maybe add that the closer slime to the ball will get collision detection first
        checkCollide(screen.getSlime1());
        checkCollide(screen.getSlime2());
        
        
        //STEP 1
        //UPDATES POSITION
        if(((p.x+v.x) > Global.LEFT_BOUND+r) && ((p.x+v.x) < Global.RIGHT_BOUND-r)) //Establishes Left and Right bounds
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
       
        if(p.y-r>Global.FLOOR-r) //Creates a floor so my slimes don't fall into the pits of hell
        {
            p.y = Global.FLOOR-r;
            v.y = 0;
        }
        
        if (v.y >= 0)//makes sure gravity is less when slime is past peak
            a.y = Global.GRAVITY_GOING_DOWN;
        
    }
    
    public void checkCollide(Slime s)
    {
        if(p.y-r>s.p.y+s.b())
        {
            //check which side collision nodes start on
            if(p.x>s.getNodes(0).getPos().x&&p.x<=s.getNodes(s.nodeNumber-1).getPos().x)
            {
                
                //setVelocity(new Vector(v.x,-v.y));
            }
        }
        else
        {
            for(CollisionNode c : s.getNodes())
            {
                if(r*r>Math.pow((p.x-c.getPos().x),2)+Math.pow((p.y-c.getPos().y),2))
                {
                    v.addVectors(v,s.v);
                    //setVelocity(v.reflect(v,c.getNormal()));
                    System.out.println("collided");
                }
                
            }
            
        }
        //testing System.out.println(s.getNodes(0).getPos().x + "\t" + s.getNodes(0).getPos().y + "\t" + v.x + "\t" + v.y);
    }
}