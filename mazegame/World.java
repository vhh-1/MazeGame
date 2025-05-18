package mazegame;
import java.util.*;
import java.awt.*;

public class World {
    public Player player;

    //key with which we will track the walls
    public int[][] key;
    public double nodeSize;
    public double keySize = 35;

    public double collisiondistance = 10;
    public int[] collisions;

    public int[] exitcoordinates = new int[2];
    public RectPrism2 exit;
    public int requiredkeys = 3;
    public String notif;
    public boolean displayingNotif;

    public boolean touching;

    


    HashMap<Integer, Block> blocks;
    

    /*
     *    A   B   C
     *    D   E   F
     *    G   H   I
     */

    public World(Player player) {
        this.player = player;
        player.world = this;
        this.collisions = new int[9];
        this.blocks = new HashMap<Integer, Block>();
        
    }

    //Failed Attempt to remove border lines between adjacent prisms. 
    private void rectLineRemoval(RectPrism2 r, int[][] key, int i, int j){
        
        if(i>0&&key[i-1][j]==0){
            System.err.println("Rem1");
            r.Lt.triangles[0].borders[0]=true;
            r.Lt.triangles[1].borders[0]=true;
            // r.Fr.triangles[0].borders[0]=false;
            // r.Bk.triangles[1].borders[0]=false;
        }
        if(i<key.length-1&&key[i+1][j]==0){
            System.err.println("Rem2");
            r.Rt.triangles[0].borders[0]=true;
            r.Rt.triangles[1].borders[0]=true;
            // r.Fr.triangles[1].borders[0]=false;
            // r.Bk.triangles[0].borders[0]=false;
        }
        if(j>0&&key[i][j-1]==0){
            System.err.println("Rem3");
            r.Fr.triangles[0].borders[0]=true;
            r.Fr.triangles[1].borders[0]=true;
            // r.Rt.triangles[1].borders[0]=false;
            // r.Lt.triangles[0].borders[0]=false;
        }
        if(j<key[i].length-1&&key[i][j+1]==0){
            System.err.println("Rem4");
            r.Bk.triangles[0].borders[0]=true;
            r.Bk.triangles[1].borders[0]=true;
            // r.Rt.triangles[0].borders[0]=false;
            // r.Lt.triangles[1].borders[0]=false;
        }
    }

    //generate map from walls. add each prism to the player's graphicspanel
    //improvement would be to store all prisms in world and have graphicspanel instead reference world
    public void makeMap(int[][] key, double height, double nodeSize) {
        this.nodeSize = nodeSize;
        this.key = key;
        for(int i = 0; i < key.length; i++) {
            for(int j = 0; j < key[i].length; j++) {
                if (key[i][j] == 1) {
                    RectPrism2 r = new RectPrism2(new Point(i*nodeSize, j*nodeSize, 0), 0, nodeSize, height, nodeSize);
                    // rectLineRemoval(r, key, i, j);
                    player.graphicsPanel.addRect(r);
                }
                //Creates a key
                if(key[i][j] == 2) {
                    double x = i*nodeSize + (nodeSize/2) - (keySize/2);
                    double y = j*nodeSize + (nodeSize/2) - (keySize/2);

                    RectPrism2 r = new RectPrism2(new Point(x, y, 0), 0, keySize, keySize, keySize,Color.YELLOW);
                    Block b = new Block(r);
                    b.i = i;
                    b.j = j;
                    player.graphicsPanel.addRect(r);
                    blocks.put(i*key[0].length+j, b);
                }
                //creates exit
                if(key[i][j] == 3) {
                    exitcoordinates[0] = i;
                    exitcoordinates[1] = j;
                    RectPrism2 r = new RectPrism2(new Point(i*nodeSize, j*nodeSize, 0), 0, nodeSize, 20, nodeSize, Color.GREEN);
                    this.exit = r;
                    player.graphicsPanel.addRect(r);
                }

                
            }
        }
    }
    //convert world coordinates into indices
    public int getIndex(double x, double y) {
        int i = (int)Math.floor(x / nodeSize);
        int j = (int)Math.floor(y / nodeSize);
        if (i < 0 || i >= key.length) return 0;
        if (j < 0 || j >= key[0].length) return 0;
        return key[i][j];
    }

    public int convertIndex(double d) {
        return (int)Math.floor(d / nodeSize);
    }

    //Checks to see if a player is touching a key
    public void checkTouching() {
        if (collisions[4] != 2) return;
        double x = player.camera.point.x;
        double y = player.camera.point.y;
        int i = this.convertIndex(x);
        int j = this.convertIndex(y);

        double xmin = i*nodeSize + (nodeSize/2) - (keySize/2);
        double xmax = i*nodeSize + (nodeSize/2) + (keySize/2);
        double ymin = j*nodeSize + (nodeSize/2) - (keySize/2);
        double ymax = j*nodeSize + (nodeSize/2) + (keySize/2);

        if(x < xmax && x > xmin && y < ymax && y > ymin) {
            System.out.println("You found a key!");
            notify("You found a key!");
            player.keys += 1;
            key[i][j] = 0;
            Block temp = blocks.get(i*key[0].length+j);
            player.graphicsPanel.removeRect(temp.rect);


            // if (player.keys >= this.requiredkeys) {
            //     key[exitcoordinates[0]][exitcoordinates[1]] = 3;
            //     player.graphicsPanel.removeRect(exit);
            // }
        }
    }

