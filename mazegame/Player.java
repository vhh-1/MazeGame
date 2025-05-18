package mazegame;

import java.util.HashSet;
import java.util.Set;
import java.awt.event.*;


public class Player {
    public Camera camera;
    public GraphicsPanel graphicsPanel;
    public Input inputhandler;
    public World world;
    public int keys;
    public int currentLevel=-1;
    public boolean testing=false;//Allows for vertical movement if true

    public int timeLimit=300;

     //Windows Compatibilty
    public String yeskey="\u2611";
    public String nokey="\u2610";

    //scale of FOV as well as scale of screen
    public static final double scale = 1200;


    public static final double rotateSpeed = Math.toRadians(2);
    public static final double moveSpeed = 2;

    public Player() { 
        this.camera = null;
        this.graphicsPanel = null;
        this.inputhandler = new Input();
        this.keys = 0;
    }

    public void addGraphicsPanel(GraphicsPanel g) {
        this.graphicsPanel = g;
        this.graphicsPanel.player = this;
    }

    public void clearInputs(){
        this.inputhandler.keysHeld.clear();
    }

    

    public void handleInput() {
        // System.out.println(this.inputhandler.keysHeld);
        
        //first find the x and y direction according to the camera's orientation.
        double x = moveSpeed*Math.cos(this.camera.theta);
        double y = moveSpeed*Math.sin(this.camera.theta);
        

        //a and b: movement is orthogonal to the cameras viewpoint. therefore, add x to the camera's y, and y to the camera's x.
        if (this.inputhandler.keysHeld.contains(KeyEvent.VK_A) && this.world.collisions[3] != 1) {;
            if (this.world.collisions[0] != 1 && this.world.collisions[6] != 1) {
                this.camera.point.x += y;
                this.camera.point.y += -x;
            }

            
        }
        if (this.inputhandler.keysHeld.contains(KeyEvent.VK_D) && this.world.collisions[5] != 1) {
            if (this.world.collisions[2] != 1 && this.world.collisions[8] != 1) {
                this.camera.point.x += -y;
                this.camera.point.y += x;
            }

            
        }
        
        // positive and negative, forwards and backwards
        if (this.inputhandler.keysHeld.contains(KeyEvent.VK_W) && this.world.collisions[1] != 1) {

            if (this.world.collisions[0] != 1 && this.world.collisions[2] != 1) {
                this.camera.point.x += x;
                this.camera.point.y += y;
            }

            
        }
        if (this.inputhandler.keysHeld.contains(KeyEvent.VK_S )&& this.world.collisions[7] != 1) {

            if (this.world.collisions[6] != 1 && this.world.collisions[8] != 1) {
                this.camera.point.x += -x;
                this.camera.point.y += -y;
            }


            
        }

        //just for testing
        if (testing==true&&this.inputhandler.keysHeld.contains(KeyEvent.VK_E)) {
            this.camera.point.z += moveSpeed;
        }
        if (testing==true&&this.inputhandler.keysHeld.contains(KeyEvent.VK_Q)) {
            this.camera.point.z -= moveSpeed;
        }

        //rotation, modifying phi and theta.
        if (this.inputhandler.keysHeld.contains(KeyEvent.VK_UP)) {
            this.camera.phi += rotateSpeed;
        }
        if (this.inputhandler.keysHeld.contains(KeyEvent.VK_DOWN)) {
            this.camera.phi -= rotateSpeed;
        }
        if (this.inputhandler.keysHeld.contains(KeyEvent.VK_RIGHT)) {
            this.camera.theta += rotateSpeed;
        }
        if (this.inputhandler.keysHeld.contains(KeyEvent.VK_LEFT)) {
            this.camera.theta -= rotateSpeed;
        }



        //wrap angles between negative pi and pi
        this.camera.normalizeAngles();
    }

    //Looks for keys
    public class Input implements KeyListener {
        private Set<Integer> keysHeld;

        //track keys held. add and remove when key is pressed and released
        public Input() {
            this.keysHeld = new HashSet<Integer>();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            keysHeld.add(e.getKeyCode());
            //System.out.println("Key pressed: " + KeyEvent.getKeyText(e.getKeyCode()));
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keysHeld.remove(e.getKeyCode());
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

       
    }


    




    public class Camera{
        Point point;
        double theta;
        double phi;

