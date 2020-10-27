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
        System.out.println("Errors from Learning Rates: ");
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
        System.out.println("Errors from Momentums: ");
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
            backPropForTuning.nn.hiddenLayers = hiddenLayers;
            double error = backPropForTuning.trainNetwork(trainingSet);
            errors.add(error);
            if(error < lowestError){
                lowestError = error;
                optimalNodesPerLayerNum = hiddenLayer;
            }
        }
        System.out.println("Errors from Number of Hidden Layer Nodes: ");
        for(double error: errors){
            System.out.println(error);
        }
        return optimalNodesPerLayerNum;
    }
}