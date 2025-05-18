package mazegame;

import java.util.ArrayList;
//Binary heap 
public class BinaryHeap<T> implements PriorityQueue<T>{
    private int size;
    Node root;

    // Arraylist for storing data
    public ArrayList<Node> heap;

    //Node holds item and priority
    public class Node {
        T item;
        double priority;

        Node(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }
    }

    public BinaryHeap() {
        this.size = 1; //0 is alwways null

        this.heap = new ArrayList<Node>();
        this.heap.add(null);
    }


    @Override
    public int size() {
        return this.size;
    }
    @Override
    public boolean isEmpty() {
        return this.size == 1;//0 is always null
    }


    //Creates a new node, adds it to the end, then swims up
    @Override
    public void insert(T item, double priority) {
        Node newNode = new Node(item, priority);
        heap.add(newNode);
        this.size++;
        sortup(this.size - 1); 
        
    }

    //Keeps swaping the bottom node with the node above it, until it satisfies heap order
    private void sortup(int index) {
        while (index > 1) {
            int parentIndex = (index) / 2;
            Node current = heap.get(index);
            Node parent = heap.get(parentIndex);
    
            if (current.priority >= parent.priority) {
                break;
            }
    
            // Swap
            heap.set(index, parent);
            heap.set(parentIndex, current);
            index = parentIndex;
        }
    }

    //Keeps swaping the top node with the smallest node below it, until it satisfies heap order
    private void sortdown(int index) {
        while (index*2 < heap.size()) {
            int left=index*2;
            int right=left+1;
            int small=left;
    
            if (right<size&&heap.get(left).priority > heap.get(right).priority) {
                small=right;
            }

            Node current=heap.get(index);
            Node child=heap.get(small);

            if(current.priority<=child.priority) break;
    
            // Swap Nodes
            heap.set(index, child);
            heap.set(small, current);
            index = small;
        }
    }
    
    //Gets/returns the smallest node from the top, sets the last node to the top, then sinks down to reorder. 
    @Override
    public T poll() {
        Node min=heap.get(1);
        heap.set(1,heap.get(heap.size()-1));
        heap.remove(heap.size()-1);
        this.size--;
        sortdown(1);
        return min.item;

    }
    @Override
    public T peek() {
        return heap.get(1).item;
    }
    @Override
    public void decrease_priority(T element, double newpriority) {
        //Allows us to change a priorty. Unsed as of yet
        for (int i = 0; i < size; i++) {
            if (heap.get(i).item.equals(element)) {
                Node node = heap.get(i);
                if (newpriority < node.priority) {
                    node.priority = newpriority;
                    sortup(i);
                }
                return;
            }
        }
        throw new IllegalArgumentException("Element not found in heap.");
    }
    

    

    @Override
    public String toString() {
        String str="";
        for(int i=1;i<heap.size();i++){
            str+=heap.get(i).item.toString();
        }
        return str;
    }

    @Override
    public void visualize() {

    }

    //Simple test
    public static void main(String[] args){
        BinaryHeap<Integer> bh=new BinaryHeap<Integer>();
        bh.insert(1,1.2);
        System.err.println("L: "+bh.toString());
        bh.insert(3,3);
        System.err.println("L: "+bh.toString());
        bh.insert(2,2);
        System.err.println("L: "+bh.toString());
        bh.insert(5,2);
        System.err.println("L: "+bh.toString());
        bh.insert(58,400);
        System.err.println("L: "+bh.toString());
        
        while(!bh.isEmpty()){
            System.out.println(bh.poll());
        }


    }  
}



