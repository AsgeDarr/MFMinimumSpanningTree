import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

public class MFMST {

	static int[][] spanningTree; 
	static int vertices; 
	static int edges;
	static int[][][] listOfSpanningTrees;
	static int B = 1000000000;

	public static void main(String [] args) throws IOException {
		ReadTestFile();	
		int[] chosenEdges;
		int[][] res = CalcMST(spanningTree);

		for(int add = 0; add< 3;add++) {
			long start = System.currentTimeMillis();
			int times = 0;
			B = 1000000000;
			do {
				chosenEdges = randomizedEdges(res.length+add);
				Check(chosenEdges);
				times++;
				//System.out.println(Check(chosenEdges, B));
			}while(times < 300);
			long end   = System.currentTimeMillis();
			//System.out.println("Chosen edges:");
			//for(int i = 0; i<chosenEdges.length;i++) 
			//	System.out.print(chosenEdges[i] + " ");
			long total = end - start;
			System.out.println("\nBest B found with "+(res.length+add) +" edges chosen: " + B + "  -  milliseconds: " + total);
		}
	}

	//Chose the egdes for check. This has to be clever and random
	public static int[] randomizedEdges(int MSTVertices) {
		int[] chosenEdges = new int[edges];
		Random random = new Random();

		do {
			for(int i = 0; i <  chosenEdges.length; i++) {
				if(random.nextBoolean()) {
					chosenEdges[i] = 1;
				}else {
					chosenEdges[i] = 0;
				}

				//System.out.print(chosenEdges[i] + "  ");
			}
			//System.out.println();

		}while(IntStream.of(chosenEdges).sum() !=MSTVertices);
		return chosenEdges;
	}




	public static int[][] CalcMST(int[][] P) {
		int[][] MST = P;
		int[][] result = new int[spanningTree.length][3]; 
		int e = 0;
		int i = 0;  
		bubbleSort(MST);
		subset subsets[] = new subset[MST.length];
		for(i=0; i<MST.length; ++i)
			subsets[i]= new subset();
		for (int j = 0; j < MST.length; j++)
		{
			subsets[j].parent = j;
			subsets[j].rank = 0;
		}
		i = -1; 
		while (i < MST.length-1)
		{
			int [] nextEdge = new int[3];
			i++;
			nextEdge[0] = MST[i][0];
			nextEdge[1] = MST[i][1];
			nextEdge[2] = MST[i][2];
			int x = find(subsets, nextEdge[0]-1);
			int y = find(subsets, nextEdge[1]-1);
			if (x != y)
			{
				result[e++] = nextEdge;
				Union(subsets, x, y);
			}
		}
		System.out.println("MST:");
		int[][] cropRes = new int[e][3];
		for (i = 0; i < e; i++) {
			System.out.println(result[i][0]+" -- " + result[i][1]+" == " + result[i][2]);
			cropRes[i][0] = result[i][0];
			cropRes[i][1] = result[i][1];
			cropRes[i][2] = result[i][2];
		}	
		return cropRes;
	}

	//Checks if there exists an MFMST
	public static String Check(int[] chosenEdges) {
		/*if(!CheckEveryVerticesExists(chosenEdges)) {
			return "NO - Not every vertices exists";
		}*/
		if(!CheckMaxCriteria(chosenEdges)) {
			return "NO";
		}

		return "YES";
	}

	public static boolean CheckMaxCriteria(int[] chosenEdges) {
		int firstSum = 0;
		int secondSum = 0;
		for(int i = 0; i < spanningTree.length; i++) {
			if(chosenEdges[i] == 1) {
				firstSum += spanningTree[i][2];
				secondSum += spanningTree[chosenEdges.length-(i+1)][2];
			}
		}
		
		if( Math.max(firstSum,secondSum) <= B ) {
			B = Math.max(firstSum,secondSum);
			return true;
		}else {
			return false;
		}

	}


