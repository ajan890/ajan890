import java.util.ArrayList;

public class PointGenerator {
	//amt is number of pixels moved per movement
    public static ArrayList<double[]> Lerp(int[] a, int[] b, float amt){
        double[] dir = new double[2];
        dir[0] = b[0] - a[0];
        dir[1] = b[1] - a[1];

        dir = normalize(dir);

        ArrayList<double[]> result = new ArrayList<>();
        double[] curLocation = new double[]{a[0], a[1]};
        while(curLocation[0] < b[0]){
            result.add(curLocation.clone());
            curLocation[0] += dir[0] * amt;
            curLocation[1] += dir[1] * amt;
        }
        result.add(new double[]{b[0], b[1]});

        return new ArrayList<>(result);
    }

    //amountOfPoints is the number of divisions of the curve
    public static ArrayList<double[]> Bezier(int[] a, int[] ha, int[] b, int[] hb, int amountOfPoints) {
        return (new Bezier(a, ha, b, hb, amountOfPoints)).points;
    }

    private static double[] normalize(double[] vector){
        double dist = Math.sqrt((vector[0] * vector[0]) + (vector[1] * vector[1]));
        vector[0] /= dist;
        vector[1] /= dist;

        return vector;
    }
}
