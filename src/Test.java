import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

public class Test {
	
	private static Graph generateCicleGraph() {
		Graph g = new Graph();
		
		for(int i = 0; i < 6; i++)
			g.addVertex();
		
		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(1, 0);
		g.addEdge(1, 3);
		g.addEdge(2, 0);
		g.addEdge(2, 4);
		g.addEdge(3, 1);
		g.addEdge(3, 5);
		g.addEdge(4, 2);
		g.addEdge(4, 5);
		g.addEdge(5, 3);
		g.addEdge(5, 4);
		
		
		return g;
	}
	
	private static Graph generateGraph() {
		Graph g = new Graph();
		for(int i = 0; i <= 14; i++)
			g.addVertex();
		
		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(0, 3);
		g.addEdge(1, 0);
		g.addEdge(1, 3);
		g.addEdge(1, 4);
		g.addEdge(2, 0);
		g.addEdge(2, 3);
		g.addEdge(2, 5);
		g.addEdge(3, 0);
		g.addEdge(3, 1);
		g.addEdge(3, 2);
		g.addEdge(3, 4);
		g.addEdge(3, 5);
		g.addEdge(3, 6);
		g.addEdge(4, 1);
		g.addEdge(4, 3);
		g.addEdge(4, 6);
		g.addEdge(5, 2);
		g.addEdge(5, 3);
		g.addEdge(5, 6);
		g.addEdge(6, 3);
		g.addEdge(6, 4);
		g.addEdge(6, 5);
		g.addEdge(6, 7);
		g.addEdge(7, 6);
		g.addEdge(7, 8);
		g.addEdge(7, 10);
		g.addEdge(8, 7);
		g.addEdge(8, 9);
		g.addEdge(8, 10);
		g.addEdge(8, 11);
		g.addEdge(9, 8);
		g.addEdge(9, 11);
		g.addEdge(9, 12);
		g.addEdge(10, 7);
		g.addEdge(10, 8);
		g.addEdge(10, 11);
		g.addEdge(10, 13);
		g.addEdge(11, 8);
		g.addEdge(11, 9);
		g.addEdge(11, 10);
		g.addEdge(11, 12);
		g.addEdge(11, 13);
		g.addEdge(11, 14);
		g.addEdge(12, 9);
		g.addEdge(12, 11);
		g.addEdge(12, 14);
		g.addEdge(13, 10);
		g.addEdge(13, 11);
		g.addEdge(13, 14);
		g.addEdge(14, 11);
		g.addEdge(14, 12);
		g.addEdge(14, 13);
		
		
		return g;
	}
	
	private static void test1 () throws ContradictionException {
		int source = 4;
		//int target = 2;
		Graph g = generateGraph();
		remark1_ii_ solver = new remark1_ii_();
		solver.findHamiltonianCycle(g, g.vertices.get(source));
	}
	
	private static Collection<Edge> createDiamondsInGraph(Graph g) {
		LinkedList<Edge> diamonds = new LinkedList<Edge>();
		diamonds.add(new Edge(2,3));
		diamonds.add(new Edge(13,14));
		diamonds.add(new Edge(10,13));
		
		return diamonds;
	}

	private static HashMap<Integer, Vertex> createInicialVertices(Graph g) {
		HashMap<Integer, Vertex> lambida = new HashMap<>();
		// lambida will map position into vertex
		// lambida.put(position, g.vertices.get(number_of_vertex));
		// position starts at 0
		
		lambida.put(10, g.vertices.get(3));
		//lambida.put(6, g.vertices.get(6));*/
		return lambida;
	}
	
	private static void test3() throws ContradictionException, TimeoutException {
		int source = 14;
		int target = 4;
		Graph g = generateGraph();
		HashMap<Integer, Vertex> lambida = createInicialVertices(g);
		Collection<Edge> diamonds = createDiamondsInGraph(g);
		task3 solver = new task3();
		solver.findHamiltonianPath(g, g.vertices.get(source), g.vertices.get(target), lambida, diamonds);
		
	}

	public static void main(String[] args) throws ContradictionException, TimeoutException {
		//test1();
		test3();
		
	}



}
