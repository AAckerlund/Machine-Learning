import java.util.ArrayList;

public class BackPropagation {
    // Contains the backpropagation training algorithms for both regression and classification
    // Assuming using logistic functions as activation functions
    int maxIterations;
    boolean useMomentum;
    boolean isClassification;
    double learningRate;    // How quickly gradient descent changes the function values
    Network nn;             // The network that is being trained

    public BackPropagation(Network nn, int maxIterations, boolean useMomentum,
                           boolean isClassification, double learningRate) {
        // Constructor that only saves arguments about the type of problem and training method
        this.nn = nn;
        this.maxIterations = maxIterations;
        this.useMomentum = useMomentum;
        this.isClassification = isClassification;
        this.learningRate = learningRate;
    }

    // trains neural net on a shuffled training set until error does not improve (or until a set max)
    public void trainNetwork() {    //TODO: Implement this
        /*double bestError = Double.MAX_VALUE;
        double prevError = Double.MAX_VALUE - 1;

        int iteration = 0;

        double output = nn.feedForward().get(0).getValue(); //TODO: Implement this for multiple outputs
        prevError = calculateSError(output, target);

        // train using Gradient Descent repeatedly until either error does not improve or we reach a specified max iteration
        while ((prevError < bestError) || (iteration < maxIterations)) {
            bestError = prevError;
            prevError = calculateSError(output, target);
        }*/
    }

    private double calculateSError(double output,double target) {
        // Calculates squared error for regression
        return Math.pow(target-output, 2);
    }

    private void updateWeights() {
        nn.pushWeightUpdates();
    }

    // updates neural net on an example using backprop
    public void trainExample(Node example) {    // TODO: make this work for multiple output nodes (multi-class)
        double target = example.getId();    // stores the true value to be guessed
        Neuron biasNeuron = nn.getBiasNeuron();

        for (Neuron outputNeuron : nn.feedForward(example.getData())) { //start at outputs, this should work for multiple outputs
            double output = outputNeuron.getValue();

            //save delta value for calculating the delta rule in hidden nodes
            outputNeuron.setDelta(calculateDeltaLogistic(true, output, target, outputNeuron));

            for (Neuron prev : outputNeuron.getUpstream()) {
                double update = calculateWeightUpdate(true, output, target, outputNeuron, prev);
                prev.saveWeightUpdate(outputNeuron, update);    // save weight update to upstream neuron
            }

            //change bias node's weight towards the current node TODO: Might not need this since upstream probably covers it
            //double update = calculateWeightUpdate(true, output, target, outputNeuron, biasNeuron);
            //biasNeuron.saveWeightUpdate(outputNeuron, update);
        }

        ArrayList<ArrayList<Neuron>> hiddenLayers = nn.getHiddenLayers();
        //check if there are any hidden layers
        if (hiddenLayers != null) {
            //work backwards along the hidden layers to the beginning
            for (int i = hiddenLayers.size()-1; i > 0; i--) {
                for (Neuron currentNeuron : hiddenLayers.get(i)) {
                    double output = currentNeuron.getValue();

                    //save delta value for calculating the delta rule in hidden nodes
                    currentNeuron.setDelta(calculateDeltaLogistic(true, output, target, currentNeuron));

                    for (Neuron prev : currentNeuron.getUpstream()) {
                        double update = calculateWeightUpdate(true, output, target, currentNeuron, prev);
                        prev.saveWeightUpdate(currentNeuron, update);    // save weight update to upstream neuron
                    }
                }
            }

            //first hidden layer receives from the input layer
            for (Neuron currentNeuron : hiddenLayers.get(0)) {
                double output = currentNeuron.getValue();

                //save delta value for calculating the delta rule in hidden nodes
                currentNeuron.setDelta(calculateDeltaLogistic(true, output, target, currentNeuron));

                for (Neuron prev : currentNeuron.getUpstream()) {
                    double update = calculateWeightUpdate(true, output, target, currentNeuron, prev);
                    prev.saveWeightUpdate(currentNeuron, update);    // save weight update to upstream neuron
                }
            }
        }

    }

    private double calculateWeightUpdate(boolean outputLayer, double output, double target, Neuron n, Neuron precNeuron) {
        if (useMomentum) {  // add an extra momentum term to the weight update
            //TODO: Implement momentum
            return 0;
        }
        else {
            return -learningRate * calculateGradientLogistic(outputLayer, output, target, n, precNeuron);
        }
    }

    private double calculateGradientLogistic(boolean outputLayer, double output, double target, Neuron n, Neuron precNeuron) {
        // helper function to calculate "error" term delta, effective multiplies by the input
        return calculateDeltaLogistic(outputLayer, output, target, n) * precNeuron.getValue();
    }

    private double calculateDeltaLogistic(boolean outputLayer, double output, double target, Neuron n) {
        // helper function to calculate delta, the error term using input, output, error, and other deltas
        // use a different function for output layer, since they dont have weights in front
        if (outputLayer) {
            return -(target-output)*output*(1-output);  // error term for output neuron
        }
        else {
            ArrayList<Double> downstreamWeights = n.getWeights();
            ArrayList<Double> deltas = new ArrayList<>();
            for (Neuron neuron : n.getDownstream()) {
                // collect deltas from neurons in next layer
                deltas.add(neuron.getDelta());
            }

            // sum products of weights and deltas
            double sum = 0;
            for (int i = 0; i < downstreamWeights.size(); i++) {
                sum += downstreamWeights.get(i) * deltas.get(i);
            }

            return output * (1 - output) * sum;
        }
    }


}
