public class Vector
{
    public double x;
    public double y;
    
    public Vector(double a, double b)
    {
        x = a;
        y = b;        
    }  
    public Vector(Vector a)
    {
        x=a.x;
        y=a.y;
    }
    public static Vector addVectors(Vector a, Vector b)
    {
        return new Vector(a.x + b.x, a.y + b.y);
    }
    public static Vector subtractVectors(Vector a, Vector b)
    {
        return new Vector(a.x - b.x, a.y - b.y);
    }
    public double dot(Vector a)
    {
        return x*a.x+y*a.y;
    }
    
    //remember cos(angle) will always be positive because of the nature of arctan
    public double angle()
    {
        return Math.atan(-y/x);
    }
    
    //isn't this fundamentally flawed since it wont scale the magnitude accurately
    public Vector scale(double scale)
    {
        //double angle = angle();
        //double newX = x*scale*Math.cos(angle)*(x>=0 ? 1 : -1);//makes newX proper no matter the x sign
        //return new Vector(newX,y*scale*Math.abs(Math.sin(angle)));
        
        return new Vector(x*scale,y*scale);
    }
    
    //reflects a over b
    public static Vector reflect(Vector a,Vector b)
    {
        //return subtractVectors(a,b.scale(2*((b.x*a.x)+(a.y*b.y))));
        //return subtractVectors(a,scale(b,2*((b.x*a.x)+(a.y*b.y))/((b.x*b.x)+(b.y*b.y))));
        return subtractVectors(a,b.scale(2*a.dot(b)/b.r2()));//a-(2a*b/|b|^2)b
    }
    
    public Vector unitVector()
    {
        return scale(1.0/getMagnitude());
    }
    
    public double getMagnitude()
    {
        return Math.sqrt(r2());
    }
    
    public double r2()
    {
        return x*x+y*y;
    }
    
    public double distanceTo(Vector point)
    {
        return subtractVectors(this,point).getMagnitude();
    }
    
    public double distanceTo(Line l)
    {
        //since its a line segment this is tricky
        Vector start = l.getStart();
        Vector end = l.getEnd();
        Vector n = l.normal();//make sure n is unit
        Vector v = subtractVectors(start,this);
        double theoreticalDistance = Math.abs(v.dot(n));
        
        //checks if the point is perpendicular to the line segment. If not then the shortest distance
        //is really the distance to one of the vertices.
        return (l.contains(addVectors(this,n.scale(theoreticalDistance))) ||
                (l.contains(addVectors(this,n.scale(-theoreticalDistance)))))
                ? theoreticalDistance : Math.min(distanceTo(start),distanceTo(end));
    }
    
    public String toString()
    {
        return "("+x+", "+y+")";
    }
    
    public boolean equals(Object a)
    {
        assert a instanceof Vector;
        Vector v = (Vector)a;
        return x==v.x&&y==v.y;
    }
}