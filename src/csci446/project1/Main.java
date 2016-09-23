package csci446.project1;

import csci446.project1.GraphSystem.Graph;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        // Basic Settings
        int startingColors = 3; //k
        int endingColors = 4;
        int numberOfTries = 2;
        int startingGraphSize = 10;
        int graphIncrementSize = 2; //Normally 10, but it will take forever for backtracking.
        int graphIncrementCount = 10;
        boolean multithreaded = false;

        Graph[] graphs = new Graph[graphIncrementCount*numberOfTries];

        MinConflict[] minConflictSolutions = new MinConflict[graphIncrementCount*numberOfTries*(endingColors - startingColors + 1)];
        SimpleBacktracking[] simpleBacktrackingSolutions = new SimpleBacktracking[graphIncrementCount*numberOfTries*(endingColors - startingColors + 1)];
        BacktrackingWithForwardChecking[] backtrackingWithForwardCheckingSolutions = new BacktrackingWithForwardChecking[graphIncrementCount*numberOfTries*(endingColors - startingColors + 1)];
        Genetic[] geneticSolutions = new Genetic[graphIncrementCount*numberOfTries*(endingColors - startingColors + 1)];

        // Run all the tests, storing their graphs and results.

        for (int colors = startingColors; colors <= endingColors; colors++) {
            for (int testSet = 1; testSet <= graphIncrementCount; testSet++) {
                for (int tryNum = 1; tryNum <= numberOfTries; tryNum++) {
                    graphs[testSet * tryNum - 1] = new Graph(startingGraphSize + ((testSet - 1) * graphIncrementSize));
                    if(multithreaded) {
                        MinConflictThread minConflictThread = new MinConflictThread(testSet, colors, startingColors, tryNum, graphs);
                        minConflictThread.run();
                        SimpleBacktrackingThread simpleBacktrackingThread = new SimpleBacktrackingThread(testSet, colors, startingColors, tryNum, graphs);
                        simpleBacktrackingThread.run();
                        BacktrackingWithForwardCheckingThread backtrackingWithForwardCheckingThread = new BacktrackingWithForwardCheckingThread(testSet, colors, startingColors, tryNum, graphs);
                        backtrackingWithForwardCheckingThread.run();

                        minConflictThread.join();
                        minConflictSolutions[testSet * (colors - startingColors + 1) * tryNum - 1] = minConflictThread.getInstance();

                        simpleBacktrackingThread.join();
                        simpleBacktrackingSolutions[testSet * (colors - startingColors + 1) * tryNum - 1] = simpleBacktrackingThread.getInstance();

                        backtrackingWithForwardCheckingThread.join();
                        backtrackingWithForwardCheckingSolutions[testSet * (colors - startingColors + 1) * tryNum - 1] = backtrackingWithForwardCheckingThread.getInstance();
                    } else {
                        minConflictSolutions[testSet * (colors - startingColors + 1) * tryNum - 1] = new MinConflict(graphs[testSet * tryNum - 1].points.length, colors, graphs[testSet * tryNum - 1].points, graphs[testSet * tryNum - 1].connections);
                        simpleBacktrackingSolutions[testSet * (colors - startingColors + 1) * tryNum - 1] = new SimpleBacktracking(colors, graphs[testSet * tryNum - 1]);
                        backtrackingWithForwardCheckingSolutions[testSet * (colors - startingColors + 1) * tryNum - 1] = new BacktrackingWithForwardChecking(colors, graphs[testSet * tryNum - 1]);
                        geneticSolutions[testSet * (colors - startingColors + 1) * tryNum - 1] = new Genetic(graphs[testSet * tryNum - 1], colors);
                    }
                }
            }
        }
        //Print Solutions
        System.out.println("\nFinal Results\n");
        for (int colors = startingColors; colors <= endingColors; colors++) {
            System.out.println("Test Graphs With " + colors + " Colors:");
            for (int testSet = 1; testSet <= graphIncrementCount; testSet++) {
                System.out.println("\t" + (startingGraphSize + (testSet - 1) * graphIncrementSize) + " points:");
                for (int tryNum = 1; tryNum <= numberOfTries; tryNum++) {
                    System.out.println("\t\tTry " + tryNum + ":");
                    if(minConflictSolutions[testSet * (colors - startingColors + 1) * tryNum - 1].success) {
                        System.out.println("\t\t\tMinConflict: Success, Iterations: " + minConflictSolutions[testSet * (colors - startingColors + 1) * tryNum - 1].timesColored);
                    } else {
                        System.out.println("\t\t\tMinConflict: Failure, Iterations: " + minConflictSolutions[testSet * (colors - startingColors + 1) * tryNum - 1].timesColored);
                    }
                    if(simpleBacktrackingSolutions[testSet * (colors - startingColors + 1) * tryNum - 1].success) {
                        System.out.println("\t\t\tSimpleBacktracking: Success, Iterations: " + simpleBacktrackingSolutions[testSet * (colors - startingColors + 1) * tryNum - 1].iterations);
                    } else {
                        System.out.println("\t\t\tSimpleBacktracking: Failure, Iterations: " + simpleBacktrackingSolutions[testSet * (colors - startingColors + 1) * tryNum - 1].iterations);
                    }
                    if(backtrackingWithForwardCheckingSolutions[testSet * (colors - startingColors + 1) * tryNum - 1].success) {
                        System.out.println("\t\t\tBacktrackingWithForwardChecking: Success, Iterations: " + backtrackingWithForwardCheckingSolutions[testSet * (colors - startingColors + 1) * tryNum - 1].iterations);
                    } else {
                        System.out.println("\t\t\tBacktrackingWithForwardChecking: Failure, Iterations: " + backtrackingWithForwardCheckingSolutions[testSet * (colors - startingColors + 1) * tryNum - 1].iterations);
                    }
                    if(geneticSolutions[testSet * (colors - startingColors + 1) * tryNum - 1].success) {
                        System.out.println("\t\t\tGenetic Algorithm: Success, Iterations: " + geneticSolutions[testSet * (colors - startingColors + 1) * tryNum - 1].iterations);
                    } else {
                        System.out.println("\t\t\tGenetic Algorithm: Failure, Iterations: " + geneticSolutions[testSet * (colors - startingColors + 1) * tryNum - 1].iterations);
                    }
                }
            }
        }
    }
}
