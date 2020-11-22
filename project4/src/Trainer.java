import java.util.ArrayList;

public abstract class Trainer
{
    public Trainer()
    {

    }
    abstract void train(ArrayList<Node> trainingSet);
    abstract double bestMSE();
}
