package LossFunctions;

import java.util.ArrayList;

public class Precision {
    private ArrayList<Integer[]> results;
    private int truePositives = 0, falsePositives = 0;
    private double precision;
    
    public Precision(ArrayList<Integer[]> results){
        this.results = results;
    }

    public ArrayList<Integer> getClasses(){
        ArrayList<Integer> classes = new ArrayList<>();
        for(Integer[] result: results){
            if(!classes.contains(result[1])){
                classes.add(result[1]);
            }
        }
        return classes;
    }

    public void setTrueAndFalsePositives(int _class){
        truePositives = 0;
        falsePositives = 0;
        for(Integer[] result: results){
            if(result[0] == _class){
                if(result[1] == _class){
                    truePositives += 1;
                }
                else{
                    falsePositives += 1;
                }
            }
        }
    }

    public double findPrecision(){
        if(truePositives + falsePositives == 0){
            return 0;
        }
        precision = (double) truePositives / (truePositives + falsePositives);
        return precision;
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
    
    public int getFalsePositives()
    {
        return falsePositives;
    }
    
    public void setFalsePositives(int falsePositives)
    {
        this.falsePositives = falsePositives;
    }
    
    public double getPrecision()
    {
        return precision;
    }
    
    public void setPrecision(double precision)
    {
        this.precision = precision;
    }
}
