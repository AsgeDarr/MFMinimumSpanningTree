import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class MFMST {

	static int[][] spanningTree; 
	static int vertices; 
	static int edges;
	
	
	public static void main(String [] args) throws IOException {
		int B = 4;
		ReadTestFile();
		int[] chosenEdges = randomizedEdges();

		System.out.println(Check(chosenEdges, B));
	}

	//Chose the egdes for check. This has to be clever and random
	public static int[] randomizedEdges() {
		int[] edges;

		edges = new int[] {1,0,1};

		return edges;
	}

	//Checks if there exists an MFMST
	public static String Check(int[] chosenEdges, int B) {
		if(!CheckEveryVerticesExists(chosenEdges)) {
			return "NO";
		}
		if(!CheckMaxCriteria(chosenEdges,B)) {
			return "NO";
		}
		
		return "YES";
	}

	public static boolean CheckMaxCriteria(int[] chosenEdges,int B) {
		int firstSum = 0;
		int secondSum = 0;
		
		for(int i = 0; i < spanningTree.length; i++) {
			if(chosenEdges[i] == 1) {
				firstSum += spanningTree[i][2];
				secondSum += spanningTree[chosenEdges.length-(i+1)][2];
			}
		}
		
		
		System.out.println("Max( " + firstSum + " , " + secondSum + " ) <= "+ B);
		
		if( Math.max(firstSum,secondSum) <= B ) {
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




	@SuppressWarnings("resource")
	public static void ReadTestFile() throws IOException {
		//Takes input from user
		Scanner fileReader = new Scanner(System.in);  
		System.out.println("Enter the name of the test file: ");
		String filepath = fileReader.nextLine();
		if(!filepath.contains(".uwg")) {
			filepath = "src/" + filepath + ".uwg";
		}
		filepath = filepath.toLowerCase();
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
		for(int i = 0; i<spanningTree.length;i++) {
			for(int j = 0; j< 3; j++) {
				System.out.print(spanningTree[i][j] + " ");
			}
			System.out.println();
		}

	}

}
