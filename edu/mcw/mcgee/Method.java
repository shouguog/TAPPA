package edu.mcw.mcgee;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.math.*;
import java.awt.Color;
public class Method {

	/**
	 * @param args
	 */
	//keep meaningful value, ie how many numbers should be keep 
	//Check Whether a gene in a gene list
	static boolean geneInGenelist(String gene, String[] geneList){
		boolean inList = false;
		for(int i=0; i<geneList.length; i++){
			if(geneList[i].equals(gene)){
				inList = true;
			}
		}
		return inList;
	}
	static double getSimpleValue(double value, int numValue){
		return (int)(value*Math.pow(10,numValue))/Math.pow(10,numValue);
	}
	//Calculate P Value of Permute
	static double PermutePValue(double inputValue, double[] sampleValues){
		int numThreshhold = 0;//better than inputValue 
		if(inputValue>0){
			for(int i=0; i<sampleValues.length; i++)
				if(sampleValues[i]>inputValue)
					numThreshhold++;
		}else
		{
			for(int i=0; i<sampleValues.length; i++)
				if(sampleValues[i]<inputValue)
					numThreshhold++;
		}
		return (double)numThreshhold/sampleValues.length;
	}
	static double PermutePValueBinary(double inputValue, double[] sampleValues){
		int numThreshhold = 0;//better than inputValue 
		if(inputValue>0.5){
			for(int i=0; i<sampleValues.length; i++)
				if(sampleValues[i]>inputValue)
					numThreshhold++;
		}else
		{
			for(int i=0; i<sampleValues.length; i++)
				if(sampleValues[i]<inputValue)
					numThreshhold++;
		}
		return (double)numThreshhold/sampleValues.length;
	}
	//Color defination
	static Color getColor(double diff){//used to define the color of frame of every gene
		if(PathwayFiles.TypeOfphenoType.equals("binary")){
			if(diff<0.01)
				return new Color(255,0,0);
			else if(diff<0.05)
				return new Color(200,55,0);				
			else if(diff<0.1)
				return new Color(170,85,0);
			else if(diff<0.3)
				return new Color(50,205,0);
			else
				return new Color(0,255,0);
		}else{
			if(Math.abs(diff)>0.2)
				return new Color(255,0,0);
			else if(Math.abs(diff)>0.1)
				return new Color(200,55,0);				
			else if(Math.abs(diff)>0.05)
				return new Color(170,85,0);
			else
				return new Color(0,255,0);
		}
	}
	//Produce a random list
	static int[] randomList(int N){
		return QuickSort.randomList(N);
	}
	//calculate mean
	public static double mean(double[] p) {
	    double sum = 0;  // sum of all the elements
	    for (int i=0; i<p.length; i++) {
	        sum += p[i];
	    }
	    return sum / p.length;
	}
	//calculate mean
	public static double std(double[] p) {
		double mean = mean(p);
	    double sum = 0;  // sum of all the elements
	    for (int i=0; i<p.length; i++) {
	        sum += Math.pow((p[i]-mean),2);
	    }
	    return Math.sqrt(sum / p.length);
	}
	//normalization
	static double[] Normalization(double origin[]){
		double mean=0;
		double std=0;
		double[] normalizedArray=new double[origin.length];
		for(int i=0; i<origin.length; i++){
			mean += origin[i]/(origin.length);
		}
		//System.out.println(mean);
		for(int i=0; i<origin.length; i++){
			std = std + Math.pow((origin[i]-mean),2);
		}
		std=Math.sqrt(std/origin.length);
		for(int i=0; i<origin.length; i++){
			normalizedArray[i] = Method.getSimpleValue(1/(1 + Math.exp((-origin[i]+mean)/std))-0.5,4);
		}
		return normalizedArray;
	}
	//Used to read expression data
	static Hashtable readDataMatrix(String fileName){
		int nRow=0, nCol=0;
		Hashtable data=new Hashtable();
		BufferedReader br=null;
		String line;
		try{
  	    	br = new BufferedReader(new FileReader(fileName));
		    }catch(Exception eFile){System.out.println("Can not read file");}
	    try{
	    	line = br.readLine();//read header
  	    	while((line = br.readLine())!=null){
  	    		String[] items = line.split("\t");
  	    		double[] dataLine = new double[items.length-1];
  	    		for(int i=1; i<items.length; i++)
  	    			dataLine[i-1]=Double.parseDouble(items[i]);
  	    		data.put(items[0], dataLine);
  	    	}
	    }catch(Exception e){System.out.println("File Format errors");}
	    return data;
	}
	
	
	//used to read phenoDataBinary
	static double[] readPhenoDataBinary(String fileName){
		double[] data=null;
		BufferedReader br=null;
		String line;
		int numExpGroup1 = 0;
		int numExpGroup2 = 0;
		try{
  	    	br = new BufferedReader(new FileReader(fileName));
		    }catch(Exception eFile){System.out.println("Can not read file");}
	    try{
	    	line = br.readLine();//read header
    		String[] items = line.split("\t");
    		data = new double[items.length];
    		for(int i=0; i<items.length; i++){
    			data[i]=Double.parseDouble(items[i]);
    			if(data[i]==1)
    				numExpGroup1++;
    			if(data[i]==0)
    				numExpGroup2++;
    			//not 0 or 1, error information
    		}
	    }catch(Exception e){System.out.println("File Format errors");}
		PathwayFiles.expNumGroup1 = numExpGroup1;
		PathwayFiles.expNumGroup2 = numExpGroup2;
	    return data;
	}
	//used to read phenoData
	static double[] readPhenoData(String fileName){
		double[] data=null;
		BufferedReader br=null;
		String line;
		try{
  	    	br = new BufferedReader(new FileReader(fileName));
		    }catch(Exception eFile){System.out.println("Can not read file");}
	    try{
	    	line = br.readLine();//read header
    		String[] items = line.split("\t");
    		data = new double[items.length];
    		for(int i=0; i<items.length; i++)
    			data[i]=Double.parseDouble(items[i]);
	    }catch(Exception e){System.out.println("File Format errors");}
		PathwayFiles.expNum = data.length;
	    return data;
	}
	//This is used to permute the data matrix
	
