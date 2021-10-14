
public class Genetic {
    public static void main(String[] args)
    {
    //     FunTree test = new FunTree();
    //    System.out.println(test);
    // //    System.out.println(test.evaluate(1));
    // //    System.out.println(test.getRandomNode());

    //    FunTree mutated = test.mutation();
    //    System.out.println(test);
    //    System.out.println("\n" + mutated);
    //    System.out.println("\n" + mutated.evaluate(1));

    FunTree father = new FunTree();
    FunTree mother = new FunTree();
    FunTree child = father.crossover(mother);
    System.out.println(father + "\n" + mother + "\n" + child);
       //test.printTree();
        //read in our data
            //reserve part of the data as "future data points"
        //generate 100 random, but valid trees in a array

        //set float value fittest greater than selected value
        //loop while fittest > some value
            //get the next generation
            //get the fittest in new generation

        //test if our returned expression is "over-fitted"     
    }

    //method to get fittest in given array of trees

    //method to run tournament selection, returning a tree
        //pick N random trees from list and return the fittest
            //increment times generation lived
            //if times reproduced > random(5)
                //"kill it"

    //method to produce the next generation
        //declare new population of trees
        //loop new population >= current population or until current population is below certain level
            //run tournament selection to get one tree
            //if 30% chance
                //add mutated offspring to new population
            //else
                //run tournament to get another tree and do crossover operation 
                //and add offspring to new population
        //return the new generation
}
