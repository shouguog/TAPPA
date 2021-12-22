package edu.mcw.mcgee;

import java.awt.*;

import javastat.inference.nonparametric.RankSumTest;

import javax.swing.JLabel;
import javax.swing.*;

import java.util.Hashtable;
import java.util.Vector;
import java.sql.*;
import java.util.*;

class mapLabel extends JLabel {

  Vector coordinates;
  Vector selectedCoordinates;
  Hashtable hashExpression;
//  Vector allCoordinates;
//  Vector chipCoordinates;
  double ratio = 1.00D;
  Vector selectedArea = new Vector(1);
  ImageIcon iconPic;
  int a1 = 2;
  int a2 = 2;
  int a3 = 0;
  int a4 = 0;

  
//construction function
  public mapLabel(String pathwayName, Icon icon, Vector allGenes, Vector selectedGenes, ImageIcon img, Hashtable profile)
    {
        super(icon);
        iconPic = img;
        coordinates = allGenes;
        selectedCoordinates = selectedGenes;
        hashExpression = profile;
        //       chipCoordinates = getPathwayCoorinChip(pathwayName);
    }

    public void redrawMap()
    {
      repaint();
    }

    public void paintComponent(Graphics g)
    {
        drawMapGraphics(g, "drawOnScreen");
    }

