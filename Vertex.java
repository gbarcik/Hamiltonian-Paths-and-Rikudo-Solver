import java.util.*;

public class Vertex{
	private int vertex;
	private LinkedList<Vertex> neighbours;

	public Vertex(int vertex, LinkedList<Vertex> neighbours){
		this.vertex = vertex;
		this.neighbours = neighbours;
	}
	
	public Vertex(int vertex) {
		this.vertex = vertex;
		this.neighbours = new LinkedList<Vertex>();
	}

	public int get_vertex(){
		return this.vertex;
	}

	public LinkedList<Vertex> get_neighbours(){
		return this.neighbours;
	}

	public String toString(){
		return ""+vertex;
	}

	public boolean isNeighbour (Vertex v){
		for(Vertex vert : this.neighbours){
			if(vert.get_vertex() == v.get_vertex())
				return true;
		}

		return false;
	}
}