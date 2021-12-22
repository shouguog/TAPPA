package edu.mcw.mcgee;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

public class expressionProfileLabel extends JLabel {
  Hashtable hashPictureData;
  Vector vtPictureData;
  Vector vtGeneName;
  String pictureDescription;
  String[] lineSpots;

  public expressionProfileLabel(Hashtable hash, String description, String[] linespots){
    lineSpots = linespots;
    hashPictureData = hash;
    pictureDescription = description;
  }

  public void paint(Graphics g) {
    g.setColor(Color.black);

    g.fillRect(30, 30, 400, 3);
    g.fillRect(30, 30, 3, 300);
    g.fillRect(30, 330, 400, 3);
    g.fillRect(430, 30, 3, 300);
    vtPictureData = new Vector(1);
    vtGeneName    = new Vector(1);
    Enumeration enum = hashPictureData.keys();
    int numberHyb = 0;
    while (enum.hasMoreElements()) {
      Object key = enum.nextElement();
      Vector vtLine = (Vector) hashPictureData.get(key);
      vtPictureData.addElement(vtLine);
      vtGeneName.addElement(key);
    }
    try {
      numberHyb = ( (Vector) vtPictureData.get(0)).size(); //Number of experiments
    }
    catch (Exception e) {}

    if (numberHyb >= 2) {
      int intervalHyb = 400 / (numberHyb - 1); //Interval between experiments
      //Draw Coordinates
      for (int i = 0; i < numberHyb; i++) {
        g.fillRect(30 + i * intervalHyb, 320, 3, 10);
        g.drawString(Integer.toString(i+1), 28 + i* intervalHyb, 350);
      }

//
      double doubleYmax = -100.0;
      double doubleYmin = 100.0;

      for (int i = 0; i < vtPictureData.size(); i++) {
        Vector vtLine = (Vector) vtPictureData.get(i);
        for (int j = 0; j < vtLine.size(); j++) {
          try{
            if (doubleYmax < Double.parseDouble( (String) vtLine.get(j)))
              doubleYmax = Double.parseDouble( (String) vtLine.get(j));

            if (doubleYmin > Double.parseDouble( (String) vtLine.get(j)))
              doubleYmin = Double.parseDouble( (String) vtLine.get(j));
          }catch(Exception ex){}
        }
      }
      //Draw Coordinates
      g.fillRect(30, 80, 10, 3);
      g.fillRect(30, 280, 10, 3);
      g.fillRect(30, 180, 10, 3);
      g.drawString((Double.toString(doubleYmax)).substring(0,4), 0, 80);
      g.drawString((Double.toString(doubleYmin)).substring(0,4), 0, 280);
      int intAverage = (int)(500*(doubleYmin + doubleYmax));
      g.drawString((Double.toString( intAverage / 1000.0D)).substring(0,4), 0, 180);
      //Draw Line
              for (int i = 0; i < vtPictureData.size(); i++) {
                boolean select = false;
                for(int color=0; color<lineSpots.length; color++){
                  if(((String)vtGeneName.get(i)).equals(lineSpots[color])){
                    select = true;
                    break;
                  }
                }
                if(select){
                  g.setColor(Color.orange);
                }else{
                  g.setColor(Color.gray);
                }
                Vector vtLine = (Vector) vtPictureData.get(i);
                for (int j = 0; j < vtLine.size() - 1; j++) {
                  //System.out.println(vtLine);
                  int intYStart = 0;
                 try{
                   intYStart = new Double(200 *
                                          ((Double.parseDouble( (String) vtLine.get(j))) -
                                           doubleYmin) / (doubleYmax - doubleYmin)).intValue();

                   g.fillOval(28 + j * intervalHyb,
                              278 - intYStart,
                              5,
                              5);
                 }catch(Exception ex){
                 }


                  int intYEnd =0;
                  try{  intYEnd = new Double(200 *
                                         (Double.parseDouble( (String) vtLine.get(j+1)) -
                                          doubleYmin) / (doubleYmax - doubleYmin)).intValue();
                    if(!vtLine.get(j+1).toString().equals((new String("NaN")))){
                    g.fillOval(28 + (j+1) * intervalHyb,
                               278 - intYEnd,
                               5,
                               5);
                    }
                  }catch(Exception ex){
                  }
                  try{
                    intYEnd = new Double(200 *
                                         (Double.parseDouble( (String) vtLine.get(j+1)) -
                                          doubleYmin) / (doubleYmax - doubleYmin)).intValue();
                    intYStart = new Double(200 *
                       ((Double.parseDouble( (String) vtLine.get(j))) -
                        doubleYmin) / (doubleYmax - doubleYmin)).intValue();
                    if(!vtLine.get(j).toString().equals((new String("NaN"))) &&
                       !vtLine.get(j+1).toString().equals((new String("NaN")))){
                      g.drawLine(30 + j * intervalHyb,
                                 280 - intYStart,
                                 30 + (j + 1) * intervalHyb,
                                 280 - intYEnd);
                    }
                  }catch(Exception ex){}
                }
              }

      g.setColor(Color.black);
      g.setFont(new java.awt.Font("Dialog", 0, 14));
      g.drawString(pictureDescription, 60, 370);
    }else{
      g.drawString("Sorry, no expression profile id provided", 60, 150);
    }
  }

public void setLineSelect(String[] spot){
    lineSpots = spot;
  }
}

