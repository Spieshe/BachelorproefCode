package ca.pfv.spmf.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Filter2 {
// Gathers all data in a single file with respect to the different users by adding a newline
    public static void main(String[] args) throws IOException {
        File path = new File("C:\\Users\\spies\\Documents\\VUB\\BA3\\Bachelorproef\\frequent itemset mining\\src\\ca\\pfv\\spmf\\test\\OPAprioriS");

        File[] files = path.listFiles();
        File output = new File("AprioriS.txt");
        if (output.createNewFile()) {
            System.out.println("File created: " + output.getName());
        } else {
            System.out.println("File already exists.");
        }

        FileWriter writer = new FileWriter("AprioriS.txt");
        for(File file : files) {
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()) {
                String data = reader.nextLine();
                writer.write(data);
                writer.write("\n");
            }
            writer.write("\n");
            reader.close();
        }
        writer.close();
        System.out.println("Successfully wrote to the file.");
    }
}
