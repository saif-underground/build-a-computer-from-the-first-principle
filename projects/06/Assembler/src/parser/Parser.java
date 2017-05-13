/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import wrappers.FileWrapper;

/**
 * goes through the code line by line. determines command type, extracts symbol, destination, comp
 * and jump portions
 * @author Saif
 */
public class Parser {
    FileWrapper file;
    INSTRUCTION_TYPE currentInstrType = null;
    String currentInstr = null;
    String variableNamePattern = "[a-zA-Z:\\$_\\.]+[a-zA-Z:\\$_\\.0-9]*";
    String srcRegPattern = "M|D|MD|A|AM|AD|AMD";
    String compPattern = 
              "D\\+A|"
            + "D\\-A|"
            + "D\\+1|"
            + "A\\+1|"
            + "D\\-1|"
            + "A\\-1|"
            + "A\\-D|"
            + "D&A|"
            + "D\\|A|"
            + "!D|"
            + "!A|"
            + "\\-D|"
            + "\\-A|"
            + "\\-1|"
            + "D\\+M|"
            + "M\\+1|"
            + "M\\-1|"
            + "D\\-M|"
            + "M\\-D|"
            + "D&M|"
            + "D\\|M|"
            + "!M|"
            + "\\-M|"
            + "M|"
            + "D|"
            + "A|"
            + "0|"
            + "1"
            ;
    
    String jmpMnemonicPattern = "JGT|JEQ|JGE|JLT|JNE|JLE|JMP";
    
    Pattern aTypeInstrPattern;
    Pattern lTypeInstrPattern;
    Pattern cTypeInstrPatternDestCompJmp;
    Pattern cTypeInstrPatternDestComp;
    Pattern cTypeInstrPatternCompJmp;
    Pattern cTypeInstrPatternComp;
    public Parser(String fileName){
        file = new FileWrapper(fileName);
        aTypeInstrPattern = Pattern.compile("@"+"("+variableNamePattern+"|[0-9]+" +")");
        
        lTypeInstrPattern = Pattern.compile("\\((" + variableNamePattern + ")\\)");
        
        cTypeInstrPatternDestCompJmp = Pattern.compile(String.format("^(%s)=(%s);(%s)$",srcRegPattern, compPattern, jmpMnemonicPattern));
        cTypeInstrPatternDestComp = Pattern.compile(String.format("^(%s)=(%s)$",srcRegPattern, compPattern));
        cTypeInstrPatternCompJmp = Pattern.compile(String.format("^(%s);(%s)$",compPattern, jmpMnemonicPattern));
        cTypeInstrPatternComp = Pattern.compile(String.format("^(%s)$",compPattern));
    }
    
    public boolean hasMoreCommands(){
        return file.hasNextLine();
    }
    
    public void advance(){
        currentInstr = file.getNextLine();
    }
    
    public INSTRUCTION_TYPE getCurrentInstructionType(){
        if(currentInstr == null)
            throw new RuntimeException();
        currentInstrType = null;
        if(currentInstr.matches(aTypeInstrPattern.toString())){
                currentInstrType = INSTRUCTION_TYPE.A_TYPE;
        }
        if(currentInstr.matches(lTypeInstrPattern.toString())){
            currentInstrType = INSTRUCTION_TYPE.L_TYPE;
        }
        if(currentInstr.matches(cTypeInstrPatternDestCompJmp.toString()) 
                | currentInstr.matches(cTypeInstrPatternDestComp.toString()) 
                | currentInstr.matches(cTypeInstrPatternCompJmp.toString()) 
                | currentInstr.matches(cTypeInstrPatternComp.toString())){
            currentInstrType = INSTRUCTION_TYPE.C_TYPE;
        }
        if(currentInstrType == null){
            throw new RuntimeException();
        }
        return currentInstrType;
    }
    
    public String symbol(){
        if(currentInstrType == INSTRUCTION_TYPE.C_TYPE){
            throw new RuntimeException();
        }
        String symbol = null;
        if(currentInstrType == INSTRUCTION_TYPE.L_TYPE){
            Matcher matcher = lTypeInstrPattern.matcher(currentInstr);
            if(matcher.find()){
                symbol = matcher.group(1);
            }
        }
        
        if(currentInstrType == INSTRUCTION_TYPE.A_TYPE){
            Matcher matcher = aTypeInstrPattern.matcher(currentInstr);
            if(matcher.find()){
                symbol = matcher.group(1);
            }
        }
        return symbol;
    }
    
    public String dest(){
        if(currentInstrType != INSTRUCTION_TYPE.C_TYPE){
            throw new RuntimeException();
        }
        String symbol=null;
        Matcher matcher = cTypeInstrPatternDestComp.matcher(currentInstr);
        if(matcher.find()){
                symbol = matcher.group(1);
        }
        return symbol;
    }
    
    public String comp(){
        if(currentInstrType != INSTRUCTION_TYPE.C_TYPE){
            throw new RuntimeException();
        }
        String symbol=null;
        Matcher matcher = cTypeInstrPatternDestCompJmp.matcher(currentInstr);
        if(matcher.find()){
            return matcher.group(2);
        }
        
        matcher = cTypeInstrPatternDestComp.matcher(currentInstr);
        if(matcher.find()){
            return matcher.group(2);
        }
        matcher = cTypeInstrPatternCompJmp.matcher(currentInstr);
        if(matcher.find()){
            return matcher.group(1);
        }
        
        matcher = cTypeInstrPatternComp.matcher(currentInstr);
        if(matcher.find()){
            return matcher.group(1);
        }
        
        return symbol;
    }
    
    public String jmp(){
        if(currentInstrType != INSTRUCTION_TYPE.C_TYPE){
            throw new RuntimeException();
        }
        String symbol=null;
        Matcher matcher = cTypeInstrPatternCompJmp.matcher(currentInstr);
        if(matcher.find()){
            symbol = matcher.group(2);
        }
        return symbol;
    }
    
    private String getCurrentInstr(){
        return currentInstr;
    }
}
