import java.io.File;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.*;
/**
 * WidgetsClient will take inventory of widgets, their current cost, and charge a 40% markup of the most recent 
 * and thus, expensive widgets. 
 * 
 * Client class also takes in orders from customers for widgets and sells it to them as well as 
 * displaying the actual cost vs. the total cost for the bookkeeper's records
 * 
 * Jason Navi
 * 3/3/2017
 */
public class WidgetsClient
{
    public static void main(String[] args){
        //loads the text file with the widget transactions
        File readMessages = new File("transactions.txt");
        //creates text files that will contain the information regarding the bookkeeping and customer reciepts respectively
        File bookKeeping = new File("Book Keeping.txt");
        File customerReciepts = new File("Customer Reciepts.txt");
        //will temporarily hold the transaction to be processed
        String[] transaction;
        //initializes a stack of widgets that will hold all shipments recieved
        Stack widgets = new Stack();
        //initializes a Queue of Widgets that will hold the backorders when the widgets run out
        Queue backOrder = new Queue();
        //keeps track of highest cost shipment
        double currentCost = 0.0;
        //calculates how many widgets are left after subtracting the most recent shipment
        int amountToSell = 0;
        //sums up the actual cost of the transaction
        double actualCost = 0;
        //Keeps track of the queue cost
        double queueCost = 0.0;

        //put in a try/catch block incase the file is not found or there is an input/output error
        try{
            //adds the new text file that will contain the bookkeeping and customer reciepts respectively
            bookKeeping.createNewFile();
            customerReciepts.createNewFile();
            //links FileWriter to new text files so messages will be written to them
            FileWriter writeBooks = new FileWriter(bookKeeping);    
            FileWriter writeReciepts = new FileWriter(customerReciepts);

            //used to read the text file with the shipment information to recieve or sell widgets
            Scanner readFile = new Scanner(readMessages);

            //while there are more incoming shipments/orders
            while(readFile.hasNextLine()){
                //reads transaction on next line and split it into three different elements of the array by space
                transaction = readFile.nextLine().split(" ");
                /*if the transaction starts with a 'R' then you have an incoming  
                shipment and you should add it to the top of the stack and update the currentCost*/
                if(transaction[0].charAt(0) == ('R')){
                    currentCost = Double.parseDouble(transaction[2]);       
                    widgets.push(new Widget(Integer.parseInt(transaction[1]), currentCost));
                    //while you have customers on the queue and you still have widgets left to sell on the stack
                    while(!backOrder.isEmpty() && !widgets.isEmpty()){
                        /*take the 1st customer on line and process the transaction with the widget amount and cost. 
                        processTransaction returns a modified stack so the current stack will be updated*/
                        Widget backWidget = backOrder.dequeue();
                        widgets = processTransaction(widgets, backWidget.getWidget(), backWidget.getCost());

                        /*store the queue's cost(cost of last shipment that could possibly have not been filled), amount left to sell, and actual total cost
                        of the sale in their respective variables. Pop each one off the stack as you store the values to not interfere with the widget objects*/ 
                        queueCost = widgets.peek().getCost();
                        widgets.pop();
                        amountToSell = (int)widgets.peek().getCost();
                        widgets.pop();
                        actualCost = widgets.peek().getCost();
                        widgets.pop();

                        //writes to the file the actual total cost of the sale 
                        System.out.println("Total cost = $" + actualCost);

                        //If the customers whole order was fulfilled print out the reciept to the file
                        if(amountToSell == 0){
                            System.out.println("Customer Reciept: " + backWidget.getWidget() +  " @ " + (backWidget.getCurrentCost() * 1.40) + " = $" + (backWidget.getWidget() * (backWidget.getCurrentCost() * 1.40)) + "\n");
                        }
                        //else print out the reciept to the file along with a message telling the user that the remaining balance will be put sold when a new shipment arrives 
                        else{
                            System.out.println("Customer Reciept: " + (backWidget.getWidget() - amountToSell) +  " @ " + (backWidget.getCurrentCost() * 1.40) + " = $" + ((backWidget.getWidget() - amountToSell) * (backWidget.getCurrentCost() * 1.40)) + "\nThe remaining " + amountToSell + " will be sold to you at it's current price when we restock \n");

                            //makes a temporary queue and adds the order that has still not been fully fulfilled to the front 
                            Queue temp = new Queue();
                            temp.enqueue(new Widget(amountToSell, queueCost, backWidget.getCurrentCost()));

                            //loops through all the Widget objects inside the old queue
                            while(!backOrder.isEmpty()){
                                //appends to the back of the new queue(temp), the widget object at the front of the old queue(backOrder)
                                temp.enqueue(backOrder.dequeue());
                            }

                            //makes the old queue(backOrder) point to the new queue(temp)
                            backOrder = temp;
                        }
                    }
                }
                //else the transaction is a sale and if the stack is not empty you should output the transaction details to the text file
                else if(!widgets.isEmpty()){
                    /*take the 1st customer on line and process the transaction with the widget amount and cost. 
                    processTransaction returns a modified stack so the current stack will be updated*/
                    widgets = processTransaction(widgets, Integer.parseInt(transaction[1]), widgets.peek().getCost());

                    /*store the queue's cost(cost of last shipment that could possibly have not been filled), amount left to sell, and actual total cost
                    of the sale in their respective variables. Pop each one off the stack as you store the values to not interfere with the widget objects*/ 
                    queueCost = widgets.peek().getCost();
                    widgets.pop();
                    amountToSell = (int)widgets.peek().getCost();
                    widgets.pop();
                    actualCost = widgets.peek().getCost();
                    widgets.pop();

                    //writes to the file the actual total cost of the sale 
                    System.out.println("Total cost = $" + actualCost);

                    //If the customers whole order was fulfilled print out the reciept to the file
                    if(amountToSell == 0){
                        System.out.println("Customer Reciept: " + transaction[1] +  " @ " + (currentCost * 1.40) + " = $" + (Integer.parseInt(transaction[1]) * (currentCost * 1.40)) + "\n"); 
                    }
                    else{
                        //else print out the reciept to the file along with a message telling the user that the remaining balance will be sold when a new shipment arrives
                        System.out.println("Customer Reciept: " + (Integer.parseInt(transaction[1]) - amountToSell) +  " @ " + (currentCost * 1.40) + " = $" + ((Integer.parseInt(transaction[1]) - amountToSell) * (currentCost * 1.40)) + "\nThe remaining " + amountToSell + " will be sold to you at it's current price when we restock \n");

                        //add the customer's remaining balance to the queue which includes how many widgets to be sold, the original cost of the widget and the currentCost(highest possible cost of shipments recieved)
                        backOrder.enqueue(new Widget(amountToSell, queueCost, currentCost));
                    }
                }
                else{
                    //if the stack was empty but an order was made, add the customer's remaining balance to the queue including the widgets to be sold, the original cost of the widget and the currentCost(highest possible cost of shipments recieved)
                    backOrder.enqueue(new Widget(Integer.parseInt(transaction[1]), queueCost, currentCost));
                }
                //write to the new text file the escape sequence to make a new line
                writeBooks.write("\r\n");    
                writeReciepts.write("\r\n");    
            }
            //close the FileWriter and Scanner objects
            readFile.close();
            writeBooks.close();
            writeReciepts.close();
            //if the file is not found, or there is an input/output error, or any other error print it to the console
        } catch(FileNotFoundException error){
            error.printStackTrace();
        } catch(IOException error){
            error.printStackTrace();
        } catch(Exception error){
            error.printStackTrace();
        }
    }

