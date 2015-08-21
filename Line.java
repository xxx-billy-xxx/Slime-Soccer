//these are line segments, used as the sides of the ngon representing the slime.
public class Line
{
    private CollisionNode start;
    private CollisionNode end;
    //no one sees the collision nodes when accessings this class, they see the position vectors only
    
    public Line(CollisionNode a, CollisionNode b)
    {
        start = a;
        end = b;
    }
    
    public boolean contains(Vector point)
    {
        Vector v = getVector();
        Vector v1 = getStart();
        
        if (point.x<v1.x||point.x>getEnd().x)
            return false;
            
        double delta = point.x-v1.x;
        return point.y==delta+v1.y;//makes sure the slope of the difference between point and start
                                    //is the same as the slope of the line segment
    }
    
    public Vector getVector()
    {
        return Vector.subtractVectors(getEnd(),getStart());
    }
    
    public Vector normal()
    {
        Vector v = getVector();
        return new Vector(-v.y,-v.x).unitVector();
    }
    
    public Vector getStart()
    {
        return start.getPos();
    }
    
    public Vector getEnd()
    {
        return end.getPos();
    }
}