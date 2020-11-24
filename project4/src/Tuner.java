import java.util.ArrayList;

public abstract class Tuner
{
    abstract void tune(ArrayList<Node> trainingSet, ArrayList<Node> tuningSet);
    abstract void tune(String dataSet);
}
