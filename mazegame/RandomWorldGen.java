package mazegame;

import java.util.Random;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class RandomWorldGen {

    private static Random r= new Random();
    
    public int[][] map;
    public ArrayList<int[][]> pointMaps;
    public int[][] pathMap;
    public int[][] walls;
    public int[][] points;
    public int[] metadata;
   
    
    private int xmax;
    private int ymax;
    private MazeMaker maze;
    private boolean foundPaths;
    private int keys;

    private final double wallDenisty=1.3;



    RandomWorldGen(int xmax, int ymax, int keys){
        this.xmax=xmax;
        this.ymax=ymax;
        this.keys=keys;
        int[]metadata={150,150,keys};
        this.metadata=metadata;
        generateMap();
    }

    public int[][] getMap(){
        return pathMap;
    }

    //Returns a random integer between two numbers.
    private static int randRange(int min,int max){
        return r.nextInt(max-min+1)+min;
    }

    //Function called to generate Map. 
    private void generateMap(){
        pickKeyPoints();
        initBFS();
        mergePaths();
        convertMap();
        finalizeMap();
    }
    //Helper functions that checks if an Array Contains an element. 
    private static boolean acontains(int[] arr, int val){
        for(int x:arr) if(x==val) return true;
        return false;
    }

    //#region Rand Picking and Init
    //Randomly picks Keys and an exit
    private void pickKeyPoints(){
        points=new int[keys+1][2];
        int[]badX=new int[points.length];
        int[]badY=new int[points.length];
        for(int i=0;i<points.length;i++){
            int x=randRange(2, this.xmax-2);
            while(acontains(badX,x)){
                x=randRange(2, this.xmax-2);
            }
            points[i][0]=x;
            badX[i]=x;
            int y=randRange(2, this.xmax-2);
            while(acontains(badY,y)){
                y=randRange(2, this.xmax-2);
            }
            points[i][1]=y;
            badY[i]=y;
        }
    }

    //Walls scale better with size
    //Picks the Random Walls
    private void createRandWalls(){
        walls=new int[xmax][ymax];
        for(int i=0;i<(int)Math.pow((xmax*ymax),(1/wallDenisty));i++){
            walls[randRange(2, this.xmax-2)][randRange(2, this.ymax-2)]=1;
        }
        for(int[]x:points){
            walls[x[0]][x[1]]=0;
        }

    }

    //Adds the Random Walls
    private void addRandWalls(int[][] needWalls){
        int c1=0;
        for(int[] x:needWalls){
            int c2=0;
            for(int y:x){
                if(y==0&&walls[c1][c2]==1) needWalls[c1][c2]=9;
                // else System.out.println("BLOXK");
                c2++;
            }
            c1++;
        }
    }  
   
    //Create a U shape around each key in a random orientation. 
    private void addKeyBlocks(int[][]pointMap){
        for(int[]x:points){
            addKeyBlocksHelper(x,pointMap,randRange(0, 3));
            }
        }
    
    private void addKeyBlocksHelper(int[] x,int[][]pointMap,int num){
        if (num==0&&pointMap[x[0]-1][x[1]]!=1){
            pointMap[x[0]+1][x[1]]=9;
            if(x[1]>=1){
                pointMap[x[0]+1][x[1]-1]=9;
                pointMap[x[0]][x[1]-1]=9;
            }
            if(x[1]<=xmax){
                pointMap[x[0]+1][x[1]+1]=9;
                pointMap[x[0]][x[1]+1]=9;
            }
            
        }
        else if (num==2&&pointMap[x[0]+1][x[1]]!=1){
            pointMap[x[0]-1][x[1]]=9;
            if(x[1]>=1){
                pointMap[x[0]-1][x[1]-1]=9;
                pointMap[x[0]][x[1]-1]=9;
            }
            if(x[1]<=xmax){
                pointMap[x[0]-1][x[1]+1]=9;
                pointMap[x[0]][x[1]+1]=9;
            }
            
        }
        else if (num==1&&pointMap[x[0]][x[1]+1]!=1){
            pointMap[x[0]+1][x[1]]=9;
            if(x[0]>=1){
                pointMap[x[0]-1][x[1]+1]=9;
                pointMap[x[0]-1][x[1]]=9;
            }
            if(x[0]<=ymax){
                pointMap[x[0]+1][x[1]+1]=9;
                pointMap[x[0]+1][x[1]]=9;
            }
            
        }
        else if (num==3&&pointMap[x[0]][x[1]-1]!=1){
            pointMap[x[0]-1][x[1]]=9;
            if(x[0]>=1){
                pointMap[x[0]-1][x[1]-1]=9;
                pointMap[x[0]-1][x[1]]=9;
            }
            if(x[0]<=ymax){
                pointMap[x[0]+1][x[1]-1]=9;
                pointMap[x[0]+1][x[1]]=9;
            }
            
        }
        else addKeyBlocksHelper(x,pointMap,randRange(0, 3));


    }
    //#endregion
    //Old Function that would use Maze Maker Class
     private void makeBlankMaze(){
        maze=new MazeMaker(xmax, ymax);
        maze.border(xmax-1, ymax-1);
        this.map=maze.makeMaze();
        maze.addExit(points[0][0],points[0][1]);
        maze.addKey(points[1][0],points[1][1]);
        maze.addKey(points[2][0],points[2][1]);
        maze.addKey(points[3][0],points[3][1]);
        // maze.addDummy(points[4][0],points[4][1]);
        // maze.addDummy(points[5][0],points[5][1]);
        // return map;
    }
    //#region DFS 
//No longer used due to poor path shapes. 
    private void startDFS(){
        this.pointMaps=new ArrayList<int[][]>();
        for(int[]x:points){
            int[][] pointMap=new int[xmax][ymax];
            addRandWalls(pointMap);
            dfs(1,1,x[0],x[1],pointMap);
            pointMaps.add(pointMap);
        }        
    }
    private int dfs(int x, int y,int tx, int ty, int[][]pointMap){
        //Add Fail detection
        pointMap[x][y]=2;
        if(x==tx&&y==ty) return 1;
        else{
            if(x+2<this.xmax&&pointMap[x+1][y]==0){
                if(dfs(x+1,y,tx,ty,pointMap)==1)  return 1;
                else pointMap[x+1][y]=1;
            }
            if(y+2<this.ymax&&pointMap[x][y+1]==0){
                if (dfs(x,y+1,tx,ty,pointMap)==1) return 1;
                else pointMap[x][y+1]=1;
            }
            if(y>1&&pointMap[x][y-1]==0){
                if (dfs(x,y-1,tx,ty,pointMap)==1) return 1;
                else pointMap[x][y-1]=1;
            } 
            if(x>1&&pointMap[x-1][y]==0){
                if(dfs(x-1,y,tx,ty,pointMap)==1)  return 1;
                else pointMap[x-1][y]=1;
            }
            
        }
        return 0;
    }
//#endregion
    //#region BFS
    //BFS node that contains coorinates and the path to get there
    private class BFSNode{
        public int x;
        public int y;
        public int[][] path;

        BFSNode(int x,int y, int[][]path){
            this.x=x;
            this.y=y;
            this.path=path;
        }
    }

    //Starts the BFS process. Used to avoid recursion and stack Overflow
    private void initBFS(){
        boolean worked=false;
        this.pointMaps=new ArrayList<int[][]>();
        int counter=-1;
        while(!worked){
            counter++;
            pointMaps.clear();
            createRandWalls();
            worked=startBFS();
        }
        System.out.println("Failed "+counter+" times.");
        
    }

    //Sets Up Varaibles, and starts BFS for each key/exit
    private boolean startBFS(){
        foundPaths=true;
        for(int[]x:points){
            int[][] pointMap=new int[xmax][ymax];
            addRandWalls(pointMap);
            addKeyBlocks(pointMap);
            Queue<BFSNode> q=new LinkedList<BFSNode>();
            q.add(new BFSNode(1,1,new int[][]{{1,1}}));
            int[][]path=bfs(x[0],x[1],q,pointMap);
            if (path==null) {
                // System.out.println("FAIL");
                foundPaths=false;
                return foundPaths;        

            }
            // else for(int[]p:path) System.out.println(Arrays.toString(p));
            else pointMaps.add(convertBFSPath(path,pointMap));
            
        }
        
        return foundPaths;        
    }

    //Runs the BFS, keeping track of path
    private int[][] bfs(int tx, int ty, Queue<BFSNode> q, int[][]pointMap){
        //Add Fail detection
        while(!q.isEmpty()){
            BFSNode v=q.poll();
            pointMap[v.x][v.y]=1;
            // System.out.println("Current:");
            // for(int[]p:v.path) System.out.println(Arrays.toString(p));
            if(v.x==tx&&v.y==ty){
                return v.path;
            }
            //This Part checks to see where it hasnt explored, and updates the path in that direction. 
            else{            
                if(v.x+2<this.xmax&&pointMap[v.x+1][v.y]==0){
                    int[][] newPath=new int[v.path.length+1][2];
                    for(int i=0;i<v.path.length;i++) newPath[i]=v.path[i];
                    newPath[newPath.length-1]=new int[]{v.x+1,v.y};                                      
                    q.add(new BFSNode(v.x+1,v.y,newPath)); 
                }
                if(v.y+2<this.ymax&&pointMap[v.x][v.y+1]==0){
                    int[][] newPath=new int[v.path.length+1][2];
                    for(int i=0;i<v.path.length;i++) newPath[i]=v.path[i];
                    newPath[newPath.length-1]=new int[]{v.x,v.y+1};                                      
                    q.add(new BFSNode(v.x,v.y+1,newPath)); 
                }
                if(v.y>1&&pointMap[v.x][v.y-1]==0){
                    int[][] newPath=new int[v.path.length+1][2];
                    for(int i=0;i<v.path.length;i++) newPath[i]=v.path[i];
                    newPath[newPath.length-1]=new int[]{v.x-1,v.y};                                      
                    q.add(new BFSNode(v.x-1,v.y,newPath)); 
                } 
                if(v.x>1&&pointMap[v.x-1][v.y]==0){
                    int[][] newPath=new int[v.path.length+1][2];
                    for(int i=0;i<v.path.length;i++) newPath[i]=v.path[i];
                    newPath[newPath.length-1]=new int[]{v.x,v.y};                                      
                    q.add(new BFSNode(v.x,v.y-1,newPath));             
                }
                                          
            }
        
        }
        return null;
        
    }
    
    //Turns the returned path, into the actual path on the array
    private int[][] convertBFSPath(int[][] bfspath,int[][] map){
        for(int[]x:bfspath) map[x[0]][x[1]]=2;
        return map;
    }
    //#endregion
    //#region Finalize Paths
    //Merges the individual BFS paths into one array
    private void mergePaths(){
        pathMap=new int[xmax][ymax];
        for(int[][] x:pointMaps){
            int c1=0;
            for(int[] y:x){
                int c2=0;
                for(int z:y){
                    if(x[c1][c2]==2) pathMap[c1][c2]=1;
                    else if(x[c1][c2]==9&&pathMap[c1][c2]!=1&&pathMap[c1][c2]!=2) pathMap[c1][c2]=9;
                    c2++;
                }
                c1++;
            }
        }
    }
    
    //Converts map from numbers from this appliaction, to the numbers used for the renderer
    private void convertMap(){
        for(int x=0;x<pathMap.length;x++){
            for(int y=0;y<pathMap[x].length;y++){
                if(pathMap[x][y]==1) pathMap[x][y]=0;
                else if(pathMap[x][y]==9) pathMap[x][y]=1;
                else if(x==0||y==0||x==xmax-1||y==ymax-1)pathMap[x][y]=1;
                else if (pathMap[x][y]==0)pathMap[x][y]=8;
            }
        } 
        for(int i=0;i<points.length;i++){
            if (i==0) pathMap[points[i][0]][points[i][1]]=3;
            //CHANGE
            if (1<=i&&i<=keys) pathMap[points[i][0]][points[i][1]]=2;
        }
        
    }

    //Finshes the above step(this is here for future expansion with this specific set of walls/path, as they can be either or, without affecting the map)
    private void finalizeMap(){
        for(int x=0;x<pathMap.length;x++){
            for(int y=0;y<pathMap[x].length;y++){
                // if(pathMap[x][y]==1) pathMap[x][y]=0;
                // else if(pathMap[x][y]==9) pathMap[x][y]=1;
                // else if(x==0||y==0||x==xmax-1||y==ymax-1)pathMap[x][y]=1;
                // else 
                if (pathMap[x][y]==8)pathMap[x][y]=0;
            }
        } 

    }
    //#endregion

    public static void main(String[] args) {
        RandomWorldGen r=new RandomWorldGen(10,10,3);
        // for(int[] x:r.map){
        //     System.out.println(Arrays.toString(x));
        // }
        
        // for(int[][] x:r.pointMaps){
        //     System.out.println("----------------");
        //     for(int[] y:x){
        //         System.out.println(Arrays.toString(y));

        //     }
        // }
        System.out.println("----------------------------------");

        for(int[] y:r.pathMap){
                System.out.println(Arrays.toString(y));

            }


    }
}
    /*
    Idea for RMG:
    1. Pick keys and exits
    2. create border
    
    Method 1:
    3. Store current Maze
    4. Randomly genreate some Walls
    5. Prfomrs BFS to keys and exit to ensure a path still exsits. If not revert to old save an try again
    6. Stop after 5 fails in a row. 

    Method 2;
    3. Create 1 or 2 "dummy Points"
    4. DFS to each point, mark path
    5. Adjust each path adding squggles, conenctions to other paths, etc.
    6. make everything else a wall

    7. process and return array. 
    
    */    

