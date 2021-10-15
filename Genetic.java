import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class Genetic {
    public static void main(String[] args) {
        // FunTree test = new FunTree();
        // System.out.println(test);
        // // System.out.println(test.evaluate(1));
        // // System.out.println(test.getRandomNode());

        // FunTree mutated = test.mutation();
        // System.out.println(test);
        // System.out.println("\n" + mutated);
        // System.out.println("\n" + mutated.evaluate(1));

        // FunTree father = new FunTree();
        // FunTree mother = new FunTree();
        // FunTree child = father.crossover(mother);
        // System.out.println(father + "\n" + mother + "\n" + child);

        // read in our data
        List<Float[]> data = new ArrayList<>();
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader("./dataset1.csv"));
            String row = csvReader.readLine();
            while (row != null) {
                String[] rowS = row.split(",");
                if(!rowS[0].equals("x"))
                {
                    Float[] rowF = new Float[2];
                    for(int i = 0; i < rowS.length; i++)
                    {
                        rowF[i] = Float.parseFloat(rowS[i]);
                    }
                    data.add(rowF);
                }
                row=csvReader.readLine();
            }
            csvReader.close();
        }
        catch(Exception e)
        {
            System.out.println("data not found");
        }
        
        // reserve part of the data as "future data points"
        // generate 100 random, but valid trees in a array
        FunTree[] population = new FunTree[100];
        for(int i = 0; i < population.length; i++)
        {
            population[i] = new FunTree();
        }

        // set float value fittest greater than selected value
        FunTree fittestTree = getFittest(population, data);
        float fittestVal = fittestTree.getFitness(data);

        // loop while fittest > some value
        while(fittestVal >= 0.5)
        {
            // get the fittest in new generation
            fittestTree = nextGen(population, data);
            fittestVal = fittestTree.getFitness(data);
            System.out.println(fittestVal);
        }
        
        System.out.println(fittestTree);
        

        // test if our returned expression is "over-fitted"
    }

    // method to get fittest in given array of trees
    private static FunTree getFittest(FunTree[] trees, List<Float[]> data)
    {
        float minVal = 0.f;
        FunTree minTree = new FunTree();
        float curFitness;
        for(int i = 0; i < trees.length; i++)
        {
            curFitness = trees[i].getFitness(data);
            if(curFitness <= minVal)
            {
                minTree.rootNode.replace(trees[i].rootNode);
                minVal = curFitness;
            }
        }

        return minTree;
    }

    // method to run tournament selection, returning a tree
    // pick N random trees from list and return the fittest
    private static FunTree tournament(FunTree[] trees, List<Float[]> data)
    {
        FunTree[] selectedTrees = new FunTree[5];
        for(int i = 0; i < 5; i++)
        {
            int random = (int) Math.random() * trees.length;
            selectedTrees[i] = trees[random];
        }

        return getFittest(selectedTrees, data);
        
    }

    // method to produce the next generation and return the fittest in the generation
    private static FunTree nextGen(FunTree[] population, List<Float[]> data)
    {
        // declare new population of trees
        FunTree[] nextGen = new FunTree[population.length];

        //track fittest tree
        FunTree fittestTree = new FunTree();
        float fittestVal = Float.MAX_VALUE;
        //loop until new population is fulled
        for(int i = 0; i < nextGen.length; i+=2)
        {
            // run tournament selection to get one tree
            FunTree selected = tournament(population, data);
            nextGen[i] = selected;
            float fitness = selected.getFitness(data);
            if(fitness <= fittestVal)
            {
                fittestVal = fitness;
                fittestTree.rootNode.replace(selected.rootNode);
            }
            // if 30% chance
            if((int)Math.random() * 10 <= 3)
            {
                //add mutated offspring to new population
                FunTree mutated = selected.mutation();
                nextGen[i+1] = mutated;
                float mutatedFitness = mutated.getFitness(data);
                if(mutatedFitness <= fittestVal)
                {
                    fittestVal = mutatedFitness;
                    fittestTree.rootNode.replace(mutated.rootNode);
                }
            }
            else
            {
                //run tournament to get another tree and do crossover operation
                //and add offspring to new population
                FunTree mate = tournament(population, data);
                FunTree child = selected.crossover(mate);
                nextGen[i+1] = child;
                float childFitness = child.getFitness(data);
                if(childFitness <= fittestVal)
                {
                    fittestVal = childFitness;
                    fittestTree.rootNode.replace(child.rootNode);
                }
            }
        }

        //set the population to the next gen
        population = nextGen;

        // return the new generation
        return fittestTree;
    }
    
}
