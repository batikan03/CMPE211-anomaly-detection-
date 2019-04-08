import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import java.awt.Color;
import java.io.File;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
//import edu.princeton.cs.algs4.*;



public class StatisticalAnomalyDetection extends JFrame  {
	
	private int numOfPoints = 301;
	private int n = 10;
	private double[] values;
	private double[] mAValues;
	
	
	public StatisticalAnomalyDetection() {
		
		this.values = new double[numOfPoints];
		this.mAValues = new double[numOfPoints - n];
		
		readData();
		
		 XYSeriesCollection dataset = new XYSeriesCollection();	
		
		 XYSeries series = createSeries("Value");
		 
		 XYSeries mASeries = createMASeries();
		 
		 dataset.addSeries(series);
				 
		 dataset.addSeries(mASeries);
	        
           
         
         double meanOfValues = calculateMeanOfMA();
         System.out.println(meanOfValues);
         double stdOfValues = calculateStdOfMA(meanOfValues);
         System.out.println(stdOfValues);
         
         subtractMeanFromValues(meanOfValues);
         
         
         XYSeries seriesAfterMeanSubtract = createSeries("Value After Mean Substract");
         
         dataset.addSeries(seriesAfterMeanSubtract);
         
         JFreeChart chart = ChartFactory.createScatterPlot("Statistical Anomaly Chart", "Time", "Val", dataset);
         
         XYPlot plot = (XYPlot)chart.getPlot();
         plot.setBackgroundPaint(new Color(255,218,196));
         
         ChartPanel panel = new ChartPanel (chart);
         setContentPane(panel);
      
         
         int outlierInd = detectOutliers(stdOfValues, meanOfValues);
         System.out.println(outlierInd);
   
		
	}
	
	private int detectOutliers(double std, double m)
	{
		for (int i = 0; i < this.values.length; i++) {
			
			if ( this.values[i] < (m - 3 * std))
				return i;
			else if ( this.values[i] > (m + 3 * std))
				return i;
		}
		
		return -1;
	}
	

	private double calculateStdOfMA(double m)
	{
		double sum = 0;
	    for (int i = 0; i < this.mAValues.length; i++) {
	        sum += (this.mAValues[i] - m) * (this.mAValues[i] - m);
	    }
	    return Math.sqrt(sum / (this.mAValues.length ));
	}
	
	private double calculateMeanOfMA()
	{
		double sum = 0;
	    for (int i = 0; i < this.mAValues.length; i++) {
	        sum += this.mAValues[i];
	    }
	    return sum / this.mAValues.length;
	}
	
	private void subtractMeanFromValues(double m)
	{
		for (int i = 0; i < this.values.length; i++) {
	        this.values[i] -= m;
	    }
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        
		StatisticalAnomalyDetection anomalyDetection = new StatisticalAnomalyDetection();
	    anomalyDetection.setSize(800, 200);
		anomalyDetection.setLocationRelativeTo(null);
		anomalyDetection.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		anomalyDetection.setVisible(true);
   
	}
	     
	

	private XYSeries createMASeries() {
       
        XYSeries series = new XYSeries("Moving Average");
        
        
        for (int i = n; i < numOfPoints; i++) {
        	double sum = 0;
        	int k = 0;
        	for (k=0; k < n; k++) {
        		//if (i + k == numOfPoints)
        			//break;
        		sum += this.values[i - k];
        	}
        	this.mAValues[i - n] = sum / n; 
        	System.out.println(i - n);
        
        	series.add(i, this.mAValues[i- n]);
        }
        
        
        
        return series;
        }

	private void readData() {
		
		File data = new File("messages.txt");
        Scanner sc = null;
		try {
			sc = new Scanner(data);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int counter = 1;
		
        while(sc.hasNextLine()) {
       	// System.out.println(sc.nextLine());
        	this.values[counter - 1] =  Double.parseDouble(sc.nextLine());  
     	
        	counter++;
        	
        }
	}
	
	
	private XYSeries createSeries(String keyValue) {
      
        XYSeries series = new XYSeries(keyValue);
     	
    	for (int i = 0; i < this.values.length; i++) {
        	series.add(i + 1, this.values[i]);
        	
    	}
        return series;
        }

}
