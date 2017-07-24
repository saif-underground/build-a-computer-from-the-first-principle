/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualmachine;

import enums.COMMAND_TYPE;
import java.io.File;
import java.io.IOException;
import parser.CodeWriter;
import parser.Parser;

/**
 *
 * @author Saif
 */
public class VirtualMachine {

    String inputFolderName = "prog";
    String outputFileName = "prog.asm";

    Parser parser;
    CodeWriter codeWriter;

    public VirtualMachine() throws IOException {

        codeWriter = new CodeWriter(inputFolderName + "/" + outputFileName);
    }

    public void run() throws IOException {
        File folder = new File(inputFolderName);
        File[] listOfFiles = folder.listFiles();

        for (File inputFile : listOfFiles) {
            if(!inputFile.getName().endsWith(".vm")){
                continue;
            }
            String currentFileName = inputFile.getName().substring(0, inputFile.getName().indexOf('.'));
            String currentFunctionName = "";
            parser = new Parser(inputFile.getAbsolutePath());
            while (parser.hasMoreCommands()) {
                parser.advance();
                COMMAND_TYPE cmdType = parser.commandType();
                if (cmdType == COMMAND_TYPE.C_ARITHMATIC) {
                    codeWriter.writeArithmatic(parser.arg1());
                } else if (cmdType == COMMAND_TYPE.C_POP || cmdType == COMMAND_TYPE.C_PUSH) {
                    codeWriter.writePushPop(cmdType, parser.arg1(), parser.arg2());
                }else if(cmdType == COMMAND_TYPE.C_LABEL){
                    String labelName = String.format("%s.%s$%s", currentFileName, currentFunctionName, parser.arg1());
                    System.out.println(labelName);
                    codeWriter.writeLabel(labelName);
                }
            }
        }
    }

    public static void main(String args[]) throws IOException {
        new VirtualMachine().run();
    }
}
