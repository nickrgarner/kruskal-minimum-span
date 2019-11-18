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

		// Create heap object initialized with array of 5000
		Kruskal main = new Kruskal();
		Heap mainHeap = main.new Heap(maxEdges);

		// Import edges from input file, print heap
		mainHeap.importEdges();
		mainHeap.printAllEdges();

		// Create adjacency list and insert edges from heap
		AdjList adjList = main.new AdjList(mainHeap.getNumNodes());
		for (int i = 0; i < mainHeap.size(); i++) {
			adjList.insertEdge(mainHeap.getHeapArray()[i]);
		}

		// Create MST and UpTreeList
		UpTreeList upTrees = main.new UpTreeList(mainHeap.getNumNodes());
		MSTList mst = main.new MSTList();

		// Remove edges from heap in min-weight order, add to MST if edge joins UpTrees
		// together
		while (!upTrees.verifyMST()) {
			Edge current = mainHeap.deleteMin();
			if (upTrees.union(current.getVertex1(), current.getVertex2())) {
				mst.insert(current);
			}
		}

		// Print MST
		mst.printMST();

		// Print adjacency list
		adjList.printAdjList();
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
				if (h[2 * m + 2].getWeight() < h[2 * m + 1].getWeight()) {
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
				System.out.printf("%4d %4d\n", vertex1, vertex2);
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

		/**
		 * Returns the number of nodes in the graph based on the highest vertex value in
		 * the heap
		 * 
		 * @return Number of nodes in the graph
		 */
		public int getNumNodes() {
			int max = -1;
			for (int i = 0; i < size; i++) {
				if (heapArray[i].getVertex1() > max) {
					max = heapArray[i].getVertex1();
				}
				if (heapArray[i].getVertex2() > max) {
					max = heapArray[i].getVertex2();
				}
			}
			// Number of nodes is n + 1
			return max + 1;
		}
	}

	/**
	 * Class defines state and behavior for a 2D array holding record of adjacent
	 * vertices for each vertex in the graph. Edges are read from the heap and their
	 * endpoints used to fill the AdjList.
	 * 
	 * @author Nick Garner, nrgarner
	 *
	 */
	private class AdjList {

		/** 2D Array holding adjacent nodes for each vertex */
		private int[][] adjArray;

		/**
		 * Creates a new AdjList for the given number of nodes
		 * 
		 * @param numNodes Number of nodes in the graph
		 */
		public AdjList(int numNodes) {
			adjArray = new int[numNodes][numNodes];
		}

		/**
		 * Takes an edge and adds its endpoints to each other's adjacency lists as bool
		 * values of 1
		 * 
		 * @param edge
		 */
		public void insertEdge(Edge edge) {
			int v1 = edge.getVertex1();
			int v2 = edge.getVertex2();
			// Insert v2 into v1's list
			adjArray[v1][v2] = 1;
			adjArray[v2][v1] = 1;
		}

		/**
		 * Loops through adjacency list and prints out adjacent nodes for each vertex
		 */
		public void printAdjList() {
			int numNodes = adjArray.length;
			for (int i = 0; i < numNodes; i++) {
				int currentAdj = -1;
				for (int k = 0; k < numNodes; k++) {
					if (adjArray[i][k] == 1) {
						if (currentAdj < 0) {
							currentAdj = k;
						} else {
							System.out.printf("%4d ", currentAdj);
							currentAdj = k;
						}
					}
				}
				System.out.printf("%4d\n", currentAdj);
			}
		}
	}

	/**
	 * Class defines state and behavior for an array-based representation of
	 * UpTrees. Nodes start out as singleton sets and are unioned together as edges
	 * are deleted from the heap.
	 * 
	 * @author Nick Garner
	 *
	 */
	private class UpTreeList {

		/** Array to hold UpTrees */
		private int[] forest;

		/**
		 * Creates a new UpTreeList for the given number of nodes
		 * 
		 * @param numNodes
		 */
		public UpTreeList(int numNodes) {
			forest = new int[numNodes];
			initializeUpTrees(forest);
		}

		/**
		 * Checks that v1 and v2 are in different UpTrees and then joins them via a
		 * balanced Union.
		 * 
		 * @param v1 First endpoint of the current edge
		 * @param v2 Second endpoint of the current edge
		 * @return True if UpTrees are unioned together and Edge should be added to MST,
		 *         False otherwise
		 */
		public boolean union(int v1, int v2) {
			if (findRoot(v1) != findRoot(v2)) {
				// Nodes are in different UpTrees
				int root1 = findRoot(v1);
				int root2 = findRoot(v2);
				if (forest[root1] < forest[root2]) {
					forest[root1] += forest[root2];
					forest[root2] = root1;
					return true;
				} else {
					forest[root2] += forest[root1];
					forest[root1] = root2;
					return true;
				}
			}
			return false;
		}

		/**
		 * Returns the index of the root of the UpTree this node is in.
		 * 
		 * @param node Node you want to find the UpTree root of.
		 * @return Index of the UpTree root of this node.
		 */
		public int findRoot(int node) {
			if (forest[node] < 0) {
				return node;
			} else {
				return findRoot(forest[node]);
			}
		}

		/**
		 * Initializes upTreeArray with singleton sets, i.e. value of -1 for node count
		 * of 1
		 * 
		 * @param upTreeArray Array to initialize with singleton sets
		 */
		public void initializeUpTrees(int[] upTreeArray) {
			for (int i = 0; i < upTreeArray.length; i++) {
				upTreeArray[i] = -1;
			}
		}

		/**
		 * Returns the forest array for this UpTreeList
		 * 
		 * @return Forest array of UpTrees
		 */
		public int[] getForest() {
			return forest;
		}

		/**
		 * Loops through the UpTree array and returns true if there is only one root and
		 * all nodes point to it.
		 * 
		 * @return True if UpTree array has exactly one root and all nodes are in the
		 *         same UpTree, False otherwise.
		 */
		public boolean verifyMST() {
			boolean rootFound = false;
			for (int i = 0; i < forest.length; i++) {
				if (forest[i] < 0) {
					if (!rootFound) {
						rootFound = true;
					} else {
						return false;
					}
				}
			}
			return true;
		}
	}

	/**
	 * Class defines state and behavior for a LinkedList of edges representing the
	 * Minimum Spanning Tree for the graph being parsed. Functionality is included
	 * for inserting new edges at the end of the list.
	 * 
	 * @author Nick Garner
	 *
	 */
	private class MSTList {

		/** Pointer to the node at the front of the list */
		private Edge head;

		/** Number of elements in the MST */
		private int size;

		/**
		 * Creates a new empty MST with a null head and a size of 0.
		 */
		public MSTList() {
			head = null;
			size = 0;
		}

		/**
		 * Inserts the given edge into the MST linked list in increasing order of
		 * endpoint values.
		 * 
		 * @param edge Edge to insert into the list.
		 */
		public void insert(Edge edge) {
			if (size() == 0) {
				head = edge;
				size++;
			} else {
				int v1 = Integer.min(edge.getVertex1(), edge.getVertex2());
				int v2 = Integer.max(edge.getVertex1(), edge.getVertex2());
				Edge current = head;
				Edge previous = null;
				while (current != null && Integer.min(current.getVertex1(), current.getVertex2()) < v1) {
					previous = current;
					current = current.next;
				}

				while (current != null && Integer.min(current.getVertex1(), current.getVertex2()) == v1
						&& Integer.max(current.getVertex1(), current.getVertex2()) < v2) {
					previous = current;
					current = current.next;
				}
				if (current == head) {
					edge.next = head;
					head = edge;
				} else {
					previous.next = edge;
					edge.next = current;
				}
				size++;
			}
		}

		/**
		 * Returns a pointer to the head of the MSTList
		 * 
		 * @return Pointer to the MSTList head
		 */
		public Edge getHead() {
			return head;
		}

		/**
		 * Number of elements in the MST
		 * 
		 * @return Number of elements in the MST
		 */
		public int size() {
			return size;
		}

		public void printMST() {
			Edge current = head;
			while (current != null) {
				System.out.printf("%4d %4d\n", Integer.min(current.getVertex1(), current.getVertex2()),
						Integer.max(current.getVertex1(), current.getVertex2()));
				current = current.next;
			}
		}
	}
}
