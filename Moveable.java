import java.awt.*;

public abstract class Moveable
{
    protected Vector p;
    protected Vector fp;//position
    protected Vector v;         //velocity
    protected Vector a;         //acceleration
    
    protected Screen screen;
    
        
    public Moveable(Screen s, Vector vector)
    {
        screen = s;
        p = vector;
        fp = vector;
        v = new Vector(0,0);
        a = new Vector(0,0);
    }
    public Vector getPosition()
    {
        return p;
    }
    public Vector getFPosition()
    {
        return fp;
    }
    public void setFPosition(Vector vec)
    {
        p=fp;
        fp = vec;
    }
    public Vector getVelocity()
    {
        return v;
    }
    public void setVelocity(Vector vec)
    {
        v = vec;
    }
    public Vector getAcceleration()
    {
        return a;
    }   
    public void setAcceleration(Vector vec)
    {
        a = vec;
    }
    
    public abstract void draw(Graphics g);
    public abstract void updateVariables();
        
    
}