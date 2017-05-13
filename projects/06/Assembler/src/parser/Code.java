/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Saif
 */
public class Code {
    Map<String, String> destCodeMap = new HashMap();
    Map<String, String> cmpCodeMap = new HashMap();
    Map<String, String> jmpCodeMap = new HashMap();
    
    public Code(){
        destCodeMap.put(null, "000");
        destCodeMap.put("M", "001");
        destCodeMap.put("D", "010");
        destCodeMap.put("MD", "011");
        destCodeMap.put("A", "100");
        destCodeMap.put("AM", "101");
        destCodeMap.put("AD", "110");
        destCodeMap.put("AMD", "111");
        
        cmpCodeMap.put("0", "0101010");
        cmpCodeMap.put("1", "0111111");
        cmpCodeMap.put("-1", "0111010");
        cmpCodeMap.put("D", "0001100");
        cmpCodeMap.put("A", "0110000");
        cmpCodeMap.put("!D", "0001101");
        cmpCodeMap.put("!A", "0110001");
        cmpCodeMap.put("-D", "0001111");
        cmpCodeMap.put("-A", "0110011");
        cmpCodeMap.put("D+1", "0011111");
        cmpCodeMap.put("A+1", "0110111");
        cmpCodeMap.put("D-1", "0001110");
        cmpCodeMap.put("A-1", "0110010");
        cmpCodeMap.put("D+A", "0000010");
        cmpCodeMap.put("D-A", "0010011");
        cmpCodeMap.put("A-D", "0000111");
        cmpCodeMap.put("D&A", "0000000");
        cmpCodeMap.put("D|A", "0010101");
        
        cmpCodeMap.put("M", "1110000");
        cmpCodeMap.put("!M", "1110001");
        cmpCodeMap.put("-M", "1110011");
        cmpCodeMap.put("M+1", "1110111");
        cmpCodeMap.put("M-1", "1110010");
        cmpCodeMap.put("D+M", "1000010");
        cmpCodeMap.put("D-M", "1010011");
        cmpCodeMap.put("M-D", "1000111");
        cmpCodeMap.put("D&M", "1000000");
        cmpCodeMap.put("D|M", "1010101");
        
        jmpCodeMap.put(null, "000");
        jmpCodeMap.put("JGT", "001");
        jmpCodeMap.put("JEQ", "010");
        jmpCodeMap.put("JGE", "011");
        jmpCodeMap.put("JLT", "100");
        jmpCodeMap.put("JNE", "101");
        jmpCodeMap.put("JLE", "110");
        jmpCodeMap.put("JMP", "111");
        
    }
    public String dest(String destReg){
        return destCodeMap.get(destReg);
    }
    
    public String comp(String compMnemonic){
        return cmpCodeMap.get(compMnemonic);
    }
    
    public String jump(String jmpMnemonic){
        return jmpCodeMap.get(jmpMnemonic);
    }
}
