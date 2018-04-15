import java.io.*;
import java.util.*;

public class GraphGen
{
    private static final double epsilon = 0.0001;
    private static final int vertMax = 4039;
    private static int vertlim = 1000;
    private static final String face_file = "facebook_combined.txt";

    private static ArrayList<Edge> edges = new ArrayList<Edge>();
    private static ArrayList<String> vertices = new ArrayList<String>();

    public static void main(String[] args)
    {
        if(args.length != 1 && args.length != 2 && args.length != 3){

            System.out.println("GraphGen <desired density> [number of vertice limitation]");
            System.exit(1);
        }

        if(args.length == 3)
            vertlim = Integer.valueOf(args[2]);

        Random gen = new Random();
        String initVert = String.valueOf(gen.nextInt(vertMax));
        if(Integer.valueOf(initVert) + vertlim > vertMax)
            initVert = String.valueOf(Integer.valueOf(initVert)-vertlim);
        double desDens = Double.valueOf(args[0]);

        System.out.println("init vert is: " + initVert);

        vertices.add(initVert);
        initGen(initVert, initVert);
        Double currDens = getDens(vertices.size(), edges.size());

        System.out.println("Number of edges is: " + edges.size());
        System.out.println("Number of vertices is: " + vertices.size());
        System.out.printf("Graph Density is: %.4f\n", getDens(vertices.size(), edges.size()));

        int edgComp = Double.compare(Math.abs(desDens - currDens), epsilon);
        if(edgComp > 0)
        {
            System.out.printf("\n%.4f is not equal to %.4f\nNow adjusting no of edges\n", desDens, currDens);
            int reqEdges = getReqEdges(desDens, vertices.size());
            if(currDens < desDens)
                addEdges(reqEdges);
            else
                rmEdges(reqEdges);

            System.out.println("Number of edges is now: " + edges.size());
            System.out.println("Number of vertices is now: " + vertices.size());
            System.out.printf("Graph Density is now: %.4f\n", getDens(vertices.size(), edges.size()));
        }

        Collections.sort(edges);
        writeGraph(args[1]);
    }

    private static void initGen(String preVert, String initVal)
    {
        ArrayList<String> tempVerts = new ArrayList<String>();
        Random gen = new Random();

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(face_file));
            String line;
            String delim = " ";
            String[] tokens;
            String src, tar;

            while((line = reader.readLine()) != null)
            {
                tokens = line.split(delim);
                src = tokens[0];
                tar = tokens[1];

                if(Objects.equals(preVert, src))
                    tempVerts.add(tar);
            }
        }
        catch (FileNotFoundException exception)
        {
            System.err.println("File not found.");
        }
        catch (IOException exception)
        {
            System.err.println("File could not be read.");
        }

        // System.out.println("Number of neighbours for " + preVert + " is " + tempVerts.size());
        String nexVert;

        if(tempVerts.size() == 0)
        {
            nexVert = String.valueOf(Integer.parseInt(preVert)+1);
            if(Integer.parseInt(nexVert) > vertMax)
            {
                nexVert = String.valueOf(Integer.parseInt(initVal)+1);
                initVal = String.valueOf(Integer.parseInt(initVal)+1);
            }
        }
        else
        {
            nexVert = tempVerts.get(gen.nextInt(tempVerts.size()));
            edges.add(new Edge(preVert, nexVert));
        }

        vertices.add(nexVert);
        if(vertices.size() < vertlim)
            initGen(nexVert, initVal);
    }

    private static void addEdges(int edgeNo)
    {
        Random gen = new Random();

        while(edges.size() != edgeNo)
        {
            String src = vertices.get(gen.nextInt(vertices.size()));
            String tar = vertices.get(gen.nextInt(vertices.size()));

            if(!Objects.equals(src, tar) && !edges.contains(new Edge(src, tar)));
                edges.add(new Edge(src, tar));
        }
    }

    private static void rmEdges(int edgeNo)
    {
        Random gen = new Random();
        while(edges.size() != edgeNo)
            edges.remove(gen.nextInt(edges.size()));
    }

    private static double getDens(int v, int e){
        return (double)(2*e)/(double)(v*(v-1));
    }

    private static int getReqEdges(double density, int verts){
        return (int)Math.ceil((density*verts*(verts-1))/2);
    }

    private static void writeGraph(String filename)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for(Edge edge : edges)
            {
                writer.write(edge.getVertexa() + " " + edge.getVertexb() + "\n");
            }
            writer.close();
        }
        catch(IOException exception)
        {
            System.err.println("Could not write to " + filename);
        }
    }
}

class Edge implements Comparable<Edge>
{
    private String vertexa;
    private String vertexb;

    public Edge(String vertexa, String vertexb)
    {
        this.vertexa = vertexa;
        this.vertexb = vertexb;
    }

    public String getVertexa() {
        return vertexa;
    }

    public String getVertexb() {
        return vertexb;
    }

    @Override
    public boolean equals(Object obj) 
    {
        boolean val = false;
        if(obj != null && obj instanceof Edge)
        {
            if(((Edge)obj).getVertexa() == this.vertexa && ((Edge)obj).getVertexb() == this.vertexb)
                val = true;
            else if(((Edge)obj).getVertexb() == this.vertexa && ((Edge)obj).getVertexa() == this.vertexb)
                val = true;
        }
        return val;
    }

    @Override
    public int compareTo(Edge o) 
    {
        int src = Integer.valueOf(this.getVertexa());
        int oSrc = Integer.valueOf(o.getVertexa());

        return Integer.compare(src, oSrc);
    }
}