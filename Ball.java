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
        if(checkCollide(screen.getSlime1()));
        else if(checkCollide(screen.getSlime2()));
        else
        {
            //STEP 1
            //UPDATES POSITION AND VELOCITY
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
            }
            else
            {
                v.y+=.6;
            }

            //if(v.y<.0001)v.y=0;

            //STEP 3 X ACCELERATION
            if(v.x>0)
            {
                if(v.x>1)v.x-=.05;
                else if(v.x>.5)v.x-=.025;
                else if(v.x>.1)v.x-=.001;
                else if(v.x>.01)v.x-=.0001;
                else if(v.x>.0001)v.x-=.0001;
            }
            else if(v.x<0)
            {
                if(v.x>-1)v.x+=.05;
                else if(v.x>-.5)v.x+=.025;
                else if(v.x>-.1)v.x+=.001;
                else if(v.x>-.01)v.x+=.0001;
                else if(v.x>-.0001)v.x+=.0001;
            }

            //STEP 4 CHECK BOUNDS
            adjustVelocity();
        }
    }

    public boolean checkCollide(Slime slime)
    {
        boolean collided = false;
        if(p.y>slime.p.y+slime.b())
        {
            //check which side collision nodes start on
            if(centerX()>slime.getNode(0).getPos().x&&
                centerX()<=slime.getNode(Slime.NODE_NUM-1).getPos().x)
            {
                
                v.y*=-1;
                v = v.scale(Vector.addVectors(v,slime.v),1);
                return true;
            }
        }
        else
        {
            for(CollisionNode c : slime.getNodes())
            {
                if(r*r>Math.pow((centerX()+v.x-c.getPos().x),2)+Math.pow((centerY()+v.y-c.getPos().y),2))
                {
                    double factor = ((1-v.x/Global.MAX_X_VELOCITY_BALL)+(1-v.y/Global.MAX_Y_VELOCITY_BALL))/2.0;
                    
                    setVelocity(v.reflect(v,c.getNormal()));
                    setVelocity(v.scale(v.addVectors(v,slime.v),1));
                    adjustVelocity();
                    collided = true;
                }
            }

        }
        return collided;
    }
    
    //adjusts velocity to make sure its less than max
    public void adjustVelocity()
    {
        double vxsign = Math.abs(v.x)/v.x;
        double vysign = Math.abs(v.y)/v.y;
        if (Math.abs(v.x)>Global.MAX_X_VELOCITY_BALL)
            v.x = Global.MAX_X_VELOCITY_BALL*vxsign;
        if (Math.abs(v.y)>Global.MAX_Y_VELOCITY_BALL)
            v.y = Global.MAX_Y_VELOCITY_BALL*vysign;
    }
    
    public double centerX()
    {
        return p.x+r;
    }
    
    public double centerY()
    {
        return p.y+r;
    }
}