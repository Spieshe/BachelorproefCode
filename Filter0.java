package ca.pfv.spmf.test;

import java.io.File;

public class Filter0 {

    public static void main(String[] args) {
        File path = new File("C:\\Users\\spies\\Documents\\VUB\\BA3\\Bachelorproef\\frequent itemset mining\\src\\ca\\pfv\\spmf\\test\\OPAprioriS");

        File [] files = path.listFiles();
        int deleted = 0;
        for(File file : files) {
            if(file.length() < 1)
                if (file.delete()) {
                    System.out.println("Deleted the file: " + file.getName());
                    deleted++;
                } else {
                    System.out.println("Failed to delete the file.");
                }
        }
        System.out.println("Deleted " + deleted + " files");
    }
}
