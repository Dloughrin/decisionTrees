package ID3;

import main.*;

public class Entropy
{
    public int[] targetCounts;

    private double[][] tValueCounts; // keep track of each attribute+value combination
    private double[] dataCount; // keep track of total entries at each attribute
    private Data testData;
    private double[] entro; // keep track of entropy at each attribute
    private boolean[] ignoredAttributes;

    public void setIgnoredAttributes(boolean[] ia) { ignoredAttributes = ia; }
    public boolean[] getIgnoredAttributes() { return ignoredAttributes; }
    public double[] getEntropyList(Data tData)
    {
        for(int i = 0; i < tData.getCurrentDNA(0).length(); i++)
        {
            entro[i] = 0;
            dataCount[i] = 0;
            for(int g = 0; g < Tools.attributeValues.length; g++)
            {
                tValueCounts[i][g] = 0;
            }
        }
        countValues();
        for (int i = 0; i < tData.getCurrentDNA(0).length(); i++) {

            for (int g = 0; g < Tools.attributeValues.length; g++) {
                double probability;
                if (dataCount[i] == 0) probability = 0;
                else probability = tValueCounts[i][g] / dataCount[i];

                if (!ignoredAttributes[i])
                {
                    entro[i] = -2;
                    break;
                }
                else if (probability != 0 && ignoredAttributes[i])
                {
                    entro[i] += -probability * Log_2(probability);
                }
            }
        }
        return entro;
    }

    public Entropy(Data tData)
    {
        targetCounts = new int[]{0,0,0};
        testData = tData;
        dataCount = new double[tData.getCurrentDNA(0).length()];
        entro = new double[tData.getCurrentDNA(0).length()];
        ignoredAttributes = new boolean[tData.getCurrentDNA(0).length()];

        tValueCounts = new double[tData.getCurrentDNA(0).length()][Tools.attributeValues.length];

        for(int i = 0; i < tData.getCurrentDNA(0).length(); i++)
        {
            for(int g = 0; g < Tools.attributeValues.length; g++)
            {
                tValueCounts[i][g] = 0;
            }
            ignoredAttributes[i] = true;
            entro[i] = 0;
            dataCount[i] = 0;
        }
    }
    public void countValues()
    {
        int i = 0, g = 0, v = 0;
        while(i < testData.getMax())
        {
            while(g < testData.getCurrentDNA(i).length())
            {
                while (v < tValueCounts[g].length)
                {
                    if(!ignoredAttributes[g])
                    {
                        break;
                    }
                    if (testData.getCurrentDNA(i).charAt(g) == Tools.attributeValues[v])
                    {
                        tValueCounts[g][v]++;
                        dataCount[g]++;
                    }
                    v++;
                }
                v = 0;
                g++;
            }
            if(testData.getCurrentClass(i).equals("IE"))
            {
                targetCounts[0]++;
            }
            else if(testData.getCurrentClass(i).equals("EI"))
            {
                targetCounts[1]++;
            }
            else
            {
                targetCounts[2]++;
            }
            g = 0;
            i++;
        }
    }
    private double Log_2(double n)
    {
        return (float)Math.log(n) / (float)Math.log(2);
    }
}
