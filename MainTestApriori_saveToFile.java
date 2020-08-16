package ca.pfv.spmf.test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;

/**
 * Example of how to use APRIORI algorithm from the source code.
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class MainTestApriori_saveToFile {

	public static void main(String [] arg) throws IOException {

		File path = new File("C:\\Users\\spies\\Documents\\VUB\\BA3\\Bachelorproef\\frequent itemset mining\\src\\ca\\pfv\\spmf\\test\\fimsets");

		File[] files = path.listFiles();
		assert files != null;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) { //this line weeds out other directories/folders
				System.out.println(files[i]);
			}
		}
		long time = 0;
		double memory = 0;
		String output;
		for (File file : files) {
			// File path for the input database
			String input = fileToPath("./fimsets/" + file.getName());
			// File path for saving the frequent itemsets found
			output = ".//output" + "_" + file.getName();

			double minsup = 0.2; // means a minsup of 2 transaction (we used a relative support)

			// Applying the Apriori algorithm
			AlgoApriori algo = new AlgoApriori();

			// Uncomment the following line to set the maximum pattern length (number of items per itemset)
//		algo.setMaximumPatternLength(3);

			algo.runAlgorithm(minsup, input, output);
			algo.printStats();
			time += algo.getTime();
			memory += algo.getMemory();
		}
		System.out.println("Total time: " + time + "ms");
		System.out.println("Average maximal memory: " + memory/files.length);
	}

	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestApriori_saveToFile.class.getResource(filename);
		return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}
