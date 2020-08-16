package ca.pfv.spmf.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Filter4 {

    public static void main(String[] args) throws IOException {

        File input = new File("C:\\Users\\spies\\Documents\\VUB\\BA3\\Bachelorproef\\frequent itemset mining\\AprioriS.txt");
        File output = new File("AprioriS_noSupp1.txt");
        if (output.createNewFile()) {
            System.out.println("File created: " + output.getName());
        } else {
            System.out.println("File already exists.");
        }

        FileWriter writer = new FileWriter("AprioriS_noSupp1.txt");
        Scanner reader = new Scanner(input);
        while(reader.hasNextLine()) {
            String data = reader.nextLine();
            if (data.length() == 0) {writer.append("\n"); continue;}
            String[] tkns = data.split(" #SUP: ");
            String supp = tkns[1];
            int s = Integer.parseInt(supp);
            System.out.println("Support: " + supp + "\n");
            if (s == 1) continue;
            writer.append(data);
            writer.append("\n");
        }
        reader.close();
        writer.close();
        System.out.println("Successfully wrote to the file.");
    }
}
