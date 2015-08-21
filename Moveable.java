import java.awt.*;

public abstract class Moveable
{
    protected Vector p;
    protected Vector v;         //velocity
    protected Vector a;         //acceleration
    
    protected Vector fp;//position
    protected Vector fv;
    protected Vector fa;
    
    protected Screen screen;
    
        
    public Moveable(Screen s, Vector vector)
    {
        screen = s;
        p = vector;
        fp = vector;
        v = new Vector(0,0);
        a = new Vector(0,0);
    }
    public Vector getPos()
    {
        return p;
    }
    public Vector getFPos()
    {
        return fp;
    }
    public void setFPos(Vector vec)
    {
        p=fp;
        fp = vec;
    }
    public void setPos(Vector vec)
    {
        p = vec;
    }
    public Vector getFVelocity()
    {
        return fv;
    }
    public Vector getFAcceleration()
    {
        return fa;
    }
    public Vector getVelocity()
    {
        return v;
    }
    public void setVelocity(Vector vec)
    {
        v = vec;
    }
    public void setYVelocity(double vy)
    {
        v.y = vy;
    }
    public void setXVelocity(double vx)
    {
        v.x = vx;
    }
    public Vector getAcceleration()
    {
        return a;
    }   
    public void setAcceleration(Vector vec)
    {
        a = vec;
    }
    public boolean somethingWrong()
    {
        double f = Double.NaN;
        return a.x==f || a.y==f || v.x==f || v.y==f || a.x==f || a.y==f;
    }
    
    public abstract void draw(Graphics g);
    public abstract void updateVariables();
        
    
}