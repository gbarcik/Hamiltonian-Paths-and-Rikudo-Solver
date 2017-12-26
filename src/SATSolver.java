import graph.*;
import org.sat4j.*;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;

import java.util.*;

public class SATSolver {
	
	public int vertex_id(int vertex, int index, int n_vertices){
		return index*n_vertices + vertex;
	}
	
	public LinkedList<Vertex> SAT_solver(Graph g, Vertex source, Vertex target) throws ContradictionException{
		LinkedList<Vertex> list = new LinkedList<>();
		boolean [][] positions = new boolean[g.vertices.size()][g.vertices.size()];
		
		// Start at source and Ends at target
		positions[source.getVertex()][0] = true;
		positions[source.getVertex()][g.vertices.size()-1] = true;
		
		
		final int MAXVAR = 100000;
		final int NBCLAUSES = 50000;
		ISolver solver = SolverFactory.newDefault();
		
		//prepare the solver to accept MAXVAR variables
		solver.newVar(MAXVAR);
		solver.setExpectedNumberOfClauses(NBCLAUSES);
		//Feed the solver using Dimacs format, using arrays of int
		//(best option to avoid dependencies on SAT4J IVecInt)
		
		int n_vertices = g.vertices.size();
		
		//each vertex appears at least once in the path
		// i are the vertices
		for(int i=0; i < n_vertices; i++){
			int[] clause = new int[n_vertices];
			// j are the indices
			// clause: xi0 V xi1 V ... V xiN
			for(int j=0; j < n_vertices; j++){
				clause[j] = vertex_id(i, j, n_vertices);
			}
			solver.addClause(new VecInt(clause)); // adapt Array to IVecInt
		}
		
		//each vertex appears exactly once in the path
		for(int i=0; i < n_vertices; i++){
			int[] clause = new int[2];
			// vertex i: for each pair in xi0 ... xiN, construct a clause saying that both cannot be true at the same time
			// clause: ~xik V ~xil
			for(int k=0; k < n_vertices-1; k++){
				for(int l=k+1; l < n_vertices; l++){
					clause[0] = -1*vertex_id(i, l, n_vertices);
					clause[1] = -1*vertex_id(i, k, n_vertices);
				}
				solver.addClause(new VecInt(clause));
			}	
		}
		
		//each index appears at least once in the path
		//j are the indices
		for(int j=0; j < n_vertices; j++){
			int[] clause = new int[n_vertices];
			//i are the vertices
			for(int i=0; i < n_vertices; i++){
				clause[j] = vertex_id(i, j, n_vertices);
			}
		}
		
		//each index appears exactly once in the path
		
		
		
	
		return list;
	}
	
	
	
	
	public static void main(String[] args) {
		
	}

}
