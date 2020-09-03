import java.util.HashMap;

public class Probabilities {
    // Storage class for all the conditional probabilities. This is just a hashmap with more readability
    HashMap<Float,HashMap<Integer,HashMap<Float,Float>>> classMap;  // input class id to get map of attributes
    //HashMap<Integer,HashMap<Float,Float>> attributeMap; // input attribute index to get map of possible attribute values
    //HashMap<Float,Float> attributeValueMap; // input attribute value to get the probability of the value given the class

    public Probabilities() {
        this.classMap = new HashMap<Float,HashMap<Integer,HashMap<Float,Float>>>();
    }

    public float getAttributeProbability(float attributeValue, int attributeIndex, float classID) {
        return classMap.get(classID).get(attributeIndex).get(attributeValue);
    }

    public void addAttributeProbability(float attributeValue, int attributeIndex, float classID, float probability) throws Exception {
        // automatically adds an attributeValue, index, and class along with the corresponding probability
        if (!this.classMap.containsKey(classID)) {
            this.classMap.put(classID, new HashMap<Integer,HashMap<Float,Float>>());
        }
        else if (!this.classMap.get(classID).containsKey(attributeIndex)) {
            this.classMap.get(classID).put(attributeIndex, new HashMap<Float,Float>());
        }
        else if (!this.classMap.get(classID).get(attributeIndex).containsKey(attributeValue)) {
            throw new Exception("Value already assigned");
        }
        else {
            this.classMap.get(classID).get(attributeIndex).put(attributeValue, probability);    // assign probability value
        }
    }
}
