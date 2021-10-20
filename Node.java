public class Node {
    

    //pointers to left and right
    public Node left, right;

    //operation member
    public String operation;

    //depth member variable
    // public int depth;

    //independent boolean member variable
    public boolean independentVar = false;

    public boolean[] x = new boolean[3];

    //constant int member variable
    public float constant;

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

    public void replace(Node copyFrom)
    {
        if(copyFrom.left == null)
        {
            left = null;
        }
        else
        {
            if(left == null)
            {
                left = new Node();
            }
            left.replace(copyFrom.left);
        }
        if(copyFrom.right == null)
        {
            right = null;
        }
        else
        {
            if(right == null)
            {
                right = new Node();
            }
            right.replace(copyFrom.right);
        }
        constant = copyFrom.constant;
        independentVar = copyFrom.independentVar;
        operation = copyFrom.operation;
        x[0] = copyFrom.x[0];
        x[1] = copyFrom.x[1];
        x[2] = copyFrom.x[2];
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
                for(int i =0; i<x.length; i++)
                {
                    if(x[i])
                    {
                        if(i == 0)
                        {
                            return "X";
                        }
                        if(i == 1)
                        {
                            return "Y";
                        }
                        return "Z";
                    }
                }
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
