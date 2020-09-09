package LossFunctions;

import java.util.ArrayList;

public class Recall {
    public ArrayList<Integer[]> results;

    public Recall(ArrayList<Integer[]> results){
        this.results = results;
    }
    public int truePositives;
    public int falseNegatives;
    public double recall;

    public void setTruePositivesAndFalseNegatives(ArrayList<Integer> classes, int _class){
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
                    /*ystem.out.println("FALSE POSITIVE: ");
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
}
