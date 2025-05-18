package mazegame;
import java.util.*;

public class MazeMaker {

    public int xmax;
    public int ymax;
    public int[][] arr;

    private ArrayList<Vertex> rootList;

    //Vertex sub class(ends of a wall)
    private class Vertex{
        public int x;
        public int y;

        public Vertex[] vList;
        public Vertex parent;

        Vertex(int x, int y){
            this.x=x;
            this.y=y;
            this.vList=new Vertex[4];
        }

        @Override
        public String toString(){
            return "("+x+","+y+")";
        }
    }

    

    MazeMaker(int xmax,int ymax){
        this.xmax=xmax;
        this.ymax=ymax;
        rootList=new ArrayList<Vertex>();
    }
    
    //Creates a vertices, and a link beween two indecies/map coords
    public void addWall(int x1, int y1, int x2, int y2){
        if(x1>this.xmax||x2>this.xmax||y1>this.ymax||y2>this.ymax) throw new IndexOutOfBoundsException("Points must be within map Bounds"); 
        
        int orientation;
        if(x1==x2&&y1==y2)throw new IndexOutOfBoundsException("Points must be not be the same"); 
        if(x1>x2&&y1==y2) orientation=2;
        else if(x1<x2&&y1==y2) orientation=0;
        else if(y1>y2&&x1==x2) orientation=3;
        else if(y1<y2&&x1==x2) orientation=1;
        else throw new IndexOutOfBoundsException("Points must be aligned to an axis"); 
        

        Vertex v= new Vertex(x1,y1);
        Vertex w= new Vertex(x2,y2);
        w.parent=v;
        Vertex s=search(v.x,v.y);
        if(s!=null){
            // System.err.println("a1");
            // System.err.println("else "+v.toString());
            s.vList[orientation]=w;
            // w.vList[(orientation+2)%4]=s;
            w.parent=v;
            // System.out.println("FOUND");
        }
        else{
            // System.err.println("a2");
            v.vList[orientation]=w;
            // w.vList[(orientation+2)%4]=v;
            rootList.add(v);
            // w.parent=v;
            // System.out.println("ROOT");
        }

    }

    //Adds key(2) at specific map coords
    public void addKey(int x, int y){
        arr[x][y]=2;
    }

    //Adds exit(3) at specific map coords
    public void addExit(int x, int y){
        arr[x][y]=3;
    }

    //Add DUmmy(4) for rand maze alg
    public void addDummy(int x, int y){
        arr[x][y]=4;
    }

    //branches through list of vertex to see if a given vertex exsists. 
    public Vertex search(int x, int y){
        for(Vertex v: rootList){
            Vertex s= searchHelper(x, y,v);
            if(s!=null) return s;
        }
        return null;
    }

    private Vertex searchHelper(int x, int y, Vertex v){
        if(v.x==x&&v.y==y) return v;
        for(Vertex z:v.vList){
            if(z!=null&&v.parent!=z){
                if(z.x==x&&z.y==y) return z;
                else return searchHelper(x,y,z);
            }
        }
        return null;
    }

    //Takes every vertex, and draws a wall in the array(line of ones) between every vertex, and its linked vertecies. 
    public int[][] makeMaze(){
        arr=new int[xmax][ymax];
        for(Vertex v: rootList){
            draw(v);
        }
        return arr;
    }

    //Draws the walls recursivly from a vertex to all its children. 
    private void draw(Vertex v){
        // System.err.println("Drawing Vertex at"+v.toString());
        for(int i=0;i<4;i++){
            if(v.vList[i]!=null) {
                // System.err.println("tESTING Vertex at"+v.vList[i].toString()+"o: "+i+"P: "+v.vList[i].parent);
                if(i==0){
                    // System.err.println(":"+v.x+v.y+v.vList[i].x);
                    for(int j=v.x;j<=v.vList[i].x;j++){
                        arr[j][v.y]=1;
                    }
                }
                else if(i==2){
                    for(int j=v.x;j>v.vList[i].x;j--){
                        arr[j][v.y]=1;
                    }
                }
                else if(i==1){
                    for(int j=v.y;j<=v.vList[i].y;j++){
                        arr[v.x][j]=1;
                    }
                }
                else if(i==3){
                    for(int j=v.y;j>v.vList[i].y;j--){
                        arr[v.x][j]=1;
                    }
                }
                // if (v.vList[i]!=v.parent)  
                draw(v.vList[i]);
            }

            }
        }
    
    //Prints every non 0 map coordinate pair(used for testing)
    public void testgrid(){
        for(int i=0;i<this.xmax;i++){
            for(int j=0;j<this.ymax;j++){
                if(arr[i][j]>=1) System.out.println(i+","+j);
            }
        }
        for(Vertex v:rootList){
            System.err.println("root: "+v.toString());
        }
    }

    //Creates a square between 0,0 and a given set of coords
    //Usually used to create the border around the walls
    public void border(int xmax, int ymax){
        this.addWall(0,0,xmax,0);
        this.addWall(xmax,0,xmax,ymax);
        this.addWall(xmax,ymax,0,ymax);
        this.addWall(0,ymax,0,0);
    }

    //Takes an int array of points, and creates a linkage of walls between them(used to make creating maps easier)
    public void parseArray(int[] varr){
        for(int i=0;i<=varr.length-4;i+=2){
            this.addWall(varr[i], varr[i+1], varr[i+2], varr[i+3]);
        }
    }

    //Test
    public static void main(String[] args){
        MazeMaker m=new MazeMaker(20,20);
        m.addWall(0,0,0,2);
        m.addWall(0,2,2,2);
        System.err.println(m.rootList.get(0).vList.toString());
        m.addWall(0,0,2,0);
        for (int i=0;i<4;i++){
            if(m.rootList.get(0).vList[i]!=null) System.out.println("i: "+i);
        }
        m.makeMaze();
        m.testgrid();
        

        
    }
    
}

