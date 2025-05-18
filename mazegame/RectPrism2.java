package mazegame;

import java.awt.*;

public class RectPrism2 {

    //Instance Variables
    public Point center;
    public double theta;
    public double threshold;

    public double width;
    public double height;
    public double length;

    public Color color;

    public Rectangle Rt;
    public Rectangle Lt;

    public Rectangle Fr;
    public Rectangle Bk;

    public Rectangle Tp;
    public Rectangle Bt;

    public Rectangle[] rectangles;

    //4 sides
    public Rectangle[] sides;

    //4 bottom points
    public Point[] basepoints;

    // 8 points
    public Point a;
    public Point b;
    public Point c;
    public Point d;
    public Point e;
    public Point f;
    public Point g;
    public Point h;

    public RectPrism2(Point origin, double theta, double width, double height, double length, Color color) {
        this.color=color;
        
        double w = width;
        double l = length;

        this.center = origin;
        this.theta = theta;

        //calculate the ratio of the rectangle's length to width
        this.threshold = Math.atan2(l, w);


        //Calculate each of the 8 points 
        a = origin.translate(0, 0, 0);
        b = origin.translate(w*Math.cos(theta+(Math.PI/2)), w*Math.sin(theta+(Math.PI/2)), 0);
        c = origin.translate(l*Math.cos(theta), l*Math.sin(theta), 0);
        d = origin.translate(l*Math.cos(theta)+w*Math.cos(theta+(Math.PI/2)), l*Math.sin(theta)+w*Math.sin(theta+(Math.PI/2)), 0);

        e = origin.translate(0, 0, height);
        f = origin.translate(w*Math.cos(theta+(Math.PI/2)), w*Math.sin(theta+(Math.PI/2)), height);
        g = origin.translate(l*Math.cos(theta), l*Math.sin(theta), height);
        h = origin.translate(l*Math.cos(theta)+w*Math.cos(theta+(Math.PI/2)), l*Math.sin(theta)+w*Math.sin(theta+(Math.PI/2)), height);

        //generate rectangles
        Bt = new Rectangle(a, b, c, d, color);
        Tp = new Rectangle(e, f, g, h, color);
        Fr = new Rectangle(g, h,c,d, color);
        Bk = new Rectangle(a, b, e, f, color);
        Rt = new Rectangle(a, c, e, g, color);
        Lt = new Rectangle(b, d, f, h, color);

        rectangles = new Rectangle[]{Bt, Tp, Fr, Bk, Rt, Lt};
        sides = new Rectangle[]{Bk, Lt, Fr, Rt};

        basepoints = new Point[]{a, b, d, c};
        
    }

    public RectPrism2(Point origin, double theta, double width, double height, double length) {
        this( origin,  theta,  width, height, length, Color.RED);
    }

    public void UpdateVisibility(Player.Camera c) {

        Point p = c.point;

        //set all to false
        for (Rectangle r : this.rectangles) {
            r.triangles[0].visible = false;
            r.triangles[1].visible = false;
            r.visible=false;
        }
        //set top and bottom visibility according to camera Z
        if (a.z > p.z) {
            Bt.triangles[0].visible = true;
            Bt.triangles[1].visible = true;
            Bt.visible=true;
        }
        if (e.z < p.z) {
            Tp.triangles[0].visible = true;
            Tp.triangles[1].visible = true;
            Tp.visible=true;
        }

        //find the closest point to the player
        int minindex = 0;
        double min = 10000000;
        for(int i = 0; i < 4; i++) {
            double temp = Point.distance(basepoints[i], p);
            if (temp < min) {
                minindex = i;
                min = temp;
            }
        }


        //find the clockwise and counterclockwise points from the closes point
        Point leftpoint = basepoints[(minindex-1+4) %4];
        Point rightpoint = basepoints[(minindex+1+4) %4];

        //translate points to on-screen coordinates
        int leftx = c.convertPoint(leftpoint)[0];
        int midx = c.convertPoint(basepoints[minindex])[0];
        int rightx = c.convertPoint(rightpoint)[0];
        
        //if the leftpoint is further right on screen than the midpoint, that means the rectangle should not be visible. same with the right point
        if (leftx < midx) {
            
            sides[(minindex-1+4)%4].triangles[0].visible = true;
            sides[(minindex-1+4)%4].triangles[1].visible = true;
            sides[(minindex-1+4)%4].visible = true;

        }
        if (rightx > midx) {
            sides[minindex].triangles[0].visible = true;
            sides[minindex].triangles[1].visible = true;
            sides[minindex].visible = true;
        }
       //System.out.println(leftx + "  " + midx + "  " + rightx);
        
    }


}
