/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csci446.project1;

import csci446.project1.GraphSystem.Connection;
import csci446.project1.GraphSystem.Point;
import java.util.ArrayList;


/**
 *
 * @author Lukasrama
 */

public class MinConflict {
      
    public int[] color; //array of colors where color[i] = color and i = pointID
    public int[] conflicts; //array of conflicts where conflicts[i] = # of conflicts and i = pointID
    public Point[] pointsArray;
    public ArrayList<Connection> connectionsArray;
    public int[] connections;   //array of connections where connection[i] = # of connections and i = pointID
    public int mostConnections; //pointID of the point with the most connections
    public int mostConf;    //pointID of the point with the most conflicts
    public int itterations;     //number of itterations that have occured
    public int total;   //total number of conflicts in colored graph
    public int numcolors;   //k
    public int max = 50;   //max number of itterations before while loop breaks
    public int last = 0;    //last point colored by the while loop
    
    public MinConflict(int points, int colors, Point[] poi, ArrayList<Connection> con){
    numcolors = colors;
    color = new int[points];
    conflicts = new int[points];
    pointsArray = poi;
    connectionsArray = con;
    connections = new int[points];

    
    //Builds connections[], finds pointID with most connections and sets itterations to 0
    checkConnections();
    mostConnections();
    itterations = 0;

    //randomly colors graph. This is to insure that all points have a color. Below we will color specific points to try and optimize coloring.
    for(int i = 0; i < color.length; i++){
        //color[i] = (int)(Math.random() * colors) +1;
        color[i] = numcolors;
    }
    
    //initially color graph, starting with the point taht has the most connections
    color[mostConnections] = 1;
    for(int i =0; i < connectionsArray.size(); i++){
        if(connectionsArray.get(i).point1.id == mostConnections){
            color[connectionsArray.get(i).point2.id] = 2;
        }
        if(connectionsArray.get(i).point1.id != mostConnections && connectionsArray.get(i).point2.id != mostConnections){
            recolor(connectionsArray.get(i).point1.id);
        }
    }

    //initial/debugging prints
    //printConf();
    //printCol();
    //printConn();
    
    
    //check conflicts at every point
    for(int i =0; i < points; i++){
         conflicts[i] = checkCon(i);
         totalConf();
    }
    
    //now the graph is fully colored and all conflicts are recorded, we will recolor points w/ conflicts
    while(total > 0){
        //breaks loop if loop has run a max number of times
        if(itterations == max){
            break;
        }
        itterations = itterations + 1;
        mostConf();     //finds point with most conflicts
        totalConf();    //finds total number of conflicts
        System.out.print("P" + last + " was last recolored. Changing point to P");
        //To try and prevent loops, if the last point still has most conflicts, this loop finds another
        if(mostConf == last){
            for(int j = 0; j<conflicts.length; j++ ){
                if(conflicts[j] == conflicts[mostConf] && j != mostConf){
                    System.out.println(j);
                    recolor(j);
                }
            }
        }
        recolor(mostConf);  //recolors the point with most conflicts
        //recheck conflicts
        for(int i =0; i < points; i++){
            conflicts[i] = checkCon(i);
        }
        System.out.println("Total Conflicts Remaining: " + totalConf());
    }
    
    
    //prints off point colors and conflicts and most connections post coloring
    printConf();
    System.out.println();
    printCol();
    System.out.println();

    if(itterations == max){
        System.out.println("MinConflict: FAILURE");
    }
    else{
        System.out.println("MinConflict: SUCCESS");

    }

    System.out.println("MinConflict: Total conflicts remaining: " + totalConf());
    System.out.println("MinConflict: P" + mostConnections +" has the most connections with " + connections[mostConnections]);
    System.out.println("MinConflict: P" + mostConf +" has the most conflicts with " + conflicts[mostConf]);
    System.out.println("MinConflict: Ran " + itterations + " times");

    }
    
    //checks the number of conflicts any given point has
    public int checkCon(int point){
        int cons = 0;
             for(int j=0; j<connectionsArray.size();j++){
                if(connectionsArray.get(j).point1.id == point){
                    if(color[connectionsArray.get(j).point1.id] == color[connectionsArray.get(j).point2.id] && color[connectionsArray.get(j).point1.id] != 0)
                       cons = cons+1;
            }
                if(connectionsArray.get(j).point2.id == point){
                    if(color[connectionsArray.get(j).point1.id] == color[connectionsArray.get(j).point2.id] && color[connectionsArray.get(j).point1.id] != 0)
                       cons = cons+1;
            }
               
         }
        //System.out.println(cons);
        return cons;
    }
    
    //prints off the number of connections each point has
    public void printConn(){
        for(int i = 0; i < connections.length; i++){
            System.out.println("P" + i + " has " + connections[i] + " connections");
        }
    }
    
    //prints off the number of conflicts each point has
    public void printConf(){
    //print list of conflicts
    for(int i =0; i <conflicts.length; i++){
        System.out.println("MinConflict: P" + i + ": " + conflicts[i] + " conflicts");
    }
    }
    
    //prints off the color at each point
    public void printCol(){
    //print out color[]
    for(int i = 0; i < color.length; i++){
        System.out.println("MinConflict: P" + i + ": color " +color[i]);
    }
    }
    
    //returns the total number of conflicts left 
    public int totalConf(){
        total = 0;
        for(int i = 0; i < conflicts.length; i++){
            total = total + conflicts[i];
        }
        return total;
    }
    
    //Used to build the connections[] by checking the number of connections each point has
    public void checkConnections(){
        for(int j=0; j < pointsArray.length; j++){
            for(int i = 0; i < connectionsArray.size(); i++){
                if(connectionsArray.get(i).point1.id == j){
                    connections[j] +=1;
                }
                if(connectionsArray.get(i).point2.id == j){
                    connections[j] +=1;
                }
            }
        }
    }
    
    //returns the pointID of the point with the most connections
    public int mostConnections(){
        int temp = 0;
        mostConnections = 0;
        for(int i = 0; i < connections.length; i++){
            if(connections[i] > temp){
                temp = connections[i];
                mostConnections = i;
            }
        }
        return mostConnections;
    }
    
    //returns the pointID of the point with the most conflicts
    public int mostConf(){
        int temp = 0;
        for(int j=0; j < conflicts.length; j++){
            if(conflicts[j] >= temp){
                temp = conflicts[j];
                mostConf = j;
            }
            
        }
        return mostConf;
    }
    
    //recolors a point to minimize local conflicts
    public void recolor(int pointID){
        int temp = checkCon(pointID);
        //System.out.println(checkCon(color[pointID]));
        for(int i = 1; i <= numcolors; i++){ 
            last = pointID; //used to try an prevent loops
            System.out.println("MinConflict: RECOLORING P" + pointID + " to " + i);
            color[pointID] = i;
            System.out.println("MinConflict: P" + pointID + " now has " + checkCon(pointID) + " conflicts");
            if(checkCon(pointID) == 0){
                break;
            }
            if(checkCon(pointID) < temp){
                break;
            }
        }
    }
}
