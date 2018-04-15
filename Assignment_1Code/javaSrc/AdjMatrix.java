import java.io.*;
import java.util.*;


/**
 * Adjacency matrix implementation for the FriendshipGraph interface.
 * 
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2016.
 */
public class AdjMatrix <T extends Object> implements FriendshipGraph<T>
{
    private boolean[][] adjArray;
    private ArrayList<T> labels;

	/**
	 * Contructs empty graph.
	 */
    public AdjMatrix() {
        adjArray = new boolean[0][0];
        labels = new ArrayList<T>();
    } // end of AdjMatrix()
    
    
    public void addVertex(T vertLabel) {
        boolean[][] tempArray;
        tempArray = adjArray;
        adjArray = new boolean[adjArray.length+1][adjArray.length+1];

        for(int i = 0; i < tempArray.length; i++)
            for(int j = 0; j < tempArray.length; j++)
                adjArray[i][j] = tempArray[i][j];

        labels.add(adjArray.length-1, vertLabel);
    } // end of addVertex()
	
    
    public void addEdge(T srcLabel, T tarLabel) {
        int source = labels.indexOf(srcLabel);
        int target = labels.indexOf(tarLabel);

        if(source == -1 || target == -1)
            return;

        adjArray[source][target] = true;
        adjArray[target][source] = true;
    } // end of addEdge()
	

    public ArrayList<T> neighbours(T vertLabel) {
        ArrayList<T> neighbours = new ArrayList<T>();
        int i = labels.indexOf(vertLabel);

        if(i == -1)
            return neighbours;

        for(int j = 0; j < adjArray.length; j++)
            if(adjArray[i][j])
                neighbours.add(labels.get(j));

        return neighbours;
    } // end of neighbours()
    
    
    public void removeVertex(T vertLabel) {

        if(!labels.contains(vertLabel))
            return;

        int pos = labels.indexOf(vertLabel);
        for(int i = pos; i < adjArray.length-1; i++)
            for(int j = 0; j < adjArray.length-1; j++)
            {
                adjArray[i][j] = adjArray[i+1][j];
                adjArray[j][i] = adjArray[j][i+1];
            }

    boolean[][] tempArray;
    tempArray = adjArray;
    adjArray = new boolean[adjArray.length-1][adjArray.length-1];

    for(int i = 0; i < adjArray.length; i++)
            for(int j = 0; j < adjArray.length; j++)
                adjArray[i][j] = tempArray[i][j];

    labels.remove(vertLabel);
    } // end of removeVertex()
	
    
    public void removeEdge(T srcLabel, T tarLabel) {
        adjArray[labels.indexOf(srcLabel)][labels.indexOf(tarLabel)] = false;
        adjArray[labels.indexOf(tarLabel)][labels.indexOf(srcLabel)] = false;
    } // end of removeEdges()
	
    
    public void printVertices(PrintWriter os) {
        for(Object item : labels)
            os.print(item + " ");
        os.println();
    } // end of printVertices()
	
    
    public void printEdges(PrintWriter os) {
        for(int i = 0; i < adjArray.length; i++)
            for(int j = 0; j < adjArray.length;  j++)
                if(adjArray[i][j])
                    os.println(labels.get(i) +"" + labels.get(j));
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
    
} // end of class AdjMatrix

class Pair
{
    private int vert;
    private int dist;

    public Pair(int vert, int dist)
    {
        this.dist = dist;
        this.vert = vert;
    }

    public int getVert() {
        return vert;
    }
     
    public int getDist() {
        return dist;
    }
}