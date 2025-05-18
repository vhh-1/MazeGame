package mazegame;

public interface PriorityQueue<T>{
    public abstract int size();
    public abstract boolean isEmpty();

    
    public abstract T poll();
    public abstract T peek();
    public abstract void insert(T element, double priority);
    public abstract void decrease_priority(T element, double priority);



    public abstract String toString();
    public abstract void visualize();

}