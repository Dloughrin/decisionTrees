package main;

import java.io.*;

public class Data
{
    private int[] index;
    private String[] DNA;
    private String[] classification;
    private int max;

    public int getMax() { return max; }
    public int getCurrentIndex(int i) { return index[i]; }
    public String getCurrentDNA(int i) { return DNA[i]; }
    public String getCurrentClass(int i) { return classification[i]; }
    public void setClassification(int i, String clas) { classification[i] = clas; }
    public Data(boolean training)
    {
        if (training)
        {
            index = new int[2000];
            DNA = new String[2000];
            classification = new String[2000];
            max = 2000;
            trainingData();
        }
        else
        {
            index = new int[5000];
            DNA = new String[5000];
            classification = new String[5000];
            testingData();
        }
    }
    public Data(int[] i, String[] dna, String[] cla, int m)
    {
        index = i;
        DNA = dna;
        classification = cla;
        max = m;
    }
    private void trainingData()
    {
        String csv = "./data/training.csv";
        String line = "";
        BufferedReader reader = null;

        try {

            reader = new BufferedReader(new FileReader(csv));
            int i = 0;

            while ((line = reader.readLine()) != null)
            {
                String[] temp = line.split(",");
                index[i] = Integer.parseInt(temp[0]);
                DNA[i] = temp[1];
                classification[i] = temp[2];
                max = i+1;
                i++;
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void testingData()
    {
        String csv = "./data/testing.csv";
        String line = "";
        BufferedReader reader = null;

        try {

            reader = new BufferedReader(new FileReader(csv));
            int i = 0;

            while ((line = reader.readLine()) != null)
            {
                String[] temp = line.split(",");
                index[i] = Integer.parseInt(temp[0]);
                DNA[i] = temp[1];
                classification[i] = null;
                max = i+1;
                i++;
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void writeOutDecisions(Data decisions)
    {
        String csv = "./data/decisions.csv";
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(csv);

            fileWriter.append("id,class\n");

            for(int i = 0; i < decisions.getMax(); i++)
            {
                fileWriter.append(Integer.toString(decisions.getCurrentIndex(i)));
                fileWriter.append(",");
                fileWriter.append(decisions.getCurrentClass(i));
                fileWriter.append("\n");
            }
        }
        catch (Exception e) {
        System.out.println("Error in CsvFileWriter !!!");
        e.printStackTrace();
    } finally {

        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error while flushing/closing fileWriter !!!");
            e.printStackTrace();
        }
    }

    }
}
