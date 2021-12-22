package edu.mcw.mcgee;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.*;

import javastat.inference.nonparametric.RankSumTest;
import javastat.inference.onesample.OneSampMeanTTest;
import jxl.Workbook;
import jxl.write.Number;
import jxl.write.*;
import java.util.HashMap;

public class Analyzer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static void analysisQuantitative(){
		Hashtable hashExpression = Method.readDataMatrix(PathwayFiles.expressionFileName);
		double[] phenoType = Method.readPhenoData(PathwayFiles.phenoFileName);
		Hashtable hashExpression_normal = new Hashtable();
		//System.out.println(hashExpression.size());
		Enumeration geneIDSet = hashExpression.keys();
		Object geneID = "";
		try{
		while((geneID=(String)geneIDSet.nextElement())!=null){
			//System.out.print(geneID);
			double[] arrayExpression = (double[])hashExpression.get(geneID);
			double[] arrayExpressionNormalized = Method.Normalization(arrayExpression);
			hashExpression_normal.put(geneID, arrayExpressionNormalized);
		}
		}catch(Exception e){;}
		System.out.println("Step one finished");
		//test parsepath;
		Vector pathwayFiles = PathwayFiles.getKEGG();
        
		HashMap pathwayName = PathwayFiles.getPathwayName();
		String header[] = {"PathwayFile",
				           "Description",
				           "Corr:ConnIndex-Pheno",
				           "Corr:CountUp-Pheno",
				           "Corr:CountDown-Pheno",
				           "Corr:CountUpDown-Pheno",
				           "Corr:ConnIndex-CountUpAndDown",
				           "Corr:ConnIndex-CountUp",
				           "Corr:ConnIndex-CountDown",
				           };
        int numCol = 9;
        double cutoff = Double.parseDouble(JOptionPane.showInputDialog(null,
        		  "What is the cutoff for differntially expressed?(0.2-0.4)",
      		  "Enter a cutoff",
      		  JOptionPane.QUESTION_MESSAGE)); 
        PathwayFiles.cutoff = cutoff;
        while(cutoff>0.4 || cutoff<0.2){
            cutoff = Double.parseDouble(JOptionPane.showInputDialog(null,
          		  "What is the cutoff for differntially expressed?(0.2-0.4)",
        		  "Enter a cutoff",
        		  JOptionPane.QUESTION_MESSAGE)); 
            PathwayFiles.cutoff = cutoff;
        }
        Vector analysisResult = new Vector(1);
        
        for(int pathway=0; pathway<pathwayFiles.size(); pathway++){
//		for(int pathway=0; pathway<5; pathway++){
			double[] connectivityIndex = new double[PathwayFiles.expNum];
			double[] countUpRegulated = new double[PathwayFiles.expNum];
			double[] countDownRegulated = new double[PathwayFiles.expNum];
			double[] countUpAndDownRegulated = new double[PathwayFiles.expNum];
			Pathway objPathway = new Pathway();
			objPathway.parsePathway("pathways/" + pathwayFiles.get(pathway));
			Vector nodes = objPathway.getNodes();
			Vector edges = objPathway.getEdges();
			for(int exp=0; exp<PathwayFiles.expNum; exp++){
				connectivityIndex[exp]=0;
				for(int i=0; i<nodes.size(); i++){
					String nodeName = ((Node)nodes.get(i)).getName();
					double[] expressionGene = (double[])hashExpression_normal.get(nodeName);
					if (expressionGene!=null){//this gene is on array
						connectivityIndex[exp] += Math.pow(expressionGene[exp]*expressionGene[exp],0.5);
						if(expressionGene[exp]>cutoff){
							countUpRegulated[exp] += 1;
							countUpAndDownRegulated[exp] += 1;
						}
						if(expressionGene[exp]<-cutoff){
							countDownRegulated[exp] += 1;
							countUpAndDownRegulated[exp] += 1;
						}
					}
				}
				for(int i=0; i<edges.size(); i++){
					String fromNode = ((Node)(((Edge)edges.get(i)).getFromNode())).getName();
					String toNode = ((Node)((Edge)edges.get(i)).getToNode()).getName();
					double[] expressionFromGene = (double[])hashExpression_normal.get(fromNode);
					double[] expressionToGene = (double[])hashExpression_normal.get(toNode);
					if (expressionFromGene!=null && expressionToGene!=null)//these two gene are on array				
						connectivityIndex[exp] += Math.pow(Math.abs(expressionFromGene[exp]*expressionToGene[exp]),0.5);
				}
				
			}//end for(int exp=0
			analysisResult.add(pathwayFiles.get(pathway).toString().substring(0,8)
					+ "---" + pathwayName.get(pathwayFiles.get(pathway))
					+ "---" + Method.getSimpleValue(Method.spearmanRank(connectivityIndex, phenoType,1),4)
					+ "---" + Method.getSimpleValue(Method.spearmanRank(countUpRegulated, phenoType,1),4)
					+ "---" + Method.getSimpleValue(Method.spearmanRank(countDownRegulated, phenoType,1),4)
					+ "---" + Method.getSimpleValue(Method.spearmanRank(countUpAndDownRegulated, phenoType,1),4)
					+ "---" + Method.getSimpleValue(Method.spearmanRank(connectivityIndex, countUpAndDownRegulated,1),4)
					+ "---" + Method.getSimpleValue(Method.spearmanRank(connectivityIndex, countUpRegulated,1),4)
					+ "---" + Method.getSimpleValue(Method.spearmanRank(connectivityIndex, countDownRegulated,1),4)
					); 
		}
		//

