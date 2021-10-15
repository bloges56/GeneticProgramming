import java.util.Comparator;
import java.util.List;

public class FunTree implements Comparator<FunTree>
{
    //static global array of operations
    private static String[] operations = {"add", "sub", "mul", "div"};

    public static List<Float[]> data;

    //set depth max depth range
    private final int maxDepth = 5;

    //set range of constant for leaves
    private final int constantRange = 5;

    //set range for selecting random node
    private final int randomNodeRange = 2;

    //set starting point for random node
    private final int randomNodeStart = 2;

    //declare mutation tree depth
    private final int mutationDepth = 3;

    //pointer to root node
    Node rootNode;

    //constructor
    public FunTree()
    {
        rootNode = createRandomOp();
        randomTree(rootNode, 0);
    }

    //constructor with given root node
    public FunTree(Node newRoot)
    {
        rootNode = new Node();
        rootNode.replace(newRoot);
    }

    //method to generate tree with random expression
    private void randomTree(Node current, int depth)
    {
       //if no starting point is given, just return a constant 
        if(current == null)
        {
            current = createRandomLeaf(constantRange);
        }

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

    //method to return a random node
    private Node createRandomNode()
    {
        if((int)(Math.random() * 2) == 0)
        {
            return createRandomLeaf(constantRange);
        }

        return createRandomOp();
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
    public FunTree crossover(FunTree mother)
    {
        //Tree[] crosssover (Tree crossover)
        //pick random node on this tree
        FunTree father = new FunTree(rootNode);
        Node randomNodeP1 = father.getRandomNode();

        //create new tree A and set root node to randomly selected node from this tree
        // FunTree subTreeP1 = new FunTree(randomNodeP1);

        //create new tree B and set root node to given tree root node
        FunTree child = new FunTree(mother.rootNode);
        

        //pick random node of B and set it to the A root Node
        Node randomNodeP2 = child.getRandomNode();
        randomNodeP2.replace(randomNodeP1);
        return child;
    }
    


    //mutation method
    public FunTree mutation()
    {
        FunTree mutateTree = new FunTree(rootNode);

        //create random sub tree from randomly selected node
        // Node randomNode = tempTree.getRandomNode();

        FunTree randomTree = new FunTree(createRandomNode());

        if(randomTree.rootNode.operation != null)
        {
            randomTree.randomTree(randomTree.rootNode, mutationDepth);
        }
        
        Node randomNode = mutateTree.getRandomNode();

        randomNode.replace(randomTree.rootNode);

        return mutateTree;
    }


        

    //method to return a randomly selected node to be used by mutation and crossover
    public Node getRandomNode()
    {
        int decrementer = (int)((Math.random() * randomNodeRange) + randomNodeStart);
        return getRandomNodeUtil(decrementer, rootNode);
    }


    //helper method to return a randomly selected node to be used by mutation and crossover
    private Node getRandomNodeUtil(int decrementer, Node current)
    {
        if(decrementer == 0)
        {
            return current;
        }
        if(current.operation == null)
        {
            return current;
        }
        if((int)Math.random() * 2 == 0)
        {
            return getRandomNodeUtil(decrementer--, current.left);
        }
        else
        {
            return getRandomNodeUtil(decrementer--, current.right);
        }
    }


    //fitness function
    //take the area of difference from given data
    public float getFitness()
    {
        float sum = 0.f;
        for(int i =0; i<data.size(); i++)
        {
            float actual = data.get(i)[1];
            float evaluated = evaluate(data.get(i)[0]);
            sum += Math.abs(actual - evaluated);
        }

        return sum;
    }
    
    public int compare(FunTree f, FunTree f1) {
        return (int) (f1.getFitness() - f.getFitness());
     }

}