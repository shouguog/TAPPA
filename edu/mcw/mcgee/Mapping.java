package edu.mcw.mcgee;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.sql.*;
import java.awt.event.*;
import java.io.*;
import java.util.Hashtable;

public class Mapping {
	static Vector coordinatesMark;
	static JList   listCoor;
	//We use selected Area to record multiple selection
	double ratio = 1.0D;
	static Vector selectedArea = new Vector(1);
	static int intSelectedTop;
	static int intSelectedBottom;
	static int intSelectedLeft;
	static int intSelectedRight;

	static int xLocation = 0;
	static int yLocation = 0;
	static int xLocationSelf = 0;
	static int yLocationSelf = 0;
	//Used to record the popup menu
	JPopupMenu popup = new JPopupMenu();
	static int xpopPosition = 0;
	static int ypopPosition = 0;
	//
	Hashtable  hashExpressionProfile = new Hashtable();
	mapLabel background;
	String fileName="";
	Color[] colors = {Color.yellow, Color.green, Color.blue, Color.red};
	public Mapping(){
		
	}

	public static void main(String[] args) {
		Vector selectedGenes = new Vector(1);
		Vector allGenes= new Vector(1);
		allGenes = (Method.getGeneLocation("mmu00020"));
		Hashtable hashExpression = new Hashtable(1);
		hashExpression.put("12974", "mouse");
		hashExpression.put("71832", "mmu00020");
		hashExpression.put("17448", "mmu00020");
		hashExpression.put("17449", "mmu00020");
		hashExpression.put("18563", "mmu00020");
		
		selectedGenes = Method.getGeneLocationWithExpression("mmu00020", hashExpression);

		Mapping mapSample = new Mapping();
		mapSample.showFrame("mmu00020", selectedGenes, allGenes, hashExpression);
	}

	
	public void setRatio(double ratio_ref){
		ratio = ratio_ref;
	}

	public void setMapLabel(mapLabel mapLabel){
		this.background = mapLabel;
		selectedArea = new Vector(1);
		background.selectedArea = new Vector(1);
	}

	public void setfileName(String fileName){
		this.fileName = fileName;
	}

