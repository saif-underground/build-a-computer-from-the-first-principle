/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrappers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Read the assembly file and discards all the white spaces, empty lines and
 * comments.
 *
 * @author Saif
 */
public class FileWrapper {

    private String fileName;
    private int currentLocation; //begins from 0
    List<String> content;

    //set the current file name
    public FileWrapper(String fileName) {
        currentLocation = 0;
        this.fileName = fileName;
        content = new LinkedList();
        readFile();
    }

    public boolean hasNextLine() {
        return currentLocation < content.size() ? true : false;
    }

    public String getNextLine() {
        return content.get(currentLocation++);
    }

    //discard empty lines, white spaces, comments from the file
    public void readFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                //discard empty lines or comment
                if(line.isEmpty() || line.startsWith("//")){
                    continue;
                }
                //discard white spaces
                line = line.replaceAll("\\s+", "");
                line = line.replaceAll("//.*", "");
                content.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetFilePointer() {
        currentLocation = 0;
    }

    public int getCurrentLinePosition() {
        return currentLocation;
    }

    public int getNumberOfLines() {
        return content.size();
    }

    //print the content of the file
    public void printFile() {
        if (content == null) {
            return;
        }
        for (String line : content) {
            System.out.println(line);
        }
    }
    
    public static void main(String[] args){
        String fileName = "Add.asm";
        FileWrapper fw = new FileWrapper(fileName);
        fw.readFile();
        fw.printFile();
        
        System.out.println("reading line by line");
        fw.resetFilePointer();
        
        while(fw.hasNextLine()){
            System.out.println(fw.getNextLine());
        }
    }
}
