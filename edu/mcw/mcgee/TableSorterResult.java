
/*
 * This Program is used to save pathway analysis results
 * TableSorter.java is needed, because we can sort results
 */
package edu.mcw.mcgee;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import java.awt.*;
import java.io.*;
import java.util.Hashtable;


public class TableSorterResult extends JPanel {
    private boolean DEBUG = false;
    JButton buttonGet;                          //buttonGet used to draw P-value diagrams
    JButton buttonShow;                         //buttonShow used to show selected pathway diagram
    JButton buttonPdf;                          //buttonShow used to run the program automatically and create a pdf file
    JButton buttonSave;                         //buttonSave used to save the results
    Hashtable hashExpression_normal = new Hashtable();
    JFileChooser chooser = new JFileChooser();
    String fileName = "";
	double[] phenoData;                               //used to record normalized data
	
	double[] getPhenoData(){
		return phenoData;
	}
	void setPhenoData(double[] phenoData){
		this.phenoData = phenoData;
	}
	
	
    public TableSorterResult(Hashtable hashExpression){
		hashExpression_normal = hashExpression;
    }

    public TableSorterResult(Vector vt_test, int columnNum, String[] columnName, Hashtable hashExpression) {
        super(new GridLayout(1,0));
		hashExpression_normal = hashExpression;
        TableSorter sorter = new TableSorter(new MyTableModel(vt_test, columnNum, columnName)); 
        final JTable table = new JTable(sorter);
        sorter.setTableHeader(table.getTableHeader());
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        JList list = new JList();
        //Set up tool tips for column headers.
        table.getTableHeader().setToolTipText(
                "Click to specify sorting; Control-Click to specify secondary sorting");

        this.setLayout(new BorderLayout());
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(BorderLayout.CENTER, scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonGet = new JButton("Calculate P -Value with permutation method");
        buttonPanel.add(buttonGet);
        buttonShow = new JButton("Display Pathway Diagram which you select");
        buttonPanel.add(buttonShow);
        buttonPdf = new JButton("Running Automatically and create pdf file");
        buttonPanel.add(buttonPdf);
        buttonSave = new JButton("Save the Result");
        buttonPanel.add(buttonSave);
        add(BorderLayout.SOUTH, buttonPanel);

        //Add Action listener to buttonGet
        buttonGet.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              Vector vtName = new Vector();
              Hashtable hashValue = new Hashtable();
              int[] selectedRow = table.getSelectedRows();
              //prepare data
              for(int i=0; i<selectedRow.length; i++){
                vtName.addElement(table.getValueAt(selectedRow[i], 0)+".edge");
                double[] pValue =  {Double.parseDouble(table.getValueAt(selectedRow[i], 2).toString()),
                		Double.parseDouble(table.getValueAt(selectedRow[i], 3).toString()),
                		Double.parseDouble(table.getValueAt(selectedRow[i], 4).toString()),
                		Double.parseDouble(table.getValueAt(selectedRow[i], 5).toString()),
                		Double.parseDouble(table.getValueAt(selectedRow[i], 6).toString()),
                		Double.parseDouble(table.getValueAt(selectedRow[i], 7).toString()),
                		Double.parseDouble(table.getValueAt(selectedRow[i], 8).toString())};
                hashValue.put(table.getValueAt(selectedRow[i], 0)+".edge", pValue);
              }
              if(selectedRow.length>0){
            	  //permute parameters
            	  //save detail information?
            	  String message = null;
            	  int returnValue = JOptionPane.showConfirmDialog(null, 
            			  "Save Permutation Detain information", "Save Permutation Detain information", JOptionPane.YES_NO_OPTION);
            	  if (returnValue == JOptionPane.YES_OPTION) {
            		     	message = "Yes";
            	  } else if (returnValue == JOptionPane.NO_OPTION) {
            		     	message = "No";
            	  }
            	  String s = "";
            	  if(message.equals("Yes")) {
            		  int state = chooser.showSaveDialog(null);
            		  File file = chooser.getSelectedFile();
            		  if(state == JFileChooser.APPROVE_OPTION)
            		  s = file.getPath();
            	  }
            	  //number of permutation
            	  int permNum = Integer.parseInt(JOptionPane.showInputDialog(null,
            			  "Time of Permutation",
            			  "Permutation Times",
            			  JOptionPane.QUESTION_MESSAGE)); 
            	  Hashtable temp = Analyzer.analysis_perm(hashExpression_normal, permNum, vtName, s, hashValue);
          		  //header            	  
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
                  Vector analysisResult = new Vector(1);
          		  HashMap pathwayName = PathwayFiles.getPathwayName();

            	  Enumeration pathwayID = temp.keys();
            	  Object ID = "";
            	  while((pathwayID.hasMoreElements())){
            		  String pathwayString = pathwayID.nextElement().toString();
            		  double[] tempResult = (double[])temp.get(pathwayString);
            		  analysisResult.add(pathwayString.substring(0,8)
          								+ "---" + pathwayName.get(pathwayString) + "---" + 
          								tempResult[0] + "---" + 
            				  	        tempResult[1] + "---" + 
            				  	        tempResult[2] + "---" + 
            				  	        tempResult[3] + "---" + 
            				  	        tempResult[4] + "---" + 
            				  	        tempResult[5] + "---" + 
            				  	        tempResult[6] + "---");
            	  }
            	  //System.out.println(analysisResult);
                  TableSorterResultPvalue expTable = new TableSorterResultPvalue(hashExpression_normal);
                  expTable.createAndShowGUI(analysisResult, numCol, header);
              }
              return;
            }
          });


        //Add Action Listener buttonShow
        buttonShow.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            Vector vtName = new Vector();
            int[] selectedRow = table.getSelectedRows();
            //add selected table rows
            for(int i=0; i<selectedRow.length; i++){
              vtName.addElement(table.getValueAt(selectedRow[i], 0));
            }
            //Display
            for(int i=0; i<vtName.size(); i++){
            	Mapping mapSample = new Mapping();
            	Vector allGenes = Method.getGeneLocation((String)vtName.get(i));
        		//System.out.println("Shouguo" + hashExpression_normal);
            	Vector selectedGenes = Method.getGeneLocationWithExpression((String)vtName.get(i), hashExpression_normal);
            	mapSample.showFrame(vtName.get(i).toString(), selectedGenes, allGenes, hashExpression_normal);
            }
            return;
           }
         });


 
         //Add Action Listener buttonSave
         buttonSave.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
             chooser.setSelectedFile(new File((fileName).replaceAll(".txt", "")
                                              + "_P value"
                                              + ".txt"));
             int state = chooser.showSaveDialog(null);

             File file = chooser.getSelectedFile();
             String s = "CANCELED";
             if(state == JFileChooser.APPROVE_OPTION)
               s = file.getPath();
               //    JOptionPane.showMessageDialog(null, s);
             try{
               BufferedOutputStream bufOut=
                   new BufferedOutputStream(new FileOutputStream(s));
               DataOutputStream dataOut = new DataOutputStream(bufOut);
               dataOut.writeBytes(
              "Pathway"       + "\t"
              + "Description" + "\t"
              + "Genes"       + "\t");
               dataOut.writeBytes("All Genes in Chip\t");
               dataOut.writeBytes("P-Value(Cluster)\t");
               dataOut.writeBytes("P-Value(Chip)\n");

               String result = new String("");
               for(int i=0; i<table.getRowCount(); i++){
                 for(int j=0; j<table.getColumnCount()-1; j++){
                   dataOut.writeBytes((String)table.getValueAt(i,j) + "\t");
                 }
                 dataOut.writeBytes((String)table.getValueAt(i,table.getColumnCount()-1) + "\n");
               }

               bufOut.close();
               dataOut.close();}
             catch(Exception e1){
               System.out.println("can not create file" + e1.getMessage());
             }
             return;
           }
         });

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

    /**
     * Create the GUI and show it.
     */
    public void createAndShowGUI(Vector vt_test, int columnNum, String[] columnName) {
      //Make sure we have nice window decorations.
      JFrame.setDefaultLookAndFeelDecorated(true);

      //Create and set up the window.we use file name as frame title
      //Because users may want to compare results from different datasets
      JFrame frame = new JFrame("Pathway Result");

      //Create and set up the content pane.
      TableSorterResult newContentPane = new TableSorterResult(vt_test, columnNum, columnName, this.hashExpression_normal);
      newContentPane.setOpaque(true); //content panes must be opaque
      frame.setBounds(100,50,800,500);
      frame.setContentPane(newContentPane);

      //Display the window.
      frame.setVisible(true);
    }

    public static void main(String[] args) {
        Vector vector_test = new Vector(1);
        String header[] = {"Pathway","Description","Genes","All Genes in Chip","P-Value(Cluster)"};
        int numCol = 5;
        //
        vector_test.addElement("mmu00010---hsjsjs---0.111---0.0032---0.4666");
        vector_test.addElement("mmu00020---sjsjdjsdk---0.222---0.0022---0.6666");
        vector_test.addElement("mmu00030---dsdsds---0.333---0.0011---0.5666");
        
        //
        TableSorterResult expTable = new TableSorterResult(new Hashtable(1));
        expTable.createAndShowGUI(vector_test, numCol, header);
    }
  }
