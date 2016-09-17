package csci446.project1.GraphSystem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cetho on 9/1/2016.
 */
public class Graph {

    public final Point[] points;
    public final ArrayList<Connection> connections;
    public final boolean[][] adjacencyMatrix;

    public Graph(int points) {
        //Create an empty array to store the points in the graph.
        this.points = new Point[points];

        float[] xValues = new float[points];
        float[] yValues = new float[points];

        for(int i = 0; i < points; i++) {
            boolean duplicate = true;
            while(duplicate) {
                xValues[i] = (float) Math.random()*2 - 1;
                yValues[i] = (float) Math.random()*2 - 1;

                for(int j = 0; j < points; j++ ) {
                    if(xValues[j] == 0.0f) {
                        //These values haven't been initialized yet.
                        duplicate = false;
                        break;
                    }
                    if(xValues[j] == xValues[i] && i != j) {
                        if(yValues[j] == yValues[i]) {
                            //There is a duplicate point. Recreate this point.
                            break;
                        }
                    }
                }
                duplicate = false;
            }
            //Officially create the point and store it in the object.
            this.points[i] = new Point(xValues[i], yValues[i], this, i);
        }

        //Print out the points and initialize them.
        System.out.println("Points Created:");
        for (int i = 0; i < this.points.length; i++) {
            this.points[i].setup();
            System.out.print("P" + i + ": ");
            this.points[i].printPoint();
            System.out.println();
        }

        //Create connections
        this.connections = new ArrayList<Connection>();
        ArrayList<Point> pointsThatCanStillConnect = new ArrayList<Point>(Arrays.asList(this.points));
        int random;
        boolean result;
        while(pointsThatCanStillConnect.size() > 0) {
            random = (int)(Math.random() * pointsThatCanStillConnect.size());
            result = pointsThatCanStillConnect.get(random).connectToNextPoint();
            if(!result) {
                pointsThatCanStillConnect.remove(random);
            }
        }

        //Create an adjacency matrix
        this.adjacencyMatrix = new boolean[points][points];

        for (Connection connection: this.connections) {
            this.adjacencyMatrix[connection.point1.id][connection.point2.id] = true;
            this.adjacencyMatrix[connection.point2.id][connection.point1.id] = true;
        }

        System.out.println("Connections Created:");
        for (int i = 0; i < this.connections.size(); i++) {
            System.out.print("C" + i + ": P" + this.connections.get(i).point1.id);
            System.out.print(" connects to P" + this.connections.get(i).point2.id);
            System.out.println();
        }
        
    }
    
    //added returns for the array and arraylist so I could use them in MinConflict -Lukas
     public Point[] returnArray()
     {
          return(points);   
     }

     public ArrayList<Connection> returnArrayList()         
     {
          return(connections);
     }
}
