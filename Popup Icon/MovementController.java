import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MovementController extends Thread{
    JFrame window;
    ArrayList<double[]> points;

    public MovementController(JFrame window){
        this.window = window;
    }

    public MovementController(JFrame window, ArrayList<double[]> points){
        this.window = window;
        setPoints(points);
    }

    public void setPoints(ArrayList<double[]> points){
        this.points = points;
    }

    public void run(){
        int curPointIndex = 0;

        while(curPointIndex < points.size()){
            double[] curPoint = points.get(curPointIndex);
            window.setLocation((int) curPoint[0], (int) curPoint[1]);
            curPointIndex++;
            try {
                sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
