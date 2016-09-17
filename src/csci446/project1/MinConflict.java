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
    
    public MinConflict(int points, int colors, Point[] poi, ArrayList<Connection> con){
    color = new int[points];
    conflicts = new int[points];
    pointsArray = poi;
    connectionsArray = con;
    ArrayList<Point> p1 = new ArrayList<Point>();
    ArrayList<Point> p2 = new ArrayList<Point>();

    
    //arrange the connections based on point ID
    System.out.println();
    for(int i = 0; i < points; i++){
        for(int j=0; j<connectionsArray.size();j++){
            if(connectionsArray.get(j).point1.id == i){
                //System.out.print("P" + connections.get(j).point1.id);
                //System.out.print(" connects to P" + connections.get(j).point2.id);
                //System.out.println();
                p1.add(connectionsArray.get(j).point1);
                p2.add(connectionsArray.get(j).point2);
                
            }
        }
    }
    //for(int i=0; i<p1.size();i++){
    //  System.out.println(p1.get(i).id);
    //}
    
    //randomly colors graph
    for(int i = 0; i < color.length; i++){
        color[i] = (int)(Math.random() * colors) +1;
    }
    
    //print out color[]
    //for(int i = 0; i < color.length; i++){
    //    System.out.println(color[i]);
    //}
    
    //check conflicts
        for(int i =0; i < points; i++){
            conflicts[i] = checkCon(i);
        }
    
    }
    
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
}
