import java.util.ArrayList;

public class PSO extends Trainer
{
    private int numParticles;
    private int maxIterations;
    private int numValues;
    private double inertia;
    private double cogBias;
    private double socBias;
    private Particle[] particles;

    public PSO (int numValues, int maxIterations, int numParticles, double inertia, double cogBias, double socBias) {
        // Creates a PSO object in a ring structure, 2 neighbors
        this.numParticles = numParticles;
        this.maxIterations = maxIterations;
        this.numValues = numValues;
        this.inertia = inertia;
        this.cogBias = cogBias;
        this.socBias = socBias;
        this.particles = new Particle[numParticles];

        initializeSwarm();
    }

    private void initializeSwarm() {
        // Creates the ring structure and initializes the random values for each particle
        for (int i = 0; i < numParticles; i++) {
            double[] newPos = new double[numValues];

            for (int j = 0; j < numValues; j++) {
                newPos[j] = 0.01*Math.random(); // initialize each new pos component
            }
            particles[i] = new Particle(newPos);
        }
        // Link Particles in ring structure
        for (int i = 0; i < particles.length; i++) {
            Particle[] neighbors = new Particle[2];
            if (i == 0) {
                // link to last particle and next particle in array
                neighbors[0] = particles[particles.length-1];
                neighbors[1] = particles[i+1];
            }
            else if (i == particles.length-1) {
                // link to previous particle and first particle in array
                neighbors[0] = particles[i-1];
                neighbors[1] = particles[0];
            }
            else {
                // link to previous particle and next particle
                neighbors[0] = particles[i-1];
                neighbors[1] = particles[i+1];
            }
            particles[i].setNeighbors(neighbors);
        }
    }

    private Chromosome trainPSO(ArrayList<Node> trainingSet, int inputLayerNodeNum, int[] hiddenLayerNodeNums, double[] outputLayerClasses, boolean isClassification) {
        // train by moving every particle in iterations and comparing performances, return a chromosome with the best values
        Network nn = new Network(inputLayerNodeNum,hiddenLayerNodeNums,outputLayerClasses,isClassification);
        //TODO: Define a termination condition for the training (convergence or max iterations)
        int iteration = 0;
        while (iteration < maxIterations) {
            // Evaluation, pbest loop
            for (int i = 0; i < numParticles; i++) {
                // evaluate particle performances, set pbests
                Particle cParticle = particles[i];
                //nn.updateChromosome(cParticle.getPosition());    // puts particle position into neural network
                if (!isClassification) {    // use MSError for classification
                    double error = nn.calculateMSError(trainingSet);
                    if (error < cParticle.getPbestError()) {
                        cParticle.setPbest(cParticle.getPosition());    // set pbest if error is better
                        cParticle.setPbestError(error);
                    }
                }

            }

            // Neighborhood search loop
            for (int i = 0; i < numParticles; i++) {
                // search neighbors, set nbests
                Particle cParticle = particles[i];
                Particle[] neighbors = cParticle.getNeighbors();

                // set nbest by comparing the best errors of the neighbors
                if (neighbors[0].getPbestError() < neighbors[1].getPbestError()) {
                    cParticle.setNbest(neighbors[0].getPbest());
                } else {
                    cParticle.setNbest(neighbors[1].getPbest());
                }
            }

            // Movement loop
            for (int i = 0; i < numParticles; i++) {
                Particle cParticle = particles[i];
                //update velocity v(t)=wv(t-1) + c1r1(pb-x(t)) + c2r2(nb-x(t))
                double[] vel = AM.add(AM.scalar(inertia, cParticle.getPrevVelocity()),  // inertia
                        AM.scalar(cogBias * Math.random(), AM.sub(cParticle.getPbest(), cParticle.getPosition())),    // cognitive component
                        AM.scalar(socBias * Math.random(), AM.sub(cParticle.getNbest(), cParticle.getPosition())));   // social component
                //update position and save prev velocity
                cParticle.setPosition(AM.add(cParticle.getPosition(), vel));
                cParticle.setPrevVelocity(vel);
            }
        }
        return null;//TODO not sure if we need to change this but it was missing.
    }

    @Override
    void train()
    {

    }
}
