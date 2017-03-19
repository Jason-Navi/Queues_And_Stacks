
/**
 * Represents a Circular Queue Class
 * 
 * Jason Navi 
 * 3/4/2017
 */
public class Queue
{
    private Widget[] data;
    private int front; 
    private int rear;
    private int count; 
    private int maxSize; 

    public Queue(){
        front = 0; 
        rear = -1;
        count = 0;
        maxSize = 100;
        data = (Widget[]) new Widget[maxSize];
    }

    public boolean isEmpty(){
        return count == 0;
    }
    
    public void enqueue(Widget x){
        if(count == maxSize){
            maxSize *= 2;
            Widget[] newData = new Widget[maxSize];
            int i = front; 
            for(int j = 0; j < count; j++){
                newData[j] = data[i];
                i = (i + 1) % count;
            }
            data = newData;
            front = 0; 
            rear = count - 1;
        }
        rear = (rear + 1) % maxSize;
        count++;
        data[rear] = x;
    }

    public Widget dequeue(){
        if(isEmpty())
            throw new IllegalArgumentException("The Queue is empty");
        Widget oldFront = data[front];
        front = (front + 1) % maxSize;
        count--; 
        if(count == 0){
            front = 0; 
            rear = -1;
        }
        return oldFront;
    }
}
