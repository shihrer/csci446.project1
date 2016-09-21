package csci446.project1;

import csci446.project1.GraphSystem.Graph;

public class Main {

    public static void main(String[] args) {

        // Basic Settings
        int numberOfPoints = 10;
        int numberOfColors = 3; //k
        
        // Create a graph.
	    Graph graph = new Graph(numberOfPoints);

        //runs graph coloring with min conflict
        MinConflict minConflict = new MinConflict(numberOfPoints, numberOfColors, graph.points, graph.connections);
        //run graph coloring with simple backtracking
        SimpleBacktracking simpleBacktracking = new SimpleBacktracking(numberOfColors, graph);
        //run graph coloring with forward-checking
        BacktrackingWithForwardChecking backtrackingWithForwardChecking = new BacktrackingWithForwardChecking(numberOfColors, graph);
    }
    
}
