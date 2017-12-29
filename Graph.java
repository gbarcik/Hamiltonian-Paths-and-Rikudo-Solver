import java.util.ArrayList;
import java.util.HashSet;

public class Graph {
	int num_vertices;
	ArrayList<Vertex> vertices;
	HashSet<Edge> edges;
	
	public Graph () {
		num_vertices = 0;
		edges = new HashSet<Edge>();
		vertices = new ArrayList<Vertex>();
	}
	
	void addVertex() {
		vertices.add(new Vertex(num_vertices+1));
		num_vertices++;
	}
	
	void addEdge(Vertex source, Vertex target) {
		edges.add(new Edge(source, target));
		vertices.get(source.get_vertex()).get_neighbours().add(target);
	}
	
	void addEdge(int source, int target) {
		edges.add(new Edge(source, target));
		vertices.get(source).get_neighbours().add(vertices.get(target));
	}
}
