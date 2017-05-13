/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Saif
 */
public class Assembler {
    public static String fileName = "Rect";
    private SymbolTable symbolTable;
    public static void main(String args[]){
        Assembler assembler = new Assembler();
        assembler.buildSymbolTable();
        assembler.generateCode();
    }
    
    public Assembler(){
        symbolTable = new SymbolTable();
    }
    
    public void buildSymbolTable(){
        Parser parser = new Parser(fileName + ".asm");
        int currentLine = 0;
        int ramAddr = 0;
        while(parser.hasMoreCommands()){
            parser.advance();
            if(parser.getCurrentInstructionType() == INSTRUCTION_TYPE.L_TYPE){
                String symbol = parser.symbol();
                symbolTable.addEntry(symbol, currentLine);
            }else{
                currentLine++;
            }
        }
    }
    
    public void generateCode(){
        Code code = new Code();
        StringBuilder assemblyCode = new StringBuilder();
        Parser parser = new Parser(fileName+".asm");
        int ramAddr = 16;
        while(parser.hasMoreCommands()){
            parser.advance();
            if(parser.getCurrentInstructionType() == INSTRUCTION_TYPE.A_TYPE){
                String symbol = parser.symbol();
                if(!symbol.matches("^[0-9]+$")){
                    if(!symbolTable.contains(symbol))
                        symbolTable.addEntry(symbol, ramAddr++);
                }
                
                String instr;
                if(symbol.matches("^[0-9]+$")){
                    int symbolVal = Integer.parseInt(symbol);
                    String number = String.format("%15s", Integer.toBinaryString(symbolVal)).replace(" ", "0");
                    instr = "0"+number;
                }else{
                    String address = symbolTable.getAddress(symbol);
                    instr = "0"+address;
                }
                assemblyCode.append(instr+"\n");
            }
            
            if(parser.getCurrentInstructionType() == INSTRUCTION_TYPE.C_TYPE){
                String dest = code.dest(parser.dest());
                String comp = code.comp(parser.comp());
                //System.out.println(parser.comp() + " " + comp);
                String jmp = code.jump(parser.jmp());
                String instr = comp + dest + jmp;
                assemblyCode.append("111"+instr+"\n");
            }
        }
        
        try {
            Files.write(Paths.get(fileName+".hack"), assemblyCode.toString().getBytes());
        } catch (IOException ex) {
            Logger.getLogger(Assembler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
