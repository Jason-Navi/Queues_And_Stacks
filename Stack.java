
/**
 * Represents an ArrayStack class that will hold Widgets.
 * 
 * Jason Navi 
 * 3/3/2017
 */
public class Stack
{
    private Widget[] data;
    private int top;
    private int maxSize;
    
    public Stack(){
        top = -1;
        maxSize = 100;
        data = (Widget[]) new Widget[maxSize];
    }
    
    public boolean isEmpty(){
        return top == -1;
    }
    
    public Widget peek(){
        if(isEmpty())
            throw new IllegalArgumentException("There are no more shipments of the product, sorry!");
        else return data[top];
    }
    
    public Widget pop(){
        if(isEmpty())
            throw new IllegalArgumentException("Stack is empty and does not have any more product to pop off");
        return data[top--];
    }
    
    public void push(Widget x){
        if(top == maxSize - 1){
            maxSize *= 2;
            Widget[] newStack = new Widget[maxSize];
            for(int i = 0; i <= top; i++)
                newStack[i] = data[i];
            data = newStack;
        }
        data[++top] = x;
    }
}
