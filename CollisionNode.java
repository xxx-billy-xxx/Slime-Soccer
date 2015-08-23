public class CollisionNode
{
    private Vector p;
    private final Vector normal;
    private Slime slime;
    
    public CollisionNode(Vector pos, Slime sl)
    {
        slime = sl;
        p = pos;
        normal = calculateNormal();
    }
    
    private Vector calculateNormal()
    {
        double b = slime.b();
        double a = slime.a();
        double a2 = a*a;
        double h = slime.p.x+a;
        double k = slime.p.y+b;
        double dx = p.x-h;
        
        double slope = b*dx/(a2*Math.sqrt(1-dx*dx/a2));
        double perp = -1/slope;
        //if (dx!=0)
          //  perp*=-1/dx;
        
        double ny = -1;//y part has to be negative since its always going up!
        double nx = -1/perp;//since ny/nx should be perp
        return new Vector(nx,ny);//.unitVector();
    }
    
    public void move(Vector delta)
    {
        p = Vector.addVectors(p,delta);
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