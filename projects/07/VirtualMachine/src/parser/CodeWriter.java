/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;
import enums.COMMAND_TYPE;
import enums.KEY_WORDS;
import enums.RAM_ADDRESS;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Saif
 */
public class CodeWriter {
    String fileName;
    String output = "";
    FileWriter fileWriter = null;
    public CodeWriter(String fileName) throws IOException{
        setFileName(fileName);
        
    }
    
    public void setFileName(String fileName) throws IOException{
        this.fileName = fileName;
        writeBootStrapCode();
    }
    
    private void writeBootStrapCode() throws IOException{
        fileWriter = new FileWriter(fileName);
        String code = null;
        code = "@" + RAM_ADDRESS.STACK.getBegin() + "\n";
        code = code + 
                "D=A\n" +
                "@SP\n"  +
                "M=D\n";
        fileWriter.write(code);
    }
    
    public void writeArithmatic(String command) throws IOException{
        String code = null;
        if(command.equalsIgnoreCase(KEY_WORDS.ADD.getKeyWord())){
            code = "@SP\n" +
                    "A=M-1\n" +
                    "D=M\n" +
                    "A=A-1\n" +
                    "M=D+M\n" +
                    "D=A+1\n" +
                    "@SP\n" +
                    "M=D\n";
        }
        
        if(command.equalsIgnoreCase(KEY_WORDS.SUB.getKeyWord())){
            code = "@SP\n" +
                    "A=A-1\n" +
                    "D=M\n" +
                    "A=A-1\n" +
                    "M=D-M\n" +
                    "D=A+1\n" +
                    "@SP\n" +
                    "M=D\n";
        }
        
        if(command.equalsIgnoreCase(KEY_WORDS.NEG.getKeyWord())){
            code = "@SP\n" +
                    "A=A-1\n" +
                    "D=M\n" +
                    "A=A-1\n" +
                    "M=D-M\n" +
                    "D=A\n" +
                    "@SP\n" +
                    "M=D\n";
        }
        
        fileWriter.write(code);
        fileWriter.flush();
    }
    
    public void writePushPop(COMMAND_TYPE type, String segment, String index) throws IOException{
        String code = "";
        if(segment.equalsIgnoreCase(KEY_WORDS.CONSTANT.getKeyWord())){
            code = "@" + index + "\n" 
                    + "D=A\n"
                    + "@SP\n"
                    + "A=M\n"
                    + "M=D\n"
                    + "@SP\n"
                    + "M=M+1\n"
                    ;
            fileWriter.write(code);
            fileWriter.flush();
        }
    }
}
