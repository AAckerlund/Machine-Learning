public class RunWithTuning {
    public String tuningTarget;
    public String method;
    public double[] learningRates;
    public double[] momentums;
    public int[] nodesPerLayerNum;

    public RunWithTuning(String tuningTarget, String method) {
        this.tuningTarget = tuningTarget;
        this.method = method;
    }

    public double runClassificationLearningRate() {
        double optimalLearningRate = learningRates[0];
        for (double learningRate : learningRates) {
            //TODO: run backpropagation for classification and get performance from CrossEntropy
            //TODO: find best learning rate from performance
        }
        return optimalLearningRate;
    }

    public double runClassificationmomentum() {
        double optimalMomentum = momentums[0];
        for (double momentum : momentums) {
            //TODO: run backpropagation for classification and get performance from CrossEntropy
            //TODO: find best momentum from performance
        }
        return optimalMomentum;
    }

    public int runClassificationNodesPerLayerNum() {
        int optimalNodesPerLayerNum = nodesPerLayerNum[0];
        for (double nodeNum : nodesPerLayerNum) {
            //TODO: run backpropagation for classification and get performance from CrossEntropy
            //TODO: find best node number from performance
        }
        return optimalNodesPerLayerNum;
    }


    public double runRegressionLearningRate() {
        double optimalLearningRate = learningRates[0];
        for (double learningRate : learningRates) {
            //TODO: run backpropagation for regression and get performance from Mean Squared Error
            //TODO: find best learning rate from performance
        }
        return optimalLearningRate;
    }

    public double runRegressionMomentum() {
        double optimalMomentum = momentums[0];
        for (double momentum : momentums) {
            //TODO: run backpropagation for regression and get performance from Mean Squared Error
            //TODO: find best momentum from performance
        }
        return optimalMomentum;
    }

        public int runRegressionNodesPerLayerNum(){
            int optimalNodesPerLayerNum = nodesPerLayerNum[0];
            for (double nodeNum : nodesPerLayerNum) {
                //TODO: run backpropagation for regression and get performance from Mean Squared Error
                //TODO: find best node number from performance
            }
            return optimalNodesPerLayerNum;
        }
}
