package ca.pfv.spmf.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import ca.pfv.spmf.algorithms.frequentpatterns.fpgrowth.AlgoFPMax;

/**
 * Example of how to use FPMax from the source code and save
 * the resutls to a file.
 * @author Philippe Fournier-Viger (Copyright 2008)
 */
public class MainTestFPMax_saveToFile {

	public static void main(String [] arg) throws FileNotFoundException, IOException {
		// the file paths
		File path = new File("C:\\Users\\spies\\Documents\\VUB\\BA3\\Bachelorproef\\frequent itemset mining\\src\\ca\\pfv\\spmf\\test\\fimsets");

		File[] files = path.listFiles();
		assert files != null;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) { //this line weeds out other directories/folders
				System.out.println(files[i]);
			}
		}
		String output;
		double minsup = 0.2; // means a minsup of 2 transaction (we used a relative support)
		long total_time = 0;
		double memory = 0;
		for (File file : files) {
			String input = fileToPath("./fimsets/" + file.getName());
			output = ".//output" + "_" + file.getName();
			// Applying the FPMax algorithm
			AlgoFPMax algo = new AlgoFPMax();
			algo.runAlgorithm(input, output, minsup);
			algo.printStats();
			total_time += algo.getTime();
			memory += algo.getMemory();
		}
		System.out.println("Total time is: " + total_time);
		System.out.println("Average memory: " + memory/files.length);
	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestFPMax_saveToFile.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
}