	static Hashtable permuteDataMatrix(Hashtable hashInput){
		int nRow=0, nCol=0;
		Hashtable data=new Hashtable();
		Enumeration enuEntry = hashInput.keys();
		while(enuEntry.hasMoreElements()){
			Object obj = enuEntry.nextElement();
			double[] dataLine = (double[])hashInput.get(obj);
			int expNum = dataLine.length; double[] dataLine_new = new double[expNum];
			int[] randomList = randomList(expNum);
			for (int i=0; i<randomList.length; i++)
				dataLine_new[i]=dataLine[randomList[i]];
			data.put(obj, dataLine_new);
		}
	    return data;
	}
		   
	//This is used to add noise to data matrix
	
	static Hashtable addNoiseDataMatrix(Hashtable hashInput, double noiseLevel){//noiseLevel: percent of original value
		int nRow=0, nCol=0;
		Hashtable data=new Hashtable();
		Enumeration enuEntry = hashInput.keys();
		while(enuEntry.hasMoreElements()){
			Object obj = enuEntry.nextElement();
			double[] dataLine = (double[])hashInput.get(obj);
			int expNum = dataLine.length; double[] dataLine_new = new double[expNum];
			Random generator = new Random();
			for (int i=0; i<expNum; i++)
				dataLine_new[i]=dataLine[i]*(1+noiseLevel*generator.nextGaussian()/100);
			data.put(obj, dataLine_new);
		}
	    return data;
	}
		   
