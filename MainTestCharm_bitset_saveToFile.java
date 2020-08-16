package ca.pfv.spmf.test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import ca.pfv.spmf.algorithms.frequentpatterns.charm.AlgoCharm_Bitset;
import ca.pfv.spmf.algorithms.frequentpatterns.charm.AlgoDCharm_Bitset;
import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;


/**
 * Example of how to use CHARM-Bitset algorithm from the source code
 * and save the result to a file.
 * @author Philippe Fournier-Viger 2014
 */
public class MainTestCharm_bitset_saveToFile {

	public static void main(String [] arg) throws IOException{

		File path = new File("C:\\Users\\spies\\Documents\\VUB\\BA3\\Bachelorproef\\frequent itemset mining\\src\\ca\\pfv\\spmf\\test\\fimsets");

		File [] files = path.listFiles();
		assert files != null;
		for (int i = 0; i < files.length; i++){
			if (files[i].isFile()){ //this line weeds out other directories/folders
				System.out.println(files[i]);
			}
		}
		long time = 0;
		double memory = 0;
		String output;
		for(File file : files){
			System.out.println("Starting with: " + file.getName());
			// File path for the input database
			String input = fileToPath("./fimsets/" + file.getName());
			// File path for saving the frequent itemsets found
			output = ".//output" + "_" + file.getName();

			// the minsup threshold
			// Note : 0.4 means a minsup of 2 transaction (we used a relative support)
			double minsup = 0.2;

			// Read the input file
			TransactionDatabase database = new TransactionDatabase();
			try {
				database.loadFile(input);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Applying the CHARM algorithm
			AlgoCharm_Bitset algo = new AlgoCharm_Bitset();
			//AlgoDCharm_Bitset algo = new AlgoDCharm_Bitset();

//		// Set this variable to true to show the transaction identifiers where patterns appear in the output file
//		algo.setShowTransactionIdentifiers(true);


			algo.runAlgorithm(output, database, minsup, true, 10000);
			// NOTE 1: if you  use "true" in the line above, CHARM will use
			// a triangular matrix  for counting support of itemsets of size 2.
			// For some datasets it should make the algorithm faster.

			// Print statistics about the algorithm execution.
			algo.printStats();
			time += algo.getTime();
			memory += algo.getMemory();
			try {
				if (arg[0].equals("1")) {
					System.out.println("Press enter to continue...");
					System.in.read();
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("No stepwise approach detected");
			}
		}
		System.out.println("Total time: " + time);
		System.out.println("Average maximal memory: " + memory/files.length);

	}

	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestCharm_bitset_saveToFile.class.getResource(filename);
		return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}
