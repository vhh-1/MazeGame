package mazegame;

// import javafx.scene.shape.Polygon;

public class Block{
    public RectPrism2 rect;
    public Rectangle collisionbox;

    public int i;
    public int j;
    
    public Block(RectPrism2 rect) {
        this.rect = rect;
        this.collisionbox = rect.Bt;

    }

    //using triangle check function.
    
}
