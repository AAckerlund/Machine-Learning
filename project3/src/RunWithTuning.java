import java.util.ArrayList;

public class RunWithTuning {
    public BackPropagation backPropForTuning;
    ArrayList<Node> trainingSet;
    public double[] learningRates;
    public double[] momentums;
    public int[] hiddenLayerNodeNums;

    public RunWithTuning(ArrayList<Node> trainingSet, double[] learningRates, double[] momentums, int[] hiddenLayerNodeNums) {
        this.trainingSet = trainingSet;
        this.learningRates = learningRates;
        this.momentums = momentums;
        this.hiddenLayerNodeNums = hiddenLayerNodeNums;
    }
    
    public void tune()
    {
        for(int i = 0; i < momentums.length; i++)
        {
            for(int j = 0; j < learningRates.length; j++)
            {
                if(hiddenLayers.size() > 0)
                {
                    for(int k = 0; k < hiddenLayers.size(); k++)
                    {
        
                    }
                }
                else
                {
                
                }
            }
        }
    }

    public double runLearningRate() {
        double optimalLearningRate = learningRates[0];
        double lowestError = Double.POSITIVE_INFINITY;
        ArrayList<Double> errors = new ArrayList<>();
        for (double learningRate : learningRates) {
            backPropForTuning.learningRate = learningRate;
            double error = backPropForTuning.trainNetwork(trainingSet);
            errors.add(error);
            if(error < lowestError){
                lowestError = error;
                optimalLearningRate = learningRate;
            }
        }
        System.err.println("Errors from Learning Rates: ");
        for(double error: errors){
            System.out.println(error);
        }
        return optimalLearningRate;
    }

    public double runMomentum() {
        double optimalMomentum = momentums[0];
        double lowestError = Double.POSITIVE_INFINITY;
        ArrayList<Double> errors = new ArrayList<>();
        for (double momentum : momentums) {
            backPropForTuning.momentumScale = momentum;
            double error = backPropForTuning.trainNetwork(trainingSet);
            errors.add(error);
            if(error < lowestError){
                lowestError = error;
                optimalMomentum = momentum;
            }
        }
        System.err.println("Errors from Momentums: ");
        for(double error: errors){
            System.out.println(error);
        }
        return optimalMomentum;
    }

    public ArrayList<Neuron> runNodesPerLayerNum() {
        ArrayList<Neuron> optimalNodesPerLayerNum = hiddenLayers.get(0);
        double lowestError = Double.POSITIVE_INFINITY;
        ArrayList<Double> errors = new ArrayList<>();
        for (ArrayList<Neuron> hiddenLayer : hiddenLayers) {
            backPropForTuning.nn.setHiddenLayers(hiddenLayers);
            double error = backPropForTuning.trainNetwork(trainingSet);
            errors.add(error);
            if(error < lowestError){
                lowestError = error;
                optimalNodesPerLayerNum = hiddenLayer;
            }
        }
        System.err.println("Errors from Number of Hidden Layer Nodes: ");
        for(double error: errors){
            System.out.println(error);
        }
        return optimalNodesPerLayerNum;
    }
}