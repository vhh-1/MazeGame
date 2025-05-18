package mazegame;

public class Mazes {

    public MazeMaker maze;

    //Store metadata in mazes(number of keys, spawn coords, etc.)
    private int[][] metadata={{250,350,1},{150,650,2},{550,750,2},{150,1450,3},{150,150,0}};

    //Get Maze Metadata
    public int[] getMetadata(int num){
        if(metadata[num]!=null) return metadata[num];
        else throw new IndexOutOfBoundsException("Maze does not exsist");
    }

    //Get and make the maze key
    public int[][] getMaze(int num){
        if (num==0) return maze0();
        else if (num==1) return maze1();
        else if (num==2) return maze2();
        else if (num==3) return maze3();
        else if (num==4) return maze4();
        else throw new IndexOutOfBoundsException("Maze does not exsist");
    }
    //#region Mazes
    //The following are all the individual mazes
    private int[][] maze0(){
        MazeMaker m=new MazeMaker(5,5);
        m.border(4,4);
        m.addWall(2, 0, 2, 2);
        int[][] map=m.makeMaze();
        m.addKey(1,1);
        m.addExit(3, 1);
        // m.testgr();
        
        return map;
        
    }
    private int[][] maze1(){
        MazeMaker m=new MazeMaker(8,8);
        m.border(7,7);
        int[]vertList1={2,2,2,3,4,3,4,5,7,5};
        m.parseArray(vertList1);
        m.addWall(4,0,4,1);
        m.addWall(2,7,2,5);
        m.addWall(6,2,6,3);
        int[][] map=m.makeMaze();
        m.addKey(6, 6);
        m.addKey(6, 4);
        m.addExit(6,1);
        return map;
    }
    private int[][] maze2(){
        MazeMaker m=new MazeMaker(12,12);
        m.border(11,11);
        int[]vertList1={8,11,8,4,5,4,5,2};
        int[]vertList2={3,9,4,9,4,10};
        m.parseArray(vertList1);
        m.parseArray(vertList2);
        m.addWall(2,2,9,2);
        m.addWall(0,4,3,4);
        m.addWall(0,6,4,6);
        m.addWall(0,10,1,10);
        m.addWall(10,4,11,4);
        m.addWall(10,8,11,8);
        m.addWall(8,6,9,6);
        m.addWall(8,10,9,10);
        m.addWall(6,6,6,9);
        m.addWall(2,8,6,8);
        int[][] map=m.makeMaze();
        m.addKey(3,10);
        m.addKey(10,10);
        m.addExit(5,9);
        
        return map;
    }
    private int[][] maze3(){
        MazeMaker m=new MazeMaker(16,16);
        m.border(15,15);
        //Big wall chunks
        int[] vertList1={2,15,2,11,7,11,7,10,7,7,10,7,11,7};
        int[] vertList2={7,11,7,13,4,13};
        int[] vertList3={2,9,5,9,5,5,15,5};
        int[] vertList4={9,12,9,13,15,13};
        int[] vertList5={10,10,11,10,11,11,13,11,13,7,15,7};
        int[] vertList6={10,0,10,3,13,3};
        int[] vertList7={0,7,2,7,2,6,3,6,3,2,5,2,5,3,7,3,7,2,8,2};
        m.parseArray(vertList1);
        m.parseArray(vertList2);
        m.parseArray(vertList3);
        m.parseArray(vertList4);
        m.parseArray(vertList5);
        m.parseArray(vertList6);
        m.parseArray(vertList7);
        //Small wall protrusions
        m.addWall(7,10,8,10);
        m.addWall(10,7,10,8);
        m.addWall(12,1,13,1);
        m.addWall(0,4,1,4);
        m.addWall(3,2,2,2);

        int[][] map=m.makeMaze();
        m.addKey(6, 12);
        m.addKey(1, 6);
        m.addKey(11, 1);
        m.addExit(14,8);
        // m.testgrid();
        return map;
        
    }
    private int[][] maze4(){
        MazeMaker m=new MazeMaker(3,5);
        m.border(2,4);
        int[][] map=m.makeMaze();
        m.addExit(1, 3);
        // m.testgrid();
        
        return map;
        
    }
    //#endregion
}
