package csci446.project1;
import csci446.project1.GraphSystem.Graph;
import csci446.project1.GraphSystem.Point;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

class Genetic {
    private Graph graph;
    private int k;
    private Random randomGenerator;
    private List<Chromosome> population;
    private Chromosome winner;
    public boolean success = false;
    public int iterations = 0;

    Genetic(Graph graph, int k) {
        this.graph = graph;
        this.k = k;
        population = new ArrayList<Chromosome>();
        randomGenerator = new Random();
        this.run();
    }
    private void run()
    {
        generateRandomPopulation(graph.points.length * 100);

        while(!foundSolution() && this.iterations < graph.points.length * 100) {
            tournament();
            breedPopulation();
            this.iterations++;
        }

        if(success) {
            System.out.println("Found solution in " + this.iterations + " iterations.");
            System.out.println("Solution: " + winner.toString());
        }
        else{
            System.out.println("No solution found in " + this.iterations + " iterations.");
        }
    }

    private boolean foundSolution() {
        for(Chromosome candidate : population){
            if(candidate.getConflicts() == 0) {
                winner = candidate;
                this.success = true;
                return true;
            }
        }
        return false;
    }

    private void generateRandomPopulation(int popSize) {
        for(int i = 0; i < popSize; i ++){
            Chromosome chromosome = createRandomChromosome();
            fitness(chromosome);
            population.add(chromosome);
        }
    }
    private Chromosome createRandomChromosome() {
        int[] chromosome = new int[graph.points.length];
        for(int i = 0; i < chromosome.length; i++){
            chromosome[i] = randomGenerator.nextInt(k);
        }

        return new Chromosome(chromosome);
    }
    private void fitness(Chromosome chromosome) {
        List<Point> visited = new ArrayList<>();
        int conflicts = 0;
        // Tracks how many nodes without conflicts a candidate has
        int chainCount = 0;
        // Check for conflicts
        // Chromosome applies to colors in the graph.points array
        // Check for conflicts
        // Return count of conflicts
        Stack<Point> nodeStack = new Stack<>();
        nodeStack.push(graph.points[0]);
        while(!nodeStack.isEmpty()) {
            Point vertex = nodeStack.pop();
            //check if I've been already
            if(!visited.contains(vertex)) {
                visited.add(vertex);
                //label vertex as visited
                for(Point point : vertex.connectedPoints)
                    nodeStack.push(point);

                for(Point connectedPoint : vertex.connectedPoints){
                    if(chromosome.getNodeColor(vertex.id) == chromosome.getNodeColor(connectedPoint.id) && !visited.contains(connectedPoint))
                    {
                        conflicts++;
                    }
                }
            }
        }

        chromosome.setConflicts(conflicts);
        chromosome.setNodesWithoutConflicts(chainCount);
    }

    private void tournament() {
        //This will remove half the population
        List<Chromosome> populationCopy = new ArrayList<Chromosome>(population);
        population = new ArrayList<Chromosome>();
        while(populationCopy.size() > 0) {
            // Pick two random chromosomes, remove 'em
            int c1i = randomGenerator.nextInt(populationCopy.size());
            Chromosome c1 = populationCopy.get(c1i);
            populationCopy.remove(c1i);
            int c2i = randomGenerator.nextInt(populationCopy.size());
            Chromosome c2 = populationCopy.get(c2i);
            populationCopy.remove(c2i);
            // Compare fitness of two chromosomes.  Throw out the loser.
            if (c1.getConflicts() > c2.getConflicts())
                population.add(c2);
            else
                population.add(c1);
        }
    }

    private void breedPopulation(){
        List<Chromosome> populationCopy = new ArrayList<Chromosome>(population);
        population = new ArrayList<Chromosome>();

        while(populationCopy.size() > 0) {
            // Pick two random chromosomes, remove 'em
            int c1i = randomGenerator.nextInt(populationCopy.size());
            Chromosome c1 = populationCopy.get(c1i);
            populationCopy.remove(c1i);
            int c2i = randomGenerator.nextInt(populationCopy.size());
            Chromosome c2 = populationCopy.get(c2i);
            populationCopy.remove(c2i);

            //breed them, add parents and offspring back to population
            List<Chromosome> family = crossover(c1, c2);
            population.addAll(family);
        }
    }

    private List<Chromosome> crossover(Chromosome c1, Chromosome c2){
        int crossoverPoint = randomGenerator.nextInt(c1.getChromosome().length);
        List<Chromosome> family = new ArrayList<>();
        family.add(c1);
        family.add(c2);
        //This creates two offspring
        int[] p1 = c1.getChromosome();
        int[] p2 = c2.getChromosome();
        int[] child1 = new int[p1.length];
        int[] child2 = new int[p1.length];
        for(int i = 0; i < crossoverPoint; i++) {
            child1[i] = p1[i];
            child2[i] = p2[i];
        }
        for(int i = crossoverPoint; i < c1.getChromosome().length; i++){
            child1[i] = p2[i];
            child2[i] = p1[i];

        }

        Chromosome childChromosome1 = new Chromosome(child1);
        Chromosome childChromosome2 = new Chromosome(child2);
        fitness(childChromosome1);
        fitness(childChromosome2);
        family.add(childChromosome1);
        family.add(childChromosome2);

        return family;
    }
}
