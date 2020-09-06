import java.util.ArrayList;
import java.util.HashMap;

public class TrainingSetAlgorithm
{
    Probabilities probabilities;  // Contains all the conditional probabilities of attribute values given a class,
    // probably multidimensional. First index = classID, second index = attributeIndex, third index = attributeValue
    ArrayList<Node> trainingSet;    // Contains current training set
    int numAttributes;  // Defines number of attributes per example
    int[] numValues;      // Defines number of possible values for attributes, denoted by the attribute index
    int[] classIDs;        // Stores the different classIDs (Since some classes don't start at 0)
    HashMap<Integer,ArrayList<Node>> classLists;  // Contains training sets divided by class into separate lists

    public TrainingSetAlgorithm(ArrayList<Node> trainingSet) {
        // initialize probability storage object, divide up classes into their own lists
        this.trainingSet = trainingSet;
        this.numAttributes = trainingSet.get(0).data.length;
        this.numValues = new int[numAttributes];
        for (int i = 0; i < numAttributes; i++) {
            numValues[i] = findNumValues(i);
        }
        this.probabilities = new Probabilities();
        this.classLists = new HashMap<Integer, ArrayList<Node>>();
        int numClasses = findNumClasses();

        divideClasses(numClasses);
    }
    private int findNumValues(int attributeIndex) {
        ArrayList<Integer> values = new ArrayList<Integer>();
        for (Node node: trainingSet) {
            if (!values.contains((int) node.getData()[attributeIndex])) {
                values.add((int) node.getData()[attributeIndex]);
            }
        }
        return values.size();
    }
    private int findNumClasses() {
        ArrayList<Integer> classes = new ArrayList<Integer>();
        for (Node node : trainingSet) {
            if (!classes.contains((int) node.getId())) {
                classes.add((int) node.getId());
            }
        }
        this.classIDs = new int[classes.size()];
        for (int i = 0; i < classes.size(); i++) {
            classIDs[i] = classes.get(i);
            System.out.println("Assigning: " + classes.get(i) + " to index " + i);
        }
        return classes.size();
    }
    private void divideClasses(int numClasses) {
        // divides the trainingSet by into multiple arraylists containing the same class
        // These sets are then placed into a map with their class IDs as keys

        for (int currentClassIndex = 0; currentClassIndex < numClasses; currentClassIndex++) {
            // uses array of available class IDs
            // create a list for each class
            ArrayList<Node> classList = new ArrayList<Node>();
            classLists.put(classIDs[currentClassIndex], classList);
            System.out.println("ClassIndex: " + currentClassIndex);
            for (int exampleIndex = 0; exampleIndex < trainingSet.size(); exampleIndex++) {
                // loop through training set and add matching class entries to list
                Node example = trainingSet.get(exampleIndex);
                if (example.getId() == classIDs[currentClassIndex]) {
                    classList.add(example);
                }
            }
        }
    }

    public float classProbability(int classID, ArrayList<Node> trainingSet) {
        // input a class and a training set of nodes
        // Actually, just use the classlists
        int numberOfClasses = classLists.get(classID).size();
        float probability = (float)numberOfClasses/trainingSet.size();

        // return the probability the class occurs in the training set Q(C=ci)
        return probability;
    }

    public float attributeProbability(float attributeValue, int attributeIndex, int classID) {
        // input an attribute value, attribute (assume attributeIndex is just an integer index), and a class
        float probability;

        ArrayList<Node> classSet = classLists.get(classID);
        // loop through classSet and count the total nodes of a given classID that have a certain attribute value
        int numberWithAttributeValue = 0;
        for (int i = 0; i < classSet.size(); i++) {
            if ((int)trainingSet.get(i).getData()[attributeIndex] == attributeValue) {
                numberWithAttributeValue++;
            }
        }
        numberWithAttributeValue++; // Add +1 in case there is a 0 term
        probability = (float)numberWithAttributeValue/(classSet.size() + numAttributes);  // divide by the number of elements in that class

        // return the probability of that attribute given the class (read from a list or table)
        return probability;
    }

    public void train() {
        // Take a training set of nodes
        // Use attributeprobability and classProbability to hopefully populate probabilities table
        for (int classID : classLists.keySet()) {
            for (int attributeIndex = 0; attributeIndex < numAttributes; attributeIndex++) {
                for (int attributeValue = 0; attributeValue < numValues[attributeIndex]; attributeValue++) {
                    float p = attributeProbability(attributeValue, attributeIndex, classID);
                    try {
                        probabilities.addAttributeProbability(attributeValue, attributeIndex, classID, p);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }
        }
        probabilities.printModel();
        // No output, but save probability values for each class
        // F(Aj=ak; C=ci)
    }

    public float classifyExample(float[] data) {
        // uses ClassProbability and AttributeProbability to return class with highest calculated value
        float mostProbableClass = 0;
        float mostProbableClassProbability = 0;

        float posteriorProb;
        for (int classID =0; classID < classLists.size(); classID++) {
            posteriorProb = (float)classLists.get(classID).size()/classLists.size();    // Calculate prior class probability
            for (int attributeIndex = 0; attributeIndex < numAttributes; attributeIndex++) {
                // find F(Aj=ak; C=ci), multiply by running product for probability
                posteriorProb = posteriorProb * probabilities.getAttributeProbability((int)data[attributeIndex],
                        attributeIndex, classID);
                }
            if (posteriorProb > mostProbableClassProbability) {
                mostProbableClass = classID;
                mostProbableClassProbability = posteriorProb;
            }
        }

        return mostProbableClass;
    }


}
