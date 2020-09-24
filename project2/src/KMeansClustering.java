import java.util.ArrayList;

public class KMeansClustering {
    // Contains methods for partitioning a dataset into clusters and returning their centroids
    private int k;  // defines the number of clusters/centroids to calculate
    private ArrayList<Node> dataset;    // original dataset
    private ArrayList<Node> centroids;  // list of centroids generated by kmeans
    private ArrayList<ArrayList<Node>> clusters;    // contains lists of each current cluster

    public KMeansClustering(int k, ArrayList<Node> dataset) {
        // Constructs the class, divides dataset automatically into k clusters
        this.k = k;
        this.dataset = dataset;
    }

    private ArrayList<Node> generateRandomCentroids() {
        // helper method generates new centroids at the beginning of the clustering process
        ArrayList<Node> newCentroids = new ArrayList<Node>();

        return newCentroids;
    }

    private void assignClusters() {
        // assigns points in the datasets to clusters centered around the current centroids
    }

    private void shiftCentroids() {
        // moves the centroids based on the means calculated for each new cluster
    }

    private ArrayList<Float> calculateMean(ArrayList<Node> cluster) {
        // calculates the mean values of a given cluster
        ArrayList<Float> mean = new ArrayList<Float>();
        // go through each attribute and calculate the mean

        return mean;
    }

    public ArrayList<Node> getCentroids() {
        // returns list of centroids **maybe approximates it to the kmeans
        return centroids;
    }
}
