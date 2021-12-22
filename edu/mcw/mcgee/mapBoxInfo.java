package edu.mcw.mcgee;

import javastat.inference.nonparametric.RankSumTest;

import javax.swing.*;
import javax.swing.table.*;

import edu.mcw.mcgee.TableSorterResultPvalue.MyTableModel;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.Vector;
import java.sql.*;
import java.awt.event.*;
import java.io.*;
import java.util.Hashtable;


public class mapBoxInfo extends JFrame {
  private boolean DEBUG = false;
  JFileChooser chooser = new JFileChooser();
  Vector selectedGenes = new Vector(1);
  Vector allGenes= new Vector(1);
  Hashtable hashExpressionProfile = new Hashtable();
  //selected
  JLabel jLabelSelected = new JLabel();
  JScrollPane jScrollPaneSelected = new JScrollPane();
  //JTextArea jTextAreaSelected = new JTextArea();
  JTable jTableSelected;
  JButton jButtonSelected = new JButton();
  JButton jButtonExpressionSelected = new JButton();
  JButton jButtonDetailInformation = new JButton();

  //All
  JLabel jLabelAllinPathway = new JLabel();
  JScrollPane jScrollPaneAllinPathway = new JScrollPane();
  JTextArea jTextAreaAllinPathway = new JTextArea();
  JButton jButtonAllinPathway = new JButton();


