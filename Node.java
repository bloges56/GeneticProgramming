public class Node {


    //pointers to left and right
    public Node left, right;

    //operation member
    public String operation;

    //depth member variable
    // public int depth;

    //independent boolean member variable
    public boolean independentVar = false;

    //constant int member variable
    public int constant;

    //constructor for a root Node
    public Node()
    {
        // depth = 0;
        left = right = null;
    }

    //constructor(Node parent)
    public Node(Node parent)
    {
        // depth = parent.depth++;
        left = right = null;
    }

    //method to initialize the constant
    public void setConstant(int value)
    {
        constant = value;
    }

    //method to set the independent var
    public void setIndependent(boolean val)
    {
        independentVar = val;
    }

    //method to set operation
    public void setOperation(String op)
    {
        operation = op;
    }

    //override toString
    @Override
    public String toString()
    {
        //check if leaf
        if(operation == null)
        {
            //check if independent var
            if(independentVar)
            {
                return "X";
            }
            else
            {
                return String.valueOf(constant);
            }
        }
        return operation;
    }
    //method to return if a node is a leaf node

}
