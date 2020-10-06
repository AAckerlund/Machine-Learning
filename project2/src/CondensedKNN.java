import java.util.ArrayList;
import java.util.Collections;

public class CondensedKNN {
    public ArrayList<Node> condenseSet(ArrayList<Node> data) {
        // returns a condensed copy of the inputted set
        ArrayList<Node> shuffledSet = new ArrayList<>(data);    // copy input so original isn't modified
        Collections.shuffle(shuffledSet);       // randomize order

        ArrayList<Node> condensedSet = new ArrayList<>(); // set to add condensed points to

        ArrayList<Integer> checkedIndices = new ArrayList<>(); // keep track of the indices that have been checked
        condensedSet.add(shuffledSet.get(0));   // initialize condensed set with random entry, or first of the shuffled set
        checkedIndices.add(0);

        boolean ZChanged = true;   // flag to indicate if condensed set has been added to

        while (ZChanged) {
            // loop through, comparing points and adding points until no points are added anymore
            ZChanged = false;

            //TODO: add functionality for regression, where values will not be exactly equal
            for (int i = 1; i < shuffledSet.size(); i++) {
                // Loop calculate nearest condensed point to each point, adding the point to the condensed set if class don't match
                if (checkedIndices.contains(i)) {
                    // skip index if it has already been added. Won't change result because the class should be correct, but it's redundant
                    continue;
                }
                Node currentPoint = shuffledSet.get(i);
                Node nearestPoint = findMinimumDistancePoint(condensedSet, currentPoint);

                if (currentPoint.getId() != nearestPoint.getId()) {
                    // add to condensed set if points don't match
                    condensedSet.add(currentPoint);
                    checkedIndices.add(i);
                    ZChanged = true;
                }
            }
        }
        return condensedSet;
    }

    private Node findMinimumDistancePoint(ArrayList<Node> condensedSet, Node point) {
        // searches for and returns the point in condensedSet that has the minimum distance to point
        Node nearestPoint = condensedSet.get(0);
        float nearestDist = Float.POSITIVE_INFINITY;
        for (Node node : condensedSet) {
            float dist = Calc.dist(point.getData() , node.getData(), node.getIgnoredAttr());
            if (dist < nearestDist) {
                nearestDist = dist;
                nearestPoint = node;
            }
        }
        return nearestPoint;
    }
}