    /**processes a transaction and returns the updated stack*/
    public static Stack processTransaction(Stack widgets, int amountToSell, double widgetCost){
        //writes to the file a preset actual cost message and initializes actualCost to 0.0
        System.out.println("Actual cost: ");
        double actualCost = 0.0;

        //while there are no more widgets left on the stack or you have completely filled the current sale
        while(!widgets.isEmpty() && amountToSell != 0){
            //if the current widget shipment has more widgets than the customers order
            if(widgets.peek().getWidget() > amountToSell){
                //add to the actualCost the real cost of the current widgets in the shipment on the stack times the amount of widgets you are selling
                actualCost += widgetCost * amountToSell;
                //update the current amount of widgets on the top of the stack by subtracting the number of widgets by how many you are selling in the sale
                widgets.peek().setWidget(widgets.peek().getWidget() - amountToSell);
                //write to the file how many widgets you are selling, the real price per unit, and the total cost of that sale
                System.out.println(amountToSell + " @ " + widgetCost + " = $" + (amountToSell * widgetCost));
                //the customer's whole sale was fulfilled so update amountToSell to 0(break you out of the loop) and the current widgetCost to 0.0 
                amountToSell = 0;
                widgetCost = 0.0;
            }
            //else there are not enough widgets in the current shipment on the top of the stack to fill the customer's entire order
            else{
                //add to the actualCost the real cost of the current widgets in the shipment on the stack times the amount of widgets you are selling
                actualCost += widgetCost * widgets.peek().getWidget();
                //subtract the widgets in the shipment at the top of the stack from the amount of widgets left that the customer wants 
                amountToSell -= widgets.peek().getWidget();

                //write to the file how many widgets you are selling, the real price per unit, and the total cost of that sale
                System.out.println(widgets.peek().getWidget() + " @ " + widgetCost + " = $" + (widgets.peek().getWidget() * widgetCost));

                //pop the shipment since there are no widgets left in it
                widgets.pop();
                //If there is a shipment left in the stack, then update widgetCost to the next shipment's cost located at the top of the stack
                if(!widgets.isEmpty())
                    widgetCost = widgets.peek().getCost();
            }
        }

        /*pushes the total cost of the transaction, the amount left to be sold in the transaction, and the actual cost of the last shipment(used when adding unfulfilled order to queue) 
        onto the stack so it can communicate with the main method and pop off those pieces of data so it does not interfere with the stack's shipment information*/
        widgets.push(new Widget(0, (double)actualCost));
        widgets.push(new Widget(0, (double)amountToSell));
        widgets.push(new Widget(0, widgetCost));

        //returns the updated stack
        return widgets;
    }
}
