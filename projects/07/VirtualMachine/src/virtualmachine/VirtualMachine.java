/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virtualmachine;

import enums.COMMAND_TYPE;
import java.io.IOException;
import parser.CodeWriter;
import parser.Parser;

/**
 *
 * @author Saif
 */
public class VirtualMachine {

    Parser parser;
    CodeWriter codeWriter;

    public VirtualMachine(String inputFile, String outputFile) throws IOException {
        parser = new Parser(inputFile);
        codeWriter = new CodeWriter(outputFile);
    }

    public void run() throws IOException {
        while (parser.hasMoreCommands()) {
            parser.advance();
            COMMAND_TYPE cmdType = parser.commandType();
            if (cmdType == COMMAND_TYPE.C_ARITHMATIC) {
                codeWriter.writeArithmatic(parser.arg1());
            } else {
                codeWriter.writePushPop(cmdType, parser.arg1(), parser.arg2());
            }
        }
    }

    public static void main(String args[]) throws IOException {
        String inputFile = "SimpleAdd.vm";
        String outputFile = "SimpleAdd.asm";
        new VirtualMachine(inputFile, outputFile).run();
    }
}
