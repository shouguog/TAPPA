package edu.mcw.mcgee;

import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.*;


public class MainFrame extends JFrame {
	  JButton jButtonRun = new JButton("Run");
	  JButton jButtonExit = new JButton("Exit");
	  JButton jButtonAbout = new JButton("About us");
	  JButton jButtonBrowserExpression = new JButton("Choose Expression File");
	  JButton jButtonBrowserPheno = new JButton("Choose Pheno Data");
	  JTextField jTextFieldExpression = new JTextField("");
	  JTextField jTextFieldPheno = new JTextField("");
	  
	  JRadioButton jRadioButtonQuant   = new JRadioButton("Quantatative traits",true);
	  JRadioButton jRadioButtonBinary    = new JRadioButton("Binary traits",false);
	  ButtonGroup bgroup = new ButtonGroup();

	  JRadioButton jRadioButtonHuman   = new JRadioButton("human",true);
	  JRadioButton jRadioButtonMouse    = new JRadioButton("mouse",false);

	  ButtonGroup bgroupOrganism = new ButtonGroup();

	  
	  public MainFrame() {
		  super("Pathway Analysis by MCW");
		  this.getContentPane().setLayout(null);
		  this.setSize(600,450);
		  this.setLocation(100,100);
		  //
		  PathwayFiles.expressionFileName="";
		  PathwayFiles.phenoFileName="";
		  
		  //Expression file
		  jTextFieldExpression.setBounds(100,50,270,30);
		  jTextFieldExpression.setEditable(false);
		  this.getContentPane().add(jTextFieldExpression);
		  jButtonBrowserExpression.setBounds(400,50,170,30);
		  this.getContentPane().add(jButtonBrowserExpression);
		  jButtonBrowserExpression.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	        	  chooseExpressioFile();
	        	  return;
	          }
	          });		  
		  //PhenoTyepe
		  jTextFieldPheno.setBounds(100,150,270,30);
		  jTextFieldPheno.setEditable(false);
		  this.getContentPane().add(jTextFieldPheno);
		  jButtonBrowserPheno.setBounds(400,150,170,30);
		  this.getContentPane().add(jButtonBrowserPheno);
		  jButtonBrowserPheno.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	        	  choosePhenoFile();
	        	return;
	          }
	          });
		  //Quantatative traits or binary traits
		  bgroup.add(jRadioButtonQuant);
		  bgroup.add(jRadioButtonBinary);
		  JPanel radioPanel = new JPanel();
		  radioPanel.setLayout(new GridLayout(1, 2));
		  radioPanel.add(jRadioButtonQuant);
		  radioPanel.add(jRadioButtonBinary);
		  radioPanel.setBounds(100,200,300,30);
		  this.getContentPane().add(radioPanel);
		  
		  //Human or Mouse
		  bgroupOrganism.add(jRadioButtonMouse);
		  bgroupOrganism.add(jRadioButtonHuman);
		  JPanel radioPaneOrganism = new JPanel();
		  radioPaneOrganism.setLayout(new GridLayout(1, 2));
		  radioPaneOrganism.add(jRadioButtonHuman);
		  radioPaneOrganism.add(jRadioButtonMouse);
		  radioPaneOrganism.setBounds(100,260,300,30);
		  this.getContentPane().add(radioPaneOrganism);
		  
		  
		  //Add Button Run and Action listener
		  jButtonRun.setBounds(100,330,70,30);
		  this.getContentPane().add(jButtonRun);
		  jButtonRun.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	        	  if(PathwayFiles.expressionFileName.equals("") || PathwayFiles.phenoFileName.equals("")){
	        	       JOptionPane.showMessageDialog(null,"You must provide Expression and Pheno file","Medical College of Wisconsin",JOptionPane.ERROR_MESSAGE);
	        	  }else{
	        		  //organism
	        		  if(jRadioButtonHuman.isSelected())
	        			  PathwayFiles.organism = "human";
	        		  else
	        			  PathwayFiles.organism = "mouse"; //need to add error information here
	        		  //binary or quantitative
	        		  if(jRadioButtonQuant.isSelected()){
	        			  Analyzer.analysisQuantitative();
	        			  PathwayFiles.TypeOfphenoType = "quant";
	        		  }
	        		  else{
	        			  Analyzer.analysisBinary();
	        			  PathwayFiles.TypeOfphenoType = "binary";
	        		  }
	        	  }
	        	return;
	          }
	          });

		  //Add ButtonExit and Action listener
		  jButtonExit.setBounds(250,330,70,30);
		  this.getContentPane().add(jButtonExit);
		  jButtonExit.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	        	  System.exit(0);
	        	  return;
	          }
	          });
		  //Add ButtonAbout and Action listener
		  jButtonAbout.setBounds(400,330,130,30);
		  this.getContentPane().add(jButtonAbout);
		  jButtonAbout.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	      		AboutFrame aboutFrame = new AboutFrame(new Frame("MCW"));
	    		aboutFrame.setSize(500, 200);
	    		aboutFrame.setLocation(200,200);
	    		aboutFrame.show();
	    		return;
	          }
	          });
	  }
	  
	  void chooseExpressioFile(){
		    JFileChooser chooser = new JFileChooser();
		    chooser.showOpenDialog(null);
		    try{
		      File file = chooser.getSelectedFile();
		      String stringFileName = file.getPath();
		      jTextFieldExpression.setText(stringFileName);
		      PathwayFiles.expressionFileName = stringFileName;
		    }catch(Exception e1){
		      System.out.println("You select nothing");
		    }
		  }

	  void choosePhenoFile(){
		    JFileChooser chooser = new JFileChooser();
		    chooser.showOpenDialog(null);
		    try{
		      File file = chooser.getSelectedFile();
		      String stringFileName = file.getPath();
		      jTextFieldPheno.setText(stringFileName);
		      PathwayFiles.phenoFileName=stringFileName;
		    }catch(Exception e1){
		      System.out.println("You select nothing");
		    }
		  }

	  
	  public static void main(String[] args) {
	    MainFrame mainFrame = new MainFrame();
	    mainFrame.setVisible(true);
	  }
}