	//chosenEdges contains a list of chosen vertices where the index represent the edge in the matrix. 
	//1 if chosen, 0 if not chosen
	public static boolean CheckEveryVerticesExists(int[] chosenEdges) {
		int[] verticesCheck = new int[vertices];
		int[] verification = new int[vertices];
		for(int i = 0; i < spanningTree.length; i++) {
			if(i<vertices) {
				verification[i] = i+1;
			}
			if(chosenEdges[i] == 1) {
				verticesCheck[spanningTree[i][0]-1] = spanningTree[i][0];
				verticesCheck[spanningTree[i][1]-1] = spanningTree[i][1];
			}
		}
		if(Arrays.equals(verticesCheck, verification)) {
			return true;
		}else {

			return false;
		}
	}

	static int find(subset subsets[], int i)
	{
		if (subsets[i].parent != i)
			subsets[i].parent = find(subsets, subsets[i].parent);
		return subsets[i].parent;
	}


	@SuppressWarnings("resource")
	public static void ReadTestFile() throws IOException {
		//Takes input from user
		Scanner fileReader = new Scanner(System.in);  
		System.out.println("Enter the name of the test file: ");
		String filepath = fileReader.nextLine();
		if(!filepath.contains(".uwg")) {
			filepath = "src/" + filepath + ".uwg";
		}
		//filepath = filepath.toLowerCase();
		System.out.println("The filename path is : " + filepath);

		//Loads .uwg-file into matrix
		Scanner input = new Scanner (new File(filepath));
		//Does file exists?
		File file = new File(filepath);
		if (file.exists() && file.isFile())
		{
			System.out.println("file exists, and it is a file");
		}
		//Read rows for size of spanningTree
		BufferedReader testFileReader = null;
		BufferedReader rowCounter = new BufferedReader(new FileReader(filepath));
		int rows = 0;
		while (rowCounter.readLine() != null) rows++;
		rowCounter.close();
		System.out.println("Rows of matrix: " + (rows-2) + "\n");
		//Create matrix for the tree
		spanningTree = new int[rows-2][3]; 
		//Load data into matrix
		try {
			testFileReader = new BufferedReader(new FileReader(filepath));
			for(int i = 0; i < rows && testFileReader.ready(); i++) {
				String[] splittedRow = testFileReader.readLine().split(" "); 
				if(i==0) {
					vertices = Integer.parseUnsignedInt(splittedRow[0]);
				}
				if(i==1) {
					edges = Integer.parseUnsignedInt(splittedRow[0]);
				}

				if(i>1) {
					for(int j = 0; j < 3; j++) {
						spanningTree[i-2][j] = Integer.parseUnsignedInt(splittedRow[j]);
					}
				}
			}

		} catch(IOException e) {
			throw e;
		} finally {
			assert testFileReader != null;
			testFileReader.close();
		}

	}


	static void Union(subset subsets[], int x, int y)
	{
		int xroot = find(subsets, x);
		int yroot = find(subsets, y);

		if (subsets[xroot].rank < subsets[yroot].rank)
		{
			subsets[xroot].parent = yroot;
		}else if (subsets[xroot].rank > subsets[yroot].rank)
		{
			subsets[yroot].parent = xroot;
		}else
		{
			subsets[yroot].parent = xroot;
			subsets[xroot].rank++;
		}
	}

	public static void bubbleSort(int[][] array) {
		boolean swapped = true;
		int j = 0;
		int[] temp = new int[3];
		while (swapped) {
			swapped = false;
			j++;
			for (int i = 0; i < array.length - j; i++) {
				if (array[i][2] > array[i + 1][2]) {
					temp[0] = array[i][0];
					temp[1] = array[i][1];
					temp[2] = array[i][2];
					array[i][0] = array[i + 1][0];
					array[i][1] = array[i + 1][1];
					array[i][2] = array[i + 1][2];
					array[i + 1][0] = temp[0];
					array[i + 1][1] = temp[1];
					array[i + 1][2] = temp[2];
					swapped = true;
				}
			}
		}
	}

}


class subset
{
	int parent, rank;
};
