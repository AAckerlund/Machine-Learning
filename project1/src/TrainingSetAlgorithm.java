import java.util.ArrayList;
import java.util.HashMap;

public class TrainingSetAlgorithm
{
    Probabilities probabilities;  // Contains all the conditional probabilities of attribute values given a class,
    // probably multidimensional. First index = classID, second index = attributeIndex, third index = attributeValue
    ArrayList<Node> trainingSet;    // Contains current training set
    HashMap<Float,ArrayList<Node>> classLists;  // Contains training sets divided by class into separate lists

    public TrainingSetAlgorithm(ArrayList<Node> trainingSet) {
        // initialize probability storage object, divide up classes into their own lists
        this.trainingSet = trainingSet;
        this.probabilities = new Probabilities();
        divideClasses();
    }

    public void divideClasses() {
        // divides the trainingSet by into multiple sets containing the same class
        // These sets are then placed into a map with their class IDs as keys
        // ** I'm not sure how the classes will be indexed yet, so I'll leave that til later **

        //  classes
        //  for (example in trainingSet) {
        //      if (example.getId()
    }

    public float classProbability(float classID, ArrayList<Node> trainingSet) {
        // input a class and a training set of nodes
        float probability = 0;

        // loop through trainingSet and count the total nodes containing the given classID
        int numberOfClass = 0;
        for (int i = 0; i < trainingSet.size(); i++) {
            if (trainingSet.get(i).id == classID) {
                numberOfClass++;
            }
        }
        probability = (float)numberOfClass/trainingSet.size();

        // return the probability the class occurs in the training set Q(C=ci)
        return probability;
    }

    public float attributeProbability(float attributeValue, int attributeIndex, float classID) {
        // input an attribute value, attribute (assume attributeIndex is just an integer index), and a class
        float probability = 0;

        ArrayList<Node> classSet = classLists.get(classID);
        // loop through classSet and count the total nodes of a given classID that have a certain attribute value
        int numberWithAttributeValue = 0;
        for (int i = 0; i < classSet.size(); i++) {
            if (trainingSet.get(i).getData()[attributeIndex] == attributeValue) {
                numberWithAttributeValue++;
            }
        }
        probability = (float)numberWithAttributeValue/classSet.size();  // divide by the number of elements in that class

        // return the probability of that attribute given the class (read from a list or table)
        return probability;
    }

    public void train(ArrayList<Node> trainingSet) {
        // Take a training set of nodes
        // Use attributeprobability and classProbability to hopefully populate probabilities table

        // No output, but save probability values for each class
        // F(Aj=ak; C=ci)
    }

    public float classifyExample(float[] data) {
        // uses ClassProbability and AttributeProbability to return class with highest calculated value
        float mostProbableClass = 0;
        float mostProbableClassProbability = 0;

        //  posteriorProb;
        //  for (class in list_of_classes) {
        //      posteriorProb = classProbability(class);
        //      for (attributeIndex in list_of_attributes) {
        //          // find F(Aj=ak; C=ci), multiply by running product for probability
        //          posteriorProb = posteriorProb * probabilities(data[attributeIndex], attributeIndex, class)
        //          }
        //      if (posteriorProb > mostProbableClassProbability) {
        //          mostProbableClass = class;
        //          mostProbableClassProbability = posteriorProb;
        //      }
        //  }

        return mostProbableClass;
    }


}
