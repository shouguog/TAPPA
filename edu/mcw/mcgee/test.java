package edu.mcw.mcgee;
import java.util.*;
import javastat.inference.nonparametric.RankSumTest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import java.awt.*;

import javax.swing.JOptionPane;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Vector pathwayFiles = PathwayFiles.getKEGG();
		Pathway objPathway = new Pathway();
		objPathway.parsePathway("pathways/" + pathwayFiles.get(0));
		Vector nodes = objPathway.getNodes();
		Vector edges = objPathway.getEdges();
		for(int i=0; i<nodes.size(); i++)
			System.out.println(((Node)(nodes.get(i))).getName());
		//for(int i=0; i<edges.size(); i++)
		//	System.out.println(edges.get(i));

		//		double abc[] = {1,2, 3, 4};
		//System.out.println(abc.toString());
		/*
		double abc= 0.9;
		double sample[] =  {0.1,0.2,0.3,0.4,5};
		System.out.println(Method.PermutePValueBinary(abc,sample));
		/*
		System.out.println(Method.getSimpleValue(0.3243242432434,4));
        double[] abc = {1,2,3,4,5,6,7};
        Hashtable temp = new Hashtable(1);
        temp.put("abc", abc);
  	  	Enumeration pathwayID = temp.keys();
  	  	Object ID;
        while((pathwayID.hasMoreElements())){
		  double[] tempResult = (double[])temp.get(pathwayID.nextElement());
		  System.out.println(tempResult[0] + "---" + 
				  	         tempResult[1] + "---" + 
				  	         tempResult[2] + "---" + 
				  	         tempResult[3] + "---" + 
				  	         tempResult[4] + "---" + 
				  	         tempResult[5] + "---" + 
				  	         tempResult[6] + "---");
	  }
	  */
		// TODO Auto-generated method stub
		//Random generator = new Random();
		//for(int i=0; i<100; i++)
		//	System.out.println(generator.nextGaussian());
//		double[] anc={1,2,3,4,5};
//		System.out.println(Method.mean(anc));
//		System.out.println(Method.std(anc));
		//test_nonParameter();
	}
	
	static void test_nonParameter(){
			double [] testdata2={1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,6,7,7,7,8,9,9,9}; 
			double [] testdata1={20,30,40,50,60,70}; 

//			 non-null constructor 
			RankSumTest testclass1=new RankSumTest(0.02,"less",testdata2,testdata1); 
			double wAlpha=testclass1.wAlpha; 
			double testStatistic=testclass1.testStatistic; 
			double pValue=testclass1.pValue; 
			System.out.println(pValue);
			System.out.println(wAlpha);
			System.out.println(testStatistic);
	}
	static void random(){
		Random r = new Random(109876L);
		int i = r.nextInt();
		int j = r.nextInt();
		long l = r.nextLong();
		float f = r.nextFloat();
		double d = r.nextDouble();
		double k = r.nextGaussian();
		//The nextInt(), nextLong(), and nextBytes() methods all cover their respective ranges with equal likelihood. For example, to simulate a six-sided die; that is to generate a random integer between 1 and 6, you might write 

		Random r1 = new Random();
		int die = r1.nextInt();
		die = Math.abs(die);
		die = die % 6;
		die += 1;
		System.out.println(die);
		//The nextGaussian() method returns a pseudo-random, Gaussian distributed, double value with mean 0.0 and standard deviation 1.0. 

		//The nextBytes() method fills a byte[] array with random bytes. For example,

		byte[] ba = new byte[10];
		Random r2 = new Random();
		r2.nextBytes(ba);
		for (int ii = 0; ii < ba.length; ii++) {
			System.out.println(ba[ii]);
		}
	}
}
