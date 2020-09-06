package MinMax;

public class IrisDataMinMax {
    double SL_min = 4.3;
    double SL_max = 7.9;
    double[] SL  = {SL_min, SL_max};
    //[4.3, 4.6600003, 5.0200005, 5.3800006, 5.7400007, 6.100001, 6.460001, 6.820001, 7.1800013, 7.5400014, 7.9000015]

    double SW_min = 2.0;
    double SW_max = 4.4;
    double[] SW = {SW_min, SW_max};
    //[2.0, 2.24, 2.48, 2.72, 2.96, 3.2, 3.44, 3.68, 3.92, 4.16, 4.3999996]

    double PL_min = 1.0;
    double PL_max = 6.9;
    double[] PL = {PL_min, PL_max};
    //[1.0, 1.59, 2.18, 2.77, 3.3600001, 3.9500003, 4.5400004, 5.1300006, 5.7200007, 6.310001, 6.900001]

    double PW_min = 0.1;
    double PW_max = 2.5;
    double[] PW = {PW_min, PW_max};
    //[0.1, 0.34, 0.58000004, 0.82000005, 1.0600001, 1.3000001, 1.5400001, 1.7800001, 2.02, 2.26, 2.5]

    public double[][] GlassMinMaxList = {SL, SW, PL, PW};
}
