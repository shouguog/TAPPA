package edu.mcw.mcgee;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
//import com.borland.jbcl.layout.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.Vector;
import java.awt.*;
import javax.swing.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.awt.event.*;
import java.io.*;

public class expressionProfilePicture extends JFrame {

  String pictureDescription;
  Hashtable pictureData;
  Vector vtPictureData;
  Vector vtHeader;
  JTable jTableExpression;
  String[] lineSpots = {};
  public expressionProfileLabel label;

  public expressionProfilePicture(Hashtable hashExpression, String pictureDes) throws HeadlessException {
    super("Expression Profiles");
    pictureDescription = pictureDes;
    pictureData        = hashExpression;
    label = new expressionProfileLabel(pictureData, pictureDescription, lineSpots);
    this.getContentPane().setBackground(Color.white);
//Top Add label
    JSplitPane jSplitPane1 = new JSplitPane();
    jSplitPane1.setDividerLocation(400);
    jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
    jSplitPane1.add(label,JSplitPane.TOP);

//    this.getContentPane().add(label, new XYConstraints(25, 10,450,360));
//
    vtPictureData = new Vector(1);
    vtHeader      = new Vector(1);
    vtHeader.addElement("Gene Name");
    Enumeration enumElement = pictureData.elements();

    int intHyb = ((Vector)enumElement.nextElement()).size();
    for(int i=0; i<intHyb; i++){
      vtHeader.addElement("Exp" + (i+1));
    }

    Enumeration enumKey = pictureData.keys();
    while(enumKey.hasMoreElements()){
      Vector vtLine = new Vector(1);
      String key = (String)enumKey.nextElement();
      vtLine.addElement(key);
      Vector vtLineExpression = (Vector) pictureData.get(key);
      for(int i=0; i<vtLineExpression.size(); i++){
        vtLine.addElement(vtLineExpression.get(i));
      }
      vtPictureData.addElement(vtLine);
    }
    jTableExpression = new JTable(vtPictureData, vtHeader);
    jTableExpression.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    JScrollPane pane = new JScrollPane();
    pane.getViewport().add(jTableExpression);
//
    //
    
    JButton jButtonFindSelect = new JButton("Find Selected");
    jButtonFindSelect.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          String[] lineSelect = new String[jTableExpression.getSelectedRowCount()];
          int[] intLine       = jTableExpression.getSelectedRows();
          for(int i=0; i<jTableExpression.getSelectedRowCount(); i++)
            lineSelect[i] = (String)jTableExpression.getValueAt(intLine[i], 0);
          lineSpots = lineSelect;
          label.setLineSelect(lineSelect);
          label.repaint();
          return;
        }
    });

    //Bottom add table and buttons
    JPanel bottom = new JPanel();
    bottom.setLayout(new BorderLayout());
    bottom.add(pane, BorderLayout.CENTER);

    JPanel south = new JPanel();
    south.setLayout(new GridLayout(1,1));
    south.add(jButtonFindSelect);
    bottom.add(south, BorderLayout.SOUTH);

    jSplitPane1.add(bottom, JSplitPane.BOTTOM);
  //Add to frame
    this.getContentPane().add(jSplitPane1);

  }

  public void setlineSpots(String[] spot){
    lineSpots = spot;
  }

  public static void main(String[] args) throws HeadlessException {
    Hashtable hashSample = new Hashtable();
    for(int i= 1; i<5; i++){
      Vector line = new Vector(1);
      line.addElement(Double.toString(0.3 + i*0.5));
      line.addElement(Double.toString(0.5 + i*0.7));
      line.addElement("NdddfdfaN");
      line.addElement(Double.toString(0.3 + i*0.5));
      line.addElement(Double.toString(0.5 + i*0.7));
      line.addElement(Double.toString(0.5 + i*0.7));
      hashSample.put("Line:" + i, line);
    }
    expressionProfilePicture expressionProfilePicture1
        = new expressionProfilePicture(hashSample, "Just A Test");
    String[] abc = {"Line:2"};
    expressionProfilePicture1.label.repaint();
    expressionProfilePicture1.setLocation(50, 50);
    expressionProfilePicture1.setSize(500, 700);
    expressionProfilePicture1.setVisible(true);
  }
}