	static void writeDataMatrix(double[][]matrixData, String fileName){
		BufferedWriter br=null;
		String line;
		try{
  	    	br = new BufferedWriter(new FileWriter(fileName));
		    }catch(Exception eFile){System.out.println("Can not create file");}
	    try{//Some code to write
	    	;
	    
	    }catch(Exception e){System.out.println("File Format errors");}
	}

	public static float spearmanRank(double[] wksp_1,double[] wksp_2, float factor) {
	int j;
	double vard,t,sg,sf,fac,en3n,en,df,aved;
	double[] wksp1=new double[wksp_1.length];
	double[] wksp2=new double[wksp_2.length];
	for(int i=0; i<wksp_1.length; i++){
		wksp1[i]=wksp_1[i];
		wksp2[i]=wksp_2[i];
	}
	double d;
	
	//int n=matrix.getRowDimension();
	int n=wksp1.length;
	//System.out.println(n);
	//wksp1=new double[n];
	//wksp2=new double[n];
	for (j=0;j<n;j++) {
//	    wksp1[j]=matrix.get(j,e1);
//	    wksp2[j]=matrix.get(j,e2);
	}
	sort2(wksp1,wksp2); // Sort each of the data arrays, and convert the entries to ranks.
	sf=crank(wksp1);
	sort2(wksp2,wksp1);
	sg=crank(wksp2);
	d=0.0;
	for (j=0;j<n;j++){
//		System.out.println("d1="+wksp1[j]); 
//		System.out.println("d2="+wksp2[j]); 
		d += Math.pow((wksp1[j]-wksp2[j]),2); // Sum the squared diference of ranks.
	}
	//System.out.println("d="+d);
	en=n;
	en3n=en*en*en-en;
	aved=en3n/6.0-(sf+sg)/12.0; // Expectation value of D,
	fac=(1.0-sf/en3n)*(1.0-sg/en3n);
	vard=((en-1.0)*en*en*Math.pow((en+1.0),2)/36.0)*fac; // and variance of D give
	return(float)((1.0-(6.0/en3n)*(d+(sf+sg)/12.0))/Math.sqrt(fac)*factor); // Rank correlation coecient,
    }
    
