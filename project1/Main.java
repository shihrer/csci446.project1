package csci446.project1;

import csci446.project1.GraphSystem.Graph;

public class Main {

    public static void main(String[] args) {

        // Basic Settings
        int numberOfPoints = 4;
        int numberOfColors = 4; //k
        
        // Create a graph.
	    Graph graph = new Graph(numberOfPoints);
            //runs graph coloring with min conflict
            MinConflict minconflict = new MinConflict(numberOfPoints, numberOfColors, graph.returnArray(), graph.returnArrayList());
    }
    
}