    private void drawMapGraphics(Graphics g, String s){
      super.paintComponent(g);
      g.setColor(Color.white);
      g.fillRect(0,
                 0,
                 iconPic.getIconWidth(),
                 iconPic.getIconHeight());
      g.drawImage(iconPic.getImage(),
                  0,
                  0,
                  new Double(iconPic.getIconWidth()*ratio).intValue(),
                  new Double(iconPic.getIconHeight()*ratio).intValue(),
                  0,
                  0,
                  iconPic.getIconWidth(),
                  iconPic.getIconHeight(),
                  this);
      //Draw a Color bar
      g.drawString("Low", 15,40);
      g.drawString("High", 95,40);
		for (int i=0; i<=255; i=i+5) {
			g.setColor(new Color(i,255-i,0));
		    g.fillRect((int)(i+200)/5,20,1,20);
		}
      //figure out all genes
      for(int i = 0; i< coordinates.size(); i++){
        g.setColor(Color.black);
        String[] coordinate = ((String)coordinates.get(i)).split("\t");
        String[] strCoordinate = coordinate[1].split(",");
            int    intCoordinate[] = {0, 0, 0, 0};
            for(int j=0; j<strCoordinate.length; j++){
              intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
              intCoordinate[j] = new Double(intCoordinate[j]*ratio).intValue();
            }
            g.fillRect(intCoordinate[0],  intCoordinate[1],
                       3,
                       intCoordinate[3] -  intCoordinate[1]);
            g.fillRect(intCoordinate[0],  intCoordinate[1],
                       intCoordinate[2] - intCoordinate[0],
                       3);
            g.fillRect(intCoordinate[0],  intCoordinate[3],
                       intCoordinate[2] - intCoordinate[0],
                       3);
            g.fillRect(intCoordinate[2],  intCoordinate[1],
                       3,
                       intCoordinate[3] -  intCoordinate[1] + 3 );

            g.drawRect(intCoordinate[0],  intCoordinate[1],
                       intCoordinate[2] - intCoordinate[0],
                       intCoordinate[3] -  intCoordinate[1]);

          }

      //figure out genes in data Matrix
      //We only use the BEST GENE to define box color, when there are several genes in a location
      	Hashtable drawCoordinate = new Hashtable(1);  //used to record the Box has been drawn 
        for(int i = 0; i< selectedCoordinates.size(); i++){
          //g.setColor(Color.red);
          String[] coordinate = ((String)selectedCoordinates.get(i)).split("\t");
          //define expression
  			if(PathwayFiles.TypeOfphenoType.equals("binary")){
  				double[] expression = (double[])(hashExpression.get(coordinate[0]));
  				//System.out.println("I am here" + hashExpression.size());
  				double[] expressionGroup1 = new double[PathwayFiles.expNumGroup1];
  				double[] expressionGroup2 = new double[PathwayFiles.expNumGroup2];
  				for(int exp=0; exp<expressionGroup1.length; exp++){
  					expressionGroup1[exp] = expression[exp];
  				}
  				for(int exp=0; exp<expressionGroup2.length; exp++){
  					expressionGroup2[exp] = expression[exp+expressionGroup1.length];
  				}
  				double pValue = new RankSumTest(0.02,"equal",expressionGroup1,expressionGroup2).pValue;
  				if(drawCoordinate.containsKey(coordinate[1])){//this Box has been drawn
  					if(Double.parseDouble((String)(drawCoordinate.get(coordinate[1]))) < pValue){ //only need to redraw when meet lower value 
  	  					g.setColor(Method.getColor(pValue)); 
  	  					String[] strCoordinate = coordinate[1].split(",");
  	  					int    intCoordinate[] = {0, 0, 0, 0};
  	  					for(int j=0; j<strCoordinate.length; j++){
  	  						intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
  	  						intCoordinate[j] = new Double(intCoordinate[j]*ratio).intValue();
  	  					}
  	  					g.fillRect(intCoordinate[0],  intCoordinate[1],
  	  							3,
  	  							intCoordinate[3] -  intCoordinate[1]);
  	  					g.fillRect(intCoordinate[0],  intCoordinate[1],
  	  							intCoordinate[2] - intCoordinate[0],
  	  							3);
  	  					g.fillRect(intCoordinate[0],  intCoordinate[3],
  	  							intCoordinate[2] - intCoordinate[0],
  	  							3);
  	  					g.fillRect(intCoordinate[2],  intCoordinate[1],
  	  							3,
  	  							intCoordinate[3] -  intCoordinate[1] + 3 );
  	  					g.drawRect(intCoordinate[0],  intCoordinate[1],
  	  							intCoordinate[2] - intCoordinate[0],
  	  							intCoordinate[3] -  intCoordinate[1]);
  	  					//record this position
  	  					drawCoordinate.put(coordinate[1], Double.toString(pValue));
  	  					//g.drawString( "Locu(" + i + ")", intCoordinate[0] + 20, intCoordinate[1] + 20);
  					}
  				}else{//this Box has not been drawn
  					g.setColor(Method.getColor(pValue)); 
  					String[] strCoordinate = coordinate[1].split(",");
  					int    intCoordinate[] = {0, 0, 0, 0};
  					for(int j=0; j<strCoordinate.length; j++){
  						intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
  						intCoordinate[j] = new Double(intCoordinate[j]*ratio).intValue();
  					}
  					g.fillRect(intCoordinate[0],  intCoordinate[1],
  							3,
  							intCoordinate[3] -  intCoordinate[1]);
  					g.fillRect(intCoordinate[0],  intCoordinate[1],
  							intCoordinate[2] - intCoordinate[0],
  							3);
  					g.fillRect(intCoordinate[0],  intCoordinate[3],
  							intCoordinate[2] - intCoordinate[0],
  							3);
  					g.fillRect(intCoordinate[2],  intCoordinate[1],
  							3,
  							intCoordinate[3] -  intCoordinate[1] + 3 );
  					g.drawRect(intCoordinate[0],  intCoordinate[1],
  							intCoordinate[2] - intCoordinate[0],
  							intCoordinate[3] -  intCoordinate[1]);
  					//record this position
  					drawCoordinate.put(coordinate[1], Double.toString(pValue));
  					//g.drawString( "Locu(" + i + ")", intCoordinate[0] + 20, intCoordinate[1] + 20);
  				}
  			}else{//quantitative
  				double[] expression = (double[])(hashExpression.get(coordinate[0]));
  				double corrValue = Method.spearmanRank(expression, Method.readPhenoData(PathwayFiles.phenoFileName),1);
  				if(drawCoordinate.containsKey(coordinate[1])){//this Box has been drawn
  					if(Math.abs(Double.parseDouble((String)(drawCoordinate.get(coordinate[1])))) > Math.abs(corrValue)){ //only need to redraw when meet higher  
  	  					g.setColor(Method.getColor(corrValue)); 
  	  					String[] strCoordinate = coordinate[1].split(",");
  	  					int    intCoordinate[] = {0, 0, 0, 0};
  	  					for(int j=0; j<strCoordinate.length; j++){
  	  						intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
  	  						intCoordinate[j] = new Double(intCoordinate[j]*ratio).intValue();
  	  					}
  	  					g.fillRect(intCoordinate[0],  intCoordinate[1],
  	  							3,
  	  							intCoordinate[3] -  intCoordinate[1]);
  	  					g.fillRect(intCoordinate[0],  intCoordinate[1],
  	  							intCoordinate[2] - intCoordinate[0],
  	  							3);
  	  					g.fillRect(intCoordinate[0],  intCoordinate[3],
  	  							intCoordinate[2] - intCoordinate[0],
  	  							3);
  	  					g.fillRect(intCoordinate[2],  intCoordinate[1],
  	  							3,
  	  							intCoordinate[3] -  intCoordinate[1] + 3 );
  	  					g.drawRect(intCoordinate[0],  intCoordinate[1],
  	  							intCoordinate[2] - intCoordinate[0],
  	  							intCoordinate[3] -  intCoordinate[1]);
  	  					//record this position
  	  					drawCoordinate.put(coordinate[1], Double.toString(corrValue));
  	  					//g.drawString( "Locu(" + i + ")", intCoordinate[0] + 20, intCoordinate[1] + 20);
  					}
  				}else{//this Box has not been drawn
  					g.setColor(Method.getColor(corrValue)); 
  					String[] strCoordinate = coordinate[1].split(",");
  					int    intCoordinate[] = {0, 0, 0, 0};
  					for(int j=0; j<strCoordinate.length; j++){
  						intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
  						intCoordinate[j] = new Double(intCoordinate[j]*ratio).intValue();
  					}
  					g.fillRect(intCoordinate[0],  intCoordinate[1],
  							3,
  							intCoordinate[3] -  intCoordinate[1]);
  					g.fillRect(intCoordinate[0],  intCoordinate[1],
  							intCoordinate[2] - intCoordinate[0],
  							3);
  					g.fillRect(intCoordinate[0],  intCoordinate[3],
  							intCoordinate[2] - intCoordinate[0],
  							3);
  					g.fillRect(intCoordinate[2],  intCoordinate[1],
  							3,
  							intCoordinate[3] -  intCoordinate[1] + 3 );
  					g.drawRect(intCoordinate[0],  intCoordinate[1],
  							intCoordinate[2] - intCoordinate[0],
  							intCoordinate[3] -  intCoordinate[1]);
  					//record this position
  					drawCoordinate.put(coordinate[1], Double.toString(corrValue));
  					//g.drawString( "Locu(" + i + ")", intCoordinate[0] + 20, intCoordinate[1] + 20);
  				}
  			}
        }
        //Draw Selected Area
        g.setColor(Color.orange);
        for(int i=0; i<selectedArea.size(); i++){
          int[] coordinates = (int[])selectedArea.get(i);
          g.fillRect(coordinates[0],  coordinates[1],
                     2,
                     coordinates[3]);
          g.fillRect(coordinates[0],  coordinates[1],
                     coordinates[2],
                     2);
          g.fillRect(coordinates[0], coordinates[1] + coordinates[3],
                     coordinates[2],
                     2);
          g.fillRect(coordinates[2] + coordinates[0],  coordinates[1],
                     2,
                     coordinates[3] + 2 );

        }
        g.setColor(Color.black);
        g.fillRect(a1,  a2,
                   2,
                   a4);
        g.fillRect(a1,  a2,
                   a3,
                   2);
        g.fillRect(a1, a2 + a4,
                   a3,
                   2);
        g.fillRect(a3 + a1,  a2,
                   2,
                   a4 + 2 );


    }
}

