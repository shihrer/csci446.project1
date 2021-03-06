package csci446.project1;

import csci446.project1.GraphSystem.Graph;

/**
 * Created by cetho on 9/22/2016.
 */
public class GeneticThread extends Thread {

    public volatile Genetic instance;

    public int testSet;
    public int colors;
    public int startingColors;
    public int tryNum;
    public Graph[] graphs;
    public boolean terminated;

    public GeneticThread(int testSet, int colors, int startingColors, int tryNum, Graph[] graphs) {
        this.testSet = testSet;
        this.colors = colors;
        this.startingColors = startingColors;
        this.tryNum = tryNum;
        this.graphs = graphs;
        this.terminated = false;
    }

    public void run() {
        instance = new Genetic(graphs[testSet * tryNum - 1], colors);
        this.terminated = true;
    }

    public Genetic getInstance() {
        return instance;
    }
}
