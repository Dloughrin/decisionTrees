package ID3;

import main.*;

public class ID3
{
    private Node trainingTree;
    private Data trainingData;
    private Entropy entropy;
    private double[] entropyList;
    private Data testingData;
    private Node currentTree;

    public ID3(Data examples)
    {
        trainingData = examples;
        entropy = new Entropy(trainingData);

        trainingTree = train(trainingData, entropy.getIgnoredAttributes());

        if(Tools.tree) trainingTree.print();
    }
    /*
    Train will create the tree used for deciding the values of the testing data.
        - Only option is Entropy, no Chi-square implemented
     */
    public Node train(Data currentData, boolean[] attributes)
    {
        Node root = new Node();
        int attribute;

        entropyList = entropy.getEntropyList(currentData);
            //list created to keep track of entropy values for each attribute
        entropy.setIgnoredAttributes(attributes);
            /*
            list created to keep track of previously used attributes.
            can't find a solution to the problem of never seeming to revert even when removing
              it from the entropy class completely.
             */

        /*
         the following tests check to see if any of the values are the only attribute
         remaining in the data.
        */
        if(testAttributes("EI", currentData))
        {
            root.classification = true;
            root.setClassification("EI");
            return root;
        }
        else if(testAttributes("IE", currentData))
        {
            root.classification = true;
            root.setClassification("IE");
            return root;
        }
        else if(testAttributes("N", currentData))
        {
            root.classification = true;
            root.setClassification("N");
            return root;
        }
        /*
         if there is anything in attributes that has not been used,
         then this value will be false and it will skip to making the tree
        */
        boolean entroEmpty = true;
        for(int i = 0; i < attributes.length; i++)
        {
            if(attributes[i]) entroEmpty = false;
        }
        if(entroEmpty)
        {
            //counted the amount of each target attribute in Entropy.
            // this will return a node with whichever value has the most
            // if there is no remaining attributes
            int t = -1;
            int count = -1;
            String te = "";
            for(int i = 0; i < entropy.targetCounts.length; i++)
            {
                if (entropy.targetCounts[i] > count)
                {
                    t = i;
                    count = entropy.targetCounts[i];
                }
            }
            if(t == 0) te = "IE";
            if(t == 1) te = "EI";
            if(t == 2) te = "N";

            root.classification = true;
            root.setClassification(te);
            return root;
        }
        /*
         find the best attribute to use based on its entropy value, then split
         and add the selected attribute to the 'used' tracker
         */
        attribute = findBestAttribute();
        attributes[attribute] = false;
        entropy.setIgnoredAttributes(attributes);
        entropyList = entropy.getEntropyList(currentData);

        root.setPos(attribute);
        Node[] children = new Node[Tools.attributeValues.length];

        /*
         creates 8 children nodes for a value
         if there is no data for certain characters, then it's a leaf node
         */
        for(int i = 0; i < Tools.attributeValues.length; i++)
        {
            children[i] = new Node(null, root, Tools.attributeValues[i], -1);
            Data tData = findSameAttribute(attribute,Tools.attributeValues[i], currentData);

            if(tData == null)
            {
                Node newChild = new Node();
                newChild.classification = true;
                int t = -1;
                int count = -1;
                for(int g = 0; g < entropy.targetCounts.length; g++)
                {
                    if (entropy.targetCounts[g] > count)
                    {
                        t = g;
                        count = entropy.targetCounts[g];
                    }
                }
                String te = "";
                if(t == 0) te = "IE";
                if(t == 1) te = "EI";
                if(t == 2) te = "N";
                //System.out.println("N: " + entropy.targetCounts[2] + " EI: " + entropy.targetCounts[2] + " IE: " + entropy.targetCounts[0] + " || te = " + te);

                newChild.setClassification(te);
                newChild.setParent(children[i]);
                if(children[i].children == null) children[i].children = new Node[1];
                children[i].children[0] = newChild;
            }
            else
            {
                Node newChild;
                newChild = train(tData, attributes);
                newChild.setParent(children[i]);
                if(children[i].children == null) children[i].children = new Node[1];
                children[i].children[0] = newChild;

                entropy.setIgnoredAttributes(attributes);
                entropyList = entropy.getEntropyList(currentData);
            }
        }
        root.children = children;
        return root;
    }
    public Data buildDecisionTree(Data testing)
    {
        testingData = testing;
        currentTree = trainingTree;

        for(int i = 0; i < testingData.getMax(); i++)
        {
            buildDecisionTree(i);
            currentTree = trainingTree;
        }

        return testingData;
    }
    private void buildDecisionTree(int index)
    {
        for (int i = 0; i < currentTree.children.length; i++)
        {
            if(currentTree.children[i] == null || currentTree.children[i].classification)
            {
                testingData.setClassification(index,currentTree.getClassStr());
                return;
            }
            if (testingData.getCurrentDNA(index).charAt(currentTree.getPos()) == currentTree.children[i].getDNA())
            {
                currentTree = currentTree.children[i].children[0];
                buildDecisionTree(index);
            }
        }
    }
    private boolean testAttributes(String attribute, Data currentdata)
    {
        for(int i = 0; i < currentdata.getMax(); i++)
        {
            if(!currentdata.getCurrentClass(i).equals(attribute)) return false;
        }
        return true;
    }
    private int findBestAttribute()
    {
        int lowestEntro = -1;
        double tester = 0;
        for(int i = 0; i < entropyList.length; i++)
        {
            if(entropyList[i] == -2)
            {
                continue;
            }
            else if(entropyList[i] < tester || lowestEntro == -1)
            {
                if(Tools.debug) System.out.println(entropyList[i]  + " < " + tester);
                lowestEntro = i;
                tester = entropyList[i];
            }
        }
        return lowestEntro;
    }
    private Data findSameAttribute(int index, char value, Data tData)
    {
        int[] in = new int[tData.getMax()];
        String[] dna = new String[tData.getMax()];
        String[] cla = new String[tData.getMax()];
        int max = 0;
        dna[0] = null;

        for(int i = 0; i < tData.getMax(); i++)
        {
            if(tData.getCurrentDNA(i).charAt(index) == value)
            {
                in[max] = tData.getCurrentIndex(i);
                dna[max] = tData.getCurrentDNA(i);
                cla[max] = tData.getCurrentClass(i);
                max++;
            }
        }

        if(dna[0] == null) return null;
        else return new Data(in,dna,cla,max);
    }
}
