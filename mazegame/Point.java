package mazegame;

public class Point {
    public double x;
    public double y;
    public double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double distTo(Point a) {
        double xdiff = (this.x-a.x);
        double ydiff = (this.y-a.y);
        double zdiff = (this.z-a.z);

        return Math.sqrt(xdiff*xdiff + ydiff*ydiff + zdiff*zdiff);
    }
    
    @Override
    public String toString() {
        String s = Double.toString(x) + ", " + Double.toString(y) + ", " + Double.toString(z);
        return s;
    }
    

    public static double distance(Point a, Point b) {
        double xdiff = (a.x-b.x);
        double ydiff = (a.y-b.y);
        double zdiff = (a.z-b.z);

        return Math.sqrt(xdiff*xdiff + ydiff*ydiff + zdiff*zdiff);
    }

    //only x and y distance
    public static double flatdistance(Point a, Point b) {
        double xdiff = (a.x-b.x);
        double ydiff = (a.y-b.y);
        return Math.sqrt(xdiff*xdiff + ydiff*ydiff);
    }
    
    //rotation method to use when rendering.
    //finds the world rotation of one point relative to another.
    public static double[] rotation(Point a, Point b) {
        //System.out.println("Point A");
        //System.out.println(a.toString());
        //System.out.println("Point B");
        //System.out.println(b.toString());
        double xdiff = (b.x-a.x);
        double ydiff = (b.y-a.y);
        double zdiff = (b.z-a.z);

        //atan2 returns a value from negative pi to pi. this presents some issues with wrapping, which are handled in player.camera
        double theta = Math.atan2(ydiff, xdiff);
        double rxy = Math.sqrt(xdiff * xdiff + ydiff * ydiff);
        double phi = Math.atan2(zdiff, rxy);

    
        return new double[] {theta, phi};
    }

    public Point translate(double x, double y, double z) {
        return new Point(this.x+x, this.y+y, this.z+z);
    }
}
