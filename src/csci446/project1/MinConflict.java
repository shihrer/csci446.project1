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
    
    //starts as all 0's indicating no color. Colors will be numbers between 1 and 3 or 4 depending on k
    public int[] color;
    //finds the total number of conflicts each point has. conflicts[0] = number of conflicts point 1 has. 
    public int[] conflicts;
    public Point[] pointsArray;
    public ArrayList<Connection> connectionsArray;
    public int mostConnections;
    public int mostConf;
    public int itterations;
    public int total;
    public int numcolors;
    
    public MinConflict(int points, int colors, Point[] poi, ArrayList<Connection> con){
    numcolors = colors;
    color = new int[points];
    conflicts = new int[points];
    pointsArray = poi;
    connectionsArray = con;
    //ArrayList<Point> p1 = new ArrayList<>();
    //ArrayList<Point> p2 = new ArrayList<>();

    
    //arrange the connections based on point ID
//    System.out.println();
//    for(int i = 0; i < points; i++){
//        for(int j=0; j<connectionsArray.size();j++){
//            if(connectionsArray.get(j).point1.id == i){
//                //System.out.print("P" + connectionsArray.get(j).point1.id);
//                //System.out.print(" connects to P" + connectionsArray.get(j).point2.id);
//                //System.out.println();
//                p1.add(connectionsArray.get(j).point1);
//                p2.add(connectionsArray.get(j).point2);
//                
//            }
//        }
//    }
    
    //randomly colors graph
    for(int i = 0; i < color.length; i++){
        color[i] = (int)(Math.random() * colors) +1;
    }
    
    //initially color graph, starting with the point taht has the most connections
    mostConnections();
    color[mostConnections] = 1;
    for(int i =0; i < connectionsArray.size(); i++){
        if(connectionsArray.get(i).point1.id == mostConnections){
            color[connectionsArray.get(i).point2.id] = 2;
        }
        if(connectionsArray.get(i).point1.id != mostConnections && connectionsArray.get(i).point2.id != mostConnections){
         
            color[connectionsArray.get(i).point1.id] = (int)((Math.random() * colors) +1);
        }
    }
  
    //check conflicts
    for(int i =0; i < points; i++){
         conflicts[i] = checkCon(i);
    }
    totalConf();
    itterations = 0;
    
    //initial prints
    //printConf();
    //printCol();
    System.out.println();
    
    //now the graph is fully colored and all conflicts are recorded
    while(total > 0){
        if(itterations == 500){
            break;
        }
        itterations = itterations + 1;
        mostConf();
        totalConf();
        if(total == 0){
            break;
        }
        recolor(mostConf);
        for(int i =0; i < points; i++){
        conflicts[i] = checkCon(i);
        }
        //System.out.println(itterations + " " + total);
        //System.out.println("Total Conflicts Remaining: " + totalConf());
    }
    
    
    //prints off point colors and conflicts and most connections
    printConf();
    System.out.println();
    printCol();
    System.out.println();
    if(itterations == 50){
        System.out.println("MinConflict: FAILURE");
    }
    else{
        System.out.println("MinConflict: SUCCESS");

    }
    System.out.println("MinConflict: Total conflicts remaining: " + totalConf());
    //System.out.println("P" + mostConnections +" has the most connections.");
    System.out.println("MinConflict: Ran " + itterations + " times");
    
    }
    
    public int checkCon(int point){
        int cons = 0;
             for(int j=0; j<connectionsArray.size();j++){
                if(connectionsArray.get(j).point1.id == point){
                    if(color[connectionsArray.get(j).point1.id] == color[connectionsArray.get(j).point2.id] && color[connectionsArray.get(j).point1.id] != 0)
                       cons = cons+1;
            }
                
            // was needed before conflicts were listed twice
            
            //    if(connectionsArray.get(j).point2.id == point){
            //        if(color[connectionsArray.get(j).point1.id] == color[connectionsArray.get(j).point2.id] && color[connectionsArray.get(j).point1.id] != 0)
            //           cons = cons+1;
            //}
               
         }
        //System.out.println(cons);
        return cons;
    }
    
    public void printConf(){
    //print list of conflicts
    for(int i =0; i <conflicts.length; i++){
        System.out.println("MinConflict: P" + i + ": " + conflicts[i] + " conflicts");
    }
    }
    
    public void printCol(){
    //print out color[]
    for(int i = 0; i < color.length; i++){
        System.out.println("MinConflict: P" + i + ": color " +color[i]);
    }
    }
    
    public int totalConf(){
        total = 0;
        for(int i = 0; i < conflicts.length; i++){
            total = total + conflicts[i];
        }
        return total;
    }
    
    public int mostConnections(){
        int temp = 0;
        int count=0;
        for(int j=0; j < pointsArray.length; j++){
            for(int i = 0; i < connectionsArray.size(); i++){
                if(connectionsArray.get(i).point1.id == j){
                    count = count+1;
                }
                if(count >= temp){
                    temp = count;
                    mostConnections = j;
                }
                count = 0;
            }
        }
        return mostConnections;
    }
    
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
    
    public void recolor(int pointID){
        int temp = checkCon(color[pointID]);
        //System.out.println("----->" + conflicts[pointID]);

        //System.out.println(checkCon(color[pointID]));
        for(int i = 1; i < numcolors; i++){   
        //System.out.println("RECOLORING P" + pointID + " to " + i);
        //System.out.println(temp);
        color[pointID] = i;
        //System.out.println(i + "now has " + checkCon(color[pointID]) + " conflicts");
        if(checkCon(pointID) == 0){
            break;
        }
        if(checkCon(pointID) < temp){
            break;
        }
        }
    }
}
