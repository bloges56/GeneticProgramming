public class FunTree
{
    //static global array of operations
    public static String[] operations = {"add", "sub", "mul", "div"};

    //set depth max depth range
    private final int maxDepth = 5;

    //set range of constant for leaves
    private final int constantRange = 5;

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
        if((int)(Math.random() * maxDepth) <= depth + 1)
        {
            current.left = createRandomLeaf(constantRange);
        }
        //if operation, recurse
        else
        {
            current.left = createRandomOp();
            randomTree(current.left, ++depth);
        }

        //set right side

        //if leaf
         if((int)(Math.random() * maxDepth) <= depth + 1)
         {
             current.right = createRandomLeaf(constantRange);
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
            //reduce chances of 0
            int temp = (int)(Math.random() * range);
            if(temp == 0)
            {
                temp = (int)(Math.random() * range);
            }
            leafNode.constant = temp;
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

    //evaluate the tree at the root
    public float evaluate(float x)
    {
        return evaluateUtil(x, rootNode);
    }
 

    //method to evaluate tree with given X
    private float evaluateUtil(float x, Node current)
    {
        //if current node is null return 0
        if(current == null)
        {
            return 0;
        }

        //evaluate the left and right sides and input into operation method
        if(current.operation != null)
        {
            float left = evaluateUtil(x, current.left);
            float right = evaluateUtil(x, current.right);
            return doOperation(current.operation, left, right);
        }

        //if leaf and independentVar return x
        if(current.independentVar)
        {
            return x;
        }

        //if leaf constant, return constant
        return (float) current.constant;
    }

    //method to make a type of computation given a string
    private float doOperation(String op, float left, float right)
    {
        if(op.equals("add"))
        {
            return right + left;
        }

        else if(op.equals("sub"))
        {
            return left - right;
        }
        else if(op.equals("mul"))
        {
            return right * left;
        }

        if(right == 0)
        {
            return left;
        }

        return left / right;
    }

    
    //cross over method
    //Tree crosssover (Tree crossover)

    //mutation method
    //mutation()

    //method to return randomly selected node to be used by mutation and crossover

    //fitness function
        //take the area of difference from given data

}