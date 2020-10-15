public class Calc {
    public static float dist(float[] a1, float[] a2, int ignored) {
        // helper method to calculate euclidean distance squared between two attribute arrays
        float sum = 0;
        for (int i = 0; i < a1.length; i++) {
            if(i != ignored)
                sum += Math.pow((a1[i] - a2[i]), 2);
        }
        return sum;
    }
}