        //rendering
        final double FOV = Math.toRadians(60.0);

        //maximum top and bottom angle
        final double maxPhi = Math.toRadians(30);
        double scale;

        public Camera(double scale) {
            this.point = new Point(150, 1450, 50);
            this.theta = 0.0;
            //this.theta = Math.toRadians(10.0);
            this.phi = 0.0;
            //this.phi = Math.toRadians(20.0);
            this.scale = scale;
        }
        public Camera(Point p, double theta, double phi, double scale) {
            this.point = p;
            this.theta = theta;
            this.phi = phi;
            this.scale = scale;
        }


        public void normalizeAngles() {
            //converts theta to always be within negative pi and pi
            theta = ((theta + Math.PI) % (2 * Math.PI) + (2 * Math.PI)) % (2 * Math.PI) - Math.PI;
        
            
            //looking up and down
            if (phi > maxPhi) phi = maxPhi;
            if (phi < -maxPhi) phi = -maxPhi;
        }

        public int[] convertPoint(Point P) {

            //3d point to 2d point
            int[] l = new int[2];
            
            //world rotation of camera to point
            double[] angles = Point.rotation(this.point, P);
            double theta = wrapAngle(angles[0] - this.theta);
            double phi = wrapAngle(angles[1] - this.phi);

            //divide the angle by the FOV
            double x_ratio = theta/(FOV/2);
            double y_ratio = phi/(FOV/2);
                
            l[0] = (int) Math.round(x_ratio*(scale/2) + (scale/2));
            l[1] = (int) Math.round(-1*y_ratio*(scale/2) + (scale/2));

            return l;
        }

        public int[][] convertTri(Triangle T) {

            int[][] l = new int[2][3];

            double[] thetas = new double[3];
            double[] phis = new double[3];


            
            double min = 1000000;
            int minindex = 0;

            //find the closest point to the mid line. this point will be used to convert the other points.
            //this is to prevent wrapping
            for(int i = 0; i < 3; i++) {
                double[] angles = Point.rotation(this.point, T.points[i]);
                thetas[i] = wrapAngle(angles[0] - this.theta);
                phis[i] = wrapAngle(angles[1] - this.phi);
                if (Math.abs(thetas[i]) < min) {
                    min = Math.abs(thetas[i]);
                    minindex = i;
                }
            }
            
            
            boolean isView = (min < FOV/2);

            //check whether the triangle would be behind or in front of the camera
            //we must check the other two points in relation to the closest point.
            //suppose the closest point has a theta value of negative 30, which is still within the FOV.
            //another poiunt of the triangle has a theta value of 170.
            //this should be rendered as behind the camera. however, using typical FOV, this will be rendered in front.
            //we must convert the angle to -190 to be rendered properly.
            for(int i = 0; i < 3; i++) {
                
                //do this only if the size in degrees of the triangle is greater than 180.
                //for example, a triangle that will be 200 degrees wide should always be changed to a 160 degrees wide triangle on the other side
                if (Math.abs(thetas[i] - thetas[minindex]) > Math.PI) {
                    
                    if (thetas[i] > thetas[minindex]) {
                        thetas[i] -= Math.PI * 2;
                    } else {
                        thetas[i] += Math.PI * 2;
                    }
                } 
                //do not cull if, after transformation, the points are still opposite signs. this means they make a line in front of the camera
                if (thetas[minindex]*thetas[i] < 0){
                    isView = true;
                }

            }
            
            // cull. cull only if all 3 points are not within the FOV. and, none of the points have different signs meaning they cross over the midpoint
            if ((!isView)) {
                return null;
            }
            

            //convert 3d coordinates and return
            for (int i = 0; i < 3; i++) {

                double x_ratio = thetas[i]/(FOV/2);
                double y_ratio = phis[i]/(FOV/2);
                
                l[0][i] = (int) Math.round(x_ratio*(scale/2) + (scale/2));
                l[1][i] = (int) Math.round(-1*y_ratio*(scale/2) + (scale/2));
                
                //System.out.print(Math.toDegrees(thetas[i]) + "  ");

            }
            return l;
        }


        //wrap angle of point to within negative pi and pi
        public double wrapAngle(double angle) {
           
            return ((angle + Math.PI) % (2 * Math.PI) + 2 * Math.PI) % (2 * Math.PI) - Math.PI;
        }

        
        
        
    }
}
