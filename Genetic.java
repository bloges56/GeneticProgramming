import java.io.BufferedReader;
import java.io.FileReader;

public class Genetic {

    private static int DATA_SIZE = 500;
    private static int POPULATION_SIZE = 1500;
    private static int TOURNAMENT_SIZE = 5;
    public static void main(String[] args) {
    // read in our data
    
        Float[][] data = new Float[25000][4];
        try{
            BufferedReader csvReader = new BufferedReader(new FileReader("./dataset2.csv"));
            String row = csvReader.readLine();
            int i = 0;
            while (row != null) {
                String[] rowS = row.split(",");
                if(!rowS[0].equals("x1"))
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
        
    //      //crossover add fitter of two children


        Float[][] selectedData = new Float[DATA_SIZE][4];
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

       FunTree[] fittestTrees = new FunTree[10];
       for(int runs = 0; runs < 10; runs++)
       {
        FunTree[] generation = new FunTree[POPULATION_SIZE];
        for(int i = 0; i <POPULATION_SIZE; i++)
        {
            generation[i] = new FunTree();
        }

           // set float value fittest greater than selected value
        FunTree fittestTree = getFittest(generation);
        float fittestVal = fittestTree.getFitness();

        // loop while fittest > some value
        while(fittestVal >= 30)
        {
            // get the fittest in new generation
            FunTree[] nextGen = nextGen(generation);
            fittestTree = getFittest(nextGen);
            fittestVal = fittestTree.getFitness();
            System.out.println(fittestTree);
            System.out.println(fittestVal);
            generation = nextGen;  
        }

        fittestTrees[runs] = fittestTree;
       }
        
       FunTree bestTree = getSmallest(fittestTrees);

         // test if our returned expression is "over-fitted"
        FunTree.data = data;
        System.out.println(bestTree);
        System.out.println(bestTree.getFitness());
        
        
    }

    private static FunTree getSmallest(FunTree[] trees)
    {
        FunTree smallestTree = trees[0];
        int smallestDepth = trees[0].getDepth();
        for(int i = 1; i < trees.length; i++)
        {
            int depth = trees[i].getDepth();
            if(depth < smallestDepth)
            {
                smallestTree = trees[i];
                smallestDepth = depth;
            }
            else if(depth == smallestDepth)
            {
                if(trees[i].getSize() < smallestTree.getSize())
                {
                    smallestTree = trees[i];
                }
            }
        }
  
          return smallestTree;
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
            while(tries <= 5 && (selected.getDepth() > 8 || selected.getSize() > 30))
            {
                selected = tournament(population);
                tries++;
            }
            selected.selected++;
            if(selected.selected >= TOURNAMENT_SIZE && !selected.reproduced)
            {
                nextGen[i] = selected;
                selected.reproduced = true;
                continue;
            }
            // //if 30% chance
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
}
