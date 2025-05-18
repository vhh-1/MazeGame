package mazegame;

import java.util.Timer;
import java.util.TimerTask  ;

public class main {

        public static Mazes mazes = new Mazes();
        public static int numLevels=5;
        public static final int rwSize=10;
        public static final int rwKeys=3;
        public static boolean runTimer=false;

    //Resets Variables to start the game
    public static void gameStart(Player player){
        runTimer=true;
        player.timeLimit=300;
        player.currentLevel=(-1);
        System.out.println("PCL"+player.currentLevel);
        nextLevel(player);
    }
    //Function to run when the player loses
    public static void gameOver(Player player){
        player.graphicsPanel.GOL.setText("GAME OVER!");
        player.graphicsPanel.cards.show(player.graphicsPanel.cl, "Game Over");        
    }

    //Function to run when the player wins
    public static void gameWin(Player player){
        player.graphicsPanel.GOL.setText("YOU WON!\nScore:"+(player.currentLevel-numLevels+1));
        player.graphicsPanel.cards.show(player.graphicsPanel.cl, "Game Over");
    }

    //Resets the map/variables and starts the next level. 
    public static void nextLevel(Player player){
        player.currentLevel++;
        System.out.println("CL: "+player.currentLevel);
        player.graphicsPanel.rectprismlist.clear();
        int[] metadata=mazes.getMetadata(player.currentLevel);
        int[][] map=mazes.getMaze(player.currentLevel);
        World world = new World(player);
        world.makeMap(map, 200, 100);
        player.camera.point.x=metadata[0];
        player.camera.point.y=metadata[1];
        world.requiredkeys=metadata[2];
        player.keys=0;
        player.graphicsPanel.refresh();       
    }

    //Generates and Renders a random map
    public static void randomLevel(Player player){
        player.graphicsPanel.scoreLabel.setText("Score:"+(player.currentLevel-numLevels+1));
        player.graphicsPanel.scoreLabel.setSize(player.graphicsPanel.scoreLabel.getPreferredSize());
        player.graphicsPanel.scoreLabel.setBounds(10, 50, player.graphicsPanel.scoreLabel.getWidth(), player.graphicsPanel.scoreLabel.getHeight()); 

        RandomWorldGen rm=new RandomWorldGen(rwSize,rwSize,rwKeys);
        player.currentLevel++;
        System.out.println("CLR: "+player.currentLevel);
        int[] metadata=rm.metadata;
        int[][] map=rm.getMap();
        player.graphicsPanel.rectprismlist.clear();
        // player.graphicsPanel.refresh();

        World world = new World(player);
        world.makeMap(map, 200, 100);
        player.camera.point.x=metadata[0];
        player.camera.point.y=metadata[1];
        world.requiredkeys=metadata[2];
        player.keys=0;
    }

    //Game Loop
    public static void main(String[] args) {

        System.out.println("OH NO!");
        System.out.println("You are stuck in a Maze");
        
        //Initialize and start the game!
        Player player = new Player();
        player.camera = player.new Camera(Player.scale);
        player.graphicsPanel = new GraphicsPanel(player, Player.scale, Player.scale);

         //Windows Compatibilty 
        if(args.length>0){
            if (Integer.valueOf(args[0])==-1){
                player.graphicsPanel.delay=8;
            }
            if (Integer.valueOf(args[0])==-2){
                player.yeskey="V";
                player.nokey="X";  
            }
            if (Integer.valueOf(args[0])==-3){
                player.graphicsPanel.delay=8;
                player.yeskey="V";
                player.nokey="X";  
            }
            //Testing mode can set level 
            else if(player.testing){
                player.currentLevel=Integer.valueOf(args[0])-1;
                if (player.currentLevel+1<numLevels) nextLevel(player);
                else randomLevel(player);
            }
        }
            


/* 
//Testing MAzes
        int[][] maze1 = {
            {1,1,1,1,1,1,1,1},
            {1,0,0,0,1,0,3,1},
            {1,0,1,0,0,0,1,1},
            {1,0,1,1,1,0,1,1},
            {1,0,0,0,1,0,2,1},
            {1,0,1,0,1,1,1,1},
            {1,0,1,0,0,0,2,1},
            {1,1,1,1,1,1,1,1},
        };
        int[][] key = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,},
            
            {1, 0, 0, 2, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,},
             
            {1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1,}
        };
*/

        //Game Timer and FPS
        Timer countdownTimer = new Timer();
        
        TimerTask countdownTask = new TimerTask() {
            
            @Override
            public void run() {
                int mins=player.timeLimit%60;
                String minutes="";
                
                if (mins<10) minutes+="0"+mins;
                else minutes+=+mins;
                player.graphicsPanel.timeLabel.setText("|"+String.valueOf(player.timeLimit/60)+":"+minutes+"|");
                player.graphicsPanel.timeLabel.setSize(player.graphicsPanel.timeLabel.getPreferredSize());
                player.graphicsPanel.timeLabel.setBounds(player.graphicsPanel.x- 2*player.graphicsPanel.timeLabel.getWidth()+20, 10, player.graphicsPanel.timeLabel.getWidth(), player.graphicsPanel.timeLabel.getHeight());

                if (player.timeLimit==0&&runTimer==true) {
                    runTimer=false;
                    if(player.currentLevel>=numLevels) gameWin(player);
                    else gameOver(player);
                }
                if(runTimer==true)player.timeLimit--;
            }
        };
        countdownTimer.scheduleAtFixedRate(countdownTask, 0, 1000);
        
        
        Timer fpsTimer = new Timer();
        TimerTask fpsTask = new TimerTask() {
            @Override
            public void run() {
                // System.out.println(player.graphicsPanel.frames);
                player.graphicsPanel.fpsLabel.setText("FPS: "+String.valueOf(player.graphicsPanel.frames));
                player.graphicsPanel.fpsLabel.setSize(player.graphicsPanel.fpsLabel.getPreferredSize());
                player.graphicsPanel.fpsLabel.setBounds(10, 10, player.graphicsPanel.fpsLabel.getWidth(), player.graphicsPanel.fpsLabel.getHeight()); 

                player.graphicsPanel.frames = 0;
            }
        };
        // Schedule the task to run every 1000 milliseconds (1 second)
        fpsTimer.scheduleAtFixedRate(fpsTask, 0, 1000);
    }

}
     

