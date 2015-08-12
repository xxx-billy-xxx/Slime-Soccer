public class CollisionNode
{
    private Vector v;
    private Vector s;
    
    public CollisionNode(Vector ve, Slime sl)
    {
        v = ve;
        s = new Vector(((v.y-sl.p.y-sl.b())/(v.x-sl.p.x-sl.a()))*((sl.b()*sl.b())/(sl.a()*sl.a())),1);
    }
    
    public Vector getPos()
    {
        return v;
    }
    
    public void setPos(Vector a)
    {
        v = a;
    }
    
    public Vector getNormal()
    {
        return s;
    }
}