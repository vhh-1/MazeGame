package mazegame;

import java.awt.*;

public class Rectangle {
    /*
     * a----,b
     * |  /  |
     * c'----d
     */
    public Point a;
    public Point b;
    public Point c;
    public Point d;
    public Color color;

    public Point mid;
    public Triangle t1;
    public Triangle t2;

    public Point[] points;
    public Triangle[] triangles;
    public boolean visible;

    public Rectangle(Point a, Point b, Point c, Point d, Color color) {
        this.visible=true;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.color=color;
        //midpoint to use when calculating distance
        this.mid=new Point((a.x + d.x) / 2, (a.y + d.y) / 2, (a.z + d.z) / 2);

        this.points = new Point[]{this.a, this.b, this.c, this.d};
        this.triangles= new Triangle[]{new Triangle(c, a, b, color), new Triangle(b, d, c, color)};

        //the third edge in both triangles is the edge through the rect, make it invisible
        //the order of points in the rectangle matters.
        this.triangles[0].borders[2] = false;
        this.triangles[1].borders[2] = false;
    }
    public Rectangle(Point a, Point b, Point c, Point d){
        this(a,b,c,d,Color.RED);
    }
    

    public Rectangle translate(int x, int y, int z) {
        Point a = new Point(this.a.x + x, this.a.y + y, this.a.z + z);
        Point b = new Point(this.b.x + x, this.b.y + y, this.b.z + z);
        Point c = new Point(this.c.x + x, this.c.y + y, this.c.z + z);
        Point d = new Point(this.d.x + x, this.d.y + y, this.d.z + z);

        return new Rectangle(a, b, c,d);
    }
}


//This is a test again
