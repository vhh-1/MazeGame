//INITIAL RECTANGULAR PRISM
//NO LONGER USED
package mazegame;

public class RectPrism {
    /*
     * a----,b
     * |  /  |
     * c'----d
     */
    public Point u1;
    public Point u2;
    public Point a;
    public Point b;
    public Point c;
    public Point d;
    public Point e;
    public Point f;
    public Point g;
    public Point h;
    public Rectangle r1;
    public Rectangle r2;
    public Rectangle r3;
    public Rectangle r4;
    public Rectangle r5;
    public Rectangle r6;
    

    public Point[] points;
    public Rectangle[] rectangles;
    public int orientation;
    public int WIDTH=10;
    public int HEIGHT=50;

    //@param orientation 0=Along x, 1=Along y 

    public RectPrism(Point u1,Point u2,int orientation) {
        this.u1= u1;
        this.u2 = u2;
        this.orientation=orientation;

    
        if(orientation==1){
            a = new Point(this.u1.x + WIDTH, this.u1.y, this.u1.z+HEIGHT);
            b = new Point(this.u2.x + WIDTH, this.u2.y , this.u2.z+HEIGHT );
            c = new Point(this.u1.x + WIDTH, this.u1.y, this.u1.z);
            d = new Point(this.u2.x + WIDTH, this.u2.y , this.u2.z );
            e = new Point(this.u1.x - WIDTH, this.u1.y, this.u1.z+HEIGHT);
            f = new Point(this.u2.x - WIDTH, this.u2.y , this.u2.z+HEIGHT );
            g = new Point(this.u1.x - WIDTH, this.u1.y, this.u1.z);
            h = new Point(this.u2.x - WIDTH, this.u2.y , this.u2.z );
        }        
        else{
             a = new Point(this.u1.x , this.u1.y+ WIDTH, this.u1.z+HEIGHT);
             b = new Point(this.u2.x , this.u2.y+ WIDTH , this.u2.z+HEIGHT );
             c = new Point(this.u1.x , this.u1.y+ WIDTH, this.u1.z);
             d = new Point(this.u2.x , this.u2.y+ WIDTH , this.u2.z );
             e = new Point(this.u1.x , this.u1.y- WIDTH, this.u1.z+HEIGHT);
             f = new Point(this.u2.x , this.u2.y- WIDTH , this.u2.z+HEIGHT );
             g = new Point(this.u1.x , this.u1.y- WIDTH, this.u1.z);
             h = new Point(this.u2.x , this.u2.y- WIDTH , this.u2.z );
        }
        this.rectangles= new Rectangle[]{new Rectangle(e,f,g,h),new Rectangle(b,f,d,h),new Rectangle(a,e,c,g),new Rectangle(e,f,a,b),new Rectangle(g,h,c,d),new Rectangle(a,b,c,d)};
        //this.rectangles= new Rectangle[]{new Rectangle(e,f,a,b),new Rectangle(g,h,c,d),new Rectangle(a,b,c,d)};

    }

    public RectPrism translate(int x, int y, int z) {
        Point u1 = new Point(this.u1.x + x, this.u1.y + y, this.u1.z + z);
        Point u2 = new Point(this.u2.x + x, this.u2.y + y, this.u2.z + z);

        return new RectPrism(u1, u2, this.orientation);
    }


    public void updateVisibility(Point p) {
        
    }


}


//This is a test again
