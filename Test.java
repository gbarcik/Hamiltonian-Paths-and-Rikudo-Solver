import org.sat4j.specs.ContradictionException;

public class Test {
	
	private static Graph generateCicleGraph() {
		Graph g = new Graph();
		
		for(int i = 0; i < 6; i++)
			g.addVertex();
		
		g.addEdge(1, 2);
		g.addEdge(1, 3);
		g.addEdge(2, 1);
		g.addEdge(2, 4);
		g.addEdge(3, 1);
		g.addEdge(3, 5);
		g.addEdge(4, 2);
		g.addEdge(4, 6);
		g.addEdge(5, 3);
		g.addEdge(5, 6);
		g.addEdge(6, 4);
		g.addEdge(6, 5);
		
		
		return g;
	}
	
	private static Graph generateGraph() {
		Graph g = new Graph();
		for(int i = 0; i < 7; i++)
			g.addVertex();
		
		g.addEdge(1, 2);
		g.addEdge(1, 3);
		g.addEdge(1, 4);
		g.addEdge(2, 1);
		g.addEdge(2, 4);
		g.addEdge(2, 5);
		g.addEdge(3, 1);
		g.addEdge(3, 4);
		g.addEdge(3, 6);
		g.addEdge(4, 1);
		g.addEdge(4, 2);
		g.addEdge(4, 3);
		g.addEdge(4, 5);
		g.addEdge(4, 6);
		g.addEdge(4, 7);
		g.addEdge(5, 2);
		g.addEdge(5, 4);
		g.addEdge(5, 7);
		g.addEdge(6, 3);
		g.addEdge(6, 4);
		g.addEdge(6, 7);
		g.addEdge(7, 4);
		g.addEdge(7, 5);
		g.addEdge(7, 6);
		
		return g;
	}
	
	private static void test () throws ContradictionException {
		int source = 4;
		int target = 2;
		Graph g = generateGraph();
		SATSolver solver = new SATSolver();
		solver.SAT_solver(g, g.vertices.get(source), g.vertices.get(target));
		// we use target-1 and source-1 just for the position in the array
	}
	
	public static void main(String[] args) throws ContradictionException {
		test();
		
	}

}
