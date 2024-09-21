import java.util.ArrayList;

public class Bezier {
    public double[] p0;
    public double[] p1;
    public double[] p2;
    public double[] p3;

    public float length=0;

    public ArrayList<double[]> points;

    // Init function v0 = 1st point, v1 = handle of the 1st point , v2 = handle of the 2nd point, v3 = 2nd point
    // handle1 = v0 + v1
    // handle2 = v3 + v2
    public Bezier( int[] v0, int[] v1, int[] v2, int[] v3, int _calculatePoints)
    {
        this.p0 = new double[]{v0[0], v0[1]};
        this.p1 = new double[]{v1[0], v1[1]};
        this.p2 = new double[]{v2[0], v2[1]};
        this.p3 = new double[]{v3[0], v3[1]};

        if(_calculatePoints>0) CalculatePoints(_calculatePoints);
    }

    // 0.0 >= t <= 1.0 her be magic and dragons
    public double[] GetPointAtTime( float t )
    {
        float u = 1f - t;
        float tt = t * t;
        float uu = u * u;
        float uuu = uu * u;
        float ttt = tt * t;

        double[] p = new double[]{uuu * p0[0], uuu * p0[1]}; //first term
        p = new double[]{p[0] + 3 * uu * t * p1[0], p[1] + 3 * uu * t * p1[1]}; //second term
        p = new double[]{p[0] + 3 * u * tt * p2[0], p[1] + 3 * u * tt * p2[1]}; //third term
        p =  new double[]{p[0] + ttt * p3[0], p[1] + ttt * p3[1]}; //fourth term

        System.out.println(p[0] + " " + p[1]);

        return p;

    }

    //where _num is the desired output of points and _precision is how good we want matching to be
    public void CalculatePoints(int _num)
    {
        int _precision=100;

        if(_num>_precision) System.out.println("_num must be less than _precision");

        //calculate the length using _precision to give a rough estimate, save lengths in array
        length=0;
        //store the lengths between PointsAtTime in an array
        double[] arcLengths = new double[_precision];

        double[] oldPoint = GetPointAtTime(0);

        for(int p=1;p<arcLengths.length;p++)
        {
            double[] newPoint = GetPointAtTime((float) p/_precision); //get next point
            arcLengths[p] = Distance(oldPoint,newPoint); //find distance to old point
            length += arcLengths[p]; //add it to the bezier's length
            oldPoint = newPoint; //new is old for next loop
        }

        //create our points array
        points = new ArrayList<>();
        //target length for spacing
        float segmentLength = length/_num;

        //arc index is where we got up to in the array to avoid the Shlemiel error http://www.joelonsoftware.com/articles/fog0000000319.html
        int arcIndex = 0;

        float walkLength=0; //how far along the path we've walked
        oldPoint = GetPointAtTime(0);

        //iterate through points and set them
        for(int i=0;i<_num;i++)
        {
            float iSegLength = i * segmentLength; //what the total length of the walkLength must equal to be valid
            //run through the arcLengths until past it
            while(walkLength<iSegLength)
            {
                walkLength+=arcLengths[arcIndex]; //add the next arcLength to the walk
                arcIndex++; //go to next arcLength
            }
            //walkLength has exceeded target, so lets find where between 0 and 1 it is
            points.add(GetPointAtTime((float)arcIndex/arcLengths.length));
        }
    }

    private static double Distance(double[] a, double[] b){
        double[] dir = new double[2];
        dir[0] = b[0] - a[0];
        dir[1] = b[1] - a[1];

        return Math.sqrt((dir[0] * dir[0]) + (dir[1] * dir[1]));
    }
}
