import java.util.ArrayList;
import java.util.HashMap;

public class Network
{
    private ArrayList<Neuron> neurons, inputLayer, outputLayer;
    private ArrayList<ArrayList<Neuron>> hiddenLayers = null;
    private Neuron biasNeuron;
    private boolean isClassification;

    HashMap<Neuron, Double> outputToClass;  // HashMap for mapping an output neuron to its corresponding class

    public Network(int inputLayerNodeNum, int[] hiddenLayerNodeNums, double[] outputLayerClasses, boolean isClassification)
    {
        this.isClassification = isClassification;
        neurons = new ArrayList<>();
        inputLayer = new ArrayList<>();
        Neuron tmp;
        for(int i = 0; i < inputLayerNodeNum; i++)
        {
            tmp = new Neuron(0);
            inputLayer.add(tmp);
            neurons.add(tmp);
        }

        if(hiddenLayerNodeNums.length > 0)//there are hidden layers
        {
            hiddenLayers = new ArrayList<>();
            for(int i = 0; i < hiddenLayerNodeNums.length; i++)
            {
                hiddenLayers.add(new ArrayList<>());
                for(int j = 0; j < hiddenLayerNodeNums[i]; j++)
                {
                    tmp = new Neuron();
                    hiddenLayers.get(i).add(tmp);
                    neurons.add(tmp);
                }
            }
        }

        outputLayer = new ArrayList<>();
        if (!isClassification)
        { // For regression, just use one output neuron
            tmp = new Neuron();
            outputLayer.add(tmp);
            neurons.add(tmp);
        }
        else {
            outputToClass = new HashMap<>();
            for(double outputLayerClass : outputLayerClasses)
            {   // For Classification, add node for each class and save the class value to HashMap
                tmp = new Neuron();
                outputLayer.add(tmp);
                neurons.add(tmp);
                outputToClass.put(tmp, outputLayerClass);
            }
        }
        generateWeights();
    }

    public void generateWeights()
    {
        biasNeuron = new Neuron(1); // Initialize a bias neuron with weight 1 that functions as a bias connected to every non-input neuron
        if(hiddenLayers == null)//connect input layer to output layer if there are no hidden layers
        {
            for(Neuron value : inputLayer)
            {
                for(Neuron neuron : outputLayer)
                {
                    value.connectOutput(neuron);
                }
            }
            for (Neuron neuron : outputLayer) { // connect bias neuron to output nodes
                biasNeuron.connectOutput(neuron);
            }
        }
        else//there are hidden layers that need to be connected
        {
            for(Neuron n1 : inputLayer)//attach input to first hidden layer
            {
                for(int i = 0; i < hiddenLayers.get(0).size(); i++)
                {
                    n1.connectOutput(hiddenLayers.get(0).get(i));
                }
            }
            for(int i = 0; i < hiddenLayers.get(0).size(); i++) {
                biasNeuron.connectOutput(hiddenLayers.get(0).get(i));   // connect bias to every hidden node
            }

            for(int layeri = 0; layeri < hiddenLayers.size()-1; layeri++) { //attach hidden layers to each other
                for(int j = 0; j < hiddenLayers.get(layeri).size(); j++)
                {
                    for(int k = 0; k < hiddenLayers.get(layeri + 1).size(); k++)
                    {
                        hiddenLayers.get(layeri).get(j).connectOutput(hiddenLayers.get(layeri + 1).get(k));
                    }
                }
                for(int k = 0; k < hiddenLayers.get(layeri + 1).size(); k++)
                {
                    biasNeuron.connectOutput(hiddenLayers.get(layeri + 1).get(k));
                }
            }
            //attach each neuron of the last hidden layer to the output layer
            for(int i = 0; i < hiddenLayers.get(hiddenLayers.size()-1).size(); i++)
            {
                for(Neuron neuron : outputLayer)
                {
                    hiddenLayers.get(hiddenLayers.size() - 1).get(i).connectOutput(neuron);
                }
            }
            for(Neuron neuron : outputLayer)
            {
                biasNeuron.connectOutput(neuron);
            }
        }
    }
    
    public ArrayList<Neuron> feedForward(double[] inputNodeValues)
    {
        // Set inputs neuron values
        for (int i = 0; i < inputNodeValues.length; i++) {
            inputLayer.get(i).setValue(inputNodeValues[i]);
        }

        if(hiddenLayers == null)//just need the input and output layers
        {
            propagateLayer(inputLayer, outputLayer, true);
        }
        else    // hidden layers exist
        {
            int hiddenLayerCount = hiddenLayers.size();
            propagateLayer(inputLayer, hiddenLayers.get(0), false);
            if(hiddenLayerCount == 1)
            {
                propagateLayer(hiddenLayers.get(0), outputLayer, true);
            }
            else if(hiddenLayerCount == 2)
            {
                propagateLayer(hiddenLayers.get(0), hiddenLayers.get(1), false);
                propagateLayer(hiddenLayers.get(1), outputLayer, true);
            }
        }
        return outputLayer;
    }
    
    public void propagateLayer(ArrayList<Neuron> input, ArrayList<Neuron> output, boolean outputLayer)
    {
        ArrayList<Double> weights, values;

        //for(int j = 0; j < input.get(0).getWeights().size(); j++) //for each edge in a node of the input layer (corresponding to one output)
        for (Neuron out : output)
        {

            weights = new ArrayList<>();    //reset the array lists
            values = new ArrayList<>();

            for(Neuron neuron : input)      //for each node in the input layer
            {
                weights.add(neuron.getWeight(out));    //take its weight and value
                values.add(neuron.getValue());
            }

            //add bias neuron weight and value to respective arraylists
            for(int i = 0; i < biasNeuron.getWeights().size(); i++)
            {
                if(biasNeuron.getOutputNeuron(i) == out)  //search for weight corresponding to current Neuron
                {
                    weights.add(biasNeuron.getWeights().get(i));    //take bias weight and value
                    values.add(biasNeuron.getValue());
                }
            }

            double newValue;
            if (outputLayer) {  // Check if node is in output layer, which will mean use a different activation function
                if (!isClassification) {
                    newValue = Activation.Dot(weights, values); // use linear activation function on output node
                }
                else {
                    newValue = Activation.Sigmoidal(weights, values); // Use sigmoidal(logistic) function for output
                }
            }
            else {
                newValue = Activation.Sigmoidal(weights, values);   // use sigmoidal for all hidden layers, if any
            }
            
            out.updateSumInputs(Activation.Dot(weights, values)); //save neuron's input values
            out.updateValue(newValue);                    //push the updated value to the node
        }
    }

    public ArrayList<ArrayList<Neuron>> getHiddenLayers() {
        return hiddenLayers;
    }

    public HashMap<Neuron, Double> getOutputToClass() {
        return outputToClass;
    }

    public void pushWeightUpdates() {
        // push weight updates to weights hashmaps
        for (Neuron n : inputLayer) {
            n.pushWeightUpdate();
        }
        biasNeuron.pushWeightUpdate();
        if (hiddenLayers != null) {
            for(ArrayList<Neuron> hiddenLayer : hiddenLayers)
            {
                for(Neuron n : hiddenLayer)
                {
                    n.pushWeightUpdate();
                }
            }
        }
    }
    
    public boolean isClassification()
    {
        return isClassification;
    }
}