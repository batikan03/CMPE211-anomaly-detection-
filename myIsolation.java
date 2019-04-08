import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Collections;
import java.util.Arrays;

public class myIsolation {

	private ArrayList path = new ArrayList();
	
	public myIsolation(double[][] data,int fraction) {
		fraction = 1;
		int iso = this.recursivePartition(data, 9, 0, "start");
		System.out.println(iso);
	}
	
	public static double[] getColumn(double[][] array, int index){
		double[] column = new double[array.length]; // Here I assume a rectangular 2D array! 
	    for(int i=0; i<column.length; i++){
	       column[i] = array[i][index];
	    }
	    return column;
	}
	
	 private double [][] getGreaterValuesArray(double[] feature, double[][] data, double value) {
		 
		 double[][] tmpData = new double[feature.length][2];
		 int counter = 0;
		 for(int i = 0; i < feature.length; i++) {
	         if(value < feature[i]) {
	            tmpData[counter][0] = data[i][0];
	            tmpData[counter][1] = data[i][1];
	            counter++;
	         }
	      }
		 
		 
		 double[][] newData = new double[counter][2];
		 for(int i = 0; i < counter; i++) {
	            newData[i][0] = tmpData[i][0];
	            newData[i][1] = tmpData[i][1];
	      }
		 		 
		 return newData;
	 }

	 
	 private double [][] getSmallerValuesArray(double[] feature, double[][] data, double value) {
		 double[][] tmpData = new double[feature.length][2];
		 int counter = 0;
		 for(int i = 0; i < feature.length; i++) {
	         if(value > feature[i]) {
	            tmpData[counter][0] = data[i][0];
	            tmpData[counter][1] = data[i][1];
	            counter++;
	         }
	      }
		 
		 double[][] newData = new double[counter][2];
		 for(int i = 0; i < counter; i++) {
	            newData[i][0] = tmpData[i][0];
	            newData[i][1] = tmpData[i][1];
	      }
		 		 
		 return newData;
	 }
	 
	 
	 int findMaxIndex(double [] arr) { 
	     double max = arr[0]; 
	     int maxIdx = 0; 
	     for(int i = 1; i < arr.length; i++) { 
	          if(arr[i] > max) { 
	             max = arr[i]; 
	             maxIdx = i; 
	          } 
	     } 
	     return maxIdx; 
	}
	 
	 int findMinIndex(double [] arr) { 
		 double min = arr[0]; 
	     int minIdx = 0; 
	     for(int i = 1; i < arr.length; i++) { 
	          if(arr[i] < min) { 
	             min = arr[i]; 
	             minIdx = i; 
	          } 
	     } 
	     return minIdx; 
	}
	 
	public int recursivePartition(double [][] data ,int depth,int level,String info  ) {
		
		if(level == depth) {
			
			return -1;
			
		}
		Random r = new Random();
	    int f = r.nextInt(2) ;
	    
	    double[] feature = getColumn(data, f);
	    double min = Arrays.stream(feature)
	    	      .min()
	    	      .getAsDouble();
	    
	    int minInd = findMinIndex(feature);
	    
	    double max = Arrays.stream(feature)
	    	      .max()
	    	      .getAsDouble();
	    
	    int maxInd = findMaxIndex(feature);
	    
	    double cut =   r.nextDouble() * (max - min) + min;
	    
//	    double [] otherFeature = getColumn(data, 1-f);
//	    
//	     min = Arrays.stream(otherFeature)
//	    	      .min()
//	    	      .getAsDouble();
//	     
//	     max = Arrays.stream(otherFeature)
//	    	      .max()
//	    	      .getAsDouble();
	     
	     double[][] smaller = getSmallerValuesArray(feature, data, cut);
	     double[][] greater = getGreaterValuesArray(feature, data, cut);
	     
	     if (smaller.length == 1)
	    	 return minInd;
	     
	     if (greater.length == 1)
	    	 return maxInd;
	     
	     int isolated_index = recursivePartition(smaller, depth, level = level +1, info = "small");
	     
	     if (isolated_index != -1) 
	    	 return minInd + isolated_index;
	     
	     
	     isolated_index = recursivePartition(greater, depth, level = level +1, info = "big");
	     
	     if (isolated_index != -1)
	    	 return minInd + isolated_index;
	    
	    return -1;


	    
	}
	
	
static double[][] readData() {
	double[][] values;
	int numOfPoints = 301;
	values = new double[numOfPoints][2];
	
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
        	values[counter - 1][0] =  Double.parseDouble(sc.nextLine());  
        	values[counter - 1][1] = counter - 1;
        	counter++;
        	
        }
        
        return values;
	}
	
	public static void main(String[] args) {
		double[][] data = readData();
		for (int i = 0; i < data.length; i++)
			System.out.printf("%f, %f\n", data[i][0], data[i][1]);
		
		myIsolation iso = new myIsolation(data, 68);
	}

}
