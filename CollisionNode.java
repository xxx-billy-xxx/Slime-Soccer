public class CollisionNode
{
    private Vector p;
    private Vector normal;
    
    public CollisionNode(Vector ve, Slime sl)
    {
        p = ve;
        normal = new Vector(((p.y-sl.p.y-sl.b())/(p.x-sl.p.x-sl.a()))*((sl.b()*sl.b())/(sl.a()*sl.a())),1);
        normal = new Vector(normal.x/(Math.sqrt((normal.x*normal.x)+(normal.y*normal.y))),
            normal.y/(Math.sqrt((normal.x*normal.x)+(normal.y*normal.y))));
    }
    
    public Vector getPos()
    {
        return p;
    }
    
    public void setPos(Vector a)
    {
        p = a;
    }
    
    public Vector getNormal()
    {
        return normal;
    }
}

//y1-