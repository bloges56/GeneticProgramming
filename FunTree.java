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

    //method to print a tree
    @Override
    public String toString()
    {
        String builder = "";
        return buildTreeString(rootNode, builder, "", "");
    }

    //method to travers tree and build tree string
    private String buildTreeString(Node current, String builder, String padding, String pointer)
    {
        if(current == null)
        {
            return "";
        }
        String newPadding  = padding + "|  ";
        String left = buildTreeString(current.left, builder, newPadding, (current.right != null) ? "├──" : "└──");
        String right = buildTreeString(current.right, builder, newPadding, "└──"); 
        return builder + padding + pointer + current.toString() + "\n" + left + right;
        
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