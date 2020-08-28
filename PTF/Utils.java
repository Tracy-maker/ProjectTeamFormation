package PTF;

import java.util.ArrayList;

public class Utils {
   public static double average(ArrayList<Double> array) {

        double sum=0;

        for (Double num:array){
            sum+=num;
        }
        return sum/array.size();
    }
   public static double standardDeviation(ArrayList<Double> array){
        double average=average(array);
        double sigma=0;

        for(double number:array){
            sigma+=(number-average)*(number-average);
        }
        return Math.sqrt(sigma/array.size());
    }
}
