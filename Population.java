import java.util.ArrayList;
import java.util.List;

public class Population {
    public float[][] fixedData;
    public float[][] variableData;
    public List<FunTree> population;
    private static int populationSize = 100;
    private static int TOURNAMENT_SIZE = 3;

    public Population() {
        population = new ArrayList<FunTree>();
        for (int i = 0; i <= populationSize; i++) {
            population.add(new FunTree());
        }
    }

    public Population(List<FunTree> pop) {
        population = pop;
    }

    public List<FunTree> getFittest() {
        List<FunTree> fittestTrees = new ArrayList<FunTree>();

        for (int j = 0; j <= populationSize / 3; j++) {
            FunTree fittestTree = population.get(0);
            float fittestValue = fittestTree.getFitness(fixedData) + fittestTree.getFitness(variableData);
            for (int i = 0; i < population.size(); i++) {
                FunTree current = population.get(i);
                float fitness = current.getFitness(fixedData) + current.getFitness(variableData);
                if (fitness < fittestValue) {
                    fittestTree = population.get(i);
                    fittestValue = fitness;
                }
            }
            fittestTrees.add(fittestTree);
        }

        return fittestTrees;
    }

    public void removeLeastFit() {
        for (int j = 0; j <= populationSize / 3; j++) {
            FunTree leastFitTree = population.get(0);
            float leastFitValue = leastFitTree.getFitness(fixedData) + leastFitTree.getFitness(variableData);
            for (int i = 0; i < population.size(); i++) {
                FunTree current = population.get(i);
                float fitness = current.getFitness(fixedData) + current.getFitness(variableData);
                if (fitness > leastFitValue) {
                    leastFitTree = population.get(i);
                    leastFitValue = fitness;
                }
            }
            population.remove(leastFitTree);
        }
    }

    public void migrate(Population other) {
        other.removeLeastFit();
        other.population.addAll(getFittest());
        other.variableData = variableData.clone();
    }

    private FunTree getSmallest() {
        FunTree smallestTree = population.get(0);
        int smallestDepth = population.get(0).getDepth();
        for (int i = 1; i < population.size(); i++) {
            int depth = population.get(i).getDepth();
            if (depth < smallestDepth) {
                smallestTree = population.get(i);
                smallestDepth = depth;
            } else if (depth == smallestDepth) {
                if (population.get(i).getSize() < smallestTree.getSize()) {
                    smallestTree = population.get(i);
                }
            }
        }

        return smallestTree;
    }

    // method to get fittest in given array of trees
    public FunTree getMostFit() {
        FunTree fittestTree = population.get(0);
        float fittestValue = fittestTree.getFitness(fixedData) + fittestTree.getFitness(variableData);
        for (int i = 1; i < population.size(); i++) {
            FunTree current = population.get(i);
            float fitness = current.getFitness(fixedData) + current.getFitness(variableData);
            if (fitness < fittestValue) {
                fittestTree = current;
                fittestValue = fitness;
            }
        }

        return fittestTree;
    }

    private FunTree tournament() {
        // favor less complex solutions
        return tournamentUtil(population);
    }

    // method to run tournament selection, returning a tree
    // pick N random trees from list and return the fittest
    private FunTree tournamentUtil(List<FunTree> trees) {
        List<FunTree> selected = new ArrayList<FunTree>();
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            int random = (int) (Math.random() * trees.size());

            selected.add(trees.get(random));
        }
        Population selectedPop = new Population(selected);
        selectedPop.variableData = variableData;
        selectedPop.fixedData = fixedData;
        return selectedPop.getMostFit();
    }

    // every other generation, only create 90% of population size
    // fill rest with mutations from nextgen

    // method to produce the next generation and return the fittest in the
    // generation
    public void nextGen() {
        // declare new population of trees
        List<FunTree> nextGen = new ArrayList<FunTree>();

        // loop until new population is fulled
        while (nextGen.size() < populationSize) {
            // run tournament selection to get one tree
            FunTree selected = tournament();
            // run tournament to get another tree and do crossover operation
            // and add offspring to new population
            FunTree mate = tournament();
            // mate.selected++;
            FunTree[] children = selected.crossover(mate);
            nextGen.add(children[0]);
            if (nextGen.size() < populationSize) {
                nextGen.add(children[1]);
            }

            if ((int) (Math.random() * 4) == 0 && nextGen.size() < populationSize) {
                // add mutated offspring to new population
                FunTree mutated = selected.mutation();
                nextGen.add(mutated);
            }
        }

        // set population to nextGen
        population.removeAll(population);
        population.addAll(nextGen);
    }
}
