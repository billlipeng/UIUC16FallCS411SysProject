import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.lang.*;

public class Dijkstra {

    public static ArrayList<String> shortestPath (ArrayList<String> strList, String s, String e) {
        int n = strList.size();
        String[] sourceLoc = new String[n];
        String[] destLoc = new String[n];
        String[] arrLeft = new String[n];
        int[] duration = new int[n];

        for (int i = 0; i < strList.size(); i++) {
//            System.out.println(strList.get(i));
            arrLeft[i] = strList.get(i).split(";")[0];
            duration[i] = Integer.parseInt(strList.get(i).split(";")[1]);
            sourceLoc[i] = arrLeft[i].split(",")[0];
            destLoc[i] = arrLeft[i].split(",")[1];
        }

        Graph.Edge[] GRAPH = new Graph.Edge[strList.size()];
        for (int i = 0; i < strList.size(); i++) {
            GRAPH[i] = new Graph.Edge(sourceLoc[i], destLoc[i], duration[i]);
        }


        final String START = s;
        final String END = e;

        Graph g = new Graph(GRAPH);
        g.dijkstra(START);
        return g.printPath(END);
        //g.printAllPaths();
    }

    public static void main(String[] args) {
        ArrayList<String> l = new ArrayList<String>();
        l.add("node1,node2;2");
        l.add("node1,node3;1");
        l.add("node1,node7;1");
        l.add("node2,node4;1");
        l.add("node3,node4;2");
        l.add("node3,node6;3");
        l.add("node3,node7;2");
        l.add("node4,node5;1");
        l.add("node5,node6;1");
        l.add("node6,node7;1");
        l.add("node7,node9;2");

        String start = "node1";
        String end = "node9";

        ArrayList<String> list = shortestPath(l, start, end);
        System.out.println(list);
    }
}

class Graph {
    private final Map<String, Vertex> graph; // mapping of vertex names to Vertex objects, built from a set of Edges

//    private List<String> res = new ArrayList<>();

    /** One edge of the graph (only used by Graph constructor) */
    public static class Edge {
        public final String v1, v2;
        public final int dist;
        public Edge(String v1, String v2, int dist) {
            this.v1 = v1;
            this.v2 = v2;
            this.dist = dist;
        }
    }

    /** One vertex of the graph, complete with mappings to neighbouring vertices */
    public static class Vertex implements Comparable<Vertex>{
        public final String name;
        public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
        public Vertex previous = null;
        public final Map<Vertex, Integer> neighbours = new HashMap<>();

        public Vertex(String name)
        {
            this.name = name;
        }

        private void printPath(ArrayList<String> record)
        {
            if (this == this.previous)
            {
                //System.out.printf("%s", this.name);
                String nd = this.name+"#@";
                record.add(nd);
            }
            else if (this.previous == null)
            {
                String s = this.name + "(unreached)";
                record.add(s);
            }
            else
            {
                this.previous.printPath(record);

                String nd = this.name+"#"+this.dist;
                record.add(nd);
            }
        }

        public int compareTo(Vertex other)
        {
            if (dist == other.dist)
                return name.compareTo(other.name);

            return Integer.compare(dist, other.dist);
        }

        @Override public String toString()
        {
            return "(" + name + ", " + dist + ")";
        }
    }

    /** Builds a graph from a set of edges */
    public Graph(Edge[] edges) {
        graph = new HashMap<>(edges.length);

        //one pass to find all vertices
        for (Edge e : edges) {
            if (!graph.containsKey(e.v1)) graph.put(e.v1, new Vertex(e.v1));
            if (!graph.containsKey(e.v2)) graph.put(e.v2, new Vertex(e.v2));
        }

        //another pass to set neighbouring vertices
        for (Edge e : edges) {
            graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
            //graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph
        }
    }

    /** Runs dijkstra using a specified source vertex */
    public void dijkstra(String startName) {
        if (!graph.containsKey(startName)) {
            System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
            return;
        }
        final Vertex source = graph.get(startName);
        NavigableSet<Vertex> q = new TreeSet<>();

        // set-up vertices
        for (Vertex v : graph.values()) {
            v.previous = v == source ? source : null;
            v.dist = v == source ? 0 : Integer.MAX_VALUE;
            q.add(v);
        }

        dijkstra(q);
    }

    /** Implementation of dijkstra's algorithm using a binary heap. */
    private void dijkstra(final NavigableSet<Vertex> q) {
        Vertex u, v;
        while (!q.isEmpty()) {

            u = q.pollFirst(); // vertex with shortest distance (first iteration will return source)
            if (u.dist == Integer.MAX_VALUE) break; // we can ignore u (and any other remaining vertices) since they are unreachable

            //look at distances to each neighbour
            for (Map.Entry<Vertex, Integer> a : u.neighbours.entrySet()) {
                v = a.getKey(); //the neighbour in this iteration

                final int alternateDist = u.dist + a.getValue();
                if (alternateDist < v.dist) { // shorter path to neighbour found
                    q.remove(v);
                    v.dist = alternateDist;
                    v.previous = u;
                    q.add(v);
                }
            }
        }
    }

    /** Prints a path from the source to the specified vertex */
    public ArrayList<String> printPath(String endName) {
        if (!graph.containsKey(endName)) {
            System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
        }


//        String nd = graph.get(endName).printPath();
//        if(nd.length()>0){
//            res.add(nd);
//        }
//        System.out.println();
        ArrayList<String> l = new ArrayList<String>();
        graph.get(endName).printPath(l);
        return l;
    }
    /** Prints the path from the source to every vertex (output order is not guaranteed) */
    /*public void printAllPaths() {
        for (Vertex v : graph.values()) {
            v.printPath();
            System.out.println();
        }
    }*/
}