    /** Given a sorted array w[0..n-1], replaces the elements by their rank,
     *  including midranking of ties, and returns as s the sum of f3 ? f,
     *  where fis the number of elements in each tie.
     */
    public static double crank(double w[]) {
	int j=0,ji,jt;
	double t,rank;
	double s=0.0;
	int n=w.length;
	while (j < n-1) {
	    if (w[j+1] != w[j]) { // Not a tie.
		w[j]=j;
		++j;
	    } else { // A tie:
		for (jt=j+1; jt<n && w[jt]==w[j]; jt++); // How far does it go?
		rank=0.5*(j+jt-1);                       // This is the mean rank of the tie,
		for (ji=j;ji<=(jt-1);ji++) w[ji]=rank;   // so enter it into all the tied entries,
		t=jt-j;
		s += t*t*t-t;                            // and update s.
		j=jt;
	    }
	}
	if (j == n-1) w[n-1]=n-1; // If the last element was not tied, this is its rank.
	return s;
    }
    //used to get location information of KEGG
    public static Vector getGeneLocation(String pathwayName){
    	Vector geneLocation = new Vector(1);
		BufferedReader br=null;
		String line;
		try{
  	    	br = new BufferedReader(new FileReader(PathwayFiles.organism + "\\gene2Path.txt"));
		    }catch(Exception eFile){System.out.println("Can not read file" + eFile.getMessage());}
	    try{
  	    	while((line = br.readLine())!=null){
  	    		String[] items = line.split("\t");
  	    		if(items[1].equals(pathwayName))
  	    			geneLocation.add(items[0]+"\t"+items[2]);
  	    	}
	    }catch(Exception e){System.out.println("File Format errors" + e.getMessage());}
    	return geneLocation;
    }
    //used to get location information of KEGG which have expression value
    public static Vector getGeneLocationWithExpression(String pathwayName, Hashtable hashExpression){
    	Vector geneLocation = new Vector(1);
		BufferedReader br=null;
		String line;
		try{
  	    	br = new BufferedReader(new FileReader(PathwayFiles.organism + "\\gene2Path.txt"));
		    }catch(Exception eFile){System.out.println("Can not read file" + eFile.getMessage());}
	    try{
  	    	while((line = br.readLine())!=null){
  	    		String[] items = line.split("\t");
  	    		if(items[1].equals(pathwayName))
  	    			if(hashExpression.containsKey(items[0]))
  	    				geneLocation.add(items[0]+"\t"+items[2]);
  	    	}
	    }catch(Exception e){System.out.println("File Format errors" + e.getMessage());}
    	return geneLocation;
    }
    /**
     * Sorts an array arr[1..n] into ascending order using Quicksort,
     * while making the corresponding rearrangement of the array brr[1..n].
     */
    public static void sort2(double[] arr, double[] brr) {
	int n=arr.length;
	int i,ir=n-1,j,k,l=0;
	int[] istack;
	int jstack=0;
	double a,b,temp;
	double dummy;
	istack=new int[50];
	for (;;) {
	    if (ir-l < 7) {
		for (j=l+1;j<=ir;j++) {
		    a=arr[j];
		    b=brr[j];
		    for (i=j-1;i>=l;i--) {
			if (arr[i] <= a) break;
			arr[i+1]=arr[i];
			brr[i+1]=brr[i];
		    }
		    arr[i+1]=a;
		    brr[i+1]=b;
		}
		if (jstack==0) {
		    istack=null;
		    return;
		}
		ir=istack[jstack];
		l=istack[jstack-1];
		jstack -= 2;
	    } else {
		k=(l+ir) >> 1;
		dummy=arr[k];
		arr[k]=arr[l+1];
		arr[l+1]=arr[k];
		
		dummy=brr[k];
		brr[k]=brr[l+1];
		brr[l+1]=brr[k];
		
		if (arr[l] > arr[ir]) {
		    dummy=arr[l];
		    arr[l]=arr[ir];
		    arr[ir]=dummy;
		    
		    dummy=brr[l];
		    brr[l]=brr[ir];
		    brr[ir]=dummy;
		    
		}
		if (arr[l+1] > arr[ir]) {
		    dummy=arr[l+1];
		    arr[l+1]=arr[ir];
		    arr[ir]=dummy;
		    
		    dummy=brr[l+1];
		    brr[l+1]=brr[ir];
		    brr[ir]=dummy;
		}
		if (arr[l] > arr[l+1]) {
		    dummy=arr[l];
		    arr[l]=arr[l+1];
		    arr[l+1]=dummy;
		    
		    dummy=brr[l];
		    brr[l]=brr[l+1];
		    brr[l+1]=dummy;
		}
		i=l+1; // Initialize pointers for partitioning.
		j=ir;
		a=arr[l+1]; //Partitioning element.
		b=brr[l+1];
		for (;;) { // Beginning of innermost loop.
		    do i++; while (arr[i] < a);
		    do j--; while (arr[j] > a);
		    if (j < i) break; // Pointers crossed. Partitioning complete.
		    dummy=arr[i];
		    arr[i]=arr[j];
		    arr[j]=dummy;
		    dummy=brr[i];
		    brr[i]=brr[j];
		    brr[j]=dummy;
		} // End of innermost loop.
		arr[l+1]=arr[j]; // Insert partitioning element in both arrays.
		arr[j]=a;
		brr[l+1]=brr[j];
		brr[j]=b;
		jstack += 2;
		if (jstack > 50) System.out.println("NSTACK too small in sort2.");
		if (ir-i+1 >= j-l) {
		    istack[jstack]=ir;
		    istack[jstack-1]=i;
		    ir=j-1;
		} else {
		    istack[jstack]=j-1;
		    istack[jstack-1]=l;
		    l=i;
		}
	    }
	}
    }
}
