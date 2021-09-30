public class Tree
{
    //static global array of operations
    public static String[] operations = {"add", "sub", "mul", "div"};

    //times reproduced member
    public int reproducedCount;

    //pointer to root node
    Node rootNode;

    //constructor
    public Tree()
    {
        rootNode = null;
    }

    //constructor with given root node
    public Tree(Node root)
    {
        rootNode = root;
    }

    //method to generate tree with random expression
    public void randomExp()
    {
        rootNode = createRandomOp();
        Node currentParent = rootNode;
        
        for(int i = 0; i < 5; i++)
        {
            if((int)(Math.random() * 5) <= currentParent.depth)
            {

            }
        }

    }

        //initialize root node using node constructor
            //if rand(0,2) == 0
                //set member variable to true
            //else
                //set constant to random int
        //else
            //set left and right to new nodes
            //choose random operation
                //generate random int i
                //operation = operations[i]


    //create random constant
    private Node createRandom(int range)
    {
        Node leafNode = new Node();
        leafNode.constant = (int)(Math.random() * range);
        return leafNode;
    }

    //create random constant
    private Node createRandomOp()
    {
        Node leafNode = new Node();
        leafNode.operation = operations[(int)(Math.random() * operations.length)];
        return leafNode;
    }

    //deconstruct to expression

    //cross over method
    //Tree crosssover (Tree crossover)

    //mutation method
    //mutation()

    //method to check percantage of independent variables


    //method to return randomly selected node

    //fitness function
        //take the area of difference from given data

}