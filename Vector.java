public class Vector
{
    public double x;
    public double y;
    
    public Vector(double a, double b)
    {
        x = a;
        y = b;        
    }  
    public static Vector addVectors(Vector a, Vector b)
    {
        return new Vector(a.x + b.x, a.y + b.y);
    }
    public static Vector subtractVectors(Vector a, Vector b)
    {
        return new Vector(a.x - b.x, a.y - b.y);
    }
    //make not static 
    public static Vector scale(Vector a,double scale)
    {
        return new Vector(a.x*scale,a.y*scale);
    }
    public static Vector reflect(Vector a,Vector b)
    {
        return subtractVectors(a,scale(b,2*((b.x*a.x)+(a.y*b.y))));
        //return subtractVectors(a,scale(b,2*((b.x*a.x)+(a.y*b.y))/((b.x*b.x)+(b.y*b.y))));
    }
    public double getMagnitude()
    {
        return Math.sqrt(x*x+y*y);
    }
    
}