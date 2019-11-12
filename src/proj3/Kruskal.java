package proj3;

/**
 * Main class
 * 
 * @author Nick Garner, nrgarner
 *
 */
public class Kruskal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private class Vertex {

	}

	/**
	 * Class defines state and behavior for Edge objects, to be contained in a heap
	 * and used to find MST.
	 * 
	 * @author Nick Garner, nrgarner
	 *
	 */
	private class Edge {

		/** Int values of the two endpoints of this edge */
		private int vertex1;
		private int vertex2;

		/** Weight of this edge */
		private double weight;

		/** Pointer to the next edge in the graph */
		private Edge next;

		/**
		 * Constructs a new Edge object with the given endpoints, weight, and next Edge
		 * pointer.
		 * 
		 * @param vertex1 Endpoint of this edge
		 * @param vertex2 Endpoint of this edge
		 * @param weight  Weight of this edge for heap partial-order
		 * @param next    Pointer to next edge in the input
		 */
		public Edge(int vertex1, int vertex2, double weight, Edge next) {
			this.vertex1 = vertex1;
			this.vertex2 = vertex2;
			this.weight = weight;
			this.next = next;
		}
	}

	private class Heap {

		private Edge[] heapArray;

		private Edge root;

		public Heap(int size) {
			heapArray = new Edge[size];
			root = null;
		}

		public Edge getRoot() {
			return root;
		}
		
		public void insert(Edge edge) {
			
		}
		
		public Edge deleteMin() {
			return null;
		}
	}
}



