	//This Method is used to display
	void showFrame(String pathwayName, Vector selectedGenes, Vector allGenes, Hashtable profile){
		System.out.println(pathwayName + "------Mapping----------");
		hashExpressionProfile = profile;
		final JFrame frame = new JFrame(pathwayName + " " + this.fileName + "  " + PathwayFiles.getPathwayName().get(pathwayName+".edge"));
		final String pathwayName_rec = pathwayName;
		//System.out.println(allGenes);
		//System.out.println(selectedGenes);
		final Vector selectedGenes_rec = selectedGenes;
		final Vector allGenes_rec = allGenes;
		final ImageIcon icon;
		icon = new ImageIcon(PathwayFiles.organism + "\\Mapping\\" + pathwayName + ".gif");

		//
    ActionListener al = new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    		if(((JMenuItem) e.getSource()).getText().
    				equals("Retrive Information you selected")){
   		          if(( intSelectedLeft - intSelectedTop) < 10){
   		        	  JOptionPane.showMessageDialog(null,"Sorry! You must select again");
    		       }else{
    		  			//collect information
    			        System.out.println(selectedArea);
    		  			String fileInformationAllGene=new String("");//used to record all genes information
    					String fileInformationSelectedGene=new String("");//used to record selected genes information
    		            for(int iSelected=0; iSelected<selectedArea.size(); iSelected++){
    		               int[] coordinates = (int[])selectedArea.get(iSelected);
    		                for(int i = 0; i< allGenes_rec.size(); i++){
    		            	   String coordinate[] = ((String)allGenes_rec.get(i)).split("\t");
    		            	   String[] strCoordinate = coordinate[1].split(",");
    						int intCoordinate[] = {0, 0, 0, 0};
    						for(int j=0; j<strCoordinate.length; j++){
    							intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
    						}
    		                if(coordinates[1]/ratio  < intCoordinate[1] &&
    		                     coordinates[2]/ratio + coordinates[0]/ratio  > intCoordinate[2] &&
    		                     coordinates[0]/ratio  < intCoordinate[0] &&
    		                     coordinates[3]/ratio + coordinates[1]/ratio  > intCoordinate[3]){
    							 fileInformationAllGene = fileInformationAllGene + coordinate[0] + "\n"; 
    							}	
    		                }
    		                //collect information
    		                for(int i = 0; i< selectedGenes_rec.size(); i++){
    		                	String coordinate[] = ((String)selectedGenes_rec.get(i)).split("\t");
    		                	String[] strCoordinate = coordinate[1].split(",");
    		                	int intCoordinate[] = {0, 0, 0, 0};
    		                	for(int j=0; j<strCoordinate.length; j++){
    		                		intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
    		                	}
    		                	if(coordinates[1]/ratio  < intCoordinate[1] &&
    		                        coordinates[2]/ratio + coordinates[0]/ratio  > intCoordinate[2] &&
    		                        coordinates[0]/ratio  < intCoordinate[0] &&
    		                        coordinates[3]/ratio + coordinates[1]/ratio  > intCoordinate[3]){
    		                	fileInformationSelectedGene = fileInformationSelectedGene + coordinate[0] + "\n"; 
    		                	}
    		                }
    		            	}
    						//show informatio
    						if(!fileInformationAllGene.equals("")){
    							mapBoxInfo mapBox = new mapBoxInfo(pathwayName_rec+".edge", hashExpressionProfile, fileInformationSelectedGene, fileInformationAllGene);
    							mapBox.pack();
    							mapBox.setLocation(xLocation, yLocation);
    							xLocation += 20;
    							yLocation += 16;
    							mapBox.setTitle("The Result in Pathway");
    							mapBox.setSize(1000,600);
    							mapBox.setVisible(true);
    						}
    		          	}
    			}
    		if(((JMenuItem) e.getSource()).getText().
    				equals("Retrive All Information in This Pathway")){
    			String fileInformationAllGene=new String("");//used to record all genes information
    			String fileInformationSelectedGene=new String("");//used to record selected genes information
    			//collect information
    			for(int i = 0; i< allGenes_rec.size(); i++){
    				String coordinate[] = ((String)allGenes_rec.get(i)).split("\t");
    				String[] strCoordinate = coordinate[1].split(",");
    				int intCoordinate[] = {0, 0, 0, 0};
    				for(int j=0; j<strCoordinate.length; j++){
    					intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
    				}
    				fileInformationAllGene = fileInformationAllGene + coordinate[0] + "\n"; 
    			}
    			//collect information
    			for(int i = 0; i< selectedGenes_rec.size(); i++){
    				String coordinate[] = ((String)selectedGenes_rec.get(i)).split("\t");
    				String[] strCoordinate = coordinate[1].split(",");
    				int intCoordinate[] = {0, 0, 0, 0};
    				for(int j=0; j<strCoordinate.length; j++){
    					intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
    				}
    						fileInformationSelectedGene = fileInformationSelectedGene + coordinate[0] + "\n"; 
    			}
    			//show informatio
    			if(!fileInformationAllGene.equals("")){
    				mapBoxInfo mapBox = new mapBoxInfo(pathwayName_rec+".edge", hashExpressionProfile, fileInformationSelectedGene, fileInformationAllGene);
    				mapBox.pack();
    				mapBox.setLocation(xLocation, yLocation);
    				xLocation += 20;
    				yLocation += 16;
    				mapBox.setTitle("The Result in Pathway");
    				mapBox.setSize(1000,600);
    				mapBox.setVisible(true);
    			}
    		}
     		if(((JMenuItem) e.getSource()).getText().
    				equals("Zoom Picture")){
    			Object[] possibleValues = {"1.5","1.4","1.3","1.2","1.0","0.9","0.8","0.7","0.6","0.5","0.4","0.3"};
    			double selectRatio = Double.parseDouble((String)JOptionPane.showInputDialog(null, "Choose Ratio want to Zoom", "Input",
    					JOptionPane.QUESTION_MESSAGE, null, possibleValues, possibleValues[4]));
    			Mapping mapSample = new Mapping();
    			mapSample.setRatio(selectRatio);
    	        background.selectedArea = new Vector(1);
    			background.redrawMap();
    			mapSample.setMapLabel(background);
     			mapSample.setfileName(fileName);
    			mapSample.showFrame(pathwayName_rec,
    					selectedGenes_rec,
    					allGenes_rec,
    					hashExpressionProfile);
    			frame.dispose();
    		}
    	}
    	};
    	//Create menu
    	JMenuItem m = new JMenuItem("Retrive Information you selected");
    	m.addActionListener(al);
    	popup.add(m);
    	m = new JMenuItem("Retrive All Information in This Pathway");
    	m.addActionListener(al);
    	popup.add(m);
     	m = new JMenuItem("Zoom Picture");
    	m.addActionListener(al);
    	popup.add(m);
    	//Add backgroud
    	if(background==null){//If firt load calculate background, Zoom not calculate
    		if(ratio>1.0){
    			ImageIcon iconBack = new ImageIcon(Mapping.class.getResource("pic20002000.gif"));
    			background = new mapLabel(pathwayName, iconBack, allGenes, selectedGenes, icon, hashExpressionProfile);
    		}else{
    			background = new mapLabel(pathwayName, icon, allGenes, selectedGenes, icon, hashExpressionProfile);
    		}
    	}
    	background.ratio = ratio;
    	background.setVerticalAlignment(SwingConstants.TOP);
    	background.setHorizontalAlignment(SwingConstants.LEFT);
    	background.setBounds(0,0,icon.getIconWidth(), icon.getIconWidth());
    	PopupListener pl = new PopupListener();
    	background.addMouseListener(pl);

    	background.addMouseListener(new MouseAdapter(){
    		public void mouseClicked(MouseEvent me)
    		{
    			if(SwingUtilities.isLeftMouseButton(me))
    			{
    				intSelectedTop =0;
    				intSelectedBottom = 0;
    				intSelectedLeft = 0;
    				intSelectedRight= 0;
    				background.selectedArea = new Vector(1);
    				selectedArea = new Vector(1);
    				background.a1 = 0;
    				background.a2 = 0;
    				background.a3 = 0;
    				background.a4 = 0;
    				background.redrawMap();
    				//System.out.println("You have clicked here" + me.getX());
    				String fileInformationAllGene=new String("");//used to record all genes information
    				String fileInformationSelectedGene=new String("");//used to record selected genes information
    				//collect information
    				for(int i = 0; i< allGenes_rec.size(); i++){
    					String coordinate[] = ((String)allGenes_rec.get(i)).split("\t");
    					String[] strCoordinate = coordinate[1].split(",");
    					int intCoordinate[] = {0, 0, 0, 0};
    					for(int j=0; j<strCoordinate.length; j++){
    						intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
    					}
    					if(me.getX()/ratio> intCoordinate[0] &&
    							me.getX()/ratio< intCoordinate[2] &&
    							me.getY()/ratio> intCoordinate[1] &&
    							me.getY()/ratio< intCoordinate[3]){
    							fileInformationAllGene = fileInformationAllGene + coordinate[0] + "\n"; 
    					}
    				}
    				//collect information
    				for(int i = 0; i< selectedGenes_rec.size(); i++){
    					String coordinate[] = ((String)selectedGenes_rec.get(i)).split("\t");
    					String[] strCoordinate = coordinate[1].split(",");
    					int intCoordinate[] = {0, 0, 0, 0};
    					for(int j=0; j<strCoordinate.length; j++){
    						intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
    					}
    					if(me.getX()/ratio> intCoordinate[0] &&
    							me.getX()/ratio< intCoordinate[2] &&
    							me.getY()/ratio> intCoordinate[1] &&
    							me.getY()/ratio< intCoordinate[3]){
    							fileInformationSelectedGene = fileInformationSelectedGene + coordinate[0] + "\n"; 
    					}
    				}
    				//show informatio
    				if(!fileInformationAllGene.equals("")){
    					mapBoxInfo mapBox = new mapBoxInfo(pathwayName_rec+".edge", hashExpressionProfile, fileInformationSelectedGene, fileInformationAllGene);
    					mapBox.pack();
    					mapBox.setLocation(xLocation, yLocation);
    					xLocation += 20;
    					yLocation += 16;
    					mapBox.setTitle("The Result in Pathway");
    					mapBox.setSize(1000,600);
    					mapBox.setVisible(true);
    				}
    			}
    		}
    		public void mousePressed(MouseEvent me1){
    			if(SwingUtilities.isLeftMouseButton(me1))
    			{
    				if(me1.isControlDown()){
    					background.a1 = me1.getX();
    					background.a2 = me1.getY();
    				}else{
    					background.selectedArea = new Vector(1);
    					background.a1 = me1.getX();
    					background.a2 = me1.getY();
    				}
    			}
    		}

    		public void mouseReleased(MouseEvent me1){
    			if(SwingUtilities.isLeftMouseButton(me1))
    			{
    				intSelectedTop     = background.a1;
    				intSelectedBottom  = background.a2;
    				intSelectedLeft    = background.a1 + background.a3;
    				intSelectedRight   = background.a2 + background.a4;
    				int coor[] = new int[4];
    				coor[0] = background.a1;
    				coor[1] = background.a2;
    				coor[2] = background.a3;
    				coor[3] = background.a4;
    				background.selectedArea.addElement(coor);
    				selectedArea = background.selectedArea;
    			}
     	}
    });


    	background.addMouseMotionListener(new MouseMotionAdapter(){
    		public void mouseDragged(MouseEvent me1){
    			if(SwingUtilities.isLeftMouseButton(me1))
    			{
    				background.a3 = me1.getX() - background.a1;
    				background.a4 = me1.getY() - background.a2;
    				background.redrawMap();
    			}
    		}
    	});
    	//add picture in center
    	JScrollPane panel = new JScrollPane();
    	frame.getContentPane().setLayout(new BorderLayout());
    	frame.getContentPane().add(panel);
    	panel.getViewport().add(background);
    	//add button and list in south

    JPanel south = new JPanel();
    JButton buttonRetrSelected         = new JButton("Retrive Information you selected");
    JButton buttonRetrAll              = new JButton("Retrive All Information in This Pathway");
    JButton buttonZoom                 = new JButton("Zoom Picture");
    south.add(buttonRetrSelected);
    south.add(buttonRetrAll);
    south.add(buttonZoom);
    south.setBackground(Color.white);
    //Add Action Listener
    final String pathwayName_sel = pathwayName;
    buttonRetrSelected.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          //System.out.println("ddd" + intSelectedTop + "ddd" +  intSelectedBottom + "ddd" +  intSelectedLeft + "ddd" + intSelectedRight);
          if(( intSelectedLeft - intSelectedTop) < 10){
            JOptionPane.showMessageDialog(null,"Sorry! You must select again");
          }else{
  			//collect information
	        System.out.println(selectedArea);
  			String fileInformationAllGene=new String("");//used to record all genes information
			String fileInformationSelectedGene=new String("");//used to record selected genes information
            for(int iSelected=0; iSelected<selectedArea.size(); iSelected++){
               int[] coordinates = (int[])selectedArea.get(iSelected);
                for(int i = 0; i< allGenes_rec.size(); i++){
            	   String coordinate[] = ((String)allGenes_rec.get(i)).split("\t");
            	   String[] strCoordinate = coordinate[1].split(",");
				int intCoordinate[] = {0, 0, 0, 0};
				for(int j=0; j<strCoordinate.length; j++){
					intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
				}
                if(coordinates[1]/ratio  < intCoordinate[1] &&
                     coordinates[2]/ratio + coordinates[0]/ratio  > intCoordinate[2] &&
                     coordinates[0]/ratio  < intCoordinate[0] &&
                     coordinates[3]/ratio + coordinates[1]/ratio  > intCoordinate[3]){
					 fileInformationAllGene = fileInformationAllGene + coordinate[0] + "\n"; 
					}	
                }
                //collect information
                for(int i = 0; i< selectedGenes_rec.size(); i++){
                	String coordinate[] = ((String)selectedGenes_rec.get(i)).split("\t");
                	String[] strCoordinate = coordinate[1].split(",");
                	int intCoordinate[] = {0, 0, 0, 0};
                	for(int j=0; j<strCoordinate.length; j++){
                		intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
                	}
                	if(coordinates[1]/ratio  < intCoordinate[1] &&
                        coordinates[2]/ratio + coordinates[0]/ratio  > intCoordinate[2] &&
                        coordinates[0]/ratio  < intCoordinate[0] &&
                        coordinates[3]/ratio + coordinates[1]/ratio  > intCoordinate[3]){
                	fileInformationSelectedGene = fileInformationSelectedGene + coordinate[0] + "\n"; 
                	}
                }
            	}
				//show informatio
				if(!fileInformationAllGene.equals("")){
					mapBoxInfo mapBox = new mapBoxInfo(pathwayName_rec+".edge", hashExpressionProfile, fileInformationSelectedGene, fileInformationAllGene);
					mapBox.pack();
					mapBox.setLocation(xLocation, yLocation);
					xLocation += 20;
					yLocation += 16;
					mapBox.setTitle("The Result in Pathway");
					mapBox.setSize(1000,600);
					mapBox.setVisible(true);
				}
          	}
        return;
        }
    });


    buttonRetrAll.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
			String fileInformationAllGene=new String("");//used to record all genes information
			String fileInformationSelectedGene=new String("");//used to record selected genes information
			//collect information
			for(int i = 0; i< allGenes_rec.size(); i++){
				String coordinate[] = ((String)allGenes_rec.get(i)).split("\t");
				String[] strCoordinate = coordinate[1].split(",");
				int intCoordinate[] = {0, 0, 0, 0};
				for(int j=0; j<strCoordinate.length; j++){
					intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
				}
				fileInformationAllGene = fileInformationAllGene + coordinate[0] + "\n"; 
			}
			//collect information
			for(int i = 0; i< selectedGenes_rec.size(); i++){
				String coordinate[] = ((String)selectedGenes_rec.get(i)).split("\t");
				String[] strCoordinate = coordinate[1].split(",");
				int intCoordinate[] = {0, 0, 0, 0};
				for(int j=0; j<strCoordinate.length; j++){
					intCoordinate[j] = Integer.valueOf(strCoordinate[j]).intValue();
				}
						fileInformationSelectedGene = fileInformationSelectedGene + coordinate[0] + "\n"; 
			}
			//show informatio
			if(!fileInformationAllGene.equals("")){
				mapBoxInfo mapBox = new mapBoxInfo(pathwayName_rec+".edge", hashExpressionProfile, fileInformationSelectedGene, fileInformationAllGene);
				mapBox.pack();
				mapBox.setLocation(xLocation, yLocation);
				xLocation += 20;
				yLocation += 16;
				mapBox.setTitle("The Result in Pathway");
				mapBox.setSize(1000,600);
				mapBox.setVisible(true);
			}
         return;
        }
    });

    buttonZoom.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Object[] possibleValues = {"1.5","1.4","1.3","1.2","1.0","0.9","0.8","0.7","0.6","0.5","0.4","0.3"};
          double selectRatio = Double.parseDouble((String)JOptionPane.showInputDialog(null, "Choose Ratio want to Zoom", "Input",
              JOptionPane.QUESTION_MESSAGE, null, possibleValues, possibleValues[4]));
          Mapping mapSample = new Mapping();
	      background.selectedArea = new Vector(1);
		  background.redrawMap();
          mapSample.setMapLabel(background);
          mapSample.setRatio(selectRatio);
          mapSample.setfileName(fileName);
          mapSample.showFrame(pathwayName_rec,
                              selectedGenes_rec,
                              allGenes_rec,
                              hashExpressionProfile);
          frame.dispose();

          return;
        }
    });




    frame.getContentPane().add(BorderLayout.SOUTH, south);

    //Add some Labels
    JPanel north = new JPanel();
    JLabel introLabel = new JLabel("<html><Center><font color = black>You can Select Part of the Pathway, and CTRL Key can be used multiple Selection</font><br>" +
                                   "<font color = black>All Genes in Pathway&nbsp</font>" +
                                   "<font color = red>All Genes With Expression&nbsp</font></Center></html>");
    north.add(introLabel);

    north.setBackground(Color.white);
    frame.getContentPane().add(BorderLayout.NORTH, north);
    frame.setSize(1000,700);//Here we must extend the function to define the frame size according the picture size
//    frame.setResizable(false);
    frame.setLocation( 10 + xLocationSelf, 10 + yLocationSelf);
    frame.show();
  }

   class PopupListener extends MouseAdapter {
     public void mousePressed(MouseEvent e) {
       if(SwingUtilities.isRightMouseButton(e))
         maybeShowPopup(e);
     }

     public void mouseReleased(MouseEvent e) {
       if(SwingUtilities.isRightMouseButton(e))
         maybeShowPopup(e);
     }

     private void maybeShowPopup(MouseEvent e) {
       if (e.isPopupTrigger()) {
         popup.show(e.getComponent(), e.getX(), e.getY());
         xpopPosition = e.getX();
         ypopPosition = e.getY();
       }
     }
   }


  }

