import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class Genetic {
    public static Comparator<FunTree> treeComparator = new Comparator<FunTree>() {
        @Override
        public int compare(FunTree f1, FunTree f2) {
            return (int) (f2.getFitness() - f1.getFitness());
        }
    };
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
        
        FunTree.data = data;
        // reserve part of the data as "future data points"
        // generate 100 random, but valid trees in a array
       
        PriorityQueue<FunTree> population = new PriorityQueue<>(treeComparator);
        for(int i = 0; i <100; i++)
        {
            population.add(new FunTree());
        }

        // set float value fittest greater than selected value
        FunTree fittestTree = population.peek();
        float fittestVal = fittestTree.getFitness();

        // loop while fittest > some value
        while(fittestVal >= 0.5)
        {
            // get the fittest in new generation
            fittestTree = nextGen(population);
            fittestVal = fittestTree.getFitness();
            System.out.println(fittestVal);
        }
        
        System.out.println(fittestTree);
        

        // test if our returned expression is "over-fitted"
    }

    // method to get fittest in given array of trees
    // private static FunTree getFittest(FunTree[] trees)
    // {
    //     PriorityQueue<FunTree> selected = new PriorityQueue<>();
    //     for(int i = 0; i < trees.length; i++)
    //     {
    //         selected.add(trees[i]);
    //     }

    //     return minTree;
    // }

    // method to run tournament selection, returning a tree
    // pick N random trees from list and return the fittest
    private static FunTree tournament(FunTree[] trees)
    {
        PriorityQueue<FunTree> selectedTrees = new PriorityQueue<>(treeComparator);
        for(int i = 0; i < 5; i++)
        {
            int random = (int) Math.random() * trees.length;
            selectedTrees.add(trees[random]);
        }

        return selectedTrees.remove();
        
    }

    // method to produce the next generation and return the fittest in the generation
    private static FunTree nextGen(PriorityQueue<FunTree> population)
    {
        // declare new population of trees
        PriorityQueue<FunTree> nextGen = new PriorityQueue<>(treeComparator);
        FunTree[] popArr = (FunTree[]) population.toArray();

        //loop until new population is fulled
        for(int i = 0; i < popArr.length; i+=2)
        {
            // run tournament selection to get one tree
            FunTree selected = tournament(popArr);
            nextGen.add(selected);
            // if 30% chance
            if((int)Math.random() * 10 <= 3)
            {
                //add mutated offspring to new population
                FunTree mutated = selected.mutation();
                nextGen.add(mutated);
            }
            else
            {
                //run tournament to get another tree and do crossover operation
                //and add offspring to new population
                FunTree mate = tournament(popArr);
                FunTree child = selected.crossover(mate);
                nextGen.add(child);
            }
        }

        //set the population to the next gen
        population = nextGen;

        // return the fittest of the new generation
        return nextGen.remove();
    }
    
}
