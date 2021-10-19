import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class Genetic {
    // private static Comparator<FunTree> treeComparator = new Comparator<FunTree>() {
    //     @Override
    //     public int compare(FunTree f1, FunTree f2) {
    //         return (int) (f1.getFitness() - f2.getFitness());
    //     }
    // };

    private static int DATA_SIZE = 1000;
    private static int POPULATION_SIZE = 500;
    private static int TOURNAMENT_SIZE = 5;
    public static void main(String[] args) {
         FunTree test = new FunTree();
         System.out.println(test);
         System.out.println(test.getSize());
    //    // System.out.println(test.evaluate(1));
    //     System.out.println(test.getRandomNode());

        // FunTree mutated = test.mutation();
        // System.out.println(test);
        // System.out.println("\n" + mutated);
        // System.out.println("\n" + mutated.evaluate(1));

        // FunTree father = new FunTree();
        // FunTree mother = new FunTree();
        // FunTree child = father.crossover(mother);
        // System.out.println(father + "\n" + mother + "\n" + child);

        // read in our data
        Float[][] data = new Float[25000][2];
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader("./dataset1.csv"));
            String row = csvReader.readLine();
            int i = 0;
            while (row != null) {
                String[] rowS = row.split(",");
                if(!rowS[0].equals("x"))
                {
                    for(int j = 0; j < rowS.length; j++)
                    {
                        data[i][j] = Float.parseFloat(rowS[j]);
                        
                    }
                    i++;
                }
                row=csvReader.readLine();
            }
            csvReader.close();
        }
        catch(Exception e)
        {
            System.out.println("data not found");
        }
        
        //crossover add fitter of two children


        Float[][] selectedData = new Float[DATA_SIZE][2];
        for(int i = 0; i < DATA_SIZE; i++ )
        {
            for(int j=0; j<selectedData[i].length; j++)
            {
                selectedData[i][j] = data[i][j];
            }
        }

        FunTree.data = selectedData;
        // reserve part of the data as "future data points"
        // generate random, but valid trees in an array
       
        FunTree[] generation = new FunTree[POPULATION_SIZE];
        for(int i = 0; i <POPULATION_SIZE; i++)
        {
            generation[i] = new FunTree();
        }

        // set float value fittest greater than selected value
        FunTree fittestTree = getFittest(generation);
        float fittestVal = fittestTree.getFitness();
        int numGens = 0;

       // loop while fittest > some value
        while(fittestVal >= 0.5 && numGens <= 1000)
        {
            // get the fittest in new generation
            FunTree[] nextGen = nextGen(generation);
            fittestTree = getFittest(nextGen);
            fittestVal = fittestTree.getFitness();
            System.out.println(fittestTree);
            System.out.println(fittestVal);
            generation = nextGen;  
            numGens++;
        }
        FunTree.data = data;
        System.out.println(fittestTree);
        System.out.println(fittestTree.getFitness());
        

        // test if our returned expression is "over-fitted"
    }

      //method to get fittest in given array of trees
      private static FunTree getFittest(FunTree[] trees)
      {
          FunTree fittestTree = trees[0];
          float fittestValue = fittestTree.getFitness();
          for(int i = 1; i < trees.length; i++)
          {
              float fitness = trees[i].getFitness();
              if(fitness < fittestValue)
              {
                  fittestTree = trees[i];
                  fittestValue = fitness;
              }
          }
  
          return fittestTree;
      }
   

       // method to run tournament selection, returning a tree
    // pick N random trees from list and return the fittest
    private static FunTree tournament(FunTree[] trees)
    {
        FunTree[] selected = new FunTree[TOURNAMENT_SIZE];
        for(int i = 0; i < TOURNAMENT_SIZE; i++)
        {
            int random = (int) (Math.random() * trees.length);
            
            selected[i] = trees[random];
        }
        return getFittest(selected);
        
    }

    // method to produce the next generation and return the fittest in the generation
    private static FunTree[] nextGen(FunTree[] population)
    {
        // declare new population of trees
        FunTree[] nextGen = new FunTree[POPULATION_SIZE];

        //loop until new population is fulled
        for(int i = 0; i < population.length; i++)
        {
            // run tournament selection to get one tree
            FunTree selected = tournament(population);

            int tries = 0;
            //favor less complex solutions
            while(tries <= 3 && (selected.getDepth() > 5 || selected.getSize() > 12))
            {
                selected = tournament(population);
                tries++;
            }
            selected.selected++;
            if(selected.selected >= TOURNAMENT_SIZE && !selected.reproduced)
            {
                nextGen[i] = selected;
                selected.reproduced = true;
                i++;
            }
            //if 30% chance
            if((int) (Math.random() * 10) <= 3)
            {
                //run tournament to get another tree and do crossover operation
                //and add offspring to new population
                FunTree mate = tournament(population);
                mate.selected++;
                FunTree child = selected.crossover(mate);
                nextGen[i] = child;
                
            }
            else
            {
                //add mutated offspring to new population
                FunTree mutated = selected.mutation();
                nextGen[i] = mutated;
            }
        }

        //set population to nextGen
       return nextGen;
    }

    // private static void deepCopy(FunTree[] current, FunTree[] next)
    // {
    //     for(int i = 0; i <current.length; i++)
    //     {
    //         current[i].rootNode.replace(next[i].rootNode);
    //     }
    // }
}
