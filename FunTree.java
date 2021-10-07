public class FunTree
{
    //static global array of operations
    public static String[] operations = {"add", "sub", "mul", "div"};

    //times reproduced member
    public int reproducedCount;

    //pointer to root node
    Node rootNode;

    //constructor
    public FunTree()
    {
        rootNode = createRandomOp();
        randomTree(rootNode, 0);
        System.out.println("Tree made");

    }

    //constructor with given root node
    public FunTree(Node root)
    {
        rootNode = root;
    }

    //method to generate tree with random expression
    private void randomTree(Node current, int depth)
    {
        
        //set left side

        //if leaf
        if((int)(Math.random() * 5) <= depth + 1)
        {
            current.left = createRandomLeaf(4);
        }
        //if operation, recurse
        else
        {
            current.left = createRandomOp();
            randomTree(current.left, ++depth);
        }

        //set right side

        //if leaf
         if((int)(Math.random() * 8) <= depth + 1)
         {
             current.right = createRandomLeaf(4);
         }
         //if operation, recurse
         else
         {
             current.right = createRandomOp();
             randomTree(current.right, ++depth);
         }
    }


    //create random constant
    private Node createRandomLeaf(int range)
    {
        Node leafNode = new Node();
        //chance to be independent variable
        if((int)(Math.random() * 5) == 0)
        {
            leafNode.independentVar = true;
        }
        else
        {
            leafNode.constant = (int)(Math.random() * range);
        }
        return leafNode;
    }

    //create random constant
    private Node createRandomOp()
    {
        Node leafNode = new Node();
        leafNode.operation = operations[(int)(Math.random() * operations.length)];
        return leafNode;
    }

    @Override
    //method to print tree
    public String toString()
    {
        String treeRep = "";
        buildTreeString(rootNode, treeRep);
        return treeRep;
    }

    //method to travers tree and build tree string
    private String buildTreeString(Node current, String builder)
    {
        if(current == null)
        {
            return builder;
        }
        builder = builder.concat(current.toString());
        builder = builder.concat("\n");
        buildTreeString(current.left, builder);
        buildTreeString(current.right, builder);
        return builder;
        
    }

    public void printTree()
    {
        traverseTree(rootNode);
    }


    private void traverseTree(Node current)
    {
        if(current != null)
        {
            System.out.println(current);
            traverseTree(current.left);
            traverseTree(current.right);
        }
    }

 

    //method to evaluate tree with given X

    //cross over method
    //Tree crosssover (Tree crossover)

    //mutation method
    //mutation()

    //method to check percantage of independent variables


    //method to return randomly selected node

    //fitness function
        //take the area of difference from given data

}