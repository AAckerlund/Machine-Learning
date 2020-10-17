import java.util.ArrayList;

public class Network
{
    private ArrayList<Neuron> nodes, inputLayer, outputLayer;
    private ArrayList<ArrayList<Neuron>> hiddenLayers;
    private double bias;

    public Network(double[] inputNodeValues, int[] hiddenLayerNodeNums, int outputLayerNodeNum, double bias)
    {
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
        if(hiddenLayers == null)//connect input layer to output layer if there are no hidden layers
        {
            for(Neuron value : inputLayer)
            {
                for(Neuron neuron : outputLayer)
                {
                    value.addOutput(neuron);
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
                }
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