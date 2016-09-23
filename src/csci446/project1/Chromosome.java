package csci446.project1;

/**
 * Created by michael on 9/22/2016.
 */
public class Chromosome {
    private int[] chromosome;
    private int conflicts;
    private int nodesWithoutConflicts;

    Chromosome(int[] chromosome){
        this.chromosome = chromosome;
        this.conflicts = -1;
    }

    public int getConflicts(){
        return conflicts;
    }
    public void setConflicts(int conflicts){
        this.conflicts = conflicts;
    }
    public int getNodesWithoutConflicts(){
        return nodesWithoutConflicts;
    }
    public void setNodesWithoutConflicts(int count){
        nodesWithoutConflicts = count;
    }
    public int[] getChromosome(){
        return chromosome;
    }
    public void setChromosome(int[] chromosome){
        this.chromosome = chromosome;
    }

    public int getNodeColor(int id) {
        return chromosome[id];
    }

    @Override
    public String toString(){
        StringBuilder chromeString = new StringBuilder();
        for(int i = 0; i < chromosome.length; i++)
            chromeString.append(chromosome[i]);

        return chromeString.toString();
    }
}
