package edu.mcw.mcgee;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

public class parseBiocarta {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		parser("BioCarta\\DrawPathway.txt", "BioCarta\\DrawPathway.xml");
	}
	
	public static void parser(String inputFileName, String outputFileName){
		HashMap protein = new HashMap();//used to record protein with locuslink
		HashMap complexEn = new HashMap();//used to record protein enzyme group
		HashMap complexProtein = new HashMap();//used to record protein enzyme group
		try{ 
			DataInputStream in =
		    	new DataInputStream(
		    			new BufferedInputStream(
		       					new FileInputStream(inputFileName)
		    			)
					);	
			DataOutputStream out =
				new DataOutputStream(
						new BufferedOutputStream(
								new FileOutputStream(outputFileName)
						)
					);
			String line = new String("");
			String proteinId = new String("");
			String type = new String(""); //used to say protein with locuslink or not
			String locusID = new String("");
			String complexId = new String("");
			//String geneName = new String("");
			int time = 1;
			Vector proteinList = new Vector(); //used to record list if not with locuslink, ie in complex
			Vector proteinListInteraction = new Vector();//used to save all gene in Interaction
			Vector complexIds = new Vector();//used to record Ids in complex
			while((line=in.readLine()) != null ){
				if(line.indexOf("<Molecule molecule_type=\"protein\"")!=-1){//protein or enzyme
					String proteinLine[] = line.split("\"");
					//System.out.print("\n" + proteinLine[3] + "\t");
					proteinId = proteinLine[3];
					//Empty Vector proteinList
					proteinList = new Vector();
				}
				if(line.indexOf("<Interaction interaction_type=")!=-1){//complex
					String complexLine[] = line.split("\"");
					//System.out.print("\n" + proteinLine[3] + "\t");
					complexId = complexLine[3];
					//Empty Vector proteinList
					complexIds = new Vector();
				}
				if(line.indexOf("<Name name_type=\"LL\"")!=-1){
					type = new String("protein");
					String locusLine[] = line.split("\"");
					//System.out.print(locusLine[3] + "\t");
					locusID = locusLine[3];
				}
				if(line.indexOf("<Family family_molecule_id=\"")!=-1){
					type = new String("complexEn");
					String listLine[] = line.split("\"");
					//System.out.print(listLine[3] + "\t");
					proteinList.addElement(listLine[3]);
				};
				if(line.indexOf("</Molecule>")!=-1){
					if(type.equals("protein")){//a protein
						protein.put(proteinId, locusID);
					}else if(type.equals("complexEn")){//a complex
							complexEn.put(proteinId.toString(), proteinList);
					}
				};		
				if(line.indexOf("<Interaction interaction_type=")!=-1){
					complexIds = new Vector();
				}
				if(line.indexOf("<InteractionComponent role_type")!=-1){
					String interactionLine[] = line.split("\"");
					//System.out.print(listLine[3] + "\t");
					complexIds.addElement(interactionLine[3]);										
					//complexIds.addElement(protein.get(interactionLine[3]));					
				}
				if(line.indexOf("</Interaction>")!=-1){
					//System.out.println(complexIds);
					complexProtein.put(complexId, complexIds);
					;//WRITE interaction
				}				
			}		
			System.out.println(protein);
			System.out.println(complexEn);
			System.out.println(complexProtein);
		}catch(Exception e){
		System.out.println("Can not open the file" + e.getMessage());
	}
	}
}
