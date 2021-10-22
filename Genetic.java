import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Genetic {

    private static int DATA_SIZE = 25000;
    private static int GENERATIONS = 100;
    private static int POPULATIONS = 5;
    public static void main(String[] args) {
    // read in our data

        float[][] data = new float[25000][4];
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

        float[][] selectedData = new float[500][4];
        for(int i = 0; i< 500; i++)
        {
            selectedData[i] = data[i];
        }

        Population[] populations = new Population[POPULATIONS];

        for(int i = 0; i < 5; i++)
        {
            populations[i] = new Population();
            float[][] constant = new float[DATA_SIZE/6][4];
            float[][] variable = new float[DATA_SIZE/6][4];
            int size = DATA_SIZE/3;
            int start = i*size;
            for(int j = 0; j<constant.length; j++)
            {
                constant[j] = data[start + 2 * j];
                variable[j] = data[start + 2 * j + 1];
            }

            populations[i].fixedData = constant;
            populations[i].variableData = variable;
        }

        for(int count = 0; count<GENERATIONS; count++)
        {
            for(int i = 0; i <populations.length; i++)
            {
                populations[i].nextGen();
            }
            if(count%10 == 0)
            {
                for(int i = 0; i<populations.length; i++)
                {
                    if(i == populations.length - 1)
                    {
                        populations[i].migrate(populations[0]);
                    }
                    else
                    {
                        populations[i].migrate(populations[i+1]);
                    }
                }
            }
            List<FunTree> fittest = new ArrayList<FunTree>();
            for(int i = 0; i<populations.length; i++)
            {
                fittest.add(populations[i].getMostFit());
            }

            Population fittestPop = new Population(fittest);
            fittestPop.fixedData = data;
            fittestPop.variableData = data;
            FunTree fittestTree = fittestPop.getMostFit();
            System.out.println(fittestTree);
            System.out.println(fittestTree.getFitness(selectedData));
        }   
    }
}
