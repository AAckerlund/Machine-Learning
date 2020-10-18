import java.util.ArrayList;

public class Network
{
    private ArrayList<Neuron> nodes, inputLayer, outputLayer;
    private ArrayList<ArrayList<Neuron>> hiddenLayers = null;
    private Neuron biasNeuron;
    private double bias;
    private boolean isClassification;

    public Network(double[] inputNodeValues, int[] hiddenLayerNodeNums, int outputLayerNodeNum, double bias, boolean isClassification)
    {
        this.isClassification = isClassification;
        this.bias = bias;
        nodes = new ArrayList<>();
        inputLayer = new ArrayList<>();
        Neuron tmp;
        for(int i = 0; i < inputNodeValues.length; i++)
        {
            tmp = new Neuron(inputNodeValues[i]);
            inputLayer.add(tmp);
            nodes.add(tmp);
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
                    nodes.add(tmp);
                }
            }
        }

        outputLayer = new ArrayList<>();
        for(int i = 0; i < outputLayerNodeNum; i++)
        {
            tmp = new Neuron();
            outputLayer.add(tmp);
            nodes.add(tmp);
        }
        generateLinks();
    }

    public void generateLinks()
    {
        biasNeuron = new Neuron(1);//TODO is this value right?
        if(hiddenLayers == null)//connect input layer to output layer if there are no hidden layers
        {
            for(Neuron value : inputLayer)
            {
                for(Neuron neuron : outputLayer)
                {
                    value.addOutput(neuron);
                    biasNeuron.addOutput(neuron);
                }
            }
        }
        else//there are hidden layers that need to be connected
        {
            for(Neuron n1 : inputLayer)//attach input to first hidden layer
            {
                for(int i = 0; i < hiddenLayers.get(0).size(); i++)
                {
                    n1.addOutput(hiddenLayers.get(0).get(i));
                    biasNeuron.addOutput(hiddenLayers.get(0).get(i));
                }
            }
            try
            {
                for(int i = 0; i < hiddenLayers.size(); i++)//attach hidden layers to each other
                {
                    for(int j = 0; j < hiddenLayers.get(i).size(); j++)
                    {
                        for(int k = 0; k < hiddenLayers.get(i + 1).size(); k++)
                        {
                            hiddenLayers.get(i).get(j).addOutput(hiddenLayers.get(i + 1).get(k));
                            biasNeuron.addOutput(hiddenLayers.get(i + 1).get(k));
                        }
                    }
                }
            }
            catch(IndexOutOfBoundsException ignored){}
            for(int i = 0; i < hiddenLayers.get(hiddenLayers.size()-1).size(); i++)//attach each neuron of the last hidden layer to the output layer
            {
                for(int j = 0; j < outputLayer.size(); j++)
                {
                    hiddenLayers.get(hiddenLayers.size()-1).get(i).addOutput(outputLayer.get(j));
                    biasNeuron.addOutput(outputLayer.get(j));
                }
            }
        }
    }
    
    public ArrayList<Neuron> feedForward()
    {
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
        for(Neuron value : output)//for each node in the output layer
        {
            for(int j = 0; j < input.get(0).getWeights().size(); j++)//for each edge in a node of the input layer
            {
                weights = new ArrayList<>();//reset the array lists
                values = new ArrayList<>();
                for(Neuron neuron : input)//for each node in the input layer
                {
                    weights.add(neuron.getWeights().get(j));//take its weight and value
                    values.add(neuron.getValue());
                }
                
                //add bias neuron weight and value to respective arraylists
                for(int i = 0; i < biasNeuron.getWeights().size(); i++)
                {
                    if(biasNeuron.getOutputNeuron(i) == value)
                    {
                        weights.add(biasNeuron.getWeights().get(i));
                        values.add(biasNeuron.getValue());
                    }
                }
                
                double newValue;//determine the updated value of the output node
                if(isClassification)//classification
                    newValue = Activation.Sigmoidal(weights, values);
                else//regression
                    newValue = Activation.Linear(weights, values);
                value.updateValue(newValue);//push the updated value to the node
            }
        }
    }

    public ArrayList<Neuron> getCompleteNetwork()
    {
        return nodes;
    }

    public double getBias()
    {
        return bias;
    }
}