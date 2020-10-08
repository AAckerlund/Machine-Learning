package LossFunctions;

import java.util.ArrayList;

public class Precision {
    private ArrayList<Integer[]> results;
    private int truePositives = 0, falsePositives = 0;
    
    public Precision(ArrayList<Integer[]> results){
        this.results = results;
    }

    //retrieves all classes for the purpose of evaluating individual loss
    public ArrayList<Integer> getClasses(){
        ArrayList<Integer> classes = new ArrayList<>();
        for(Integer[] result: results){
            if(!classes.contains(result[1])){
                classes.add(result[1]);
            }
        }
        return classes;
    }

    //evaluates if true and false positives with respect to a given class
    public void setTrueAndFalsePositives(int _class){
        truePositives = 0;
        falsePositives = 0;
        for(Integer[] result: results){
            if(result[0] == _class){
                if(result[1] == _class){    //if the guess and correct class are the same
                    truePositives += 1;
                }
                else{
                    falsePositives += 1;    //if the given class was guessed, but the correct class was a different one
                }
            }
        }
    }

    //uses equation given in class for precision
    public double findPrecision(){
        if(truePositives + falsePositives == 0){
            return 0;
        }
        return (double) truePositives / (truePositives + falsePositives);
    }
    
    //getters and setters
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
    
    public int getFalsePositives()
    {
        return falsePositives;
    }
}
