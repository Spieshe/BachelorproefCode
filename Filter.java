package ca.pfv.spmf.test;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Filter {
// Filters out every file that has only entries with one transaction on the left (aka singleton itemsets)
    public static void main(String[] args) throws IOException {
        File path = new File("C:\\Users\\spies\\Documents\\VUB\\BA3\\Bachelorproef\\frequent itemset mining\\src\\ca\\pfv\\spmf\\test\\OPCharmS");

        File [] files = path.listFiles();
        int deleted = 0;
        boolean consider = true;
        for(File file : files) {
            Scanner reader = new Scanner(file);
            System.out.println("Reading file: " + file.getName());
            while(reader.hasNextLine()) {
                String data = reader.nextLine();
                System.out.println(data);
                String[] tkns = data.split("#SUP:");
                String[] deft = tkns[0].split("\\s+");
                if(deft.length == 1) {
                    consider = false;
                }
                else if(deft.length > 1) {
                    consider = true;
                    break;
                }
            }
            reader.close();
            System.out.println(consider);
            if(!consider) {
                System.out.println("Deleting file");
                if (file.delete()) {
                    System.out.println("Deleted the file: " + file.getName());
                    deleted++;
                } else {
                    System.out.println("Failed to delete the file.");
                }
            }
        }
        System.out.println("Deleted " + deleted + " files.");
        //File path = new File("C:\\Users\\spies\\Documents\\VUB\\BA3\\Bachelorproef\\ca\\op\\output_fim83.txt");
        //System.out.println(path.length());
    }

}