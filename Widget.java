
/**
 * Represents a Widget. 
 * Contains the amount of a specific widget and it's cost. 
 * Queue and Stack class will create objects of the widgets to stor
 * 
 * Jason Navi
 * 3/10/17
 */
public class Widget
{
    //represents the number of a specific widget, it's cost for the books, and the sellingCost 
    int numberWidgets;
    double cost, currentCost;

    //2 parameter constructor initializes numberWidgets and cost to their respective values
    public Widget(int numWidgets, double price){
        numberWidgets = numWidgets;
        cost = price;
    }

    //3 parameter constructor initializes numberWidgets and cost to their respective values
    public Widget(int numWidgets, double price, double currentPrice){
        numberWidgets = numWidgets;
        cost = price;
        currentCost = currentPrice;
    }
    
    //returns the amount of widgets
    public int getWidget(){
        return numberWidgets;
    }

    //sets the amount of widgets when sold
    public void setWidget(int x){
        numberWidgets = x;
    }

    //returns the cost
    public double getCost(){
        return cost;
    }
    
    //returns the cost
    public double getCurrentCost(){
        return currentCost;
    }
}
