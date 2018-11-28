package main;

import ID3.*;

public class Main
{
    public static void main(String[] args)
    {
        //Load the data files
	    Data training = new Data(true);
	    Data testing = new Data(false);
	    Data decisions;

        //Build the tree and make new data
	    ID3 id3 = new ID3(training);
	    decisions = id3.buildDecisionTree(testing);

	    //Create output file
	    Data.writeOutDecisions(decisions);
    }
}
