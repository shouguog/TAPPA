package edu.mcw.mcgee;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class Pathway {
	
	/**
	 * @param args
	 */
	Vector nodes=new Vector();
	Vector edges=new Vector();
	
	//This is used to read data from file
	
	void parsePathway(String fileName){
		BufferedReader br=null;
		String line;
		try{
  	    		br = new BufferedReader(new FileReader(fileName));
		    }catch(Exception eFile){System.out.println("Pathway file does not exist");}
	    try{
  	    	while((line = br.readLine())!=null){
  	    		if(line.indexOf("---")==-1){
 	    			Node node = new Node();
  	    			node.setName(line);
  	    			nodes.add(node);
  	    			//System.out.println(line+"--------------------------");
  	    		}else{
  	    			//System.out.println(line+"--------------------------");
  	    			String[] nodes_edge=line.split("---");
  	    			Node fromNode = new Node();
  	    			fromNode.setName(nodes_edge[0]);
  	    			Node toNode = new Node();
  	    			toNode.setName(nodes_edge[1]);
  	    			Edge edge = new Edge();
  	    			edge.setFromNode(fromNode);
  	    			edge.setToNode(toNode);
  	    			edges.add(edge);
  	    		}  	    		
  	    	}
	    }catch(Exception e){System.out.println("File Format errors");}
	}
	
	Vector getNodes(){
		return nodes;
	}
	
	Vector getEdges(){
		return edges;
	}
	int getDegree(String GeneId){
		int intDegree = 0;
		Vector edgesAll = getEdges();
		for(int i=0; i<edgesAll.size(); i++){
			if(((Edge)(edgesAll.get(i))).getFromNode().getName().equals(GeneId)
					||((Edge)(edgesAll.get(i))).getToNode().getName().equals(GeneId))
				intDegree++;
		}
		return intDegree;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
