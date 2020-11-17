public class Particle {
    private double[] position;
    private double[] prevVelocity;
    private double[] pbest;
    private double[] nbest;
    private double pbestError;
    private Particle[] neighbors;   // links this particle to its neighbors

    public Particle(double[] position) {
        this.position = position;
        pbest = position;           // set initial position to pbest
        nbest = position;           // initialize neighborhood best
        pbestError = Double.MAX_VALUE;  // initialize error to max value
        prevVelocity = new double[pbest.length];
        for (int i = 0; i < prevVelocity.length; i++) {
            prevVelocity[i] = 0;    // initialize velocity to 0
        }
    }

    public double getPbestError() {
        return pbestError;
    }

    public void setPbestError(double pbestError) {
        this.pbestError = pbestError;
    }

    public double[] getPbest() {
        return pbest;
    }

    public void setPbest(double[] newBest) {
        // sets the new personal best of this particle
        pbest = newBest;
    }

    public Particle[] getNeighbors() {
        // returns array of neighbors to this particle
        return neighbors;
    }

    public void setNeighbors(Particle[] neighbors) {
        // assigns neighbors to particle - should not ever be assigned more than once
        this.neighbors = neighbors;
    }

    public double[] getNbest() {
        return nbest;
    }

    public void setNbest(double[] nbest) {
        this.nbest = nbest;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        // updates the chromosome with a new position
        this.position = position;
    }

    public double[] getPrevVelocity() {
        return prevVelocity;
    }

    public void setPrevVelocity(double[] prevVelocity) {
        this.prevVelocity = prevVelocity;
    }
}
