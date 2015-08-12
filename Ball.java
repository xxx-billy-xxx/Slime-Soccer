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
        long start = System.nanoTime();
        checkCollide(screen.getSlime1());
        checkCollide(screen.getSlime2());
        if(v.x>Global.MAX_X_VELOCITY_BALL)v.x=Global.MAX_X_VELOCITY_BALL;
        if(v.y>Global.MAX_Y_VELOCITY_BALL)v.y=Global.MAX_Y_VELOCITY_BALL;
        
        
        //STEP 1
        //UPDATES POSITION
        if(((p.x+v.x) > Global.LEFT_BOUND+r) && ((p.x+v.x) < Global.RIGHT_BOUND+4*r)) //Establishes Left and Right bounds
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
        if(p.y+r>Global.FLOOR) //Creates a floor so my slimes don't fall into the pits of hell
        {
            p.y = Global.FLOOR-r;
            v.y*=-.65;
        }
        else if(p.y<0)
        {
            p.y=0;
            v.y*=-.65;
        }
        else
        {
            v.y+=.425;
        }
        
        //STEP 3 X ACCELERATION
        if(v.x>0)
        {
            if(v.x>1)v.x-=.05;
            else if(v.x>.5)v.x-=.025;
            else if(v.x>.1)v.x-=.001;
            else if(v.x>.01)v.x-=.0001;
            else if(v.x>.0001)v.x-=.00001;
        }
        else if(v.x<0)
        {
            if(v.x>-1)v.x+=.05;
            else if(v.x>-.5)v.x+=.025;
            else if(v.x>-.1)v.x+=.001;
            else if(v.x>-.01)v.x+=.0001;
            else if(v.x>-.0001)v.x+=.00001;
        }
        
        //STEP 4 CHECK BOUNDS
        if(v.x>Global.MAX_X_VELOCITY_BALL)v.x=Global.MAX_X_VELOCITY_BALL;
        if(v.y>Global.MAX_Y_VELOCITY_BALL)v.y=Global.MAX_Y_VELOCITY_BALL;
        long end = System.nanoTime();
        System.out.println(end-start);
    }
    
    public void checkCollide(Slime s)
    {
        if(p.y>s.p.y+s.b())
        {
            //check which side collision nodes start on
            if(p.x+r>s.getNodes(0).getPos().x&&p.x+r<=s.getNodes(s.nodeNumber-1).getPos().x)
            {
                setVelocity(v.scale(v.addVectors(v,s.v),.5));
                setVelocity(new Vector(v.x,-v.y));
                if(v.x>Global.MAX_X_VELOCITY_BALL)v.x=Global.MAX_X_VELOCITY_BALL;
                if(v.y>Global.MAX_Y_VELOCITY_BALL)v.y=Global.MAX_Y_VELOCITY_BALL;
            }
        }
        else
        {
            for(CollisionNode c : s.getNodes())
            {
                if(r*r>Math.pow((p.x+v.x+r-c.getPos().x),2)+Math.pow((p.y+v.y+r-c.getPos().y),2))
                {
                    setVelocity((v.addVectors(v,s.v)));
                    setVelocity(v.reflect(v,c.getNormal()));
                    if(v.x>Global.MAX_X_VELOCITY_BALL)v.x=Global.MAX_X_VELOCITY_BALL;
                    if(v.y>Global.MAX_Y_VELOCITY_BALL)v.y=Global.MAX_Y_VELOCITY_BALL;
                }
                
            }
            
        }
        //System.out.println(p.x+"\t"+s.getNodes(85).getPos().x+"\t"+p.y+"\t"+s.getNodes(85).getPos().y);
    }
}