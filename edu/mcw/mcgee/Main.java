package edu.mcw.mcgee;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//type
//		double[] test = {1,2,3,3.5,4,5,6};
//		double[] test = {0.1,0.2,0.3,0.35,0.4,0.5,0.6};
//		double[] test_r =Method.Normalization(test);
//		for(int i=0; i<6; i++)
//			System.out.println(test_r[i]);
		//spearman corr
		//double[] x=PhenoData.getIndex();
		//double[] y=PhenoData.getInsulin_10();
		//System.out.println(Method.spearmanRank(x,y,1) + "\t" + Method.spearmanRank(x,y,1)+ "\t" + Method.spearmanRank(x,y,1));
//		double[] test_x={5,9,17,1,2,21,3,29,7,100};
//		double[] test_y={6,16,18,1,3,21,7,20,15,22};
//		System.out.println(Method.spearmanRank(test_x,test_y,1));
		//fileInput
// Test Analyzer
//		Analyzer.analysis_Noise();
//		Analyzer.analysis_perm(100);
		PathwayFiles.expressionFileName = "expression.txt";
		PathwayFiles.phenoFileName = "phenotype.txt";
		Analyzer.analysisQuantitative();
//		Analyzer.analysisMootha();
//		Analyzer.analysis_lusis();
//		Analyzer.analysis();
		System.out.println("End.............");
	}
}
