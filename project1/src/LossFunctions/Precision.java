package LossFunctions;

import java.util.ArrayList;

public class Precision {
    public ArrayList<Integer[]> results;

    public Precision(ArrayList<Integer[]> results){
        this.results = results;
    }

    public int truePositives = 0;
    public int falsePositives = 0;
    public double precision;

    public ArrayList<Integer> getClasses(){
        ArrayList<Integer> classes = new ArrayList<>();
        for(Integer[] result: results){
            if(!classes.contains(result[1])){
                classes.add(result[1]);
            }
        }
        return classes;
    }

    public void setTrueAndFalsePositives(ArrayList<Integer> classes, int _class){
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
}
