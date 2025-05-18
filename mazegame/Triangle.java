package mazegame;

import java.awt.*;
// import javafx.scene.shape.Polygon;

public class Triangle {
    public Point a;
    public Point b;
    public Point c;
    public Point[] points;

    public Color color;

    //bool to indicate whether it is visible or not.
    public boolean visible;

    //whether or not to draw borders
    public boolean[] borders;

    public Triangle(Point a, Point b, Point c, Color color) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.points = new Point[]{this.a, this.b, this.c};
        this.borders = new boolean[] {true, true, false};
        this.visible = true;
        this.color=color;
    }

    public Triangle(Point a, Point b, Point c){
        this(a,b,c,Color.RED);
    }

    //Move Triangle
    public Triangle translate(int x, int y, int z) {
        Point a = new Point(this.a.x + x, this.a.y + y, this.a.z + z);
        Point b = new Point(this.b.x + x, this.b.y + y, this.b.z + z);
        Point c = new Point(this.c.x + x, this.c.y + y, this.c.z + z);

        return new Triangle(a, b, c);
    }

    //Custom Contains method
    public boolean contains(Point p){
        int[] yPoints = {(int)this.a.y,(int)this.b.y,(int)this.c.y};
        int[] xPoints = {(int)this.a.x,(int)this.b.x,(int)this.c.x};
        java.awt.Polygon poly = new Polygon(xPoints, yPoints, xPoints.length);
        return poly.contains(p.x,p.y);
    }


    public static boolean contains(int[] xpoints, int[] ypoints, int x, int y) {
        
        double denom = (ypoints[1]-ypoints[2])*(xpoints[0]-xpoints[2]) + (xpoints[2]-xpoints[1])*(ypoints[0]-ypoints[2]);

        if (denom == 0) return false;

        
        double a = ((ypoints[1]-ypoints[2])*(x-xpoints[2]) + (xpoints[2]-xpoints[1])*(y-ypoints[2]))/denom;
        double b = ((ypoints[2]-ypoints[0])*(x-xpoints[2]) + (xpoints[0]-xpoints[2])*(y-ypoints[2]))/denom;
        double c = 1-a-b;
        return (a>=0 && a<= 1) && (b>=0 && b<= 1) && (c>=0 && c<= 1);

    }
}

