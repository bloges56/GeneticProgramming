public class FunTree
{
    //static global array of operations
    private static String[] operations = {"add", "sub", "mul", "div"};

    public static Float[][] data;

    //set depth max depth range
    private final int maxDepth = 4;

    //set range of constant for leaves
    private final int constantRange = 10;

    //declare mutation tree depth
    private final int mutationDepth = 4;

    //chance to be independent var
    private final int independentVarChance = 2;

    //track the number of times this tree has been selected
    public int selected;

    //track if this tree has been reproduced
    public boolean reproduced;

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

    //constructor for duplicating a FunTree
    public FunTree(FunTree tree)
    {
        rootNode = new Node();
        rootNode.replace(tree.rootNode);
    }

    //method to generate tree with random expression
    private void randomTree(Node current, int depth)
    {
       //if no starting point is given, just return a constant 
        if(current == null)
        {
            current = createRandomLeaf(constantRange);
        }

        if(current.operation == null)
        {
            return;
        }

        //set left side

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

        //set right side

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
        if((int)(Math.random() * independentVarChance) == 0)
        {
            leafNode.independentVar = true;
        }
        else
        {
            leafNode.constant = (int)(Math.random() * range - range / 2.f);
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
        if(op.equals("div"))
        {
            if(right == 0.f)
            {
                return 1;
            }

            return left / right;
        }

        if(op.equals("add"))
        {
            return right + left;
        }

        if(op.equals("sub"))
        {
            return left - right;
        }

        if(op.equals("mul"))
        {
            return right * left;
        }

        return 1;
    }

    
    //cross over method
    public FunTree crossover(FunTree mother)
    {
        //Tree[] crosssover (Tree crossover)
        //pick random node on this tree
        FunTree child1 = new FunTree(rootNode);
        Node randomNodeP1 = child1.getRandomNode();


        //create new tree B and set root node to given tree root node
        FunTree child2 = new FunTree(mother.rootNode);
        

        //pick random node of B and set it to the A root Node
        Node randomNodeP2 = child2.getRandomNode();
        Node temp = new Node();
        temp.replace(randomNodeP2);

        randomNodeP2.replace(randomNodeP1);
        randomNodeP1.replace(temp);

        if(child1.getFitness() < child2.getFitness())
        {
            return child1;
        }

        return child2;
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


    public Node getRandomNode()
    {
        int depth = getDepth();
        return getRandomNodeUtil(rootNode, 0, depth);
    }

    public Node getRandomNodeUtil(Node current, int depth, int treeDepth)
    {
        if(current.operation == null)
        {
            return current;
        }
        if((int)(Math.random() * 10) <= 2 * treeDepth)
        {
            return current;
        }
        if(Math.round(Math.random()) == 0)
        {
            return getRandomNodeUtil(current.right, depth++, treeDepth);
        }
        return getRandomNodeUtil(current.left, depth++, treeDepth);
    }

    public int getDepth()
    {
        return getDepthUtil(rootNode);
    }

    private int getDepthUtil(Node current)
    {
        if(current.operation == null)
        {
            return 0;
        }

        int depthLeft = 1 + getDepthUtil(current.left);
        int depthRight = 1 + getDepthUtil(current.right);
        if(depthLeft > depthRight)
        {
            return depthLeft;
        }
        return depthRight;
    }

    public int getSize()
    {
        return getSizeUtil(rootNode);
    }

    private int getSizeUtil(Node current)
    {
        if(current == null)
        {
            return 0;
        }

        return 1 + getSizeUtil(current.left) + getSizeUtil(current.right);
    }

    //fitness function
    public float getFitness()
    {
        float sum = 0.f;
        for(int i =0; i<data.length; i++)
        {
            float actual = data[i][1];
            float evaluated = evaluate(data[i][0]);
            sum += Math.abs(actual - evaluated);
        }

        return sum;
    }
}