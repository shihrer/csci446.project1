/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csci446.project1;

import csci446.project1.GraphSystem.Connection;
import csci446.project1.GraphSystem.Point;
import java.util.ArrayList;
import java.util.Arrays;


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
    public int max = 100;   //max number of itterations before while loop breaks
    public int last = 0;    //last point colored by the while loop
    public int timesColored = 0;    //keeps track of how many times a color was changed (either initially colored or reColored)
    
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
    timesColored = 0;
    //printConn();
    
    color[mostConf] = 1;
    timesColored++;
    checkAllCon();
    //initially color graph with colors 1-3 and no conflicts
    for(int k = 1; k <=3; k++){
        for(int i = connections[mostConnections] + 1; i > 0; i--){
            for(int j = 0; j < connectionsArray.size(); j++){
                if(connections[connectionsArray.get(j).point1.id] == i && color[connectionsArray.get(j).point1.id] == 0 && color[connectionsArray.get(j).point2.id] != k){
                    color[connectionsArray.get(j).point1.id] = k;
                    timesColored ++;
                }
                if(checkCon(connectionsArray.get(j).point1.id) > 0){
                    color[connectionsArray.get(j).point1.id] = 0;
                    timesColored --;
                }                   
                if(connections[connectionsArray.get(j).point2.id] == i && color[connectionsArray.get(j).point2.id] == 0 && color[connectionsArray.get(j).point1.id] != k){
                    color[connectionsArray.get(j).point2.id] = k;
                    timesColored ++;
                }
                if(checkCon(connectionsArray.get(j).point2.id) > 0){
                    color[connectionsArray.get(j).point2.id] = 0;
                    timesColored --;
                }
                checkAllCon();
            }
        }   
    }
    
    //fill as many blank spaces with no conflicts
    for(int j = 1; j <= numcolors; j++){
        for(int i = 0; i < connectionsArray.size(); i++){
            if(color[connectionsArray.get(i).point1.id] == 0 && color[connectionsArray.get(i).point2.id] != j){
                    //color[connectionsArray.get(i).point1.id] = j;
                    timesColored ++;
                    reColor(connectionsArray.get(i).point1.id);

                }
            if(checkCon(connectionsArray.get(i).point1.id) > 0){
                reColor(connectionsArray.get(i).point1.id);
                reColor(connectionsArray.get(i).point2.id);
                timesColored ++;
            }            
            if(color[connectionsArray.get(i).point2.id] == 0 && color[connectionsArray.get(i).point1.id] != j){
                color[connectionsArray.get(i).point2.id] = j;
                timesColored ++;
                reColor(connectionsArray.get(i).point2.id);
                
            }
            if(checkCon(connectionsArray.get(i).point2.id) > 0){
                reColor(connectionsArray.get(i).point1.id);
                reColor(connectionsArray.get(i).point2.id);
                timesColored ++;
            } 
            checkAllCon();
        }
    }
    
    //colors final points
    for(int i = 0; i < color.length; i ++){
            if(color[i] == 0){
                timesColored ++;
                color[i] = numcolors;

        }
            checkAllCon();
    }
    
    //initial/debugging prints
    //printConf();
    //printCol();
    //printConn();
    
    
    //check conflicts at every point
    checkAllCon();
    
    //now the graph is fully colored and all conflicts are recorded, we will reColor points w/ conflicts
    while(total > 0){
        //breaks loop if loop has run a max number of times
        if(itterations == max){
            break;
        }
        itterations = itterations + 1;
        mostConf();     //finds point with most conflicts
        checkAllCon();
        totalConf();    //finds total number of conflicts
        //To try and prevent loops, if the last point still has most conflicts, this loop finds another
        if(mostConf == last){
            for(int j = 0; j < connectionsArray.size(); j++ ){
                if(connectionsArray.get(j).point1.id == mostConf){
                    timesColored ++;
                    reColor(connectionsArray.get(j).point2.id);
                }
                if(connectionsArray.get(j).point2.id == mostConf){
                    timesColored ++;
                    reColor(connectionsArray.get(j).point1.id); 
                }
            }
        }
        else{
        reColor(mostConf);  //reColors the point with most conflicts
        //recheck conflicts
        }
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
    if(total == 0){
        System.out.println("SUCCESS");
    }
    else{
        System.out.println("FAILURE");

    }
    System.out.println("Total conflicts remaining: " + totalConf());
    System.out.println("P" + mostConnections +" has the most connections with " + connections[mostConnections]);
    System.out.println("P" + mostConf +" has the most conflicts with " + conflicts[mostConf]);
    System.out.println("Total number of points colored/reColored: " + timesColored);

    }
    
    public void checkAllCon(){
         for(int i =0; i < color.length; i++){
         conflicts[i] = checkCon(i);
         totalConf();
    }
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
        System.out.println("P" + i + ": " + conflicts[i] + " conflicts");
    }
    }
    
    //prints off the color at each point
    public void printCol(){
    //print out color[]
    for(int i = 0; i < color.length; i++){
        System.out.println("P" + i + ": color " +color[i]);
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
    
    //reColors a point to minimize local conflicts
    public void reColor(int pointID){
        int temp = checkCon(pointID);
        timesColored ++;
        //System.out.println(checkCon(color[pointID]));
        for(int i = 1; i <= numcolors; i++){ 
            last = pointID; //used to try an prevent loops
            if(color[pointID] == 0){
                color[pointID] = numcolors;
            }

            System.out.println("RECOLORING P" + pointID + " to " + i);
            color[pointID] = i;
            System.out.println("P" + pointID + " now has " + checkCon(pointID) + " conflicts");
            if(checkCon(pointID) == 0){ //breaks if is reColored to zero conflicts
                break;
            }
            if(checkCon(pointID) < temp){
                break;
            }
            }
        }
    }

