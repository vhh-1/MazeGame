package mazegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


// import java.util.List;

public class GraphicsPanel {

    //#region Variables
    public CardLayout cards;
    public JPanel cl;
    public JFrame frame;
    public GraphicsCanvas canvas;
    // private List<int[][]> triangles;
    private BinaryHeap<Rectangle> heap;
    private BinaryHeap<Triangle> triHeap;

    public int x;
    public int y;
    public int frames=0;
    private boolean multiStart=false;

    public Player player;
    public ArrayList<Triangle> trianglelist;
    public ArrayList<RectPrism2> rectprismlist;
    public ArrayList<Polygon> renderTri;
    public ArrayList<JLabel> labelList;

    
    public JLabel fpsLabel;
    public JLabel timeLabel;
    public JLabel keyLabel;
    public JLabel notifLabel;
    public JLabel scoreLabel;
    public JPanel gameOverScreen;
    public JLabel GOL;

    public JPanel gameStartScreen;
    public JLabel title;
    public JTextArea Ita;
    public JLabel IL1;
    public JLabel IL2;
    public JLabel IL3;
    public JButton startGame;

    public int delay=16;


    //#endregion
    

    public GraphicsPanel(Player player, double x, double y) {
        //#region Init Vars
        this.x = (int)x;
        this.y = (int)y;
        cards=new CardLayout();
        cl=new JPanel(cards);

        this.player = player;
        frame = new JFrame("Maze Game");
        frame.setContentPane(cl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(this.x, this.y);
        frame.setLocationRelativeTo(null); // Center the frame
        

        rectprismlist = new ArrayList<>();
        heap = new BinaryHeap<Rectangle>();
        triHeap = new BinaryHeap<Triangle>();
        renderTri=new ArrayList<Polygon>();
        labelList=new ArrayList<JLabel>();
        this.trianglelist = new ArrayList<Triangle>();
        //#endregion

        // Create the canvas and add it to the frame
        canvas = new GraphicsCanvas();
        canvas.setLayout(null);

        //#region Main Labels
        //Create and init Labels, set font and position. 
        fpsLabel=new JLabel("");
        timeLabel=new JLabel("");
        keyLabel=new JLabel("");
        notifLabel=new JLabel("");
        scoreLabel=new JLabel("");
        
        labelList.add(fpsLabel);
        labelList.add(timeLabel);
        labelList.add(keyLabel);
        labelList.add(notifLabel);
        labelList.add(scoreLabel);
        for(JLabel l:labelList){
            l.setFont(new Font("Calibri", Font.BOLD,32));
            l.setOpaque(false);
            canvas.add(l);
        }
        fpsLabel.setFont(new Font("Calibri", Font.BOLD,20));

        fpsLabel.setSize(fpsLabel.getPreferredSize());
        scoreLabel.setSize(scoreLabel.getPreferredSize());
        timeLabel.setSize(timeLabel.getPreferredSize());
        keyLabel.setSize(keyLabel.getPreferredSize());
        notifLabel.setSize(notifLabel.getPreferredSize());

        fpsLabel.setBounds(10, 10, fpsLabel.getWidth(), fpsLabel.getHeight()); 
        scoreLabel.setBounds(10, 50, scoreLabel.getWidth(), scoreLabel.getHeight()); 
        timeLabel.setBounds(this.x- timeLabel.getWidth(), 10, timeLabel.getWidth(), timeLabel.getHeight());
        keyLabel.setBounds(this.x/2- (keyLabel.getWidth())/2, 10, keyLabel.getWidth(), keyLabel.getHeight());
        notifLabel.setBounds(this.x/2- notifLabel.getWidth()/2, 50, notifLabel.getWidth(), notifLabel.getHeight());
        
        //#endregion

        

        //#region Game Over/Win Screen
        gameOverScreen=new JPanel();
        gameOverScreen.setLayout(new BorderLayout());
        
        GOL=new JLabel("GAME OVER!");
        GOL.setHorizontalAlignment(JLabel.CENTER);
        GOL.setFont(new Font("Calibri", Font.BOLD,56));

        //Play Again Button
        JButton playAgain=new JButton("Play Again?");
        playAgain.setSize(new Dimension(400,200));
        playAgain.setFont(new Font("Calibri", Font.BOLD,56));

        playAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent f){
                player.clearInputs();
                cards.show(cl, "Game Start");
            }
        });

        gameOverScreen.add(GOL,BorderLayout.PAGE_START);
        gameOverScreen.add(playAgain,BorderLayout.CENTER );
        //#endregion


        //#region Game Start Screen
        //Panel
        gameStartScreen=new JPanel();
        gameStartScreen.setLayout(new BorderLayout());
        //Title
        title=new JLabel("MAZE GAME");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD,96));

        //Instructions
        Ita=new JTextArea(
            "Oh No! you are trapped in a maze! Collect all the yellow keys, and find the green exit of all four mazes before the timer runs out!\n\n"
            +"If you can escape with time remaining, test your aMAZEing skills and complete as many random mazes you can with the time remaining!\n\n"
            +"Use WASD to move and arrows to look around. Run into the yellow keys to collect them, and run into the green exit to escape!");
        Ita.setWrapStyleWord(true);
        Ita.setRows(8);
        Ita.setLineWrap(true);
        Ita.setFont(new Font("Calibri", Font.BOLD,32));
        
        // Start Button
        startGame=new JButton("Start!");
        startGame.setPreferredSize(new Dimension(200,100));
        startGame.setFont(new Font("Calibri", Font.BOLD,56));

        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent f){
                main.gameStart(player);
                cards.show(cl, "Canvas");
                SwingUtilities.invokeLater(()->{
                    if(!multiStart)canvas.addKeyListener(player.inputhandler);
                    canvas.setFocusable(true); // Make the canvas focusable
                    canvas.requestFocusInWindow();
                    multiStart=true;
                });
                if(!multiStart)new javax.swing.Timer(delay, e -> refresh()).start();
            }
        });

        //Add to screen
        gameStartScreen.add(title,BorderLayout.PAGE_START);
        gameStartScreen.add(Ita,BorderLayout.CENTER );
        gameStartScreen.add(startGame,BorderLayout.PAGE_END);



        cl.add(canvas, "Canvas");
        //#endregion

        frame.setVisible(true);    
        frame.add(gameStartScreen,"Game Start");
        frame.add(gameOverScreen,"Game Over");

        cards.show(cl,"Game Start");

    }

    //#region Functions
    //The following functions add objects to lists, and are other graphics manegment Functions
    public void addTriangleObject(Triangle tri) {
        this.trianglelist.add(tri);
    }

    public void addRect(RectPrism2 rectP) {
        this.rectprismlist.add(rectP);
        heapRect(rectP);
    }
    public void removeRect(RectPrism2 rectP) {
        this.rectprismlist.remove(rectP);
    }

    //Adds a prism to the heap, with priority of distnace to player
    private void heapRect(RectPrism2 rectP){
        for(Rectangle x:rectP.rectangles){
            if(x.visible) heap.insert(x,player.camera.point.distTo(x.mid));
            
        }
    }
    public void addTri(int[][] data) {
        //triangles.add(data);  // Add the new triangle data to the list
        //refresh();  // Refresh the panel to redraw everything
    }

    public void renderRectPrisms() {
        updaterectprisms();
        while(!heap.isEmpty()) {
            Rectangle x = heap.poll();
            for(Triangle y:x.triangles){
                this.addTriangleObject(y);
            }
        }
    }

    public void updaterectprisms() {
        for(RectPrism2 r: this.rectprismlist) {
            r.UpdateVisibility(player.camera);
            heapRect(r);
        }
    }

    //Called every frame to clear lsits, check collions, exists, key presses, movement, and draw new frame.
    public void refresh() {
        this.trianglelist.clear();//Empty list of triangles. 
        this.player.world.checkTouching();
        this.player.world.updateCollision();
        this.player.world.checkexit();
        this.player.world.updateKeys();
        this.player.handleInput();
        renderRectPrisms();
        frame.repaint();  // Redraw the canvas
        frames++;
    }

    //#region Rendering and Occlusion Culling
    public class GraphicsCanvas extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            //Clear known triangle to draw
            renderTri.clear();          
            double counter=1;

            for(Triangle tri : trianglelist) {
                if (!tri.visible) {
                    continue;
                }
                // World coords to screen coords
                int[][] data = player.camera.convertTri(tri);
                if(data == null) {
                    continue;
                }
                int[] xPoints = data[0];
                int[] yPoints = data[1];

                Polygon poly = new Polygon(xPoints, yPoints, xPoints.length);
                boolean draw=true;

                //For each point in every triangle that we want to draw, check it against the list of triangle we have approved to draw. 
                //If all 3 points are in triangles we are already drawing, we wont draw it. 
                for(Polygon x:renderTri){
                    for(int i=0;i<poly.npoints;i++) {
                        if(!x.contains(poly.xpoints[i],poly.ypoints[i])){
                            draw=true;
                            break;
                        }
                        else{
                            draw=false;
                        }
                    }                   
                }

                // if we are drawing add it in PriorityQueue that will be mirrored so furthest elements that we want to draw, will be drawn first. 
                if(draw){
                    triHeap.insert(tri,100/counter);
                    counter++;  
                    renderTri.add(poly);        
                }
            }
            // Draw each traignle
            while(!triHeap.isEmpty()){
                // System.out.println("draw");
                Triangle tri=triHeap.poll();
                if (!tri.visible) {
                    continue;
                }
                // World coords to screen coords
                int[][] data = player.camera.convertTri(tri);
                if(data == null) {
                    continue;
                }
                int[] xPoints = data[0];
                int[] yPoints = data[1];

                Polygon poly = new Polygon(xPoints, yPoints, xPoints.length);
                g.setColor(tri.color);
                g.fillPolygon(poly);
                g.setColor(Color.BLACK);
                for(int i = 0; i < 3; i++) {
                    
                    if (tri.borders[i]) {
                    //     if(i==0)g.setColor(Color.BLACK);
                    // if(i==1)g.setColor(Color.BLUE);
                    // if(i==2) g.setColor(Color.GREEN);
                    // else g.setColor(Color.ORANGE);
                        g.drawLine(xPoints[i], yPoints[i], xPoints[(i+1)%3], yPoints[(i+1)%3]);
                    }
                }
            }
        }
    }
    //#endregion
}
