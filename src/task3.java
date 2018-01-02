import org.sat4j.*;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.SingleSolutionDetector;

import java.util.*;

public class task3 {
	final int MAXVAR = 100000;
	final int NBCLAUSES = 50000;

	ISolver solver;

	public task3() {
		solver = SolverFactory.newDefault();
		// prepare the solver to accept MAXVAR variables
		// solver.newVar(MAXVAR);
		// solver.setExpectedNumberOfClauses(NBCLAUSES);
		// Feed the solver using Dimacs format, using arrays of int
		// (best option to avoid dependencies on SAT4J IVecInt)
	}

	private int[] traduireResponse(int[] solution, int n_vertices) {
		/*
		 * this function transforms the solution, with numbers from 1 to n² into the
		 * path of solution. For this, it only takes the positive numbers from solution
		 * and find which vertex this positive numbers represents
		 */
		int[] resp = new int[n_vertices];

		for (int i = 0; i < n_vertices * n_vertices; i++) {
			if (solution[i] > 0) {
				if (solution[i] % n_vertices == 0) {
					resp[i % n_vertices] = solution[i] / n_vertices - 1;
				} else {
					resp[i % n_vertices] = (solution[i] / n_vertices);
				}

				// i%vertices find the position in the path
				// 1 + solution[i] / n_vertices finds which vertex it represents, starting at 1.
			}
		}
		return resp;
	}

	public List<Vertex> findHamiltonianPath(Graph g, Vertex source, Vertex target, HashMap<Integer, Vertex> lambida,
			Collection<Edge> diamonds) throws ContradictionException, TimeoutException {
		
		LinkedList<Vertex> list = new LinkedList<>(); // list will be the answer with the correct path

		int n_vertices = g.num_vertices;
		int[][] x = new int[n_vertices][n_vertices];

		// We create a matrix
		// 1 2 3 4 ...
		// . . . . ...
		// . . . . ...
		// . . . . n²
		for (int i = 0, count = 1; i < n_vertices; i++) {
			for (int j = 0; j < n_vertices; j++, count++) {
				x[i][j] = count;
			}
		}

		// Start at source and Ends at target
		startAtSourceEndAtTarget(n_vertices, source, target, x);

		// each vertex appears exactly once in the path
		eachVertexAppearsExactlyOnce(n_vertices, x);

		// each index appears exactly once in the path
		eachIndexAppearsExactlyOnce(n_vertices, x);

		// Nonadjacent nodes cannot be adjacent in the path
		nonadjacentVerticesCannotBeAdjacentInThePath(n_vertices, g, x);

		// Vertices defined in the beginning
		defineVerticesInTheBeginning(n_vertices, x, lambida);

		// Add diamond edges
		addDiamondEdges(n_vertices, x, diamonds);

		System.out.println("Number of variables: " + solver.nVars());
		System.out.println("Number of constraints: " + solver.nConstraints());

		// Solve the problem

		int[] resp;
		try {
			if (solver.isSatisfiable()) {
				System.out.println("Satisfiable problem!");
				int[] solution;
				solution = solver.model();
				resp = traduireResponse(solution, n_vertices);
				System.out.println("Solution: ");
				for (int i = 0; i < resp.length - 1; i++) {
					System.out.print(resp[i] + " -> ");
					list.add(g.vertices.get(i));
				}
				System.out.println(resp[resp.length-1]);
				list.add(g.vertices.get(resp.length-1));

			} else {
				System.out.println("Unsatisfiable problem!");
			}
		} catch (TimeoutException e) {
			System.out.println("Timeout, sorry!");
		}
		
		//check for single solution
		hasSingleSolution();
		
		//check for minimal property
		minimalProperty(n_vertices);
		
		return list;
	}

	private void startAtSourceEndAtTarget(int n_vertices, Vertex source, Vertex target, int[][] x)
			throws ContradictionException {
		int[] clause = new int[1];
		clause[0] = x[source.get_vertex()][0];
		solver.addClause(new VecInt(clause));
		clause[0] = x[target.get_vertex()][n_vertices - 1];
		solver.addClause(new VecInt(clause));
	}