        TableSorterResult expTable = new TableSorterResult(hashExpression_normal);
        expTable.createAndShowGUI(analysisResult, numCol, header);
	}
	
	
	public static void analysisBinary(){
		Hashtable hashExpression = Method.readDataMatrix(PathwayFiles.expressionFileName);
		double[] phenoType = Method.readPhenoDataBinary(PathwayFiles.phenoFileName);
		Hashtable hashExpression_normal = new Hashtable();
		Enumeration geneIDSet = hashExpression.keys();
		Object geneID = "";
		try{
		while((geneID=(String)geneIDSet.nextElement())!=null){
			//System.out.print(geneID);
			double[] arrayExpression = (double[])hashExpression.get(geneID);
			double[] arrayExpressionNormalized = Method.Normalization(arrayExpression);
			hashExpression_normal.put(geneID, arrayExpressionNormalized);
		}
		}catch(Exception e){;}
		System.out.println("Step one finished");
		//test parsepath;
		Vector pathwayFiles = PathwayFiles.getKEGG();
        
		HashMap pathwayName = PathwayFiles.getPathwayName();
		String header[] = {"pathwayFiles",
		        "Description",
				"connectivityIndex_P",
				"States",
				"countUpRegulated_P",
				"countDownRegulated_P",
				"countUpAndDownRegulated_P"};
        int numCol = 7;
        Vector analysisResult = new Vector(1);
        double cutoff = Double.parseDouble(JOptionPane.showInputDialog(null,
      		  "What is the cutoff for differntially expressed?(0.2-0.4)",
    		  "Enter a cutoff",
    		  JOptionPane.QUESTION_MESSAGE)); 
        PathwayFiles.cutoff = cutoff;
        while(cutoff>0.4 || cutoff<0.2){
          cutoff = Double.parseDouble(JOptionPane.showInputDialog(null,
        		  "What is the cutoff for differntially expressed?(0.2-0.4)",
      		  "Enter a cutoff",
      		  JOptionPane.QUESTION_MESSAGE)); 
          PathwayFiles.cutoff = cutoff;
        }

//		for(int pathway=0; pathway<pathwayFiles.size(); pathway++){
			for(int pathway=0; pathway<1; pathway++){
			//System.out.println(pathwayFiles.get(pathway));
			double[] connectivityIndexGroup1 = new double[PathwayFiles.expNumGroup1];
			double[] countUpRegulatedGroup1 = new double[PathwayFiles.expNumGroup1];
			double[] countDownRegulatedGroup1 = new double[PathwayFiles.expNumGroup1];
			double[] countUpAndDownRegulatedGroup1 = new double[PathwayFiles.expNumGroup1];
			double[] connectivityIndexGroup2 = new double[PathwayFiles.expNumGroup2];
			double[] countUpRegulatedGroup2 = new double[PathwayFiles.expNumGroup2];
			double[] countDownRegulatedGroup2 = new double[PathwayFiles.expNumGroup2];
			double[] countUpAndDownRegulatedGroup2 = new double[PathwayFiles.expNumGroup2];
			Pathway objPathway = new Pathway();
			objPathway.parsePathway("pathways/" + pathwayFiles.get(pathway));
			Vector nodes = objPathway.getNodes();
			Vector edges = objPathway.getEdges();
			//Group1
			for(int exp=0; exp<PathwayFiles.expNumGroup1; exp++){
				connectivityIndexGroup1[exp]=0;
				for(int i=0; i<nodes.size(); i++){
					String nodeName = ((Node)nodes.get(i)).getName();
					double[] expressionGene = (double[])hashExpression_normal.get(nodeName);
					if (expressionGene!=null){//this gene is on array
						//System.out.println("Shouguo DEBUG" + nodeName);
						connectivityIndexGroup1[exp] += Math.pow(expressionGene[exp]*expressionGene[exp],0.5);
						if(expressionGene[exp]>cutoff){
							countUpRegulatedGroup1[exp] += 1;
							countUpAndDownRegulatedGroup1[exp] += 1;
						}
						if(expressionGene[exp]<-cutoff){
							countDownRegulatedGroup1[exp] += 1;
							countUpAndDownRegulatedGroup1[exp] += 1;
						}
					}
				}
				for(int i=0; i<edges.size(); i++){
					String fromNode = ((Node)(((Edge)edges.get(i)).getFromNode())).getName();
					String toNode = ((Node)((Edge)edges.get(i)).getToNode()).getName();
					double[] expressionFromGene = (double[])hashExpression_normal.get(fromNode);
					double[] expressionToGene = (double[])hashExpression_normal.get(toNode);
					if (expressionFromGene!=null && expressionToGene!=null)//these two gene are on array				
						connectivityIndexGroup1[exp] += Math.pow(Math.abs(expressionFromGene[exp]*expressionToGene[exp]),0.5);
				}
			//	System.out.print("\t" + connectivityIndexGroup1[exp]);
			}//exp=0
			//System.out.println();
			//Group2
			for(int exp=0; exp<PathwayFiles.expNumGroup2; exp++){
				connectivityIndexGroup2[exp]=0;
				for(int i=0; i<nodes.size(); i++){
					String nodeName = ((Node)nodes.get(i)).getName();
					double[] expressionGene = (double[])hashExpression_normal.get(nodeName);
					if (expressionGene!=null){//this gene is on array
						connectivityIndexGroup2[exp] += Math.pow(expressionGene[exp+PathwayFiles.expNumGroup1]*expressionGene[exp+PathwayFiles.expNumGroup1],0.5);
						if(expressionGene[exp+PathwayFiles.expNumGroup1]>cutoff){
							countUpRegulatedGroup2[exp] += 1;
							countUpAndDownRegulatedGroup2[exp] += 1;
						}
						if(expressionGene[exp+PathwayFiles.expNumGroup1]<-cutoff){
							countDownRegulatedGroup2[exp] += 1;
							countUpAndDownRegulatedGroup2[exp] += 1;
						}
					}
				}
				for(int i=0; i<edges.size(); i++){
					String fromNode = ((Node)(((Edge)edges.get(i)).getFromNode())).getName();
					String toNode = ((Node)((Edge)edges.get(i)).getToNode()).getName();
					double[] expressionFromGene = (double[])hashExpression_normal.get(fromNode);
					double[] expressionToGene = (double[])hashExpression_normal.get(toNode);
					if (expressionFromGene!=null && expressionToGene!=null)//these two gene are on array				
						connectivityIndexGroup2[exp] += Math.pow(Math.abs(expressionFromGene[exp+PathwayFiles.expNumGroup1]*expressionToGene[exp+PathwayFiles.expNumGroup1]),0.5);
				}
				//System.out.print("\t" + connectivityIndexGroup2[exp]);
			}//exp=0
			
			
			//System.out.println();
			
			String manWhitneyResult = pathwayFiles.get(pathway).toString().substring(0,8);
			manWhitneyResult = manWhitneyResult + "---" + pathwayName.get(pathwayFiles.get(pathway));
			RankSumTest rankSum=new RankSumTest(0.02,"less",connectivityIndexGroup1,connectivityIndexGroup2); 
			if(rankSum.pValue<0.5)
				manWhitneyResult = manWhitneyResult + "---" + Method.getSimpleValue(rankSum.pValue,4) + "---less";
			else
				manWhitneyResult = manWhitneyResult + "---" + Method.getSimpleValue((1-rankSum.pValue),4) + "---greater";				
			rankSum=new RankSumTest(0.02,"less",countUpRegulatedGroup1,countUpRegulatedGroup2); 
			manWhitneyResult = manWhitneyResult + "---" + Method.getSimpleValue(rankSum.pValue,4);
			rankSum=new RankSumTest(0.02,"less",countDownRegulatedGroup1,countDownRegulatedGroup2); 
			manWhitneyResult = manWhitneyResult + "---" + Method.getSimpleValue(rankSum.pValue,4);
			rankSum=new RankSumTest(0.02,"less",countUpAndDownRegulatedGroup1,countUpAndDownRegulatedGroup2); 
			manWhitneyResult = manWhitneyResult + "---" + Method.getSimpleValue(rankSum.pValue,4);
			
			analysisResult.add(manWhitneyResult); 
		}
		//

        TableSorterResultBinary expTable = new TableSorterResultBinary(hashExpression_normal);
        expTable.createAndShowGUI(analysisResult, numCol, header);
	}
	
	
	public static void analysis_Noise(){
		double[] noiseLevel = {1, 3, 5, 10, 20, 30 ,50};
		
		Hashtable hashExpression = Method.readDataMatrix("expression.txt");//Read data
//		test parsepath;
		Vector pathwayFiles = PathwayFiles.getKEGG();

		try { 
			//????? 
			WritableWorkbook wwb = Workbook.createWorkbook(new File("AddNoise_result.xls")); 
			for (int noiseIndex=0; noiseIndex<noiseLevel.length; noiseIndex++){
				System.out.println(noiseIndex);
				Hashtable hashExpressionWithNoise = Method.addNoiseDataMatrix(hashExpression, noiseLevel[noiseIndex]);
				Hashtable hashExpression_normal = new Hashtable();
				//????? 
				WritableSheet ws = wwb.createSheet("Noise Level at " + noiseLevel[noiseIndex] + "percent", noiseIndex); 
				Enumeration geneIDSet = hashExpression.keys();
				Object geneID = "";
				try{
					while((geneID=(String)geneIDSet.nextElement())!=null){
						//System.out.print(geneID);
						double[] arrayExpression = (double[])hashExpression.get(geneID);
						double[] arrayExpressionNormalized = Method.Normalization(arrayExpression);
						hashExpression_normal.put(geneID, arrayExpressionNormalized);
						//for(int i=0; i<arrayExpressionNormalized.length; i++)
						//	System.out.print("\t" + arrayExpressionNormalized[i]);
						//System.out.println("----");
					}
				}catch(Exception e){;}
				//Add header
				ws.addCell(new Label(0, 0, "pathwayFiles"));
				ws.addCell(new Label(1, 0, "connectivityIndex, PhenoData"));
				ws.addCell(new Label(2, 0, "countUpRegulated, PhenoData"));
				ws.addCell(new Label(3, 0, "countDownRegulated, PhenoData"));
				ws.addCell(new Label(4, 0, "countUpAndDownRegulated, PhenoData"));
				ws.addCell(new Label(5, 0, "connectivityIndex, countUpAndDownRegulated"));
				ws.addCell(new Label(6, 0, "connectivityIndex, countUpRegulated"));
				ws.addCell(new Label(7, 0, "connectivityIndex, countDownRegulated"));
				for(int pathway=0; pathway<pathwayFiles.size(); pathway++){
					//		for(int pathway=0; pathway<1; pathway++){
					double[] connectivityIndex = new double[60];
					double[] countUpRegulated = new double[60];
					double[] countDownRegulated = new double[60];
					double[] countUpAndDownRegulated = new double[60];
					Pathway objPathway = new Pathway();
					objPathway.parsePathway("pathways/" + pathwayFiles.get(pathway));
					Vector nodes = objPathway.getNodes();
					Vector edges = objPathway.getEdges();
					for(int exp=0; exp<60; exp++){
						connectivityIndex[exp]=0;
						for(int i=0; i<nodes.size(); i++){
							String nodeName = ((Node)nodes.get(i)).getName();
							double[] expressionGene = (double[])hashExpression_normal.get(nodeName);
							if (expressionGene!=null){//this gene is on array
								connectivityIndex[exp] += expressionGene[exp]*expressionGene[exp];
								if(expressionGene[exp]>0.3){
									countUpRegulated[exp] += 1;
									countUpAndDownRegulated[exp] += 1;
								}
								if(expressionGene[exp]<-0.3){
									countDownRegulated[exp] += 1;
									countUpAndDownRegulated[exp] += 1;
								}
							}
						}
						for(int i=0; i<edges.size(); i++){
							String fromNode = ((Node)(((Edge)edges.get(i)).getFromNode())).getName();
							String toNode = ((Node)((Edge)edges.get(i)).getToNode()).getName();
							double[] expressionFromGene = (double[])hashExpression_normal.get(fromNode);
							double[] expressionToGene = (double[])hashExpression_normal.get(toNode);
							if (expressionFromGene!=null && expressionToGene!=null)//these two gene are on array				
								connectivityIndex[exp] += Math.abs(expressionFromGene[exp]*expressionToGene[exp]);
						}
					}//end for(int exp=0
					ws.addCell(new Label(0, pathway+1, pathwayFiles.get(pathway).toString()));
					ws.addCell(new Number(1, pathway+1, Method.spearmanRank(connectivityIndex, PhenoData.getInsulin_10(),1)));
					ws.addCell(new Number(2, pathway+1, Method.spearmanRank(countUpRegulated, PhenoData.getInsulin_10(),1)));
					ws.addCell(new Number(3, pathway+1, Method.spearmanRank(countDownRegulated, PhenoData.getInsulin_10(),1)));
					ws.addCell(new Number(4, pathway+1, Method.spearmanRank(countUpAndDownRegulated, PhenoData.getInsulin_10(),1)));
					ws.addCell(new Number(5, pathway+1, Method.spearmanRank(connectivityIndex, countUpAndDownRegulated,1)));
					ws.addCell(new Number(6, pathway+1, Method.spearmanRank(connectivityIndex, countUpRegulated,1)));
					ws.addCell(new Number(7, pathway+1, Method.spearmanRank(connectivityIndex, countDownRegulated,1)));
				}
			}
			wwb.write(); 
			wwb.close(); 
		}catch(Exception e) { 
			System.out.println(e.toString()); 
		} 
	}
	
	public static Hashtable analysis_perm(Hashtable hashExpressionNormal, int intPerm, Vector pathwaySelected, String fileName, Hashtable origValue){
		Hashtable hashExpression = hashExpressionNormal;
		Hashtable hashResult = new Hashtable(1);
		Vector pathwayFiles = pathwaySelected;
		try { 
			WritableWorkbook wwb = null;
			WritableSheet wsSummary_mean = null;
			WritableSheet wsSummary_std = null;
			if(!fileName.equals("")){
				wwb = Workbook.createWorkbook(new File(fileName + "permute.xls")); 
				wsSummary_mean = wwb.createSheet("Summary",0);//used to save mean and STD 
				wsSummary_std = wwb.createSheet("Summary_STD",1);//used to save mean and STD 
			}
			for(int pathway=0; pathway<pathwayFiles.size(); pathway++){
				System.out.println(pathway);
				WritableSheet ws = null;
				if(!fileName.equals("")){
					ws = wwb.createSheet(pathwayFiles.get(pathway).toString(),pathway+2); 
					//???
					ws.addCell(new Label(0, 0, "connectivityIndex, PhenoData"));
					ws.addCell(new Label(1, 0, "countUpRegulated, PhenoData"));
					ws.addCell(new Label(2, 0, "countDownRegulated, PhenoData"));
					ws.addCell(new Label(3, 0, "countUpAndDownRegulated, PhenoData"));
					ws.addCell(new Label(4, 0, "connectivityIndex, countUpAndDownRegulated"));
					ws.addCell(new Label(5, 0, "connectivityIndex, countUpRegulated"));
					ws.addCell(new Label(6, 0, "connectivityIndex, countDownRegulated"));
				}
				double[][] dataPathResult = new double[7][intPerm];//used to save result
				double[] dataMean = new double[7];//used to save mean
				double[] dataStd  = new double[7];//used to save STD
				for(int perm=0; perm<intPerm; perm++){
					Hashtable hashExpression_normal = Method.permuteDataMatrix(hashExpression);
					/*
					Hashtable hashExpression_normal = new Hashtable();
					//System.out.println(hashExpression.size());
					Enumeration geneIDSet = hashExpression.keys();
					Object geneID = "";
					try{
						while((geneID=(String)geneIDSet.nextElement())!=null){
							//System.out.print(geneID);
							double[] arrayExpression = (double[])hashExpression.get(geneID);
							double[] arrayExpressionNormalized = Method.Normalization(arrayExpression);
							hashExpression_normal.put(geneID, arrayExpressionNormalized);
							//for(int i=0; i<arrayExpressionNormalized.length; i++)
							//	System.out.print("\t" + arrayExpressionNormalized[i]);
							//System.out.println("----");
						}
					}catch(Exception e){;}
					//System.out.println("Step one finished");
					 */
					double[] connectivityIndex = new double[PathwayFiles.expNum];
					double[] countUpRegulated = new double[PathwayFiles.expNum];
					double[] countDownRegulated = new double[PathwayFiles.expNum];
					double[] countUpAndDownRegulated = new double[PathwayFiles.expNum];
					Pathway objPathway = new Pathway();
					objPathway.parsePathway("pathways/" + pathwayFiles.get(pathway));
					Vector nodes = objPathway.getNodes();
					Vector edges = objPathway.getEdges();
					for(int exp=0; exp<PathwayFiles.expNum; exp++){
						connectivityIndex[exp]=0;
						for(int i=0; i<nodes.size(); i++){
							String nodeName = ((Node)nodes.get(i)).getName();
							double[] expressionGene = (double[])hashExpression_normal.get(nodeName);
							if (expressionGene!=null){//this gene is on array
								connectivityIndex[exp] += Math.pow(expressionGene[exp]*expressionGene[exp],0.5);
								if(expressionGene[exp]>PathwayFiles.cutoff){
									countUpRegulated[exp] += 1;
									countUpAndDownRegulated[exp] += 1;
								}
								if(expressionGene[exp]<-PathwayFiles.cutoff){
									countDownRegulated[exp] += 1;
									countUpAndDownRegulated[exp] += 1;
								}
							}
						}
						for(int i=0; i<edges.size(); i++){
							String fromNode = ((Node)(((Edge)edges.get(i)).getFromNode())).getName();
							String toNode = ((Node)((Edge)edges.get(i)).getToNode()).getName();
							double[] expressionFromGene = (double[])hashExpression_normal.get(fromNode);
							double[] expressionToGene = (double[])hashExpression_normal.get(toNode);
							if (expressionFromGene!=null && expressionToGene!=null)//these two gene are on array				
								connectivityIndex[exp] += Math.pow(Math.abs(expressionFromGene[exp]*expressionToGene[exp]),0.5);
						}
					}//end for(int exp=0
					dataPathResult[0][perm] = Method.spearmanRank(connectivityIndex, PhenoData.getInsulin_10(),1);
					dataPathResult[1][perm] = Method.spearmanRank(countUpRegulated, PhenoData.getInsulin_10(),1);
					dataPathResult[2][perm] = Method.spearmanRank(countDownRegulated, PhenoData.getInsulin_10(),1);
					dataPathResult[3][perm] = Method.spearmanRank(countUpAndDownRegulated, PhenoData.getInsulin_10(),1);
					dataPathResult[4][perm] = Method.spearmanRank(connectivityIndex, countUpAndDownRegulated,1);
					dataPathResult[5][perm] = Method.spearmanRank(connectivityIndex, countUpRegulated,1);
					dataPathResult[6][perm] = Method.spearmanRank(connectivityIndex, countDownRegulated,1);
				}//end for int perm
				for(int perm=0; perm<intPerm; perm++){
					for(int j=0; j<7; j++){
						if(!fileName.equals("")){
							ws.addCell(new Number(j,perm+1,dataPathResult[j][perm]));
						}
					}
				}
				double pValue[] = new double[7];
				double[] doubleValue = (double[])(origValue.get(pathwayFiles.get(pathway)));
				for(int i=0; i<7; i++){
					if(!fileName.equals("")){
						wsSummary_mean.addCell(new Number(i, pathway+1, Method.mean(dataPathResult[i])));//use mean
						wsSummary_std.addCell(new Number(i, pathway+1, Method.std(dataPathResult[i])));//use STD
					}
					pValue[i] = Method.getSimpleValue(Method.PermutePValue(doubleValue[i],dataPathResult[i]), 4);
				}
				hashResult.put(pathwayFiles.get(pathway), pValue);
			}//end for int path
			if(!fileName.equals("")){
				wwb.write(); 
				wwb.close();
			}
		}catch(Exception e) { 
			System.out.println(e.toString()); 
		} 
		return hashResult;
	}
	public static Hashtable analysis_perm_binary(Hashtable hashExpressionNormal, int intPerm, Vector pathwaySelected, String fileName, Hashtable origValue){
		Hashtable hashExpression = hashExpressionNormal;
		Hashtable hashResult = new Hashtable(1);
		Vector pathwayFiles = pathwaySelected;
		try { 
			WritableWorkbook wwb = null;
			WritableSheet wsSummary_mean = null;
			WritableSheet wsSummary_std = null;
			if(!fileName.equals("")){
				wwb = Workbook.createWorkbook(new File(fileName + "permute.xls")); 
				wsSummary_mean = wwb.createSheet("Summary",0);//used to save mean and STD 
				wsSummary_std = wwb.createSheet("Summary_STD",1);//used to save mean and STD 
			}
			for(int pathway=0; pathway<pathwayFiles.size(); pathway++){
				System.out.println(pathway);
				WritableSheet ws = null;
				if(!fileName.equals("")){
					ws = wwb.createSheet(pathwayFiles.get(pathway).toString(),pathway+2); 
					//???
					ws.addCell(new Label(0, 0, "connectivityIndex, PhenoData"));
					ws.addCell(new Label(1, 0, "countUpRegulated, PhenoData"));
					ws.addCell(new Label(2, 0, "countDownRegulated, PhenoData"));
					ws.addCell(new Label(3, 0, "countUpAndDownRegulated, PhenoData"));
					ws.addCell(new Label(4, 0, "connectivityIndex, countUpAndDownRegulated"));
				}
				double[][] dataPathResult = new double[4][intPerm];//used to save result
				double[] dataMean = new double[4];//used to save mean
				double[] dataStd  = new double[4];//used to save STD
				for(int perm=0; perm<intPerm; perm++){
					Hashtable hashExpression_normal = Method.permuteDataMatrix(hashExpression);

					double[] connectivityIndexGroup1 = new double[PathwayFiles.expNumGroup1];
					double[] countUpRegulatedGroup1 = new double[PathwayFiles.expNumGroup1];
					double[] countDownRegulatedGroup1 = new double[PathwayFiles.expNumGroup1];
					double[] countUpAndDownRegulatedGroup1 = new double[PathwayFiles.expNumGroup1];
					double[] connectivityIndexGroup2 = new double[PathwayFiles.expNumGroup2];
					double[] countUpRegulatedGroup2 = new double[PathwayFiles.expNumGroup2];
					double[] countDownRegulatedGroup2 = new double[PathwayFiles.expNumGroup2];
					double[] countUpAndDownRegulatedGroup2 = new double[PathwayFiles.expNumGroup2];
					Pathway objPathway = new Pathway();
					objPathway.parsePathway("pathways/" + pathwayFiles.get(pathway));
					Vector nodes = objPathway.getNodes();
					Vector edges = objPathway.getEdges();
					//Group1
					for(int exp=0; exp<PathwayFiles.expNumGroup1; exp++){
						connectivityIndexGroup1[exp]=0;
						for(int i=0; i<nodes.size(); i++){
							String nodeName = ((Node)nodes.get(i)).getName();
							double[] expressionGene = (double[])hashExpression_normal.get(nodeName);
							if (expressionGene!=null){//this gene is on array
								connectivityIndexGroup1[exp] += Math.pow(expressionGene[exp]*expressionGene[exp],0.5);
								if(expressionGene[exp]>PathwayFiles.cutoff){
									countUpRegulatedGroup1[exp] += 1;
									countUpAndDownRegulatedGroup1[exp] += 1;
								}
								if(expressionGene[exp]<-PathwayFiles.cutoff){
									countDownRegulatedGroup1[exp] += 1;
									countUpAndDownRegulatedGroup1[exp] += 1;
								}
							}
						}
					for(int i=0; i<edges.size(); i++){
							String fromNode = ((Node)(((Edge)edges.get(i)).getFromNode())).getName();
							String toNode = ((Node)((Edge)edges.get(i)).getToNode()).getName();
							double[] expressionFromGene = (double[])hashExpression_normal.get(fromNode);
							double[] expressionToGene = (double[])hashExpression_normal.get(toNode);
							if (expressionFromGene!=null && expressionToGene!=null)//these two gene are on array				
								connectivityIndexGroup1[exp] += Math.pow(Math.abs(expressionFromGene[exp]*expressionToGene[exp]),0.5);
					}
				}//end int exp=0
					//Group2
					for(int exp=0; exp<PathwayFiles.expNumGroup2; exp++){
						connectivityIndexGroup2[exp]=0;
						for(int i=0; i<nodes.size(); i++){
							String nodeName = ((Node)nodes.get(i)).getName();
							double[] expressionGene = (double[])hashExpression_normal.get(nodeName);
							if (expressionGene!=null){//this gene is on array
								connectivityIndexGroup2[exp] += Math.pow(expressionGene[exp+PathwayFiles.expNumGroup1]*expressionGene[exp+PathwayFiles.expNumGroup1],0.5);
								if(expressionGene[exp+PathwayFiles.expNumGroup1]>PathwayFiles.cutoff){
									countUpRegulatedGroup2[exp] += 1;
									countUpAndDownRegulatedGroup2[exp] += 1;
								}
								if(expressionGene[exp+PathwayFiles.expNumGroup1]<-PathwayFiles.cutoff){
									countDownRegulatedGroup2[exp] += 1;
									countUpAndDownRegulatedGroup2[exp] += 1;
								}
							}
						}
						for(int i=0; i<edges.size(); i++){
							String fromNode = ((Node)(((Edge)edges.get(i)).getFromNode())).getName();
							String toNode = ((Node)((Edge)edges.get(i)).getToNode()).getName();
							double[] expressionFromGene = (double[])hashExpression_normal.get(fromNode);
							double[] expressionToGene = (double[])hashExpression_normal.get(toNode);
							if (expressionFromGene!=null && expressionToGene!=null)//these two gene are on array				
								connectivityIndexGroup2[exp] += Math.pow(Math.abs(expressionFromGene[exp+PathwayFiles.expNumGroup1]*expressionToGene[exp+PathwayFiles.expNumGroup1]),0.5);
						}
							//System.out.print("\t" + connectivityIndexGroup2[exp]);
					}//exp=0
					RankSumTest rankSum=new RankSumTest(0.02,"less",connectivityIndexGroup1,connectivityIndexGroup2); 
					dataPathResult[0][perm] = Method.getSimpleValue(rankSum.pValue,4);
					rankSum=new RankSumTest(0.02,"less",countUpRegulatedGroup1,countUpRegulatedGroup2); 
					dataPathResult[1][perm] = Method.getSimpleValue(rankSum.pValue,4);
					rankSum=new RankSumTest(0.02,"less",countDownRegulatedGroup1,countDownRegulatedGroup2); 
					dataPathResult[2][perm] = Method.getSimpleValue(rankSum.pValue,4);
					rankSum=new RankSumTest(0.02,"less",countUpAndDownRegulatedGroup1,countUpAndDownRegulatedGroup2); 
					dataPathResult[3][perm] = Method.getSimpleValue(rankSum.pValue,4);
				}//end for int perm
				for(int perm=0; perm<intPerm; perm++){
					for(int j=0; j<4; j++){
						if(!fileName.equals("")){
							ws.addCell(new Number(j,perm+1,dataPathResult[j][perm]));
						}
					}
				}
				double pValue[] = new double[4];
				double[] doubleValue = (double[])(origValue.get(pathwayFiles.get(pathway)));
				for(int i=0; i<4; i++){
					if(!fileName.equals("")){
						wsSummary_mean.addCell(new Number(i, pathway+1, Method.mean(dataPathResult[i])));//use mean
						wsSummary_std.addCell(new Number(i, pathway+1, Method.std(dataPathResult[i])));//use STD
					}
					System.out.println(doubleValue[i]+"--"+dataPathResult[i][0] +"---"+dataPathResult[i][1] +"---"+dataPathResult[i][2] +"---"+dataPathResult[i][3] +"---");
					pValue[i] = Method.getSimpleValue(Method.PermutePValueBinary(doubleValue[i],dataPathResult[i]), 4);
				}
				hashResult.put(pathwayFiles.get(pathway), pValue);
			}//end for int path
			if(!fileName.equals("")){
				wwb.write(); 
				wwb.close();
			}
		}catch(Exception e) { 
			System.out.println(e.toString()); 
		} 
		return hashResult;
	}
}

