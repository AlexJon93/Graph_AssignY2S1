import java.io.*;
import java.util.*;


/**
 * Incidence matrix implementation for the FriendshipGraph interface.
 * 
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2016.
 */
public class IndMatrix <T extends Object> implements FriendshipGraph<T>
{
    boolean[][] incArray;
    private ArrayList<T> labels;
	/**
	 * Contructs empty graph.
	 */
    public IndMatrix() {
        incArray = new boolean[0][0];
        labels = new ArrayList<T>();
    } // end of IndMatrix()
    
    
    public void addVertex(T vertLabel) {
        boolean[][] tempArray;
        tempArray = incArray;

        if(tempArray.length > 0)
        {
            incArray = new boolean[incArray.length+1][incArray[0].length];
                for(int i = 0; i < tempArray.length; i++)
                    for(int j = 0; j < tempArray[0].length; j++)
                        incArray[i][j] = tempArray[i][j];
        }
        else
            incArray = new boolean[incArray.length+1][0];

        labels.add(incArray.length-1, vertLabel);

        // System.out.println("added label: " + vertLabel + " index: " + (incArray.length-1));
        } // end of addVertex()
	
    
    public void addEdge(T srcLabel, T tarLabel) {
        int source = labels.indexOf(srcLabel);
        int target = labels.indexOf(tarLabel);

        if(source == -1 || target == -1)
            return;

        // System.out.println("Adding edge between vertex at index " + source + " and vertex at index " + target);

        boolean[][] tempArray;
        tempArray = incArray;
        incArray = new boolean[incArray.length][incArray[0].length+1];

        for(int i = 0; i < tempArray.length; i++)
            for(int j = 0; j < tempArray[0].length; j++)
                incArray[i][j] = tempArray[i][j];
        
        // System.out.println("");

        incArray[source][incArray[0].length-1] = true;
        incArray[target][incArray[0].length-1] = true;
    } // end of addEdge()
	

    public ArrayList<T> neighbours(T vertLabel) {
        ArrayList<T> neighbours = new ArrayList<T>();
        int vert = labels.indexOf(vertLabel);
        if(vert == -1)
            return neighbours;

        for(int j = 0; j < incArray[0].length; j++)
            if(incArray[vert][j])
                for(int i = 0; i < incArray.length; i++)
                    if(incArray[i][j] && i != vert)
                            neighbours.add(labels.get(i));
        
        return neighbours;
    } // end of neighbours()
    
    
    public void removeVertex(T vertLabel) {
        int vert = labels.indexOf(vertLabel);
        if(vert == -1)
            return;

        for(int i = vert; i < incArray.length-1; i++)
            for(int j = 0; j < incArray[0].length; j++)
                incArray[i][j] = incArray[i+1][j];

        boolean[][] tempArray;
        tempArray = incArray;
        incArray = new boolean[incArray.length-1][incArray[0].length];

        for(int i = 0; i < incArray.length; i++)
            for(int j = 0; j < incArray[0].length; j++)
                incArray[i][j] = tempArray[i][j];
        labels.remove(vert);
    } // end of removeVertex()
	
    
    public void removeEdge(T srcLabel, T tarLabel) {
        int source = labels.indexOf(srcLabel);
        int target = labels.indexOf(tarLabel);

        if(source == -1 || target == -1)
            return;

        int edge;
        for(edge = 0; edge < incArray[0].length; edge++)
            if(incArray[source][edge] && incArray[target][edge])
                break;
        
        for(int i = 0; i < incArray.length; i++)
            for(int j = edge; j < incArray[0].length-1; j++)
                incArray[i][j] = incArray[i][j+1];

        if(incArray[0].length-1 == 0)
        {
            incArray = new boolean[incArray.length][0];
            return;
        }

        boolean[][] tempArray;
        tempArray = incArray;
        incArray = new boolean[incArray.length][incArray[0].length-1];

        for(int i = 0; i < incArray.length; i++)
            for(int j = 0; j < incArray[0].length; j++)
                incArray[i][j] = tempArray[i][j];
    } // end of removeEdges()
	
    
    public void printVertices(PrintWriter os) {
        for(Object item : labels)
            os.print(item + " ");
        os.println();
    } // end of printVertices()
	
    
    public void printEdges(PrintWriter os) {
        if(incArray.length == 0)
            return;
        
        for(int j = 0; j < incArray[0].length; j++)
        {
            for(int i = 0; i < incArray.length; i++)
                if(incArray[i][j])
                    os.print(labels.get(i));
            os.println();
        }
    } // end of printEdges()
    
    
    public int shortestPathDistance(T vertLabel1, T vertLabel2) {
        int origin = labels.indexOf(vertLabel1);
        int goal = labels.indexOf(vertLabel2);

        boolean marked[] = new boolean[labels.size()];
        
        for(int i = 0; i < marked.length; i++)
            marked[i] = false;

        Queue<Pair> queue = new ArrayDeque<Pair>();

        marked[origin] = true;
        queue.add(new Pair(origin, 0));

        while(!queue.isEmpty())
        {
            Pair nPair = queue.remove();
            int nVert = nPair.getVert();
            int nDist = nPair.getDist();

            if(nVert == goal)
                return nDist;
            for(T i : this.neighbours(labels.get(nVert)))
            {
                int cVert = labels.indexOf(i);
                if(!marked[cVert])
                {
                    marked[cVert] = true;
                    queue.add(new Pair(cVert, nDist+1));
                }
            }
        }
    	
        // if we reach this point, source and target are disconnected
        return disconnectedDist;    	
    } // end of shortestPathDistance()
    
} // end of class IndMatrix