  public mapBoxInfo(String pathwayName, Hashtable hashExpression, String selectedGenes, String allGenes) {
    try {
        this.getContentPane().setLayout(null);

        jLabelSelected.setText("Genes With Expression Data");
        jLabelSelected.setBounds(45, 6, 227, 24);
        this.getContentPane().add(jLabelSelected);

        jScrollPaneSelected.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPaneSelected.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //jTextAreaSelected.setText("");
        jScrollPaneSelected.setBounds(42, 34, 714, 262);
        jTableSelected = getSelected(pathwayName, selectedGenes,hashExpression);
        jScrollPaneSelected.getViewport().add(jTableSelected, null);
        this.getContentPane().add(jScrollPaneSelected);

        
        jButtonSelected.setText("Save Genes");
        jButtonSelected.setBounds(42, 308, 137, 29);
        this.getContentPane().add(jButtonSelected);

        jButtonExpressionSelected.setText("Show Expression");
        jButtonExpressionSelected.setBounds(43, 346, 136, 27);
        this.getContentPane().add(jButtonExpressionSelected);
        jButtonExpressionSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Hashtable hashExpressionSelected = new Hashtable();
            	int[] selectedRows = jTableSelected.getSelectedRows();
            	for(int i=0; i<selectedRows.length; i++){
            		Vector vtExpressionSelected = new Vector(1);
            		//push into vector
            		for(int j=3; j<jTableSelected.getColumnCount(); j++)
            			vtExpressionSelected.add(jTableSelected.getValueAt(selectedRows[i],j));
            		hashExpressionSelected.put(jTableSelected.getValueAt(selectedRows[i],0),
            				vtExpressionSelected);
            	}
            	expressionProfilePicture expressionProfilePicture1
                	= new expressionProfilePicture(hashExpressionSelected, "Expression Value");
            	expressionProfilePicture1.label.repaint();
            	expressionProfilePicture1.setLocation(50, 50);
            	expressionProfilePicture1.setSize(500, 700);
            	expressionProfilePicture1.setVisible(true);
            	return;
            }
        }
        );

        
        jButtonDetailInformation.setText("Detail information");
        jButtonDetailInformation.setBounds(43, 381, 137, 30);
        this.getContentPane().add(jButtonDetailInformation);
        jButtonDetailInformation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String url=PathwayFiles.geneURL;// + jTextAreaSelected.getText().replaceAll("\n", " ");
            	int[] selectedRows = jTableSelected.getSelectedRows();
            	for(int i=0; i<selectedRows.length; i++)
            		url = url + jTableSelected.getValueAt(selectedRows[i],0) + " ";
        		try
        		{
        			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        		}
        		catch(Exception eURL){}
            	return;
            }
        }
        );
        this.getContentPane().add(jButtonDetailInformation);
        
        jLabelAllinPathway.setBounds(280, 6, 327, 24);
        this.getContentPane().add(jLabelAllinPathway);

        jTextAreaAllinPathway.setText(allGenes);
        jScrollPaneAllinPathway.setBounds(880, 38, 130, 257);
        this.getContentPane().add(jScrollPaneAllinPathway);
        jScrollPaneAllinPathway.getViewport().add(jTextAreaAllinPathway, null);
        jButtonAllinPathway.setText("Save Genes");
        jButtonAllinPathway.setBounds(880, 307, 115, 32);
        this.getContentPane().add(jButtonAllinPathway);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
 
  public JTable getSelected(String pathwayName, String selectedGenes, Hashtable hashExpression){
	  String[] geneList = selectedGenes.split("\n");
		Pathway objPathway = new Pathway();
		objPathway.parsePathway("pathways/" + pathwayName);
		double[] expressionGeneDEBUG = (double[])hashExpression.get("5105");
		System.out.println(expressionGeneDEBUG[0]+"--"+expressionGeneDEBUG[1]+"--"+expressionGeneDEBUG[2]+"--"+expressionGeneDEBUG[3]);

	  if(PathwayFiles.TypeOfphenoType.equals("binary")){
			Vector nodes = objPathway.getNodes();
			Vector edges = objPathway.getEdges();
			double[] connectivityIndexGroup1 = new double[PathwayFiles.expNumGroup1];
			double[] connectivityIndexGroup2 = new double[PathwayFiles.expNumGroup2];
			//Group1
			for(int exp=0; exp<PathwayFiles.expNumGroup1; exp++){
				connectivityIndexGroup1[exp]=0;
				for(int i=0; i<nodes.size(); i++){
					String nodeName = ((Node)nodes.get(i)).getName();
					double[] expressionGene = (double[])hashExpression.get(nodeName);
					if (expressionGene!=null && Method.geneInGenelist(nodeName, geneList)){//this gene is on array and selected
						System.out.println("--Shouguo DEBUG" + nodeName);
						connectivityIndexGroup1[exp] += Math.pow(expressionGene[exp]*expressionGene[exp],0.5);
					}
				}
				for(int i=0; i<edges.size(); i++){
					String fromNode = ((Node)(((Edge)edges.get(i)).getFromNode())).getName();
					String toNode = ((Node)((Edge)edges.get(i)).getToNode()).getName();
					double[] expressionFromGene = (double[])hashExpression.get(fromNode);
					double[] expressionToGene = (double[])hashExpression.get(toNode);
					if (expressionFromGene!=null && expressionToGene!=null
							&& Method.geneInGenelist(fromNode, geneList) && Method.geneInGenelist(toNode, geneList) )//these two gene are on array and selected			
						connectivityIndexGroup1[exp] += Math.pow(Math.abs(expressionFromGene[exp]*expressionToGene[exp]),0.5);
				}
			}//exp=0
			//Group2
			for(int exp=0; exp<PathwayFiles.expNumGroup2; exp++){
				connectivityIndexGroup2[exp]=0;
				for(int i=0; i<nodes.size(); i++){
					String nodeName = ((Node)nodes.get(i)).getName();
					double[] expressionGene = (double[])hashExpression.get(nodeName);
					if (expressionGene!=null && Method.geneInGenelist(nodeName, geneList)){//this gene is on array and selected
						connectivityIndexGroup2[exp] += Math.pow(expressionGene[exp+PathwayFiles.expNumGroup1]*expressionGene[exp+PathwayFiles.expNumGroup1],0.5);
					}
				}
				for(int i=0; i<edges.size(); i++){
					String fromNode = ((Node)(((Edge)edges.get(i)).getFromNode())).getName();
					String toNode = ((Node)((Edge)edges.get(i)).getToNode()).getName();
					double[] expressionFromGene = (double[])hashExpression.get(fromNode);
					double[] expressionToGene = (double[])hashExpression.get(toNode);
					if (expressionFromGene!=null && expressionToGene!=null
							&& Method.geneInGenelist(fromNode, geneList) && Method.geneInGenelist(toNode, geneList) )//these two gene are on array and selected			
						connectivityIndexGroup2[exp] += Math.pow(Math.abs(expressionFromGene[exp+PathwayFiles.expNumGroup1]*expressionToGene[exp+PathwayFiles.expNumGroup1]),0.5);
				}
				//System.out.print("\t" + connectivityIndexGroup2[exp]);
			}//exp=0
			
			RankSumTest rankSumSelected=new RankSumTest(0.02,"less",connectivityIndexGroup1,connectivityIndexGroup2); 
			if(selectedGenes.split("\n").length>3)
				if(rankSumSelected.pValue<0.5)
					jLabelAllinPathway.setText("ZTEST of selected Genes  " + Method.getSimpleValue(rankSumSelected.pValue,4) + "---less");
				else
					jLabelAllinPathway.setText("ZTEST of selected Genes  " + Method.getSimpleValue((1-rankSumSelected.pValue),4) + "---greater");
			else
				jLabelAllinPathway.setText("");
		  //column names
		  String[] columnNames = new String[PathwayFiles.expNumGroup1 + PathwayFiles.expNumGroup2+3];
		  columnNames[0] = "GeneID";
		  columnNames[1] = "pValue";
		  columnNames[2] = "Degree";

		  for(int exp=0; exp<PathwayFiles.expNumGroup1; exp++){
			  columnNames[exp+3] = "group1";
		  }
		  for(int exp=0; exp<PathwayFiles.expNumGroup2; exp++){
			  columnNames[exp+3+PathwayFiles.expNumGroup1] = "group2";
		  }
		  //data
		  //Object[][] data = new Object[geneList.length][PathwayFiles.expNumGroup1 + PathwayFiles.expNumGroup2+3];
		  Vector vtRecord = new Vector(1);
		  for(int i=0; i<geneList.length; i++){
			  //record every gene
			  String recordString = "";
			  
			  //data[i][0] = geneList[i];
			  //data[i][2] = String.valueOf(objPathway.getDegree((String)geneList[i]));
			  double[] expression = (double[])(hashExpression.get(geneList[i]));
			  for(int j=0; j<PathwayFiles.expNumGroup1 + PathwayFiles.expNumGroup2; j++)
				  //data[i][j+3] = String.valueOf(expression[j]);
				  recordString += "---" + String.valueOf(expression[j]);
  			  //System.out.println("I am here" + hashExpression.size());
  			  double[] expressionGroup1 = new double[PathwayFiles.expNumGroup1];
  			  double[] expressionGroup2 = new double[PathwayFiles.expNumGroup2];
  			  for(int exp=0; exp<expressionGroup1.length; exp++){
  				expressionGroup1[exp] = expression[exp];
  			  }
  			  for(int exp=0; exp<expressionGroup2.length; exp++){
  				expressionGroup2[exp] = expression[exp+expressionGroup1.length];
  			  }
  			  RankSumTest rankSum=new RankSumTest(0.02,"equal",expressionGroup1,expressionGroup2); 
  			if(rankSum.pValue<0.5)
  				//data[i][1] 
    			//      = String.valueOf(Method.getSimpleValue(rankSum.pValue,4) + "---less");
  			  recordString = geneList[i] + "---" 
  			  	                         + String.valueOf(Method.getSimpleValue(rankSum.pValue,4) + "--less") + "---"
  			                             + String.valueOf(objPathway.getDegree((String)geneList[i]))
  			                             + recordString;
			else
				//data[i][1] 
  			    //      = String.valueOf(Method.getSimpleValue(rankSum.pValue,4) +  "---greater");				
	  			  recordString = geneList[i] + "---" 
                                             + String.valueOf(Method.getSimpleValue(rankSum.pValue,4) +  "-greater") + "---"
                                             + String.valueOf(objPathway.getDegree((String)geneList[i]))
                                             + recordString;
		  vtRecord.add(recordString);
		  }
	      TableSorter sorter = new TableSorter(new MyTableModel(vtRecord, PathwayFiles.expNumGroup1 + PathwayFiles.expNumGroup2+3, columnNames)); 
	      final JTable table = new JTable(sorter);
	      sorter.setTableHeader(table.getTableHeader());
	      table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		  TableColumn column = null;
		  for (int i = 0; i < PathwayFiles.expNumGroup1+PathwayFiles.expNumGroup2+3; i++) {
		      column = table.getColumnModel().getColumn(i);
	          column.setPreferredWidth(100);
		  }
	      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	      return table;
	  }else{//end if binary
		  //calculate overall result of selected
			Vector nodes = objPathway.getNodes();
			Vector edges = objPathway.getEdges();
			double[] connectivityIndex = new double[PathwayFiles.expNum];
			for(int exp=0; exp<PathwayFiles.expNum; exp++){
				connectivityIndex[exp]=0;
				for(int i=0; i<nodes.size(); i++){
					String nodeName = ((Node)nodes.get(i)).getName();
					double[] expressionGene = (double[])hashExpression.get(nodeName);
					if (expressionGene!=null && Method.geneInGenelist(nodeName, geneList)){//this gene is on array and selected
						connectivityIndex[exp] += Math.pow(expressionGene[exp]*expressionGene[exp],0.5);
					}
				}
				for(int i=0; i<edges.size(); i++){
					String fromNode = ((Node)(((Edge)edges.get(i)).getFromNode())).getName();
					String toNode = ((Node)((Edge)edges.get(i)).getToNode()).getName();
					double[] expressionFromGene = (double[])hashExpression.get(fromNode);
					double[] expressionToGene = (double[])hashExpression.get(toNode);
					if (expressionFromGene!=null && expressionToGene!=null
							&& Method.geneInGenelist(fromNode, geneList) && Method.geneInGenelist(toNode, geneList) )//these two gene are on array and selected			
						connectivityIndex[exp] += Math.pow(Math.abs(expressionFromGene[exp]*expressionToGene[exp]),0.5);
				}
			}//end for(int exp=0
			if(selectedGenes.split("\n").length>3)
				jLabelAllinPathway.setText("Correlation of selected Genes" + Method.getSimpleValue(Method.spearmanRank(connectivityIndex, Method.readPhenoData(PathwayFiles.phenoFileName),1),4));
			else
				jLabelAllinPathway.setText("");
			//header for table
		  String[] columnNames = new String[PathwayFiles.expNum+3];
		  columnNames[0] = "GeneID";
		  columnNames[1] = "correlation";
		  columnNames[2] = "Degree";
		  for(int exp=0; exp<PathwayFiles.expNum; exp++){
			  columnNames[exp+3] = "group";
		  }
		  //data
		  Object[][] data = new Object[geneList.length][PathwayFiles.expNum+3];
		  Vector vtRecord = new Vector(1);
		  for(int i=0; i<geneList.length; i++){
			  //record every gene
			  String recordString = "";
			  //data[i][0] = geneList[i];
			  //data[i][2] = String.valueOf(objPathway.getDegree((String)geneList[i]));
			  double[] expression = (double[])(hashExpression.get(geneList[i]));
			  for(int j=0; j<PathwayFiles.expNum; j++)
				  //data[i][j+3] = String.valueOf(expression[j]);
				  recordString += "---" + String.valueOf(expression[j]);
			  double corrValue = Method.getSimpleValue(Method.spearmanRank(expression, Method.readPhenoData(PathwayFiles.phenoFileName),1),4);
  			  //data[i][1] 
  			  //        = String.valueOf(corrValue);
  			  recordString = geneList[i] + "---" 
              + String.valueOf(corrValue) + "---"
              + String.valueOf(objPathway.getDegree((String)geneList[i]))
              + recordString;
  			  vtRecord.add(recordString);
		  }	
	      TableSorter sorter = new TableSorter(new MyTableModel(vtRecord, PathwayFiles.expNum+3, columnNames)); 
	      final JTable table = new JTable(sorter);
	      sorter.setTableHeader(table.getTableHeader());
		  //JTable table = new JTable(data, columnNames);
		  //column width
		  TableColumn column = null;
		  for (int i = 0; i < PathwayFiles.expNum+3; i++) {
		      column = table.getColumnModel().getColumn(i);
		      column.setMaxWidth(150);
		      column.setMinWidth(80);
	          column.setPreferredWidth(100); 
		  }
	      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		  return table;
	  }
	  
  }
  //This class is used to define table, it extends from AbstractTableModel
  class MyTableModel extends AbstractTableModel {
    private String[] columnNames;
    private Object[][] data;


    public MyTableModel(Vector vt, int columnNum, String[] columnName){
      columnNames = columnName;    //used to define the table headers         
      data  = new Object[vt.size()][columnNum];  //define the data size
      //Get data from a vector and evaluate data for tables
      for(int i=0; i< vt.size(); i++){
        String[] line = ((String)vt.get(i)).split("---");
        for(int j=0; j<line.length; j++){
            data[i][j] = line[j];
        }
       }
     }

     public int getColumnCount() {
       return columnNames.length;
     }

     public int getRowCount() {
       return data.length;
     }

     public String getColumnName(int col) {
       return columnNames[col];
     }

     public Object getValueAt(int row, int col) {
       return data[row][col];
     }


     public void setValueAt(Object value, int row, int col) {
         if (DEBUG) {
             System.out.println("Setting value at " + row + "," + col
                                + " to " + value
                                + " (an instance of "
                                + value.getClass() + ")");
         }

         data[row][col] = value;
         fireTableCellUpdated(row, col);

         if (DEBUG) {
             System.out.println("New value of data:");
             printDebugData();
         }
     }

     private void printDebugData() {
         int numRows = getRowCount();
         int numCols = getColumnCount();

         for (int i=0; i < numRows; i++) {
             System.out.print("    row " + i + ":");
             for (int j=0; j < numCols; j++) {
                 System.out.print("  " + data[i][j]);
             }
             System.out.println();
         }
         System.out.println("--------------------------");
     }
 }


}