	private void eachVertexAppearsExactlyOnce(int n_vertices, int[][] x) throws ContradictionException {
		for (int i = 0; i < n_vertices; i++) {
			int[] clause = new int[n_vertices];
			// j are the indices
			// clause: xi0 V xi1 V ... V xiN
			for (int j = 0; j < n_vertices; j++) {
				clause[j] = x[i][j];
			}
			solver.addExactly(new VecInt(clause), 1); // adapt Array to IVecInt
			// Create a cardinality constraint of the type "exactly n of those literals must
			// be satisfied".
		}
	}

	private void eachIndexAppearsExactlyOnce(int n_vertices, int[][] x) throws ContradictionException {
		for (int j = 0; j < n_vertices; j++) {
			int[] clause = new int[n_vertices];
			// i are the vertices
			for (int i = 0; i < n_vertices; i++) {
				clause[i] = x[i][j];
			}
			solver.addExactly(new VecInt(clause), 1);
		}
	}

	private void nonadjacentVerticesCannotBeAdjacentInThePath(int n_vertices, Graph g, int[][] x)
			throws ContradictionException {
		for (Vertex v : g.vertices) {
			LinkedList<Vertex> neighbours = v.get_neighbours();
			/*
			 * for all neighbours v1, ... , vk of v: ~Xv,i V Xv1,i+1 V ... V Xvk,i+1
			 */
			for (int i = 0; i < n_vertices - 1; i++) {
				int[] clause = new int[neighbours.size() + 1];
				clause[0] = -1 * x[v.get_vertex()][i];
				for (int ver = 0; ver < neighbours.size(); ver++) {
					clause[ver + 1] = x[neighbours.get(ver).get_vertex()][i + 1];
				}
				solver.addClause(new VecInt(clause));
			}
		}

	}

	private void defineVerticesInTheBeginning(int n_vertices, int[][] x, HashMap<Integer, Vertex> lambida)
			throws ContradictionException {
		// lambida defines the known vertices.
		for (int i = 0; i < n_vertices; i++) {
			if (lambida.containsKey(i)) {
				int[] clause = new int[1];
				clause[0] = x[lambida.get(i).get_vertex()][i];
				solver.addClause(new VecInt(clause));
			}
		}
	}

	private void addDiamondEdges(int n_vertices, int[][] x, Collection<Edge> diamonds) throws ContradictionException {
		for (Edge e : diamonds) {
			// ~Xsource,i V Xtarget,i-1 V Xtarget,i+1
			int[] clause = new int[2];
			clause[0] = -1 * x[e.source.get_vertex()][0];
			clause[1] = x[e.target.get_vertex()][1];
			solver.addClause(new VecInt(clause));
			
			for (int j = 1; j < n_vertices - 1; j++) {
				int[] clausee = new int[3];
				clausee[0] = -1 * x[e.source.get_vertex()][j];
				clausee[1] = x[e.target.get_vertex()][j+1];
				clausee[2] = x[e.target.get_vertex()][j-1];
				solver.addClause(new VecInt(clausee));
			}
			
			if(n_vertices >= 2) {
				clause[0] = -1 * x[e.source.get_vertex()][n_vertices-1];
				clause[1] = x[e.target.get_vertex()][n_vertices-2];
				solver.addClause(new VecInt(clause));
			}
		}
	}
	
	private boolean hasSingleSolution() throws TimeoutException {
		SingleSolutionDetector singsol = new SingleSolutionDetector(solver);
		if(singsol.hasASingleSolution()){
			System.out.println("Single solution!");
			return true;
		} else {
			System.out.println("More than one solution or any solution");
			return false;
		}
	}
	
	private void minimalProperty(int n_vertices) {
		int [] prime = solver.primeImplicant();
		
		int []resp = traduireResponse(prime, n_vertices);
		
		System.out.println("Teste: " + prime.length);
		
	}

}
