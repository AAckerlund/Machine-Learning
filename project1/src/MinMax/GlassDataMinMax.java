package MinMax;

public class GlassDataMinMax {
    private double RI_min = 1.51115;
    private double RI_max = 1.53393;
    private double[] RI = {RI_min, RI_max};
    //[1.51115, 1.513428, 1.515706, 1.5179839, 1.5202619, 1.5225399, 1.5248178, 1.5270958, 1.5293738, 1.5316517, 1.5339297]
    
    private double Na_min = 10.73;
    private double Na_max = 17.38;
    private double[] Na = {Na_min, Na_max};
    //[10.73, 11.3949995, 12.059999, 12.724999, 13.389999, 14.054999, 14.719999, 15.384999, 16.05, 16.715, 17.380001]
    
    private double Mg_min = 0;
    private double Mg_max = 4.49;
    private double[] Mg = {Mg_min, Mg_max};
    //[0.0, 0.44899997, 0.89799994, 1.3469999, 1.7959999, 2.245, 2.6939998, 3.1429996, 3.5919995, 4.0409994, 4.4899993]
    
    private double Al_min = 0.29;
    private double Al_max = 3.5;
    private double[] Al = {Al_min, Al_max};
    //[0.29, 0.611, 0.93200004, 1.253, 1.574, 1.895, 2.216, 2.5370002, 2.8580003, 3.1790004, 3.5000005]
    
    private double Si_min = 69.81;
    private double Si_max = 75.41;
    private double[] Si = {Si_min, Si_max};
    //[69.81, 70.369995, 70.92999, 71.48999, 72.04999, 72.609985, 73.16998, 73.72998, 74.28998, 74.849976, 75.40997]
    
    private double K_min = 0;
    private double K_max = 6.21;
    private double[] K = {K_min, K_max};
    //[0.0, 0.621, 1.242, 1.8629999, 2.484, 3.105, 3.726, 4.347, 4.968, 5.5889997, 6.2099996]
    
    private double Ca_min = 5.43;
    private double Ca_max = 16.19;
    private double[] Ca = {Ca_min, Ca_max};
    //[5.43, 6.5059996, 7.582, 8.658, 9.734, 10.81, 11.886001, 12.962001, 14.038001, 15.114001, 16.19]
    
    private double Ba_min = 0;
    private double Ba_max = 3.15;
    private double[] Ba = {Ba_min, Ba_max};
    //[0.0, 0.315, 0.63, 0.945, 1.26, 1.575, 1.8900001, 2.2050002, 2.5200002, 2.8350003, 3.1500003]
    
    private double Fe_min = 0;
    private double Fe_max = 0.51;
    private double[] Fe = {Fe_min, Fe_max};
    //[0.0, 0.051, 0.102, 0.153, 0.204, 0.255, 0.306, 0.357, 0.408, 0.459, 0.51]
    
    private double[][] GlassMinMaxList = {RI, Na, Mg, Al, Si, K, Ca, Ba, Fe};
    
    public double[][] getGlassMinMaxList()
    {
        return GlassMinMaxList;
    }
}
