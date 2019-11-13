package proj3;

import java.util.Scanner;

/**
 * Main class
 * 
 * @author Nick Garner, nrgarner
 *
 */
public class Kruskal {

	public static void main(String[] args) {
		// Maximum number of edges for this project
		int maxEdges = 5000;

		// Create heap object initialzed with array of 5000
		Kruskal main = new Kruskal();
		Heap mainHeap = main.new Heap(maxEdges);

		// Import edges from input file
		mainHeap.importEdges();

		// Create adjacency list and insert edges from heap
		AdjList adjList = main.new AdjList(mainHeap.size());
		for (int i = 0; i < mainHeap.size(); i++) {
			adjList.insertEdge(mainHeap.getHeapArray()[i]);
		}
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

		/**
		 * Returns the weight of this edge as a double value
		 * 
		 * @return Weight of this edge as a double
		 */
		public double getWeight() {
			return this.weight;
		}

		/**
		 * Returns the first endpoint of this edge
		 * 
		 * @return First endpoint of this edge
		 */
		public int getVertex1() {
			return vertex1;
		}

		/**
		 * Returns the second endpoint of this edge
		 * 
		 * @return Second endpoint of this edge
		 */
		public int getVertex2() {
			return vertex2;
		}
	}

	/**
	 * Class defines state and behavior for Heap data structure to read in and sort
	 * Edges from input file. Edges are partially-ordered by weight.
	 * 
	 * @author Nick Garner, nrgarner
	 *
	 */
	private class Heap {

		/** Array to hold edges in heap */
		private Edge[] heapArray;

		/** Number of edges currently in the heap */
		private int size;

		/**
		 * Creates a new heap with the given capacity
		 * 
		 * @param capacity Maximum number of elements this heap can hold
		 */
		public Heap(int capacity) {
			heapArray = new Edge[capacity];
			this.size = 0;
		}

		/**
		 * Reads input from command-line file redirection to create Edges and stores
		 * Edges in this heap partially ordered by weight.
		 */
		public void importEdges() {
			Scanner input = new Scanner(System.in);
			int vertex1 = input.nextInt();
			int vertex2;
			double weight;
			while (vertex1 != -1) {
				// Scan tokens, create new Edge, insert into heap
				vertex2 = input.nextInt();
				weight = input.nextDouble();
				Edge newEdge = new Edge(vertex1, vertex2, weight, null);
				this.insert(newEdge);

				// Grab next int and check for -1 i.e. EOF
				vertex1 = input.nextInt();
			}
			// Close Scanner
			input.close();
		}

		/**
		 * Inserts the given edge at the end of the heap and reorders the heap if
		 * necessary.
		 * 
		 * @param v Edge to insert into the heap.
		 */
		public void insert(Edge v) {
			heapArray[size] = v;
			size++;
			upHeap(heapArray, size - 1);
		}

		/**
		 * Sorts the heap after a new edge is inserted.
		 * 
		 * @param h Heap to sort
		 * @param i Position of latest element
		 */
		public void upHeap(Edge[] h, int i) {
			if (i > 0) {
				if (h[(i - 1) / 2].getWeight() > h[i].getWeight()) {
					Edge temp = h[(i - 1) / 2];
					h[(i - 1) / 2] = h[i];
					h[i] = temp;
					upHeap(h, (i - 1) / 2);
				}
			}
		}

		/**
		 * Deletes and returns the edge with the minimum weight
		 * 
		 * @return Edge in the heap with minimum weight
		 */
		public Edge deleteMin() {
			Edge output = heapArray[0];
			size--;
			heapArray[0] = heapArray[size];
			downHeap(heapArray, 0);
			return output;
		}

		/**
		 * Recursive method that restores heap partial-order after deleteMin operation
		 * 
		 * @param h Array of the heap
		 * @param m Position of current element
		 */
		public void downHeap(Edge[] h, int m) {
			int i = 0;
			if ((2 * m + 2) < size) {
				if (h[2 * m + 2].getWeight() <= h[2 * m + 1].getWeight()) {
					i = 2 * m + 2;
				} else {
					i = 2 * m + 1;
				}
			} else if ((2 * m + 1) < size) {
				i = 2 * m + 1;
			}
			if (i > 0 && h[m].getWeight() > h[i].getWeight()) {
				// Swap m with its child, call recursively
				Edge temp = h[m];
				h[m] = h[i];
				h[i] = temp;
				downHeap(h, i);
			}
		}

		/**
		 * Prints all edges in the heap in level-order
		 */
		public void printAllEdges() {
			int numEdges = size;
			for (int i = 0; i < numEdges; i++) {
				Edge current = heapArray[i];
				int vertex1 = Integer.min(current.vertex1, current.vertex2);
				int vertex2 = Integer.max(current.vertex1, current.vertex2);
				System.out.printf("%-4d %-4d %-4.1f\n", vertex1, vertex2, current.getWeight());
			}
		}

		/**
		 * Returns number of elements currently in the heap
		 * 
		 * @return Number of elements in the heap
		 */
		public int size() {
			return size;
		}

		/**
		 * Returns the array for this heap
		 * 
		 * @return Array for this heap
		 */
		public Edge[] getHeapArray() {
			return heapArray;
		}
	}

	private class AdjList {

		int[][] adjArray;

		public AdjList(int numEdges) {
			adjArray = new int[numEdges][numEdges];
		}

		public void insertEdge(Edge edge) {
			int v1 = edge.getVertex1();
			int v2 = edge.getVertex2();
		}
	}

	private class upTree {

		private class Node {

		}
	}
}
