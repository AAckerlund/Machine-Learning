public class AM {
    // Helper class for adding and multiplying arrays element-wise

    public static double[] add(double[] a, double[] b) {
        // Add two arrays
        double[] sum = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            sum[i] = a[i] + b[i];
        }
        return sum;
    }

    public static double[] add(double[] a, double[] b, double[] c) {
        // Add three arrays
        double[] sum = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            sum[i] = a[i] + b[i] + c[i];
        }
        return sum;
    }

    public static double[] sub(double[] a, double[] b) {
        // Subtract b from a
        double[] sum = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            sum[i] = a[i] - b[i];
        }
        return sum;
    }

    public static double[] scalar(double c, double[] arr) {
        // multiple array by constant c
        double[] prod = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            prod[i] = c*arr[i];
        }
        return prod;
    }

}
