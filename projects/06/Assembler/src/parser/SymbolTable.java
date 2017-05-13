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
public class SymbolTable {
    Map<String, String> symbolAddressMap;
    
    public SymbolTable(){
        symbolAddressMap = new HashMap<>();
        String address = String.format("%15s", Integer.toBinaryString(0)).replace(" ", "0");;
        symbolAddressMap.put("SP", address);
        
        address = String.format("%15s", Integer.toBinaryString(1)).replace(" ", "0");;
        symbolAddressMap.put("LCL", address);
        
        address = String.format("%15s", Integer.toBinaryString(2)).replace(" ", "0");;
        symbolAddressMap.put("ARG", address);
        
        address = String.format("%15s", Integer.toBinaryString(3)).replace(" ", "0");;
        symbolAddressMap.put("THIS", address);
        
        address = String.format("%15s", Integer.toBinaryString(4)).replace(" ", "0");;
        symbolAddressMap.put("THAT", address);
        
        address = String.format("%15s", Integer.toBinaryString(16384)).replace(" ", "0");;
        symbolAddressMap.put("SCREEN", address);
        
        address = String.format("%15s", Integer.toBinaryString(24576)).replace(" ", "0");;
        symbolAddressMap.put("KBD", address);
        
        address = String.format("%15s", Integer.toBinaryString(0)).replace(" ", "0");;
        symbolAddressMap.put("R0", address);
        address = String.format("%15s", Integer.toBinaryString(1)).replace(" ", "0");;
        symbolAddressMap.put("R1", address);
        address = String.format("%15s", Integer.toBinaryString(2)).replace(" ", "0");;
        symbolAddressMap.put("R2", address);
        address = String.format("%15s", Integer.toBinaryString(3)).replace(" ", "0");;
        symbolAddressMap.put("R3", address);
        address = String.format("%15s", Integer.toBinaryString(4)).replace(" ", "0");;
        symbolAddressMap.put("R4", address);
        address = String.format("%15s", Integer.toBinaryString(5)).replace(" ", "0");;
        symbolAddressMap.put("R5", address);
        address = String.format("%15s", Integer.toBinaryString(6)).replace(" ", "0");;
        symbolAddressMap.put("R6", address);
        address = String.format("%15s", Integer.toBinaryString(7)).replace(" ", "0");;
        symbolAddressMap.put("R7", address);
        address = String.format("%15s", Integer.toBinaryString(8)).replace(" ", "0");;
        symbolAddressMap.put("R8", address);
        address = String.format("%15s", Integer.toBinaryString(9)).replace(" ", "0");;
        symbolAddressMap.put("R9", address);
        address = String.format("%15s", Integer.toBinaryString(10)).replace(" ", "0");;
        symbolAddressMap.put("R10", address);
        address = String.format("%15s", Integer.toBinaryString(11)).replace(" ", "0");;
        symbolAddressMap.put("R11", address);
        address = String.format("%15s", Integer.toBinaryString(12)).replace(" ", "0");;
        symbolAddressMap.put("R12", address);
        address = String.format("%15s", Integer.toBinaryString(13)).replace(" ", "0");;
        symbolAddressMap.put("R13", address);
        address = String.format("%15s", Integer.toBinaryString(14)).replace(" ", "0");;
        symbolAddressMap.put("R14", address);
        address = String.format("%15s", Integer.toBinaryString(15)).replace(" ", "0");;
        symbolAddressMap.put("R15", address);
    }
    
    public void addEntry(String symbol, int addressVal){
        if(symbolAddressMap.containsKey(symbol))
            return;
        String address = String.format("%15s", Integer.toBinaryString(addressVal)).replace(" ", "0");;
        symbolAddressMap.put(symbol, address);
    }
    
    public boolean contains(String symbol){
        return symbolAddressMap.containsKey(symbol);
    }
    
    public String getAddress(String symbol){
        return symbolAddressMap.get(symbol);
    }
}
