import org.sat4j.*;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.util.*;

public class SATSolver {
	final int MAXVAR = 100000;
	final int NBCLAUSES = 50000;
	
	ISolver solver;
	
	public SATSolver () {
		solver = SolverFactory.newDefault();
		//prepare the solver to accept MAXVAR variables
		//solver.newVar(MAXVAR);
		//solver.setExpectedNumberOfClauses(NBCLAUSES);
		//Feed the solver using Dimacs format, using arrays of int
		//(best option to avoid dependencies on SAT4J IVecInt)
	}
	
	private int[] traduireResponse(int []solution, int n_vertices) {
		/* this function transforms the solution, with numbers from 1 to n² 
		 * into the path of solution. For this, it only takes the positive numbers
		 * from solution and find which vertex this positive numbers represents 
		 */
		int []resp = new int[n_vertices];
		
		for(int i = 0; i < n_vertices*n_vertices; i++) {
			if(solution[i]>0) {
				if(solution[i]%n_vertices == 0) { 
					resp[i%n_vertices] = solution[i] / n_vertices;
				} else {
					resp[i%n_vertices] = 1 + (solution[i] / n_vertices);
				}
					
				// i%vertices find the position in the path
				// 1 + solution[i] / n_vertices finds which vertex it represents, starting at 1.
			}		
		}
		return resp;
	}	
	
	public LinkedList<Vertex> SAT_solver(Graph g, Vertex source, Vertex target) throws ContradictionException{
		LinkedList<Vertex> list = new LinkedList<>();
		
		int n_vertices = g.num_vertices;
		int [][]x = new int[n_vertices][n_vertices];
		
		// We create a matrix 
		// 1  2  3  4 ...
		// .  .  .  . ...
		// .  .  .  . ...
		// .  .  .  . n²
		for(int i = 0, count = 1; i < n_vertices; i++) {
			for(int j = 0; j < n_vertices; j++, count++) {
				x[i][j] = count;
			}	
		}
			
			
		// Start at source and Ends at target
		StartAtSourceEndAtTarget(n_vertices, source, target, x);
		
		//each vertex appears exactly once in the path
		eachVertexAppearsExactlyOnce(n_vertices, x);

		//each index appears exactly once in the path
		eachIndexAppearsExactlyOnce(n_vertices, x);
		
		// Nonadjacent nodes cannot be adjacent in the path
		nonadjacentVerticesCannotBeAdjacentInThePath(n_vertices, g, x);
		
		System.out.println("Number of variables: " + solver.nVars());
		System.out.println("Number of constraints: " + solver.nConstraints());

		// Solve the problem
		int[] solution;
		try {
			if (solver.isSatisfiable()) {
				System.out.println("Satisfiable problem!");
				solution = solver.model();
				int[] resp = traduireResponse(solution, n_vertices);
				System.out.println("Solution: ");
				/*for(int i = 0; i < resp.length; i++)
					System.out.print(resp[i] + " -> ");*/
				
					
				for(int i = 0 ; i < solution.length; i++)
					if(solution[i] > 0)
						System.out.print(solution[i] + " -> ");
			} else {
				System.out.println("Unsatisfiable problem!");
			}
		} catch (TimeoutException e) {
			System.out.println("Timeout, sorry!");
		}
		
		return list;
	}
	
	void StartAtSourceEndAtTarget(int n_vertices, Vertex source, Vertex target, int[][] x) throws ContradictionException {
		int[] clause = new int[1];
		clause[0] = x[source.get_vertex()-1][0];
		solver.addClause(new VecInt(clause));
		clause[0] = x[target.get_vertex()-1][n_vertices-1];
		solver.addClause(new VecInt(clause));
	}
	
	void eachVertexAppearsExactlyOnce(int n_vertices, int [][]x) throws ContradictionException {
		for(int i=0; i < n_vertices; i++){
			int[] clause = new int[n_vertices];
			// j are the indices
			// clause: xi0 V xi1 V ... V xiN
			for(int j=0; j < n_vertices; j++){
				clause[j] = x[i][j];
			}
			solver.addExactly(new VecInt(clause), 1); // adapt Array to IVecInt
			//  Create a cardinality constraint of the type "exactly n of those literals must be satisfied".
		}
	}
	
	void eachIndexAppearsExactlyOnce(int n_vertices, int [][]x) throws ContradictionException {
		for(int j=0; j < n_vertices; j++){
			int[] clause = new int[n_vertices];
			//i are the vertices
			for(int i=0; i < n_vertices; i++){
				clause[i] = x[i][j];
			}
			solver.addExactly(new VecInt(clause), 1);
		}
	}
	
	void nonadjacentVerticesCannotBeAdjacentInThePath(int n_vertices, Graph g, int [][]x) throws ContradictionException {
		for(int k=0; k < n_vertices-1; k++){
			//ArrayList<Integer> clause = new ArrayList<Integer>();
			// if (i,j) isnt in G => clause: ~Xi,k V ~Xj,k+1
			for(int i=0; i < n_vertices; i++){
				for(int j=0; j < n_vertices; j++){
					
					if(!g.edges.contains(new Edge(i,j))){
						int []clause = new int [2];
						clause[0] = -1*x[i][k];
						clause[1] = -1*x[j][k+1];
						solver.addClause(new VecInt(clause));
						/*clause[0] = x[i][k];
						clause[1] = x[j][k+1];
						solver.addAtMost(new VecInt(clause), 1);*/
					}
				}
				
			}
			
			/*for(Edge e : g.edges) {
				clause.add(x[e.source.get_vertex()-1][k]);
				clause.add(x[e.target.get_vertex()-1][k+1]);
			}
			int []aux = new int [clause.size()];
			for(int i = 0; i < aux.length; i++)
				aux[i] = clause.get(i);
			solver.addExactly(new VecInt(aux), 1);*/
		}
	}

}
