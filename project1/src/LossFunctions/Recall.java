package LossFunctions;

import java.util.ArrayList;

public class Recall {
    private ArrayList<Integer[]> results;
    private int truePositives, falseNegatives;
    private double recall;
    
    public Recall(ArrayList<Integer[]> results){
        this.results = results;
    }

    public void setTruePositivesAndFalseNegatives(int _class){
        truePositives = 0;
        falseNegatives = 0;
        for(Integer[] result: results){
            if(result[0] == _class){
                if(result[1] == _class){
                    truePositives += 1;
                }
            }
            if(result[0] != _class){
                if(result[1] == _class){
                    /*System.out.println("FALSE POSITIVE: ");
                    System.out.println(result[1]);
                    System.out.println(result[0]);
                    System.out.println("FALSE POSITIVE: ");*/
                    falseNegatives += 1;
                }
            }
        }
    }

    public double findRecall(){
        recall = (double) truePositives / (truePositives + falseNegatives);
        return recall;
    }
    
    public ArrayList<Integer[]> getResults()
    {
        return results;
    }
    
    public void setResults(ArrayList<Integer[]> results)
    {
        this.results = results;
    }
    
    public int getTruePositives()
    {
        return truePositives;
    }
    
    public void setTruePositives(int truePositives)
    {
        this.truePositives = truePositives;
    }
    
    public int getFalseNegatives()
    {
        return falseNegatives;
    }
    
    public void setFalseNegatives(int falseNegatives)
    {
        this.falseNegatives = falseNegatives;
    }
    
    public double getRecall()
    {
        return recall;
    }
    
    public void setRecall(double recall)
    {
        this.recall = recall;
    }
}
