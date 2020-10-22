import java.util.ArrayList;

public class Network
{
    private ArrayList<Neuron> neurons, inputLayer, outputLayer;
    private ArrayList<ArrayList<Neuron>> hiddenLayers = null;
    private Neuron biasNeuron;
    private double bias;
    private boolean isClassification;

    public Network(int inputLayerNodeNum, int[] hiddenLayerNodeNums, int outputLayerNodeNum, double bias, boolean isClassification)
    {
        this.isClassification = isClassification;
        this.bias = bias;
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
        for(int i = 0; i < outputLayerNodeNum; i++)
        {
            tmp = new Neuron();
            outputLayer.add(tmp);
            neurons.add(tmp);
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
                for(int j = 0; j < outputLayer.size(); j++)
                {
                    hiddenLayers.get(hiddenLayers.size()-1).get(i).connectOutput(outputLayer.get(j));
                }
            }
            for(int j = 0; j < outputLayer.size(); j++)
            {
                biasNeuron.connectOutput(outputLayer.get(j));
            }
        }
    }
    
    public ArrayList<Neuron> feedForward(double[] inputNodeValues)
    {
        // Set inputs
        for (int i = 0; i < inputNodeValues.length; i++) {
            inputLayer.get(i).setValue(inputNodeValues[i]);
        }

        if(hiddenLayers == null)//just need the input and output layers
        {
            propagateLayer(inputLayer, outputLayer);
        }
        else
        {
            int hiddenLayerCount = hiddenLayers.size();
            propagateLayer(inputLayer, hiddenLayers.get(0));
            if(hiddenLayerCount == 1)
            {
                propagateLayer(hiddenLayers.get(0), outputLayer);
            }
            else if(hiddenLayerCount == 2)
            {
                propagateLayer(hiddenLayers.get(0), hiddenLayers.get(1));
                propagateLayer(hiddenLayers.get(1), outputLayer);
            }
        }
        return outputLayer;
    }
    
    public void propagateLayer(ArrayList<Neuron> input, ArrayList<Neuron> output)
    {
        ArrayList<Double> weights, values;

        for(int j = 0; j < input.get(0).getWeights().size(); j++) //for each edge in a node of the input layer (corresponding to one output)
        {

            weights = new ArrayList<>();    //reset the array lists
            values = new ArrayList<>();

            for(Neuron neuron : input)      //for each node in the input layer
            {
                weights.add(neuron.getWeights().get(j));    //take its weight and value
                values.add(neuron.getValue());
            }

            //add bias neuron weight and value to respective arraylists
            for(int i = 0; i < biasNeuron.getWeights().size(); i++)
            {
                if(biasNeuron.getOutputNeuron(i) == output.get(j))  //search for weight corresponding to current Neuron
                {
                    weights.add(biasNeuron.getWeights().get(i));    //take bias weight and value
                    values.add(biasNeuron.getValue());
                }
            }

            double newValue = output.get(j).getValue();//determine the updated value of the output node
            if(isClassification)//classification
                newValue = Activation.Sigmoidal(weights, values);
            
            output.get(j).updateSumInputs(Activation.Dot(weights, values)); //save neuron's input values
            output.get(j).updateValue(newValue);                    //push the updated value to the node
        }
    }

    public ArrayList<Neuron> getCompleteNetwork()
    {
        return neurons;
    }

    public ArrayList<ArrayList<Neuron>> getHiddenLayers() {
        return hiddenLayers;
    }

    public Neuron getBiasNeuron()
    {
        return biasNeuron;
    }

    public void pushWeightUpdates() {
        // push weight updates to weights hashmaps
        for (Neuron n : inputLayer) {
            n.pushWeightUpdate();
        }
        if (hiddenLayers != null) {
            for (int i = 0; i < hiddenLayers.size(); i++) {
                for (Neuron n : hiddenLayers.get(i)) {
                    n.pushWeightUpdate();
                }
            }
        }
    }
}