    //#region Notifications
    //Creates tasks for diaplying a notifcation on screen for 3s
    Timer notifTimer = new Timer();

    private TimerTask makeNotifTask(boolean display){

        TimerTask notifShowTask = new TimerTask() {
            @Override
            public void run() {
                displayingNotif=true;
                player.graphicsPanel.notifLabel.setText(notif);
                player.graphicsPanel.notifLabel.setSize(player.graphicsPanel.notifLabel.getPreferredSize());
                player.graphicsPanel.notifLabel.setBounds(player.graphicsPanel.x/2- player.graphicsPanel.notifLabel.getWidth()/2, 50, player.graphicsPanel.notifLabel.getWidth(), player.graphicsPanel.notifLabel.getHeight());


            }
        };
        TimerTask notifHideTask = new TimerTask() {
            @Override
            public void run() {
                notif="";   
                player.graphicsPanel.notifLabel.setText(notif);
                player.graphicsPanel.notifLabel.setSize(player.graphicsPanel.notifLabel.getPreferredSize());
                player.graphicsPanel.notifLabel.setBounds(player.graphicsPanel.x/2- player.graphicsPanel.notifLabel.getWidth()/2, 50, player.graphicsPanel.notifLabel.getWidth(), player.graphicsPanel.notifLabel.getHeight());
                displayingNotif=false;

            }
        };

        if(display) return notifShowTask;
        else return notifHideTask;
    }
    
    //Function to create Notification
    private void notify(String message){
        System.out.println("noty");
        notif=message;
        if(!displayingNotif){
            displayingNotif=true;
            notifTimer.purge();
            notifTimer.schedule(makeNotifTask(true), 0);
            notifTimer.schedule(makeNotifTask(false), 3000);
        }
        
    }
    //#endregion

    //Sees if a player is attempting to exit and prompts them with an error, or sends them to the next level.
    public void checkexit() {
        if(collisions[4] == 3&&touching==false) {
            if(player.keys>=requiredkeys){
                if(main.numLevels<=player.currentLevel+1) {
                    // main.gameWin(player);
                    main.randomLevel(player);
                }
                else{
                    notify("You escaped this Maze!");              
                    main.nextLevel(player);
                }
            }
            else{
                int num=requiredkeys-player.keys;
                if(num==1)notify("You need "+(num)+ " more key!");
                else notify("You need "+(num)+ " more keys!");
                System.out.println(notif);

            }
            touching=true;
        }
        else if (collisions[4]!=3) touching=false;
    }

    //world function to check for collision
    public void updateCollision() {
        double x = collisiondistance*Math.cos(player.camera.theta);
        double y = collisiondistance*Math.sin(player.camera.theta);
        
        double x_p = player.camera.point.x;
        double y_p = player.camera.point.y;

        //front, back
        collisions[1] = this.getIndex(x_p + x, y_p + y);
        collisions[7] = this.getIndex(x_p - x, y_p - y);

        //left, right
        collisions[3] = this.getIndex(x_p + y, y_p - x);
        collisions[5] = this.getIndex(x_p - y, y_p + x);

        //middle
        collisions[4] = this.getIndex(x_p, y_p);

        x = x*Math.sqrt(2)*2;
        y = y*Math.sqrt(2)*2;
        //1 and 3
        collisions[0] = this.getIndex(x_p + x+y, y_p + y-x);
        //1 and 5
        collisions[2] = this.getIndex(x_p + x-y, y_p + y+x);
        //7 and 3
        collisions[6] = this.getIndex(x_p + -x+y, y_p + -y-x);
        //7 and 5
        collisions[8] = this.getIndex(x_p + -x-y, y_p + -y+x);

        //System.out.println(x_p + "  " + y_p + "   " + convertIndex(x_p) + "   " + convertIndex(y_p));
    }

    //Updates the displayed keys on screen
    public void updateKeys(){
        String keyBox="";
        for (int i=0;i<player.keys;i++){
            keyBox+=player.yeskey;
        }
        for (int i=0;i<requiredkeys-player.keys;i++){
            keyBox+=player.nokey;
        }
        player.graphicsPanel.keyLabel.setText("Keys:"+keyBox);
        player.graphicsPanel.keyLabel.setSize(player.graphicsPanel.keyLabel.getPreferredSize());
        player.graphicsPanel.keyLabel.setBounds((int)(player.graphicsPanel.x/2- player.graphicsPanel.keyLabel.getWidth()/2), 10, player.graphicsPanel.keyLabel.getWidth(), player.graphicsPanel.keyLabel.getHeight());
    }
}
