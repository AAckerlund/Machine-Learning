import java.util.HashMap;

public class Probabilities {
    // Storage class for all the conditional probabilities. This is just a hashmap with more readability

    HashMap<Integer,HashMap<Integer,HashMap<Integer,Float>>> classMap;// input class id to get map of attributes
    /*
    HashMap<Integer,HashMap<Integer,Float>> attributeMap; // input attribute index to get map of possible attribute values
    HashMap<Integer,Float> attributeValueMap; // input attribute value to get the probability of the value given the class
    //Attributes are integers representing either a boolean or a number of bins
    */
    public Probabilities() {
        this.classMap = new HashMap<>();
    }

    public float getAttributeProbability(int attributeValue, int attributeIndex, int classID) {
        return classMap.get(classID).get(attributeIndex).get(attributeValue);
    }

    public void addAttributeProbability(int attributeValue, int attributeIndex, int classID, float probability) throws Exception {
        //automatically adds an attributeValue, index, and class along with the corresponding probability
        //System.out.println("Trying to save: " + probability + " to [" + classID + " " + attributeIndex + " " + attributeValue);
        if (!this.classMap.containsKey(classID)) {
            this.classMap.put(classID, new HashMap<>());
        }
        if (!this.classMap.get(classID).containsKey(attributeIndex)) {
            this.classMap.get(classID).put(attributeIndex, new HashMap<>());
        }
        if (this.classMap.get(classID).get(attributeIndex).containsKey(attributeValue)) {
            //throw new Exception("Value already assigned");
        }
        else {
            this.classMap.get(classID).get(attributeIndex).put(attributeValue, probability);    // assign probability value
            //System.out.println("Assigned: " + probability + " to [" + classID + " " + attributeIndex + " " + attributeValue);
        }
    }

    public void printModel() {
        for (int classID : classMap.keySet()) {
            System.out.print("                ");
            for (int attributeValue : classMap.get(classID).get(0).keySet()) {
                System.out.print("Aj = " + attributeValue + " ");
            }
            System.out.println();
            for (int attributeIndex : classMap.get(classID).keySet()) {
                // print line for each attribute given a class
                System.out.print("F(A" + attributeIndex + " | class " + classID + ")");
                for (int attributeValue : classMap.get(classID).get(attributeIndex).keySet()) {
                    // print probabilities for each attribute value
                    System.out.print(" " + String.format("%.4f", classMap.get(classID).get(attributeIndex).get(attributeValue)));
                }
                System.out.println();
            }
        }
    }
}
